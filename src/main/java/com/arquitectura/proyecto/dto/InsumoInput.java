package com.arquitectura.proyecto.dto;

import lombok.Data;

@Data
public class InsumoInput {
    private String nombre;
    private String tipo;
    private String unidadMedida;
    private Integer stockDisponible;
}
