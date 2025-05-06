package com.arquitectura.proyecto.resolver;

import com.arquitectura.proyecto.dto.CrearSolicitudInput;
import com.arquitectura.proyecto.dto.SolicitudInput;
import com.arquitectura.proyecto.model.*;
import com.arquitectura.proyecto.service.PersonalABSService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalABSResolverTest {

    @Mock
    private PersonalABSService personalABSService;

    @InjectMocks
    private PersonalABSResolver personalABSResolver;

    private Solicitud solicitudMock;
    private Usuario usuarioMock;
    private Asignatura asignaturaMock;
    private Laboratorio laboratorioMock;
    private CrearSolicitudInput crearSolicitudInputMock;
    private SolicitudInput solicitudInputMock;

    @BeforeEach
    void setUp() {
        // Configurar Usuario mock
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Test User");

        // Configurar Asignatura mock
        asignaturaMock = new Asignatura();
        asignaturaMock.setId(1L);
        asignaturaMock.setNombre("Test Asignatura");

        // Configurar Laboratorio mock
        laboratorioMock = new Laboratorio();
        laboratorioMock.setId(1L);
        laboratorioMock.setNombre("Test Lab");

        // Configurar Solicitud mock
        solicitudMock = new Solicitud();
        solicitudMock.setId(1L);
        solicitudMock.setUsuario(usuarioMock);
        solicitudMock.setAsignatura(asignaturaMock);
        solicitudMock.setLaboratorio(laboratorioMock);
        solicitudMock.setFechaSolicitud(LocalDate.now());
        solicitudMock.setFechaUso(LocalDate.now().plusDays(1));
        solicitudMock.setHorario(LocalTime.now());
        solicitudMock.setCantGrupos(2);
        solicitudMock.setEstado(false);

        // Configurar CrearSolicitudInput mock
        crearSolicitudInputMock = new CrearSolicitudInput();
        crearSolicitudInputMock.setIdUsuario(1L);
        crearSolicitudInputMock.setIdAsignatura(1L);
        crearSolicitudInputMock.setIdLaboratorio(1L);
        crearSolicitudInputMock.setFechaUso(LocalDate.now().plusDays(1).toString());
        crearSolicitudInputMock.setHorario(LocalTime.now().toString());
        crearSolicitudInputMock.setCantGrupos(2);

        // Configurar SolicitudInput mock
        solicitudInputMock = new SolicitudInput();
        solicitudInputMock.setFechaUso(LocalDate.now().plusDays(1).toString());
        solicitudInputMock.setHorario(LocalTime.now().toString());
        solicitudInputMock.setCantGrupos(2);
        solicitudInputMock.setEstado(false);
    }

    @Test
    void crearSolicitud_DeberiaRetornarNuevaSolicitud() {
        // Arrange
        when(personalABSService.crearSolicitud(any(CrearSolicitudInput.class))).thenReturn(solicitudMock);

        // Act
        Solicitud result = personalABSResolver.crearSolicitud(crearSolicitudInputMock);

        // Assert
        assertNotNull(result);
        assertEquals(solicitudMock.getId(), result.getId());
        verify(personalABSService).crearSolicitud(crearSolicitudInputMock);
    }

    @Test
    void listarSolicitudes_DeberiaRetornarListaDeSolicitudes() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(personalABSService.listarSolicitudes()).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSResolver.listarSolicitudes();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(personalABSService).listarSolicitudes();
    }

    @Test
    void listarSolicitudesRechazadas_DeberiaRetornarListaDeSolicitudesRechazadas() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(personalABSService.listarSolicitudesRechazadas()).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSResolver.listarSolicitudesRechazadas();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(personalABSService).listarSolicitudesRechazadas();
    }

    @Test
    void listarSolicitudesAprobadas_DeberiaRetornarListaDeSolicitudesAprobadas() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(personalABSService.listarSolicitudesAprobadas()).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSResolver.listarSolicitudesAprobadas();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(personalABSService).listarSolicitudesAprobadas();
    }

    @Test
    void listarSolicitudesPorFechaUso_DeberiaRetornarListaDeSolicitudesOrdenadas() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(personalABSService.listarSolicitudesPorFechaUso()).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSResolver.listarSolicitudesPorFechaUso();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(personalABSService).listarSolicitudesPorFechaUso();
    }

    @Test
    void modificarSolicitud_DeberiaRetornarSolicitudModificada() {
        // Arrange
        when(personalABSService.modificarSolicitud(eq(1L), any(Solicitud.class))).thenReturn(solicitudMock);

        // Act
        Solicitud result = personalABSResolver.modificarSolicitud(1L, solicitudInputMock);

        // Assert
        assertNotNull(result);
        assertEquals(solicitudMock.getId(), result.getId());
        verify(personalABSService).modificarSolicitud(eq(1L), any(Solicitud.class));
    }

    @Test
    void eliminarSolicitud_DeberiaRetornarTrue() {
        // Arrange
        doNothing().when(personalABSService).eliminarSolicitud(1L);

        // Act
        Boolean result = personalABSResolver.eliminarSolicitud(1L);

        // Assert
        assertTrue(result);
        verify(personalABSService).eliminarSolicitud(1L);
    }

    @Test
    void solicitudesDelProfesor_DeberiaRetornarListaDeSolicitudesDelProfesor() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(personalABSService.listarSolicitudesDelUsuario(1L)).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSResolver.solicitudesDelProfesor(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(personalABSService).listarSolicitudesDelUsuario(1L);
    }

    @Test
    void confirmarSolicitud_DeberiaRetornarSolicitudConfirmada() {
        // Arrange
        when(personalABSService.confirmarYActualizarSolicitud(1L)).thenReturn(solicitudMock);

        // Act
        Solicitud result = personalABSResolver.confirmarSolicitud(1L);

        // Assert
        assertNotNull(result);
        assertEquals(solicitudMock.getId(), result.getId());
        verify(personalABSService).confirmarYActualizarSolicitud(1L);
    }
} 