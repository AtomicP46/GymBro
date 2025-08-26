package com.gymbro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.gymbro.dto.ExercicioDTO;
import com.gymbro.enums.RegiaoCorpo;
import com.gymbro.enums.TipoExercicio;
import com.gymbro.model.Equipamento;
import com.gymbro.model.Exercicio;
import com.gymbro.repository.EquipamentoRepository;
import com.gymbro.repository.ExercicioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ExercicioService (ajustados aos enums do banco)")
class ExercicioServiceTest {

    @Mock
    private ExercicioRepository exercicioRepository;

    @Mock
    private EquipamentoRepository equipamentoRepository;

    @InjectMocks
    private ExercicioService exercicioService;

    private ExercicioDTO exercicioDTO;
    private Exercicio exercicio;
    private Equipamento equipamento;

    @BeforeEach
    void setUp() {
        exercicioDTO = new ExercicioDTO();
        exercicioDTO.setNome("Supino");
        // passa a descrição, não o name()
        exercicioDTO.setRegiao(RegiaoCorpo.PEITO.getDescricao());
        exercicioDTO.setTipo(TipoExercicio.FORCA_PESO_REPETICOES.getDescricao());
        exercicioDTO.setUnilateral(false);
        exercicioDTO.setEquipamentoId(1L);

        equipamento = new Equipamento();
        equipamento.setNome("Barra");

        exercicio = new Exercicio();
        ReflectionTestUtils.setField(exercicio, "id", 1L);
        exercicio.setNome("Supino");
        exercicio.setRegiao(RegiaoCorpo.PEITO);
        exercicio.setTipo(TipoExercicio.FORCA_PESO_REPETICOES);
        exercicio.setUnilateral(false);
        exercicio.setEquipamento(equipamento);
    }

    @Test
    @DisplayName("Deve criar exercício com sucesso")
    void deveCriarExercicioComSucesso() {
        when(exercicioRepository.existsByNomeIgnoreCase("Supino")).thenReturn(false);
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));
        when(exercicioRepository.save(any(Exercicio.class))).thenReturn(exercicio);

        Exercicio resultado = exercicioService.criarExercicio(exercicioDTO);

        assertNotNull(resultado);
        assertEquals("Supino", resultado.getNome());
        assertEquals(RegiaoCorpo.PEITO, resultado.getRegiao());
        assertEquals(TipoExercicio.FORCA_PESO_REPETICOES, resultado.getTipo());
        verify(exercicioRepository).existsByNomeIgnoreCase("Supino");
        verify(exercicioRepository).save(any(Exercicio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar exercício com nome existente")
    void deveLancarExcecaoAoCriarExercicioComNomeExistente() {
        when(exercicioRepository.existsByNomeIgnoreCase("Supino")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> exercicioService.criarExercicio(exercicioDTO)
        );
        assertEquals("Já existe um exercício com este nome", exception.getMessage());
        verify(exercicioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção para dados obrigatórios ausentes")
    void deveLancarExcecaoParaDadosObrigatoriosAusentes() {
        exercicioDTO.setNome(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> exercicioService.criarExercicio(exercicioDTO)
        );
        assertEquals("Nome do exercício é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar exercício por ID")
    void deveBuscarExercicioPorId() {
        when(exercicioRepository.findById(1L)).thenReturn(Optional.of(exercicio));

        Exercicio resultado = exercicioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Supino", resultado.getNome());
        verify(exercicioRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção para ID inválido")
    void deveLancarExcecaoParaIdInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> exercicioService.buscarPorId(0L)
        );
        assertEquals("ID deve ser positivo", exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar todos os exercícios")
    void deveListarTodosOsExercicios() {
        List<Exercicio> exercicios = Arrays.asList(exercicio);
        when(exercicioRepository.findAll()).thenReturn(exercicios);

        List<Exercicio> resultado = exercicioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(exercicioRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar exercícios por nome")
    void deveBuscarExerciciosPorNome() {
        List<Exercicio> exercicios = Arrays.asList(exercicio);
        when(exercicioRepository.findByNomeContainingIgnoreCaseOrderByNome("Sup"))
            .thenReturn(exercicios);

        List<Exercicio> resultado = exercicioService.buscarPorNome("Sup");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(exercicioRepository)
            .findByNomeContainingIgnoreCaseOrderByNome("Sup");
    }

    @Test
    @DisplayName("Deve buscar exercícios por região")
    void deveBuscarExerciciosPorRegiao() {
        List<Exercicio> exercicios = Arrays.asList(exercicio);
        when(exercicioRepository.findByRegiaoOrderByNome(RegiaoCorpo.PEITO))
            .thenReturn(exercicios);

        // busca pela descrição
        List<Exercicio> resultado = exercicioService.buscarPorRegiao(RegiaoCorpo.PEITO.getDescricao());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(exercicioRepository)
            .findByRegiaoOrderByNome(RegiaoCorpo.PEITO);
    }

    @Test
    @DisplayName("Deve atualizar exercício com sucesso")
    void deveAtualizarExercicioComSucesso() {
        ExercicioDTO novoDTO = new ExercicioDTO();
        novoDTO.setNome("Supino Inclinado");

        when(exercicioRepository.findById(1L)).thenReturn(Optional.of(exercicio));
        when(exercicioRepository
                .existsByNomeIgnoreCaseAndIdNot("Supino Inclinado", 1L))
            .thenReturn(false);
        when(exercicioRepository.save(any(Exercicio.class))).thenReturn(exercicio);

        Exercicio resultado = exercicioService.atualizarExercicio(1L, novoDTO);

        assertNotNull(resultado);
        verify(exercicioRepository).findById(1L);
        verify(exercicioRepository)
            .existsByNomeIgnoreCaseAndIdNot("Supino Inclinado", 1L);
        verify(exercicioRepository).save(any(Exercicio.class));
    }

    @Test
    @DisplayName("Deve deletar exercício com sucesso")
    void deveDeletarExercicioComSucesso() {
        when(exercicioRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> exercicioService.deletarExercicio(1L));

        verify(exercicioRepository).existsById(1L);
        verify(exercicioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve contar exercícios por região")
    void deveContarExerciciosPorRegiao() {
        when(exercicioRepository.countByRegiao(RegiaoCorpo.PEITO)).thenReturn(5L);

        long resultado = exercicioService.contarPorRegiao(RegiaoCorpo.PEITO.getDescricao());

        assertEquals(5L, resultado);
        verify(exercicioRepository).countByRegiao(RegiaoCorpo.PEITO);
    }

    @Test
    @DisplayName("Deve verificar se exercício existe")
    void deveVerificarSeExercicioExiste() {
        when(exercicioRepository.existsById(1L)).thenReturn(true);

        boolean resultado = exercicioService.existeExercicio(1L);

        assertTrue(resultado);
        verify(exercicioRepository).existsById(1L);
    }
}