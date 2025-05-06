package com.arquitectura.proyecto.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleTokenExpiredException() {
        TokenExpiredException exception = new TokenExpiredException("Token has expired");
        ResponseEntity<ApiError> response = exceptionHandler.handleTokenExpiredException(exception);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Token expirado", response.getBody().getError());
    }

    @Test
    void handleTokenInvalidException() {
        TokenInvalidException exception = new TokenInvalidException("Invalid token");
        ResponseEntity<ApiError> response = exceptionHandler.handleTokenInvalidException(exception);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Token inválido", response.getBody().getError());
    }

    @Test
    void handleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");
        ResponseEntity<ApiError> response = exceptionHandler.handleBadCredentialsException(exception);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Credenciales inválidas", response.getBody().getError());
    }

    @Test
    void handleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        ResponseEntity<ApiError> response = exceptionHandler.handleAccessDeniedException(exception);
        
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acceso denegado", response.getBody().getError());
    }

    @Test
    void handleAuthenticationException() {
        AuthenticationException exception = mock(AuthenticationException.class);
        when(exception.getMessage()).thenReturn("Authentication failed");
        
        ResponseEntity<ApiError> response = exceptionHandler.handleAuthenticationException(exception);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error de autenticación", response.getBody().getError());
    }

    @Test
    void handleValidationExceptions() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "field", "message"));
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));
        
        ResponseEntity<ApiError> response = exceptionHandler.handleValidationExceptions(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error de validación", response.getBody().getError());
    }

    @Test
    void handleAllUncaughtException() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<ApiError> response = exceptionHandler.handleAllUncaughtException(exception);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getError());
    }
} 