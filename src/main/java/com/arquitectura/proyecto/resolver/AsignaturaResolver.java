package com.arquitectura.proyecto.resolver;

import com.arquitectura.proyecto.dto.AsignaturaDto;
import com.arquitectura.proyecto.model.Asignatura;
import com.arquitectura.proyecto.service.AsignaturaService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AsignaturaResolver {

    private final AsignaturaService asignaturaService;

    public AsignaturaResolver(AsignaturaService asignaturaService) {
        this.asignaturaService = asignaturaService;
    }

    @MutationMapping
    public Asignatura crearAsignatura(@Argument String nombre, @Argument String codigo) {
        AsignaturaDto dto = new AsignaturaDto();
        dto.setNombre(nombre);
        dto.setCodigo(codigo);
        return asignaturaService.crearAsignatura(dto);
    }


    @QueryMapping
    public List<Asignatura> listarAsignaturas() {
        return asignaturaService.listarAsignaturas();
    }

    // @MutationMapping
    // public Insumo crearInsumo(@Argument InsumoInput input) {
    //     return insumoService.crearInsumo(input);
    // }

    // @MutationMapping
    // public Insumo modificarInsumo(@Argument Long id, @Argument InsumoInput input) {
    //     return insumoService.modificarInsumo(id, input);
    // }

    // @MutationMapping
    // public Boolean eliminarInsumo(@Argument Long id) {
    //     insumoService.eliminarInsumo(id);
    //     return true;
    // }
}
