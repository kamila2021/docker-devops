package com.arquitectura.proyecto.service;

import com.arquitectura.proyecto.dto.InsumoInput;
import com.arquitectura.proyecto.model.Insumo;
import com.arquitectura.proyecto.repository.InsumoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsumoServiceTest {

    @Mock
    private InsumoRepository insumoRepository;

    @InjectMocks
    private InsumoService insumoService;

    private Insumo insumoMock;
    private InsumoInput insumoInputMock;

    @BeforeEach
    void setUp() {
        insumoMock = new Insumo();
        insumoMock.setId(1L);
        insumoMock.setNombre("Test Insumo");
        insumoMock.setTipo("Test Tipo");
        insumoMock.setUnidadMedida("Test Unidad");
        insumoMock.setStockDisponible(100);

        insumoInputMock = new InsumoInput();
        insumoInputMock.setNombre("Test Insumo");
        insumoInputMock.setTipo("Test Tipo");
        insumoInputMock.setUnidadMedida("Test Unidad");
        insumoInputMock.setStockDisponible(100);
    }

    @Test
    void sotckInsumosDisponibles_DeberiaRetornarListaDeInsumosConStockPositivo() {
        // Arrange
        List<Insumo> expectedInsumos = Arrays.asList(insumoMock);
        when(insumoRepository.findByStockDisponibleGreaterThan(0)).thenReturn(expectedInsumos);

        // Act
        List<Insumo> result = insumoService.sotckInsumosDisponibles();

        // Assert
        assertNotNull(result);
        assertEquals(expectedInsumos.size(), result.size());
        assertEquals(expectedInsumos.get(0).getId(), result.get(0).getId());
        verify(insumoRepository).findByStockDisponibleGreaterThan(0);
    }

    @Test
    void crearInsumo_DeberiaRetornarNuevoInsumo() {
        // Arrange
        when(insumoRepository.save(any(Insumo.class))).thenReturn(insumoMock);

        // Act
        Insumo result = insumoService.crearInsumo(insumoInputMock);

        // Assert
        assertNotNull(result);
        assertEquals(insumoMock.getId(), result.getId());
        assertEquals(insumoMock.getNombre(), result.getNombre());
        assertEquals(insumoMock.getTipo(), result.getTipo());
        assertEquals(insumoMock.getUnidadMedida(), result.getUnidadMedida());
        assertEquals(insumoMock.getStockDisponible(), result.getStockDisponible());
        verify(insumoRepository).save(any(Insumo.class));
    }

    @Test
    void modificarInsumo_DeberiaRetornarInsumoModificado() {
        // Arrange
        when(insumoRepository.findById(1L)).thenReturn(Optional.of(insumoMock));
        when(insumoRepository.save(any(Insumo.class))).thenReturn(insumoMock);

        // Act
        Insumo result = insumoService.modificarInsumo(1L, insumoInputMock);

        // Assert
        assertNotNull(result);
        assertEquals(insumoMock.getId(), result.getId());
        assertEquals(insumoInputMock.getNombre(), result.getNombre());
        assertEquals(insumoInputMock.getTipo(), result.getTipo());
        assertEquals(insumoInputMock.getUnidadMedida(), result.getUnidadMedida());
        assertEquals(insumoInputMock.getStockDisponible(), result.getStockDisponible());
        verify(insumoRepository).save(any(Insumo.class));
    }

    @Test
    void modificarInsumo_DeberiaLanzarExcepcion_CuandoInsumoNoExiste() {
        // Arrange
        when(insumoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> insumoService.modificarInsumo(1L, insumoInputMock));
        verify(insumoRepository).findById(1L);
        verify(insumoRepository, never()).save(any(Insumo.class));
    }

    @Test
    void eliminarInsumo_DeberiaEliminarInsumo() {
        // Arrange
        doNothing().when(insumoRepository).deleteById(1L);

        // Act
        insumoService.eliminarInsumo(1L);

        // Assert
        verify(insumoRepository).deleteById(1L);
    }

    @Test
    void listarInsumos_DeberiaRetornarListaDeInsumos() {
        // Arrange
        List<Insumo> expectedInsumos = Arrays.asList(insumoMock);
        when(insumoRepository.findAll()).thenReturn(expectedInsumos);

        // Act
        List<Insumo> result = insumoService.listarInsumos();

        // Assert
        assertNotNull(result);
        assertEquals(expectedInsumos.size(), result.size());
        assertEquals(expectedInsumos.get(0).getId(), result.get(0).getId());
        verify(insumoRepository).findAll();
    }
} 