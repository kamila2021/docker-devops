package com.arquitectura.proyecto.resolver;

import com.arquitectura.proyecto.dto.InsumoInput;
import com.arquitectura.proyecto.model.Insumo;
import com.arquitectura.proyecto.service.InsumoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsumoResolverTest {

    @Mock
    private InsumoService insumoService;

    @InjectMocks
    private InsumoResolver insumoResolver;

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
    void listarInsumos_DeberiaRetornarListaDeInsumos() {
        // Arrange
        List<Insumo> expectedInsumos = Arrays.asList(insumoMock);
        when(insumoService.listarInsumos()).thenReturn(expectedInsumos);

        // Act
        List<Insumo> result = insumoResolver.listarInsumos();

        // Assert
        assertNotNull(result);
        assertEquals(expectedInsumos.size(), result.size());
        assertEquals(expectedInsumos.get(0).getId(), result.get(0).getId());
        verify(insumoService).listarInsumos();
    }

    @Test
    void stockInsumosDisponibles_DeberiaRetornarListaDeInsumosConStock() {
        // Arrange
        List<Insumo> expectedInsumos = Arrays.asList(insumoMock);
        when(insumoService.sotckInsumosDisponibles()).thenReturn(expectedInsumos);

        // Act
        List<Insumo> result = insumoResolver.stockInsumosDisponibles();

        // Assert
        assertNotNull(result);
        assertEquals(expectedInsumos.size(), result.size());
        assertEquals(expectedInsumos.get(0).getId(), result.get(0).getId());
        verify(insumoService).sotckInsumosDisponibles();
    }

    @Test
    void crearInsumo_DeberiaRetornarNuevoInsumo() {
        // Arrange
        when(insumoService.crearInsumo(any(InsumoInput.class))).thenReturn(insumoMock);

        // Act
        Insumo result = insumoResolver.crearInsumo(insumoInputMock);

        // Assert
        assertNotNull(result);
        assertEquals(insumoMock.getId(), result.getId());
        assertEquals(insumoMock.getNombre(), result.getNombre());
        verify(insumoService).crearInsumo(insumoInputMock);
    }

    @Test
    void modificarInsumo_DeberiaRetornarInsumoModificado() {
        // Arrange
        when(insumoService.modificarInsumo(eq(1L), any(InsumoInput.class))).thenReturn(insumoMock);

        // Act
        Insumo result = insumoResolver.modificarInsumo(1L, insumoInputMock);

        // Assert
        assertNotNull(result);
        assertEquals(insumoMock.getId(), result.getId());
        assertEquals(insumoMock.getNombre(), result.getNombre());
        verify(insumoService).modificarInsumo(1L, insumoInputMock);
    }

    @Test
    void eliminarInsumo_DeberiaRetornarTrue() {
        // Arrange
        doNothing().when(insumoService).eliminarInsumo(1L);

        // Act
        Boolean result = insumoResolver.eliminarInsumo(1L);

        // Assert
        assertTrue(result);
        verify(insumoService).eliminarInsumo(1L);
    }
} 