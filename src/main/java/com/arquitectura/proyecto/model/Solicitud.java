package com.arquitectura.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Solicitud.java
@Entity
@Data

public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario; // reemplaza a profesor y personal

    @ManyToOne
    @JoinColumn(name = "id_asignatura")
    private Asignatura asignatura;

    @ManyToOne
    @JoinColumn(name = "id_laboratorio")
    private Laboratorio laboratorio;

    private LocalDate fechaSolicitud;
    private LocalDate fechaUso;
    private LocalTime horario;
    private Integer cantGrupos;
    private Boolean estado;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private List<SolicitudInsumo> insumos;
}
