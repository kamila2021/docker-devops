package com.arquitectura.proyecto.repository;

import com.arquitectura.proyecto.model.Insumo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    List<Insumo> findByStockDisponibleGreaterThan(int stockDisponible);
}
