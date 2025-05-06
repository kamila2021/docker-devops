package com.arquitectura.proyecto.dto;

import lombok.Data;

@Data
public class InsumoDto {
    private Long id;
    private String nombre;
    private String tipo;
    private String unidadMedida;
    private Integer stockDisponible;
}
