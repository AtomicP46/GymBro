package com.gymbro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymbro.dto.LoginRequestDTO;
import com.gymbro.dto.LoginResponseDTO;
import com.gymbro.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO loginResponse = authService.login(loginRequestDTO);
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException ex) {
            // credenciais inválidas → 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarCredenciais(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        boolean valid = authService.validarCredenciais(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getSenha()
        );
        return ResponseEntity.ok(valid);
    }
}