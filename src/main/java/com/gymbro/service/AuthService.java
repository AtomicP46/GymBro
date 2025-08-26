package com.gymbro.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
        this.alunoService = alunoService;
        this.personalService = personalService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();

        return tentarAutenticarAluno(email, request.getSenha())
                .or(() -> tentarAutenticarPersonal(email, request.getSenha()))
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
    }

    private Optional<LoginResponseDTO> tentarAutenticarAluno(String email, String senha) {
        return alunoService.buscarEntidadePorEmail(email)
                .filter(aluno -> validarSenha(senha, aluno.getSenhaHash()))
                .map(criarResponseParaAluno());
    }

    private Optional<LoginResponseDTO> tentarAutenticarPersonal(String email, String senha) {
        return personalService.buscarEntidadePorEmail(email)
                .filter(personal -> validarSenha(senha, personal.getSenhaHash()))
                .map(criarResponseParaPersonal());
    }

    private Function<Aluno, LoginResponseDTO> criarResponseParaAluno() {
        return aluno -> {
            List<String> roles = List.of("ROLE_ALUNO");
            String token = createToken(aluno.getId(), aluno.getEmail(), roles);
            return new LoginResponseDTO(token, "Aluno", aluno.getId(), aluno.getNome(), aluno.getEmail());
        };
    }

    private Function<Personal, LoginResponseDTO> criarResponseParaPersonal() {
        return personal -> {
            List<String> roles = List.of("ROLE_PERSONAL");
            String token = createToken(personal.getId(), personal.getEmail(), roles);
            return new LoginResponseDTO(token, "Personal", personal.getId(), personal.getNome(), personal.getEmail());
        };
    }

    public boolean validarCredenciais(String emailRaw, String senhaRaw) {
        String email = emailRaw.trim().toLowerCase();

        boolean alunoValido = alunoService.buscarEntidadePorEmail(email)
                .map(aluno -> validarSenha(senhaRaw, aluno.getSenhaHash()))
                .orElse(false);

        boolean personalValido = personalService.buscarEntidadePorEmail(email)
                .map(personal -> validarSenha(senhaRaw, personal.getSenhaHash()))
                .orElse(false);

        return alunoValido || personalValido;
    }

    private boolean validarSenha(String raw, String hash) {
        return passwordEncoder.matches(raw, hash);
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
