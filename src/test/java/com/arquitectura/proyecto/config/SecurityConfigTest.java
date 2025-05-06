package com.arquitectura.proyecto.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.arquitectura.proyecto.config.filters.CustomAuthenticationFilter;
import com.arquitectura.proyecto.service.UsuarioDetailsService;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @Mock
    private AuthenticationConfiguration authConfig;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioDetailsService userDetailsService;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void authenticationManagerShouldNotBeNull() throws Exception {
        // Arrange
        when(authConfig.getAuthenticationManager()).thenReturn(authenticationManager);

        // Act
        AuthenticationManager result = securityConfig.authenticationManager(authConfig);

        // Assert
        assertNotNull(result, "Authentication manager should not be null");
        verify(authConfig).getAuthenticationManager();
    }

    @Test
    void filterChainShouldConfigureSecurityFilters() throws Exception {
        // Arrange
        HttpSecurity http = mock(HttpSecurity.class);
        DefaultSecurityFilterChain defaultFilterChain = mock(DefaultSecurityFilterChain.class);

        // Mock chain calls
        when(http.csrf(any(Customizer.class))).thenReturn(http);
        when(http.authorizeHttpRequests(any(Customizer.class))).thenReturn(http);
        when(http.sessionManagement(any(Customizer.class))).thenReturn(http);
        when(http.addFilter(any(CustomAuthenticationFilter.class))).thenReturn(http);
        when(http.build()).thenReturn(defaultFilterChain);

        // Act
        SecurityFilterChain filterChain = securityConfig.filterChain(http, authenticationManager);

        // Assert
        assertNotNull(filterChain);
        verify(http).csrf(any(Customizer.class));
        verify(http).authorizeHttpRequests(any(Customizer.class));
        verify(http).sessionManagement(any(Customizer.class));
        verify(http).addFilter(any(CustomAuthenticationFilter.class));
        verify(http).build();
    }

    @Test
    void passwordEncoderShouldNotBeNull() {
        // Act
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder, "Password encoder should not be null");
    }




}