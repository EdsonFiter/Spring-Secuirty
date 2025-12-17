package com.devedson.security.service;

import com.devedson.security.auth.JwtService;
import com.devedson.security.domain.token.Token;
import com.devedson.security.domain.token.TokenType;
import com.devedson.security.dto.AuthenticationRequest;
import com.devedson.security.dto.AuthenticationResponse;
import com.devedson.security.dto.RegisterRequest;
import com.devedson.security.domain.user.Role;
import com.devedson.security.domain.user.User;
import com.devedson.security.repository.TokenRepository;
import com.devedson.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        var saveUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(jwtToken, saveUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(jwtToken, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user){
        var allValidToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if(allValidToken.isEmpty())
            return;
        allValidToken.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(allValidToken);
    }

    private void saveUserToken(String jwtToken, User saveUser) {
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.Bearer)
                .revoked(false)
                .expired(false)
                .user(saveUser)
                .build();
        tokenRepository.save(token);
    }

}
