package com.example.chatbothexagonal.auth.application.usecase;

import com.example.chatbothexagonal.auth.application.dto.AuthResult;
import com.example.chatbothexagonal.auth.application.port.in.LoginUseCase;
import com.example.chatbothexagonal.auth.application.port.out.LoadUserPort;
import com.example.chatbothexagonal.auth.application.port.out.PasswordHasherPort;
import com.example.chatbothexagonal.auth.application.port.out.TokenEncoderPort;
import com.example.chatbothexagonal.auth.domain.exception.UserNotFoundException;
import com.example.chatbothexagonal.auth.domain.model.User;
import com.example.chatbothexagonal.auth.domain.valueObject.Email;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoginService implements LoginUseCase {

    private final LoadUserPort loadUserPort;
    private final PasswordHasherPort passwordHasherPort;
    private final TokenEncoderPort tokenEncoderPort;

    public LoginService(LoadUserPort loadUserPort,
                        PasswordHasherPort passwordHasherPort,
                        TokenEncoderPort tokenEncoderPort) {
        this.loadUserPort = loadUserPort;
        this.passwordHasherPort = passwordHasherPort;
        this.tokenEncoderPort = tokenEncoderPort;
    }

    @Override
    public AuthResult handle(String email, String rawPassword) {

        User user = loadUserPort.findByEmail(Email.of(email))
                .orElseThrow(() -> UserNotFoundException.byEmail(email));

        if (!passwordHasherPort.matches(rawPassword, user.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = tokenEncoderPort.generateAccessToken(
                user.getId(),
                user.getName(),
                user.getEmail().getValue()
        );

        return new AuthResult(
                "Bearer",
                token,
                tokenEncoderPort.accessTokenExpiresInSeconds(),
                user.getId(),
                user.getEmail().getValue(),
                user.getName()
        );
    }
}
