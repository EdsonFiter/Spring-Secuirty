package com.devedson.security.service;

import com.devedson.security.domain.token.Token;
import com.devedson.security.domain.token.TokenType;
import com.devedson.security.dto.AuthenticationRequest;
import com.devedson.security.dto.AuthenticationResponse;
import com.devedson.security.dto.RegisterRequest;
import com.devedson.security.domain.user.Role;
import com.devedson.security.domain.user.User;
import com.devedson.security.repository.TokenRepository;
import com.devedson.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
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
                .role(registerRequest.getRole())
                .build();
        var saveUser = userRepository.save(user);
        var jwtAccessToken = jwtService.generateAccessToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(jwtAccessToken, saveUser);

        return AuthenticationResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
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
        var jwtAccessToken = jwtService.generateAccessToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(jwtAccessToken, user);

        return AuthenticationResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
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

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtRefreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        jwtRefreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwtRefreshToken);
        if(userEmail != null){
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if(jwtService.isTokenValid(jwtRefreshToken, user)){
               var jwtAccessToken = jwtService.generateAccessToken(user);
               revokeAllUserTokens(user);
               saveUserToken(jwtAccessToken, user);
               var authResponse = AuthenticationResponse.builder()
                       .accessToken(jwtAccessToken)
                       .refreshToken(jwtRefreshToken)
                       .build();
               log.info("Edson Mário");
               new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
