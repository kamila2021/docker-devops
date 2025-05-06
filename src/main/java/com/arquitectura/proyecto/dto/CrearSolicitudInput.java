package com.arquitectura.proyecto.dto;

import lombok.Data;

import java.util.List;

@Data

public class CrearSolicitudInput {
    private Long idUsuario;
    private Long idAsignatura;
    private Long idLaboratorio;
    private String fechaUso; // formato "yyyy-MM-dd"
    private String horario;  // formato "HH:mm"
    private int cantGrupos;
    private List<InsumoCantidadInput> insumos;
}
