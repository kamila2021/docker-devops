package com.arquitectura.proyecto.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Insumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipo;
    private String unidadMedida;
    private Integer stockDisponible;

    @OneToMany(mappedBy = "insumo")
    private List<SolicitudInsumo> solicitudes;
}
