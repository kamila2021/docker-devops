package com.arquitectura.proyecto.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private CustomAuthenticationFilter authenticationFilter;
    private UserDetails userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        userDetails = new User(
            "test@test.com",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
    }

    @Test
    void attemptAuthenticationWithValidCredentials() {
        when(request.getParameter("username")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("password");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        Authentication result = authenticationFilter.attemptAuthentication(request, response);

        assertNotNull(result);
        assertEquals("test@test.com", result.getName());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void successfulAuthenticationShouldGenerateTokens() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        StringBuffer requestURL = new StringBuffer("http://localhost:8080/login");

        when(response.getWriter()).thenReturn(writer);
        when(request.getRequestURL()).thenReturn(requestURL);

        authenticationFilter.successfulAuthentication(request, response, filterChain, authentication);

        String responseBody = stringWriter.toString();
        assertTrue(responseBody.contains("access_token"));
        assertTrue(responseBody.contains("refresh_token"));
        assertTrue(responseBody.contains("test@test.com"));
        assertTrue(responseBody.contains("ROLE_USER"));
    }

    @Test
    void attemptAuthenticationWithInvalidCredentials() {
        when(request.getParameter("username")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            authenticationFilter.attemptAuthentication(request, response);
        });
    }

    @Test
    void successfulAuthenticationShouldSetResponseHeaders() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        StringBuffer requestURL = new StringBuffer("http://localhost:8080/login");

        when(response.getWriter()).thenReturn(writer);
        when(request.getRequestURL()).thenReturn(requestURL);

        authenticationFilter.successfulAuthentication(request, response, filterChain, authentication);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
    }
} 