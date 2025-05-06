package com.arquitectura.proyecto.resolver;

import com.arquitectura.proyecto.dto.InsumoInput;
import com.arquitectura.proyecto.model.Insumo;
import com.arquitectura.proyecto.service.InsumoService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class InsumoResolver {

    private final InsumoService insumoService;

    public InsumoResolver(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @QueryMapping
    public List<Insumo> listarInsumos() {
        return insumoService.listarInsumos();
    }

    @QueryMapping
    public List<Insumo> stockInsumosDisponibles() {
        return insumoService.sotckInsumosDisponibles();
    }

    @MutationMapping
    public Insumo crearInsumo(@Argument InsumoInput input) {
        return insumoService.crearInsumo(input);
    }

    @MutationMapping
    public Insumo modificarInsumo(@Argument Long id, @Argument InsumoInput input) {
        return insumoService.modificarInsumo(id, input);
    }

    @MutationMapping
    public Boolean eliminarInsumo(@Argument Long id) {
        insumoService.eliminarInsumo(id);
        return true;
    }
}
