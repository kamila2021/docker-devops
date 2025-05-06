package com.arquitectura.proyecto.dto;

import lombok.Data;

import java.util.List;

@Data
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private boolean accountLocked;
    private boolean enabled;
    private List<Long> roles;
}