package com.arquitectura.proyecto.service;

import com.arquitectura.proyecto.dto.InsumoInput;
import com.arquitectura.proyecto.model.Insumo;
import com.arquitectura.proyecto.repository.InsumoRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    @PreAuthorize("isAuthenticated()")
    public List<Insumo> sotckInsumosDisponibles() {
        return insumoRepository.findByStockDisponibleGreaterThan(0);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public Insumo crearInsumo(InsumoInput input) {
        Insumo insumo = new Insumo();
        insumo.setNombre(input.getNombre());
        insumo.setTipo(input.getTipo());
        insumo.setUnidadMedida(input.getUnidadMedida());
        insumo.setStockDisponible(input.getStockDisponible());
        return insumoRepository.save(insumo);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public Insumo modificarInsumo(Long id, InsumoInput input) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));

        insumo.setNombre(input.getNombre());
        insumo.setTipo(input.getTipo());
        insumo.setUnidadMedida(input.getUnidadMedida());
        insumo.setStockDisponible(input.getStockDisponible());

        return insumoRepository.save(insumo);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public void eliminarInsumo(Long id) {
        insumoRepository.deleteById(id);
    }

    @PreAuthorize("isAuthenticated()")
    public List<Insumo> listarInsumos() {
        return insumoRepository.findAll();
    }
}
