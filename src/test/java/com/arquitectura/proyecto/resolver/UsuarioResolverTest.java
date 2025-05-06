package com.arquitectura.proyecto.resolver;

import com.arquitectura.proyecto.dto.UsuarioDto;
import com.arquitectura.proyecto.model.Usuario;
import com.arquitectura.proyecto.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioResolverTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioResolver usuarioResolver;

    private Usuario usuarioMock;
    private UsuarioDto usuarioDtoMock;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setEmail("test@test.com");
        usuarioMock.setNombre("Test User");

        usuarioDtoMock = new UsuarioDto();
        usuarioDtoMock.setId(1L);
        usuarioDtoMock.setEmail("test@test.com");
        usuarioDtoMock.setNombre("Test User");
    }

    @Test
    void listarUsuarios_DeberiaRetornarListaDeUsuarios() {
        // Arrange
        List<Usuario> expectedUsers = Arrays.asList(usuarioMock);
        when(usuarioService.listarUsuarios()).thenReturn(expectedUsers);

        // Act
        List<Usuario> result = usuarioResolver.listarUsuarios();

        // Assert
        assertNotNull(result);
        assertEquals(expectedUsers.size(), result.size());
        assertEquals(expectedUsers.get(0).getId(), result.get(0).getId());
        verify(usuarioService).listarUsuarios();
    }

    @Test
    void listarProfesores_DeberiaRetornarListaDeProfesores() {
        // Arrange
        List<Usuario> expectedProfesores = Arrays.asList(usuarioMock);
        when(usuarioService.listarProfesores()).thenReturn(expectedProfesores);

        // Act
        List<Usuario> result = usuarioResolver.listarProfesores();

        // Assert
        assertNotNull(result);
        assertEquals(expectedProfesores.size(), result.size());
        verify(usuarioService).listarProfesores();
    }

    @Test
    void obtenerUsuario_DeberiaRetornarUsuario() {
        // Arrange
        when(usuarioService.obtenerUsuario(1L)).thenReturn(usuarioMock);

        // Act
        Usuario result = usuarioResolver.obtenerUsuario(1L);

        // Assert
        assertNotNull(result);
        assertEquals(usuarioMock.getId(), result.getId());
        verify(usuarioService).obtenerUsuario(1L);
    }

    @Test
    void obtenerUsuarioPorEmail_DeberiaRetornarUsuario() {
        // Arrange
        when(usuarioService.obtenerUsuarioPorEmail("test@test.com")).thenReturn(usuarioMock);

        // Act
        Usuario result = usuarioResolver.obtenerUsuarioPorEmail("test@test.com");

        // Assert
        assertNotNull(result);
        assertEquals(usuarioMock.getEmail(), result.getEmail());
        verify(usuarioService).obtenerUsuarioPorEmail("test@test.com");
    }

    @Test
    void eliminarUsuario_DeberiaRetornarTrue_CuandoEliminacionExitosa() {
        // Arrange
        when(usuarioService.eliminarUsuario(1L)).thenReturn(ResponseEntity.ok().build());

        // Act
        Boolean result = usuarioResolver.eliminarUsuario(1L);

        // Assert
        assertTrue(result);
        verify(usuarioService).eliminarUsuario(1L);
    }

    @Test
    void crearUsuario_DeberiaRetornarNuevoUsuario() {
        // Arrange
        when(usuarioService.crearUsuario(any(UsuarioDto.class))).thenReturn(usuarioMock);

        // Act
        Usuario result = usuarioResolver.crearUsuario(usuarioDtoMock);

        // Assert
        assertNotNull(result);
        assertEquals(usuarioMock.getId(), result.getId());
        verify(usuarioService).crearUsuario(usuarioDtoMock);
    }

    @Test
    void forgotPassword_DeberiaRetornarTrue_CuandoExitoso() throws Exception {
        // Arrange
        when(usuarioService.forgotPassword("test@test.com")).thenReturn(true);

        // Act
        Boolean result = usuarioResolver.forgotPassword("test@test.com");

        // Assert
        assertTrue(result);
        verify(usuarioService).forgotPassword("test@test.com");
    }

    @Test
    void editarUsuario_DeberiaRetornarUsuarioActualizado() {
        // Arrange
        when(usuarioService.actualizarUsuario(any(UsuarioDto.class))).thenReturn(usuarioDtoMock);

        // Act
        UsuarioDto result = usuarioResolver.editarUsuario(usuarioDtoMock);

        // Assert
        assertNotNull(result);
        assertEquals(usuarioDtoMock.getId(), result.getId());
        verify(usuarioService).actualizarUsuario(usuarioDtoMock);
    }

    @Test
    void updatePasswordByCode_DeberiaRetornarTrue_CuandoExitoso() throws Exception {
        // Arrange
        when(usuarioService.updatePasswordByCode("123456", "newPassword")).thenReturn(true);

        // Act
        Boolean result = usuarioResolver.updatePasswordByCode("123456", "newPassword");

        // Assert
        assertTrue(result);
        verify(usuarioService).updatePasswordByCode("123456", "newPassword");
    }
} 