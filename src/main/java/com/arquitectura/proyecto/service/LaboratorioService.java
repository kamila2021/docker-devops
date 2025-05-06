package com.arquitectura.proyecto.service;

import com.arquitectura.proyecto.dto.LaboratorioDto;
import com.arquitectura.proyecto.model.Laboratorio;
import com.arquitectura.proyecto.repository.LaboratorioRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;

    public LaboratorioService(LaboratorioRepository laboratorioRepository) {
        this.laboratorioRepository = laboratorioRepository;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public Laboratorio crearLaboratorio(LaboratorioDto input) {
        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setNombre(input.getNombre());
        laboratorio.setCodigo(input.getCodigo());
        return laboratorioRepository.save(laboratorio);
    }

    @PreAuthorize("isAuthenticated()")
    public List<Laboratorio> listarLaboratorios() {
        return laboratorioRepository.findAll();
    }

}
