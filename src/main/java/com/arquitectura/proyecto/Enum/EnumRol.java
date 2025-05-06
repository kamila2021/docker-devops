package com.arquitectura.proyecto.Enum;


import org.springframework.security.core.GrantedAuthority;

public enum EnumRol implements GrantedAuthority {
    Profesor,
    Admin;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
    public String getName() {
        return this.name();
    }
}