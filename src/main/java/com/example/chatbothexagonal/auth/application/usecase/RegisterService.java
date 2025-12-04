package com.example.chatbothexagonal.auth.application.usecase;

import com.example.chatbothexagonal.auth.application.dto.AuthResult;
import com.example.chatbothexagonal.auth.application.port.in.RegisterUseCase;
import com.example.chatbothexagonal.auth.application.port.out.LoadUserPort;
import com.example.chatbothexagonal.auth.application.port.out.PasswordHasherPort;
import com.example.chatbothexagonal.auth.application.port.out.SaveUserPort;
import com.example.chatbothexagonal.auth.application.port.out.TokenEncoderPort;
import com.example.chatbothexagonal.auth.domain.exception.UserAlreadyExistsException;
import com.example.chatbothexagonal.auth.domain.model.User;
import com.example.chatbothexagonal.auth.domain.valueObject.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@Transactional
public class RegisterService implements RegisterUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final PasswordHasherPort passwordHasherPort;
    private final TokenEncoderPort tokenEncoderPort;
    private final Clock clock;

    public RegisterService(LoadUserPort loadUserPort,
                           SaveUserPort saveUserPort,
                           PasswordHasherPort passwordHasherPort,
                           TokenEncoderPort tokenEncoderPort,
                           Clock clock) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
        this.passwordHasherPort = passwordHasherPort;
        this.tokenEncoderPort = tokenEncoderPort;
        this.clock = clock;
    }

    @Override
    public AuthResult handle(String email, String name, String rawPassword) {

        Email emailVO = Email.of(email);

        if (loadUserPort.existsByEmail(emailVO)) {
            throw new UserAlreadyExistsException(emailVO.getValue());
        }

        Instant now = Instant.now(clock);
        String passwordHash = passwordHasherPort.hash(rawPassword);

        User user = User.createNew(name, emailVO, passwordHash, now);
        User userDB = saveUserPort.save(user);

        String token = tokenEncoderPort.generateAccessToken(
                userDB.getId(),
                userDB.getName(),
                userDB.getEmail().getValue()
        );

        return new AuthResult(
                "Bearer",
                token,
                tokenEncoderPort.accessTokenExpiresInSeconds(),
                userDB.getId(),
                userDB.getName(),
                userDB.getEmail().getValue()
        );
    }
}
