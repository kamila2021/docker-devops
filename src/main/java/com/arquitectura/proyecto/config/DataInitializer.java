package com.arquitectura.proyecto.config;

import com.arquitectura.proyecto.Enum.EnumRol;
import com.arquitectura.proyecto.model.Usuario;
import com.arquitectura.proyecto.model.Rol;
import com.arquitectura.proyecto.repository.RoleRepository;
import com.arquitectura.proyecto.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public DataInitializer(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        if (!roleRepository.existsByName(EnumRol.Profesor.name())) {
            Rol userRole = Rol.builder().name(EnumRol.Profesor.getName()).build();
            roleRepository.save(userRole);
        }
        if (!roleRepository.existsByName(EnumRol.Admin.name())) {
            Rol providerRole = Rol.builder().name(EnumRol.Admin.getName()).build();
            roleRepository.save(providerRole);
        }
    }

    private void initializeUsers() {
        if(usuarioRepository.findByEmail("admin") == null) {
            Usuario newUsuario = new Usuario();
            newUsuario.setNombre("Admin");
            newUsuario.setApellido("Istrador");
            newUsuario.setEmail("admin");
            newUsuario.setPassword(passwordEncoder.encode("admin123"));
            newUsuario.setAccountLocked(false);
           newUsuario.setEnabled(true);

            newUsuario.setCreatedAt(LocalDateTime.now());
            newUsuario.setLastModifiedDate(LocalDateTime.now()); // 👈 CORRECCIÓN AQUÍ



            Rol rol = roleRepository.findRoleByName(EnumRol.Admin.getName());
            newUsuario.setRoles(new HashSet<>(List.of(rol)));
            usuarioRepository.save(newUsuario);
            System.out.println("lastModifiedDate: " + newUsuario.getLastModifiedDate()); // opcional
        }
    }


}