package com.arquitectura.proyecto.service;

import com.arquitectura.proyecto.dto.CrearSolicitudInput;
import com.arquitectura.proyecto.dto.InsumoCantidadInput;
import com.arquitectura.proyecto.model.*;
import com.arquitectura.proyecto.repository.*;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalABSServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private InsumoRepository insumoRepository;

    @Mock
    private SolicitudInsumoRepository solicitudInsumoRepository;

    @InjectMocks
    private PersonalABSService personalABSService;

    private Solicitud solicitudMock;
    private Usuario usuarioMock;
    private Asignatura asignaturaMock;
    private Laboratorio laboratorioMock;
    private Insumo insumoMock;
    private SolicitudInsumo solicitudInsumoMock;
    private CrearSolicitudInput crearSolicitudInputMock;
    private InsumoCantidadInput insumoCantidadInputMock;

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

        // Configurar Insumo mock
        insumoMock = new Insumo();
        insumoMock.setId(1L);
        insumoMock.setNombre("Test Insumo");
        insumoMock.setStockDisponible(100);

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

        // Configurar SolicitudInsumo mock
        solicitudInsumoMock = new SolicitudInsumo();
        solicitudInsumoMock.setId(1L);
        solicitudInsumoMock.setSolicitud(solicitudMock);
        solicitudInsumoMock.setInsumo(insumoMock);
        solicitudInsumoMock.setCantidad(10.0);

        // Configurar InsumoCantidadInput mock
        insumoCantidadInputMock = new InsumoCantidadInput();
        insumoCantidadInputMock.setIdInsumo(1L);
        insumoCantidadInputMock.setCantidad(10.0);

        // Configurar CrearSolicitudInput mock
        crearSolicitudInputMock = new CrearSolicitudInput();
        crearSolicitudInputMock.setIdUsuario(1L);
        crearSolicitudInputMock.setIdAsignatura(1L);
        crearSolicitudInputMock.setIdLaboratorio(1L);
        crearSolicitudInputMock.setFechaUso(LocalDate.now().plusDays(1).toString());
        crearSolicitudInputMock.setHorario(LocalTime.now().toString());
        crearSolicitudInputMock.setCantGrupos(2);
        crearSolicitudInputMock.setInsumos(Arrays.asList(insumoCantidadInputMock));
    }

    @Test
    void listarSolicitudes_DeberiaRetornarListaDeSolicitudes() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(solicitudRepository.findAll()).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSService.listarSolicitudes();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(solicitudRepository).findAll();
    }

    @Test
    void listarSolicitudesDelUsuario_DeberiaRetornarListaDeSolicitudesDelUsuario() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(solicitudRepository.findByUsuarioIdOrderByFechaUsoAsc(1L)).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSService.listarSolicitudesDelUsuario(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(solicitudRepository).findByUsuarioIdOrderByFechaUsoAsc(1L);
    }

    @Test
    void listarSolicitudesRechazadas_DeberiaRetornarListaDeSolicitudesRechazadas() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(solicitudRepository.findByEstado(false)).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSService.listarSolicitudesRechazadas();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(solicitudRepository).findByEstado(false);
    }

    @Test
    void listarSolicitudesAprobadas_DeberiaRetornarListaDeSolicitudesAprobadas() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(solicitudRepository.findByEstado(true)).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSService.listarSolicitudesAprobadas();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(solicitudRepository).findByEstado(true);
    }

    @Test
    void listarSolicitudesPorFechaUso_DeberiaRetornarListaDeSolicitudesOrdenadas() {
        // Arrange
        List<Solicitud> expectedSolicitudes = Arrays.asList(solicitudMock);
        when(solicitudRepository.findAllByOrderByFechaUsoAsc()).thenReturn(expectedSolicitudes);

        // Act
        List<Solicitud> result = personalABSService.listarSolicitudesPorFechaUso();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSolicitudes.size(), result.size());
        verify(solicitudRepository).findAllByOrderByFechaUsoAsc();
    }



    @Test
    void modificarSolicitud_DeberiaRetornarSolicitudModificada() {
        // Arrange
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudMock));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudMock);

        Solicitud datosActualizados = new Solicitud();
        datosActualizados.setFechaUso(LocalDate.now().plusDays(2));
        datosActualizados.setHorario(LocalTime.now());
        datosActualizados.setCantGrupos(3);
        datosActualizados.setEstado(true);

        // Act
        Solicitud result = personalABSService.modificarSolicitud(1L, datosActualizados);

        // Assert
        assertNotNull(result);
        assertEquals(solicitudMock.getId(), result.getId());
        verify(solicitudRepository).save(any(Solicitud.class));
    }

    @Test
    void eliminarSolicitud_DeberiaEliminarSolicitudYSusInsumos() {
        // Arrange
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudMock));
        List<SolicitudInsumo> insumos = Arrays.asList(solicitudInsumoMock);
        when(solicitudInsumoRepository.findBySolicitudId(1L)).thenReturn(insumos);
        doNothing().when(solicitudInsumoRepository).deleteAll(insumos);
        doNothing().when(solicitudRepository).delete(solicitudMock);

        // Act
        personalABSService.eliminarSolicitud(1L);

        // Assert
        verify(solicitudInsumoRepository).deleteAll(insumos);
        verify(solicitudRepository).delete(solicitudMock);
    }

    @Test
    void cancelarSolicitud_DeberiaActualizarEstadoASolicitudCancelada() {
        // Arrange
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudMock));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudMock);

        // Act
        personalABSService.cancelarSolicitud(1L);

        // Assert
        assertFalse(solicitudMock.getEstado());
        verify(solicitudRepository).save(solicitudMock);
    }

    @Test
    void confirmarYActualizarSolicitud_DeberiaConfirmarSolicitudYActualizarStock() {
        // Arrange
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudMock));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudMock);
        List<SolicitudInsumo> insumos = Arrays.asList(solicitudInsumoMock);
        when(solicitudInsumoRepository.findBySolicitudId(1L)).thenReturn(insumos);
        when(insumoRepository.save(any(Insumo.class))).thenReturn(insumoMock);

        // Act
        Solicitud result = personalABSService.confirmarYActualizarSolicitud(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getEstado());
        verify(solicitudRepository).save(solicitudMock);
        verify(insumoRepository).save(any(Insumo.class));
    }

    @Test
    void confirmarYActualizarSolicitud_DeberiaLanzarExcepcion_CuandoStockInsuficiente() {
        // Arrange
        insumoMock.setStockDisponible(1);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudMock));
        List<SolicitudInsumo> insumos = Arrays.asList(solicitudInsumoMock);
        when(solicitudInsumoRepository.findBySolicitudId(1L)).thenReturn(insumos);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> personalABSService.confirmarYActualizarSolicitud(1L));
    }
} 