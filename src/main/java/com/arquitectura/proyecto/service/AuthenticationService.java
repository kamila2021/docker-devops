package com.arquitectura.proyecto.service;


import com.arquitectura.proyecto.model.Rol;
import com.arquitectura.proyecto.model.Token;
import com.arquitectura.proyecto.model.Usuario;
import com.arquitectura.proyecto.repository.RoleRepository;
import com.arquitectura.proyecto.repository.TokenRepository;
import com.arquitectura.proyecto.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {



    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository userRepository;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    public void sendValidationEmail(Usuario user) throws MessagingException {
        try {
            var newToken = generateAndSaveToken(user);
            emailService.sendEmail(
                    user.getEmail(),
                    user.getApellido(),
                    newToken,
                    "Account activation"
            );
        } catch(Exception e) {
            throw new IllegalArgumentException("Error: " + e.getMessage());
        }

    }
    private String generateAndSaveToken(Usuario user) {

        String generatedToken= generateActivationCode(6);

        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .status(1) // 👈 aquí asignas un valor no nulo
                .build();
        user.setResetPasswordToken(generatedToken);
        userRepository.save(user);
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters="0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++ ){
            int randomIndex = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

//    public boolean validateToken(String token) throws TokenExpiredException {
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
//            JWTVerifier verifier = JWT.require(algorithm).build();
//            verifier.verify(token);
//            return true;
//        } catch (Exception ex) {
//            log.error("Invalid JWT signature");
//            return false;
//        } catch (TokenExpiredException tex) {
//            log.error("Expired token");
//            return false;
//        }
//    }

}