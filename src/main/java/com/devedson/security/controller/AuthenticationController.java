package com.devedson.security.controller;

import com.devedson.security.dto.AuthenticationRequest;
import com.devedson.security.dto.AuthenticationResponse;
import com.devedson.security.dto.RegisterRequest;
import com.devedson.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ){
        return new ResponseEntity<>( authService.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ){
        return new ResponseEntity<>(authService.authenticate(authenticationRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }
}
