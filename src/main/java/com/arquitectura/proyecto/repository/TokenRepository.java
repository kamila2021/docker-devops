package com.arquitectura.proyecto.repository;

import com.arquitectura.proyecto.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository  extends JpaRepository <Token, Long> {
}