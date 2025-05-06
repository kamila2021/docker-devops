package com.arquitectura.proyecto.service;

import com.arquitectura.proyecto.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(username);

        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }

        // Convertir todos los roles a authorities
        Collection<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> {
                    String roleName = rol.getName().startsWith("ROLE_") ? rol.getName() : "ROLE_" + rol.getName();
                    return (GrantedAuthority) new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEmail(), // Usamos email como username
                usuario.getPassword(),
                true, // enabled
                true, // accountNonExpired
                true, // accountNonLocked
                true, // credentialsNonExpired
                authorities,
                usuario.getRoles().stream()
                    .map(rol -> new UserDetailsRole(rol.getId(), rol.getName()))
                    .collect(Collectors.toList()),
                Collections.emptyList(), // options
                Collections.emptyList()  // permissions
        );
    }
} 