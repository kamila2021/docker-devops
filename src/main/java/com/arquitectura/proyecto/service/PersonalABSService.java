package com.arquitectura.proyecto.service;

import com.arquitectura.proyecto.dto.CrearSolicitudInput;
import com.arquitectura.proyecto.dto.InsumoCantidadInput;
import com.arquitectura.proyecto.dto.SolicitudInput;
import com.arquitectura.proyecto.model.*;
import com.arquitectura.proyecto.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalABSService {

    private final SolicitudRepository solicitudRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final UsuarioRepository usuarioRepository;
    private final InsumoRepository insumoRepository;
    private final SolicitudInsumoRepository solicitudInsumoRepository;


    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    public List<Solicitud> listarSolicitudesDelUsuario(Long idUsuario) {
        return solicitudRepository.findByUsuarioIdOrderByFechaUsoAsc(idUsuario);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Solicitud> listarSolicitudesRechazadas() {
        return solicitudRepository.findByEstado(false);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Solicitud> listarSolicitudesAprobadas() {
        return solicitudRepository.findByEstado(true);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Solicitud> listarSolicitudesPorFechaUso(){
        return solicitudRepository.findAllByOrderByFechaUsoAsc();
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Solicitud crearSolicitud(CrearSolicitudInput input) {
        Solicitud solicitud = new Solicitud();
        solicitud.setUsuario(usuarioRepository.findById(input.getIdUsuario())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + input.getIdUsuario())));

        solicitud.setAsignatura(asignaturaRepository.findById(input.getIdAsignatura())
            .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con ID: " + input.getIdAsignatura())));

        solicitud.setLaboratorio(laboratorioRepository.findById(input.getIdLaboratorio())
            .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado con ID: " + input.getIdLaboratorio())));

        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setFechaUso(LocalDate.parse(input.getFechaUso()));
        solicitud.setHorario(LocalTime.parse(input.getHorario()));
        solicitud.setCantGrupos(input.getCantGrupos());
        solicitud.setEstado(false); // aún no confirmada

        // Guardar primero la solicitud
        Solicitud saved = solicitudRepository.save(solicitud);

        // Guardar cada insumo solicitado
        for (InsumoCantidadInput i : input.getInsumos()) {
            Insumo insumo = insumoRepository.findById(i.getIdInsumo())
             .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + i.getIdInsumo()));


            SolicitudInsumo si = new SolicitudInsumo();
            si.setSolicitud(saved);
            si.setInsumo(insumo);
            si.setCantidad(i.getCantidad());

            solicitudInsumoRepository.save(si);
        }

        return saved;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    public Solicitud modificarSolicitud(Long idSolicitud, Solicitud datosActualizados) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setFechaUso(datosActualizados.getFechaUso());
        solicitud.setHorario(datosActualizados.getHorario());
        solicitud.setCantGrupos(datosActualizados.getCantGrupos());
        solicitud.setEstado(datosActualizados.getEstado());

        return solicitudRepository.save(solicitud); // 👈 este objeto ya tiene un ID
    }


    @PreAuthorize("hasRole('ROLE_Admin')")
    public void eliminarSolicitud(Long idSolicitud) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        List<SolicitudInsumo> insumos = solicitudInsumoRepository.findBySolicitudId(idSolicitud);
        solicitudInsumoRepository.deleteAll(insumos);

        solicitudRepository.delete(solicitud);
    }

    // MIRAR CON ATENCION ESTE CODIGO ⏰⏰⏰⏰⏰⏰⏰⏰⏰
    @PreAuthorize("hasRole('ROLE_Admin')")
    public void cancelarSolicitud(Long idSolicitud) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado(false); // o un campo como `cancelada = true`
        solicitudRepository.save(solicitud);
    }


    @Transactional
    @PreAuthorize("hasRole('ROLE_Admin')")
    public Solicitud confirmarYActualizarSolicitud(Long idSolicitud) {
        System.out.println("🔎 Buscando solicitud con ID: " + idSolicitud);

        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("❌ Solicitud no encontrada con ID: " + idSolicitud));

        System.out.println("✅ Solicitud encontrada. Estado actual: " + solicitud.getEstado());

        solicitud.setEstado(true);
        solicitudRepository.save(solicitud);

        System.out.println("📌 Estado de la solicitud actualizado a: true");

        // Buscar insumos asociados a la solicitud
        List<SolicitudInsumo> solicitudInsumos = solicitudInsumoRepository.findBySolicitudId(idSolicitud);

        System.out.println("📦 Cantidad de insumos asociados: " + solicitudInsumos.size());

        for (SolicitudInsumo si : solicitudInsumos) {
            Insumo insumo = si.getInsumo();

            if (insumo == null) {
                System.out.println("⚠️ El insumo de SolicitudInsumo ID " + si.getId() + " es null");
                continue;
            }

            System.out.println("➡️ Procesando insumo: " + insumo.getNombre());
            System.out.println("   - Stock disponible: " + insumo.getStockDisponible());
            System.out.println("   - Cantidad solicitada: " + si.getCantidad());

            int stockActual = insumo.getStockDisponible();
            int cantidadSolicitada = si.getCantidad().intValue() * solicitud.getCantGrupos().intValue(); // Multiplicamos por la cantidad de grupos

            int nuevoStock = stockActual - cantidadSolicitada;

            if (nuevoStock < 0) {
                System.out.println("❌ Stock insuficiente para el insumo: " + insumo.getNombre());
                throw new RuntimeException("❌ Stock insuficiente para el insumo: " + insumo.getNombre());
            }

            insumo.setStockDisponible(nuevoStock);
            insumoRepository.save(insumo);

            System.out.println("✅ Stock actualizado para " + insumo.getNombre() + ": nuevo stock = " + nuevoStock);
        }

        System.out.println("🎉 Confirmación y actualización de stock completada para la solicitud ID: " + idSolicitud);
        return solicitud;
    }
}