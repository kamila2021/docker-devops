package com.arquitectura.proyecto.config.provider;

import com.arquitectura.proyecto.service.UsuarioDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationProviderTest {

    @Mock
    private UsuarioDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomAuthenticationProvider authenticationProvider;

    private UserDetails mockUserDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        mockUserDetails = new User(
            "test@test.com",
            "encodedPassword",
            true,
            true,
            true,
            true,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        authentication = new UsernamePasswordAuthenticationToken("test@test.com", "password");
    }

    @Test
    void authenticateSuccessfully() {
        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(mockUserDetails);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        Authentication result = authenticationProvider.authenticate(authentication);

        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals("test@test.com", result.getName());
    }

    @Test
    void authenticateWithInvalidPassword() {
        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(mockUserDetails);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authenticationProvider.authenticate(authentication);
        });
    }

    @Test
    void authenticateWithNullCredentials() {
        Authentication authWithNullCredentials = new UsernamePasswordAuthenticationToken("test@test.com", null);

        assertThrows(BadCredentialsException.class, () -> {
            authenticationProvider.authenticate(authWithNullCredentials);
        });
    }

    @Test
    void supportsUsernamePasswordAuthentication() {
        assertTrue(authenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
} 