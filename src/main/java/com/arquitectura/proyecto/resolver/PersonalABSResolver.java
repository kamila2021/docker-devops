package com.arquitectura.proyecto.resolver;

import com.arquitectura.proyecto.dto.CrearSolicitudInput;
import com.arquitectura.proyecto.dto.InsumoCantidadInput;
import com.arquitectura.proyecto.dto.SolicitudInput;
import com.arquitectura.proyecto.model.Solicitud;
import com.arquitectura.proyecto.service.PersonalABSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class PersonalABSResolver {

    @Autowired
    private PersonalABSService personalABSService;

    @MutationMapping
    public Solicitud crearSolicitud(@Argument CrearSolicitudInput input) {
        return personalABSService.crearSolicitud(input);
    }

    @QueryMapping
    public List<Solicitud> listarSolicitudes() {
        return personalABSService.listarSolicitudes();
    }

    @QueryMapping
    public List<Solicitud> listarSolicitudesRechazadas() {
        return personalABSService.listarSolicitudesRechazadas();
    }

    @QueryMapping
    public List<Solicitud> listarSolicitudesAprobadas() {
        return personalABSService.listarSolicitudesAprobadas();
    }

    @QueryMapping
    public List<Solicitud> listarSolicitudesPorFechaUso() {
        return personalABSService.listarSolicitudesPorFechaUso();
    }

    @MutationMapping
    public Solicitud modificarSolicitud(@Argument Long idSolicitud, @Argument SolicitudInput input) {
        Solicitud datosActualizados = new Solicitud();
        datosActualizados.setFechaUso(LocalDate.parse(input.getFechaUso()));
        datosActualizados.setHorario(LocalTime.parse(input.getHorario()));
        datosActualizados.setCantGrupos(input.getCantGrupos());
        datosActualizados.setEstado(input.getEstado());

        return personalABSService.modificarSolicitud(idSolicitud, datosActualizados); // ✅ este sí tiene id
    }



    @MutationMapping
    public Boolean eliminarSolicitud(@Argument Long idSolicitud) {
        personalABSService.eliminarSolicitud(idSolicitud);
        return true;
    }


    // ✅ NUEVO: Obtener las solicitudes del profesor (por su ID)
    @QueryMapping
    public List<Solicitud> solicitudesDelProfesor(@Argument Long idUsuario) {
        return personalABSService.listarSolicitudesDelUsuario(idUsuario);
    }

    @MutationMapping
    public Solicitud confirmarSolicitud(@Argument Long idSolicitud) {
        return personalABSService.confirmarYActualizarSolicitud(idSolicitud);
    }




}
