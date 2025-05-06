package com.arquitectura.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudInput {
    private String fechaUso;
    private String horario;
    private Integer cantGrupos;
    private Boolean estado;
}
