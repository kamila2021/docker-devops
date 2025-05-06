package com.arquitectura.proyecto.repository;


import com.arquitectura.proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findById(long id);

    Usuario findByEmail(String email);
    Usuario save(Usuario usuario);
    @Query("SELECT u FROM Usuario u WHERE u.resetPasswordToken= ?1")
    Usuario findByResetPasswordToken(String token);

    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.id = ?1")
    List<Usuario> findAllByRoles_Id(Long roleId);



}