package com.arquitectura.proyecto.resolver;

import com.arquitectura.proyecto.dto.LaboratorioDto;
import com.arquitectura.proyecto.model.Laboratorio;
import com.arquitectura.proyecto.service.LaboratorioService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LaboratorioResolver {

    private final LaboratorioService laboratorioService;

    public LaboratorioResolver(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @MutationMapping
    public Laboratorio crearLaboratorio(@Argument String nombre, @Argument String codigo) {
        LaboratorioDto dto = new LaboratorioDto();
        dto.setNombre(nombre);
        dto.setCodigo(codigo);
        return laboratorioService.crearLaboratorio(dto);
    }

    @QueryMapping
    public List<Laboratorio> listarLaboratorios() {
        return laboratorioService.listarLaboratorios();
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
