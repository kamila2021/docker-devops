package com.arquitectura.proyecto.repository;

import com.arquitectura.proyecto.Enum.EnumRol;
import com.arquitectura.proyecto.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Rol,Long> {
    Rol findRoleByName(String name);

    boolean existsByName(String name);
    Optional<Rol> findById(Long id);


}