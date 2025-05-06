package com.arquitectura.proyecto.dto;

import lombok.Data;

@Data
public class SolicitudInsumoDto {
    private Long id;
    private InsumoDto insumo;
    private Double cantidad;
}
