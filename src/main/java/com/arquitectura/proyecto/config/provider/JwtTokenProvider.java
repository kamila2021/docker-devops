package com.arquitectura.proyecto.config.provider;

import com.arquitectura.proyecto.model.Token;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.arquitectura.proyecto.repository.UsuarioRepository;
import com.arquitectura.proyecto.service.UserDetailsServiceImpl;
import com.arquitectura.proyecto.repository.TokenRepository;
import com.arquitectura.proyecto.service.UserDetailsImpl;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static java.util.Arrays.stream;

@Configuration
public class JwtTokenProvider {

    private static final Logger log = LogManager.getLogger(JwtTokenProvider.class);

    private final UserDetailsServiceImpl userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository;

    String secret = "secretkey";

    public JwtTokenProvider(UserDetailsServiceImpl userDetailsService, UsuarioRepository usuarioRepository, TokenRepository tokenRepository) {
        this.userDetailsService = userDetailsService;
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
    }

    public String generateAccessToken(UserDetailsImpl user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 10000))
                .withClaim("id", user.getUserId())
                .withClaim("nombreCompleto", user.getFirstName() + " " + user.getLastName())
                .withClaim("correo", user.getEmail())
                .withClaim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("roles", user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()))
                .withClaim("opciones", user.getOptions().stream().map(o -> o.toString()).collect(Collectors.toList()))
                .withClaim("permisos", user.getPermissions().stream().map(p -> p).collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(this.secret.getBytes()));
    }

    public String generateRefreshToken(UserDetailsImpl user) {
        try {
            String token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 10000))
                    .sign(Algorithm.HMAC256(this.secret.getBytes()));

            if (token != null || !token.isEmpty()) {
                Token refToken = new Token();
                refToken.setToken(token);
                refToken.setUser(this.usuarioRepository.getById(user.getUserId()));
                refToken.setStatus(1);
                this.tokenRepository.save(refToken);
                return token;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean validateToken(String token) throws RuntimeException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (RuntimeException ex) {
            log.error("Invalid JWT signature");
            return false;
        }
    }

    public Authentication getUsernameAndPasswordAuth(String token) {
        try{
            DecodedJWT decodedJWT = this.decodeToken(token);
            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            stream(roles).forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role));
            });
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Invalid JWT signature");
        }

    }

    private DecodedJWT decodeToken(String token) throws RuntimeException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Invalid JWT signature");
        }
    }

    public HashMap<String, String> refreshTokens(String refreshToken) throws RuntimeException {
        log.info("{} [REFRESHING TOKENS] Loading refresh token and access token.");
        if (!this.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        DecodedJWT decodedJWT = decodeToken(refreshToken);
        String username = decodedJWT.getSubject();
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        HashMap<String, String> newTokens = new HashMap<>();
        newTokens.put("access_token", this.generateAccessToken(userDetails));
        newTokens.put("refresh_token", this.generateRefreshToken(userDetails));
        return newTokens;
    }

    public Long getUserIdFromToken(String token) throws Exception {
        try {
            DecodedJWT decodedJWT = this.decodeToken(token);
            Long userId = decodedJWT.getClaim("id").asLong();
            return userId;
        } catch (Exception ex) {
            throw new Exception("Invalid JWT signature");
        }
    }
}