package com.arquitectura.proyecto.repository;

import com.arquitectura.proyecto.model.SolicitudInsumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudInsumoRepository extends JpaRepository<SolicitudInsumo, Long> {
    List<SolicitudInsumo> findBySolicitudId(Long idSolicitud);
}
