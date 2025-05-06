package com.arquitectura.proyecto.resolver;

import com.arquitectura.proyecto.dto.UsuarioDto;
import com.arquitectura.proyecto.dto.UsuarioDto;
import com.arquitectura.proyecto.model.Usuario;
import com.arquitectura.proyecto.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsuarioResolver {

    private final UsuarioService usuarioService;

    @QueryMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @QueryMapping
    public List<Usuario> listarProfesores() {
        return usuarioService.listarProfesores();
    }

    @QueryMapping
    public Usuario obtenerUsuario(@Argument Long id) {
        return usuarioService.obtenerUsuario(id);
    }
    
    @QueryMapping
    public Usuario obtenerUsuarioPorEmail(@Argument String email) {
        return usuarioService.obtenerUsuarioPorEmail(email);
    }

    @MutationMapping
    public Boolean eliminarUsuario(@Argument Long usuarioId) {
        try {
            return this.usuarioService.eliminarUsuario(usuarioId).getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false; // Retorna false si hay algún error
        }
    }

    @MutationMapping
    public Usuario crearUsuario(@Argument UsuarioDto usuarioDto) {
        return this.usuarioService.crearUsuario(usuarioDto);
    }



    @MutationMapping
    public Boolean forgotPassword(@Argument String email) throws Exception {
        return usuarioService.forgotPassword(email);
    }
    @MutationMapping
    public UsuarioDto editarUsuario(@Argument UsuarioDto usuarioDto) {
        System.out.println("entroooo");
        return this.usuarioService.actualizarUsuario(usuarioDto);
    }
    @MutationMapping
    public Boolean updatePasswordByCode(@Argument String code, @Argument String password) throws Exception {
        return usuarioService.updatePasswordByCode(code, password);
    }

}