package com.arquitectura.proyecto.service;

import com.arquitectura.proyecto.dto.UsuarioDto;
import com.arquitectura.proyecto.model.Rol;
import com.arquitectura.proyecto.model.Token;
import com.arquitectura.proyecto.model.Usuario;
import com.arquitectura.proyecto.repository.RoleRepository;
import com.arquitectura.proyecto.repository.TokenRepository;
import com.arquitectura.proyecto.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;
    private UsuarioDto usuarioDtoMock;
    private Rol rolMock;

    @BeforeEach
    void setUp() {
        // Crear objetos reales en lugar de mocks
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setEmail("test@example.com");
        usuarioMock.setNombre("Test");
        usuarioMock.setApellido("User");
        usuarioMock.setPassword("encodedPassword");
        usuarioMock.setEnabled(true);
        usuarioMock.setAccountLocked(false);
        usuarioMock.setCreatedAt(LocalDateTime.now());
        usuarioMock.setLastModifiedDate(LocalDateTime.now());

        rolMock = new Rol();
        rolMock.setId(1L);
        rolMock.setName("ROLE_USER");

        Set<Rol> roles = new HashSet<>();
        roles.add(rolMock);
        usuarioMock.setRoles(roles);

        usuarioDtoMock = new UsuarioDto();
        usuarioDtoMock.setId(1L);
        usuarioDtoMock.setEmail("test@example.com");
        usuarioDtoMock.setNombre("Test");
        usuarioDtoMock.setApellido("User");
        usuarioDtoMock.setPassword("password123");
        usuarioDtoMock.setEnabled(true);
        usuarioDtoMock.setAccountLocked(false);
        usuarioDtoMock.setRoles(List.of(1L));
    }

    @Test
    void obtenerUsuarioPorEmail_DeberiaRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(usuarioMock);

        // Act
        Usuario resultado = usuarioService.obtenerUsuarioPorEmail("test@example.com");

        // Assert
        assertNotNull(resultado);
        assertEquals("test@example.com", resultado.getEmail());
    }

    @Test
    void obtenerUsuario_DeberiaLanzarExcepcion_CuandoNoExisteUsuario() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.obtenerUsuario(1L));
    }


    @Test
    void eliminarUsuario_DeberiaEliminarUsuario_CuandoExiste() {
        // Act
        ResponseEntity<?> resultado = usuarioService.eliminarUsuario(1L);

        // Assert
        assertTrue(resultado.getStatusCode().is2xxSuccessful());
        verify(usuarioRepository).deleteById(1L);
    }

}