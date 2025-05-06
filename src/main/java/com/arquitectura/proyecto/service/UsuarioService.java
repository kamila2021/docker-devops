package com.arquitectura.proyecto.service;


import com.arquitectura.proyecto.dto.UsuarioDto;
import com.arquitectura.proyecto.model.*;
import com.arquitectura.proyecto.repository.RoleRepository;
import com.arquitectura.proyecto.repository.TokenRepository;
import com.arquitectura.proyecto.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final AuthenticationService authenticationService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Usuario> listarProfesores() {
        return usuarioRepository.findAllByRoles_Id(1L);
    }

    @PreAuthorize("isAuthenticated()")
    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Usuario con id %s no encontrado", id)));
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public UsuarioDto actualizarUsuario(UsuarioDto usuarioDTO) {
        try {
            Optional<Usuario> optionalUsuario = this.usuarioRepository.findById(usuarioDTO.getId());
            if (!optionalUsuario.isPresent()) {
                throw new RuntimeException("Usuario no encontrado");
            }

            Usuario usuario = optionalUsuario.get();
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setApellido(usuarioDTO.getApellido());
            usuario.setEmail(usuarioDTO.getEmail());
            
            // Solo actualizar la contraseña si se proporciona una nueva
            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            }

            // Actualizar roles
            Set<Rol> rolesActualizados = new HashSet<>();
            for (Long roleId : usuarioDTO.getRoles()) {
                Rol rol = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleId));
                rolesActualizados.add(rol);
            }
            usuario.setRoles(rolesActualizados);

            usuario.setLastModifiedDate(LocalDateTime.now());
            usuarioRepository.save(usuario);
            
            return usuarioDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    //@PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity eliminarUsuario(Long usuarioId) {
        try {
            this.usuarioRepository.deleteById(usuarioId);
            return ResponseEntity.ok(true); // Retorna true si se elimina el usuario
        } catch (Exception e) {
            return ResponseEntity.ok(false); // Retorna false si ocurre un error
        }
    }


    public Usuario crearUsuario(UsuarioDto usuarioDto) {
        try {
            Usuario newUsuario = new Usuario();

            System.out.println("Nombre: " + usuarioDto.getNombre());
            newUsuario.setNombre(usuarioDto.getNombre());

            System.out.println("Apellido: " + usuarioDto.getApellido());
            newUsuario.setApellido(usuarioDto.getApellido());

            System.out.println("Email: " + usuarioDto.getEmail());
            newUsuario.setEmail(usuarioDto.getEmail());

            System.out.println("Password: " + usuarioDto.getPassword());
            newUsuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));

            System.out.println("Account Locked: " + usuarioDto.isAccountLocked());
            newUsuario.setAccountLocked(usuarioDto.isAccountLocked());

            System.out.println("Enabled: " + usuarioDto.isEnabled());
            newUsuario.setEnabled(usuarioDto.isEnabled());

            System.out.println("CreatedAt: " + LocalDateTime.now());
            newUsuario.setCreatedAt(LocalDateTime.now());

            // Este lo puedes dejar así de momento si lo seteás manualmente
            System.out.println("LastModifiedDate: " + LocalDateTime.now());
            newUsuario.setLastModifiedDate(LocalDateTime.now());

            // Guardar el nuevo usuario en la base de datos
            Usuario usuarioGuardado = usuarioRepository.save(newUsuario);

            System.out.println("Usuario guardado con ID: " + usuarioGuardado.getId());

            // Asignar roles al usuario
            System.out.println("Roles recibidos: " + usuarioDto.getRoles());
            asignarRolesAUsuario(usuarioGuardado.getId(), usuarioDto.getRoles());

            return usuarioGuardado;

        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public void asignarRolesAUsuario(Long usuarioId, List<Long> rolesIds) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Rol> roles = roleRepository.findAllById(rolesIds);
        usuario.setRoles(new HashSet<>(roles));
        usuarioRepository.save(usuario);
    }

    public boolean forgotPassword(String email) throws Exception {

        try {
            if (email == null) {
                throw new IllegalArgumentException("El email es nulo");
            }

            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario == null) {
                throw new IllegalArgumentException("El usuario no existe");
            }

            this.authenticationService.sendValidationEmail(usuario);

            return true;
        } catch (MessagingException me) {
            throw new MessagingException("Error al enviar correo");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean updatePasswordByCode(String code, String newPassword) throws Exception {

        try {
            if (code == null) {
                throw new IllegalArgumentException("El codigo es nulo");
            }

            Usuario user = this.usuarioRepository.findByResetPasswordToken(code);
            if(user == null) {
                throw new IllegalArgumentException("El usuario no existe");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            usuarioRepository.save(user);
            return true;

        } catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public Usuario getByResetPasswordToken(String token) {
        return usuarioRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(Usuario customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);
        customer.setResetPasswordToken(null);
        usuarioRepository.save(customer);
    }


}