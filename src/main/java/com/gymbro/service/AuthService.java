package com.gymbro.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gymbro.dto.LoginRequestDTO;
import com.gymbro.dto.LoginResponseDTO;
import com.gymbro.model.Aluno;
import com.gymbro.model.Personal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthService {

    private final AlunoService alunoService;
    private final PersonalService personalService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    public AuthService(
        AlunoService alunoService,
        PersonalService personalService,
        PasswordEncoder passwordEncoder
    ) {
        this.alunoService    = alunoService;
        this.personalService = personalService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();

        // 1) Autentica Aluno
        var alunoOpt = alunoService.buscarEntidadePorEmail(email);
        if (alunoOpt.isPresent()) {
            Aluno aluno = alunoOpt.get();
            validarSenha(request.getSenha(), aluno.getSenhaHash());
            List<String> roles = List.of("ROLE_ALUNO");
            String token = createToken(aluno.getId(), aluno.getEmail(), roles);

            return new LoginResponseDTO(
                token,
                "Aluno",
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail()
            );
        }

        // 2) Autentica Personal
        var personalOpt = personalService.buscarEntidadePorEmail(email);
        if (personalOpt.isPresent()) {
            Personal personal = personalOpt.get();
            validarSenha(request.getSenha(), personal.getSenhaHash());
            List<String> roles = List.of("ROLE_PERSONAL");
            String token = createToken(personal.getId(), personal.getEmail(), roles);

            return new LoginResponseDTO(
                token,
                "Personal",
                personal.getId(),
                personal.getNome(),
                personal.getEmail()
            );
        }

        // 3) Nenhum encontrado
        throw new BadCredentialsException("Credenciais inválidas");
    }

    public boolean validarCredenciais(String emailRaw, String senhaRaw) {
        String email = emailRaw.trim().toLowerCase();

        boolean alunoOk = alunoService.buscarEntidadePorEmail(email)
            .filter(a -> passwordEncoder.matches(senhaRaw, a.getSenhaHash()))
            .isPresent();

        boolean personalOk = personalService.buscarEntidadePorEmail(email)
            .filter(p -> passwordEncoder.matches(senhaRaw, p.getSenhaHash()))
            .isPresent();

        return alunoOk || personalOk;
    }

    private void validarSenha(String raw, String hash) {
        if (!passwordEncoder.matches(raw, hash)) {
            throw new BadCredentialsException("Credenciais inválidas");
        }
    }

    private String createToken(Long userId, String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", userId);
        claims.put("roles", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(expiry)
                   .signWith(SignatureAlgorithm.HS256, jwtSecret)
                   .compact();
    }
}