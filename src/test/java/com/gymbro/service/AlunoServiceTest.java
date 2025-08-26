package com.gymbro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gymbro.dto.AlunoDTO;
import com.gymbro.model.Aluno;
import com.gymbro.repository.AlunoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AlunoService")
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AlunoService alunoService;

    private AlunoDTO alunoDTO;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        alunoDTO = new AlunoDTO();
        alunoDTO.setNome("João Silva");
        alunoDTO.setEmail("joao@email.com");
        alunoDTO.setSenha("123456");
        alunoDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        alunoDTO.setPeso(new BigDecimal("75.5"));

        aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setSenhaHash("hashedPassword");
        aluno.setDataNascimento(LocalDate.of(1990, 1, 1));
        aluno.setPeso(new BigDecimal("75.5"));
    }

    @Test
    @DisplayName("Deve criar aluno com sucesso")
    void deveCriarAlunoComSucesso() {
        // Given
        when(alunoRepository.existsByEmail(alunoDTO.getEmail())).thenReturn(false);
        when(modelMapper.map(alunoDTO, Aluno.class)).thenReturn(aluno);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);
        when(modelMapper.map(aluno, AlunoDTO.class)).thenReturn(alunoDTO);

        // When
        AlunoDTO resultado = alunoService.criarAluno(alunoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(alunoDTO.getNome(), resultado.getNome());
        assertEquals(alunoDTO.getEmail(), resultado.getEmail());
        verify(alunoRepository).existsByEmail(alunoDTO.getEmail());
        verify(passwordEncoder).encode(anyString());
        verify(alunoRepository).save(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar aluno com email existente")
    void deveLancarExcecaoAoTentarCriarAlunoComEmailExistente() {
        // Given
        when(alunoRepository.existsByEmail(alunoDTO.getEmail())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> alunoService.criarAluno(alunoDTO)
        );
        assertEquals("Email já está em uso", exception.getMessage());
        verify(alunoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosOsAlunos() {
        // Given
        List<Aluno> alunos = Arrays.asList(aluno);
        when(alunoRepository.findAll()).thenReturn(alunos);
        when(modelMapper.map(any(Aluno.class), eq(AlunoDTO.class))).thenReturn(alunoDTO);

        // When
        List<AlunoDTO> resultado = alunoService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(alunoRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar aluno por ID")
    void deveBuscarAlunoPorId() {
        // Given
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(modelMapper.map(aluno, AlunoDTO.class)).thenReturn(alunoDTO);

        // When
        Optional<AlunoDTO> resultado = alunoService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(alunoDTO.getNome(), resultado.get().getNome());
        verify(alunoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando aluno não encontrado")
    void deveRetornarOptionalVazioQuandoAlunoNaoEncontrado() {
        // Given
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<AlunoDTO> resultado = alunoService.buscarPorId(1L);

        // Then
        assertFalse(resultado.isPresent());
        verify(alunoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve atualizar aluno com sucesso")
    void deveAtualizarAlunoComSucesso() {
        // Given
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);
        when(modelMapper.map(aluno, AlunoDTO.class)).thenReturn(alunoDTO);

        // When
        AlunoDTO resultado = alunoService.atualizarAluno(1L, alunoDTO);

        // Then
        assertNotNull(resultado);
        verify(alunoRepository).findById(1L);
        verify(alunoRepository).save(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar aluno inexistente")
    void deveLancarExcecaoAoTentarAtualizarAlunoInexistente() {
        // Given
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            java.util.NoSuchElementException.class,
            () -> alunoService.atualizarAluno(1L, alunoDTO)
        );
        verify(alunoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar aluno com sucesso")
    void deveDeletarAlunoComSucesso() {
        // Given
        when(alunoRepository.existsById(1L)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> alunoService.deletarAluno(1L));

        // Then
        verify(alunoRepository).existsById(1L);
        verify(alunoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar aluno inexistente")
    void deveLancarExcecaoAoTentarDeletarAlunoInexistente() {
        // Given
        when(alunoRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(
            java.util.NoSuchElementException.class,
            () -> alunoService.deletarAluno(1L)
        );
        verify(alunoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve buscar alunos por faixa de peso")
    void deveBuscarAlunosPorFaixaDePeso() {
        // Given
        BigDecimal pesoMin = new BigDecimal("70");
        BigDecimal pesoMax = new BigDecimal("80");
        List<Aluno> alunos = Arrays.asList(aluno);
        
        when(alunoRepository.findByPesoBetween(pesoMin, pesoMax)).thenReturn(alunos);
        when(modelMapper.map(any(Aluno.class), eq(AlunoDTO.class))).thenReturn(alunoDTO);

        // When
        List<AlunoDTO> resultado = alunoService.buscarPorFaixaPeso(pesoMin, pesoMax);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(alunoRepository).findByPesoBetween(pesoMin, pesoMax);
    }
}
