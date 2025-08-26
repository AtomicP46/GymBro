package com.gymbro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Base64;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.gymbro.dto.LoginRequestDTO;
import com.gymbro.dto.LoginResponseDTO;
import com.gymbro.model.Aluno;
import com.gymbro.model.Personal;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AuthService")
class AuthServiceTest {

    @Mock
    private AlunoService alunoService;

    @Mock
    private PersonalService personalService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private LoginRequestDTO loginRequest;
    private Aluno aluno;
    private Personal personal;

    @BeforeEach
    void setUp() {
        // Gera secret HS256 de 256 bits e codifica em Base64
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());
        ReflectionTestUtils.setField(authService, "jwtSecret", base64Secret);
        ReflectionTestUtils.setField(authService, "jwtExpirationMs", 86_400_000L);

        loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("test@email.com");
        loginRequest.setSenha("123456");

        // Popula 'aluno' incluindo o id via ReflectionTestUtils
        aluno = new Aluno();
        ReflectionTestUtils.setField(aluno, "id", 1L);
        aluno.setNome("João Aluno");
        aluno.setEmail(loginRequest.getEmail());
        aluno.setSenhaHash("hashedPassword");

        // Popula 'personal' incluindo o id via ReflectionTestUtils
        personal = new Personal();
        ReflectionTestUtils.setField(personal, "id", 2L);
        personal.setNome("João Personal");
        personal.setEmail(loginRequest.getEmail());
        personal.setSenhaHash("hashedPassword");
    }

    @Test
    @DisplayName("Deve fazer login de aluno com sucesso")
    void deveFazerLoginDeAlunoComSucesso() {
        when(alunoService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.of(aluno));
        when(passwordEncoder.matches(loginRequest.getSenha(), aluno.getSenhaHash()))
            .thenReturn(true);

        LoginResponseDTO response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("Aluno", response.getTipoUsuario());
        assertEquals(aluno.getId(), response.getId());
        assertEquals(aluno.getNome(), response.getNome());
        assertEquals(aluno.getEmail(), response.getEmail());
        assertNotNull(response.getToken());

        verify(alunoService).buscarEntidadePorEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getSenha(), aluno.getSenhaHash());
    }

    @Test
    @DisplayName("Deve fazer login de personal com sucesso")
    void deveFazerLoginDePersonalComSucesso() {
        when(alunoService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.empty());
        when(personalService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.of(personal));
        when(passwordEncoder.matches(loginRequest.getSenha(), personal.getSenhaHash()))
            .thenReturn(true);

        LoginResponseDTO response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("Personal", response.getTipoUsuario());
        assertEquals(personal.getId(), response.getId());
        assertEquals(personal.getNome(), response.getNome());
        assertEquals(personal.getEmail(), response.getEmail());
        assertNotNull(response.getToken());

        verify(personalService).buscarEntidadePorEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getSenha(), personal.getSenhaHash());
    }

    @Test
    @DisplayName("Deve lançar exceção para credenciais inválidas")
    void deveLancarExcecaoParaCredenciaisInvalidas() {
        when(alunoService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.empty());
        when(personalService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(
            BadCredentialsException.class,
            () -> authService.login(loginRequest)
        );
        assertEquals("Credenciais inválidas", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção para senha incorreta de aluno")
    void deveLancarExcecaoParaSenhaIncorretaDeAluno() {
        when(alunoService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.of(aluno));
        when(passwordEncoder.matches(loginRequest.getSenha(), aluno.getSenhaHash()))
            .thenReturn(false);
        when(personalService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(
            BadCredentialsException.class,
            () -> authService.login(loginRequest)
        );
        assertEquals("Credenciais inválidas", exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar credenciais de aluno corretamente")
    void deveValidarCredenciaisDeAlunoCorretamente() {
        when(alunoService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.of(aluno));
        when(passwordEncoder.matches(loginRequest.getSenha(), aluno.getSenhaHash()))
            .thenReturn(true);

        boolean resultado = authService.validarCredenciais(
            loginRequest.getEmail(),
            loginRequest.getSenha()
        );

        assertTrue(resultado);
        verify(alunoService).buscarEntidadePorEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getSenha(), aluno.getSenhaHash());
    }

    @Test
    @DisplayName("Deve validar credenciais de personal corretamente")
    void deveValidarCredenciaisDePersonalCorretamente() {
        when(alunoService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.empty());
        when(personalService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.of(personal));
        when(passwordEncoder.matches(loginRequest.getSenha(), personal.getSenhaHash()))
            .thenReturn(true);

        boolean resultado = authService.validarCredenciais(
            loginRequest.getEmail(),
            loginRequest.getSenha()
        );

        assertTrue(resultado);
        verify(personalService).buscarEntidadePorEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getSenha(), personal.getSenhaHash());
    }

    @Test
    @DisplayName("Deve retornar false para credenciais inválidas")
    void deveRetornarFalseParaCredenciaisInvalidas() {
        when(alunoService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.empty());
        when(personalService.buscarEntidadePorEmail(loginRequest.getEmail()))
            .thenReturn(Optional.empty());

        boolean resultado = authService.validarCredenciais(
            loginRequest.getEmail(),
            loginRequest.getSenha()
        );

        assertFalse(resultado);
    }
}