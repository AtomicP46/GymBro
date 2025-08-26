package com.gymbro.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;

import com.gymbro.dto.ExercicioProgressoDTO;
import com.gymbro.dto.TreinoExercicioDTO;
import com.gymbro.exception.BusinessException;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.model.TreinoExercicio;
import com.gymbro.repository.ExercicioRepository;
import com.gymbro.repository.TreinoExercicioRepository;
import com.gymbro.repository.TreinoRepository;

@ExtendWith(MockitoExtension.class)
class TreinoExercicioServiceTest {

    @Mock
    private TreinoExercicioRepository treinoExercicioRepository;

    @Mock
    private TreinoRepository treinoRepository;

    @Mock
    private ExercicioRepository exercicioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TreinoExercicioService treinoExercicioService;

    private TreinoExercicioDTO treinoExercicioDTO;
    private TreinoExercicio treinoExercicio;

    @BeforeEach
    void setUp() {
        treinoExercicioDTO = new TreinoExercicioDTO();
        treinoExercicioDTO.setTreinoId(1L);
        treinoExercicioDTO.setExercicioId(1L);
        treinoExercicioDTO.setAquecimento(false);

        treinoExercicio = new TreinoExercicio();
        treinoExercicio.setId(1L);                    // Definir ID para os asserts
        treinoExercicio.setTreinoId(1L);
        treinoExercicio.setExercicioId(1L);
        treinoExercicio.setSeries(3);
        treinoExercicio.setRepeticoes(12);
        treinoExercicio.setAquecimento(false);
    }

    @Test
    void adicionarExercicioAoTreino_DeveRetornarTreinoExercicio_QuandoDadosValidos() {
        when(treinoRepository.existsById(1L)).thenReturn(true);
        when(exercicioRepository.existsById(1L)).thenReturn(true);
        when(treinoExercicioRepository.existsByTreinoIdAndExercicioId(1L, 1L)).thenReturn(false);
        when(modelMapper.map(treinoExercicioDTO, TreinoExercicio.class))
            .thenReturn(treinoExercicio);
        when(treinoExercicioRepository.save(any(TreinoExercicio.class)))
            .thenReturn(treinoExercicio);

        TreinoExercicio resultado =
            treinoExercicioService.adicionarExercicioAoTreino(treinoExercicioDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(treinoExercicioRepository).save(any(TreinoExercicio.class));
    }

    @Test
    void adicionarExercicioAoTreino_DeveLancarResourceNotFoundException_QuandoTreinoNaoExiste() {
        when(treinoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
            () -> treinoExercicioService.adicionarExercicioAoTreino(treinoExercicioDTO));

        verify(treinoExercicioRepository, never()).save(any());
    }

    @Test
    void adicionarExercicioAoTreino_DeveLancarResourceNotFoundException_QuandoExercicioNaoExiste() {
        when(treinoRepository.existsById(1L)).thenReturn(true);
        when(exercicioRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
            () -> treinoExercicioService.adicionarExercicioAoTreino(treinoExercicioDTO));

        verify(treinoExercicioRepository, never()).save(any());
    }

    @Test
    void adicionarExercicioAoTreino_DeveLancarBusinessException_QuandoExercicioJaExisteNoTreino() {
        when(treinoRepository.existsById(1L)).thenReturn(true);
        when(exercicioRepository.existsById(1L)).thenReturn(true);
        when(treinoExercicioRepository.existsByTreinoIdAndExercicioId(1L, 1L)).thenReturn(true);

        assertThrows(BusinessException.class,
            () -> treinoExercicioService.adicionarExercicioAoTreino(treinoExercicioDTO));

        verify(treinoExercicioRepository, never()).save(any());
    }

    @Test
    void buscarTreinoExercicioPorId_DeveRetornarOptional_QuandoEncontrado() {
        when(treinoExercicioRepository.findById(1L))
            .thenReturn(Optional.of(treinoExercicio));

        Optional<TreinoExercicio> resultado =
            treinoExercicioService.buscarTreinoExercicioPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void obterTreinoExercicioPorId_DeveRetornarTreinoExercicio_QuandoEncontrado() {
        when(treinoExercicioRepository.findById(1L))
            .thenReturn(Optional.of(treinoExercicio));

        TreinoExercicio resultado =
            treinoExercicioService.obterTreinoExercicioPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obterTreinoExercicioPorId_DeveLancarResourceNotFoundException_QuandoNaoEncontrado() {
        when(treinoExercicioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> treinoExercicioService.obterTreinoExercicioPorId(1L));
    }

    @Test
    void listarExerciciosDoTreino_DeveRetornarLista_QuandoTreinoExiste() {
        List<TreinoExercicio> exercicios = Arrays.asList(treinoExercicio);
        when(treinoRepository.existsById(1L)).thenReturn(true);
        when(treinoExercicioRepository.findByTreinoIdOrderById(1L)).thenReturn(exercicios);

        List<TreinoExercicio> resultado =
            treinoExercicioService.listarExerciciosDoTreino(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    void listarExerciciosDoTreino_DeveLancarResourceNotFoundException_QuandoTreinoNaoExiste() {
        when(treinoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
            () -> treinoExercicioService.listarExerciciosDoTreino(1L));
    }

    @Test
    void atualizarExercicioDoTreino_DeveRetornarTreinoExercicioAtualizado_QuandoEncontrado() {
        when(treinoExercicioRepository.findById(1L))
            .thenReturn(Optional.of(treinoExercicio));

        // map(Object, destination) é void: usamos doNothing()
        doNothing().when(modelMapper).map(treinoExercicioDTO, treinoExercicio);

        when(treinoExercicioRepository.save(any(TreinoExercicio.class)))
            .thenReturn(treinoExercicio);

        TreinoExercicio resultado =
            treinoExercicioService.atualizarExercicioDoTreino(1L, treinoExercicioDTO);

        assertNotNull(resultado);
        verify(modelMapper).map(treinoExercicioDTO, treinoExercicio);
        verify(treinoExercicioRepository).save(treinoExercicio);
    }

    @Test
    void removerExercicioDoTreino_DeveRemover_QuandoEncontrado() {
        when(treinoExercicioRepository.findById(1L))
            .thenReturn(Optional.of(treinoExercicio));

        assertDoesNotThrow(() -> treinoExercicioService.removerExercicioDoTreino(1L));

        verify(treinoExercicioRepository).deleteById(1L);
    }

    @Test
    void removerExercicioDoTreino_DeveLancarResourceNotFoundException_QuandoNaoEncontrado() {
        when(treinoExercicioRepository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> treinoExercicioService.removerExercicioDoTreino(1L));

        verify(treinoExercicioRepository, never()).deleteById(any());
    }

    @Test
    void registrarExecucao_DeveRetornarTreinoExercicioAtualizado_QuandoEncontrado() {
        when(treinoExercicioRepository.findById(1L))
            .thenReturn(Optional.of(treinoExercicio));
        when(treinoExercicioRepository.save(any(TreinoExercicio.class)))
            .thenReturn(treinoExercicio);

        TreinoExercicio resultado =
            treinoExercicioService.registrarExecucao(1L, 10, 50.0f, "Teste");

        assertNotNull(resultado);
        verify(treinoExercicioRepository).save(treinoExercicio);
    }

    @Test
    void listarExerciciosAquecimento_DeveRetornarApenasAquecimento() {
        TreinoExercicio aquecimento = new TreinoExercicio();
        aquecimento.setAquecimento(true);
        TreinoExercicio principal = new TreinoExercicio();
        principal.setAquecimento(false);

        List<TreinoExercicio> todos = Arrays.asList(aquecimento, principal);
        when(treinoRepository.existsById(1L)).thenReturn(true);
        when(treinoExercicioRepository.findByTreinoIdOrderById(1L)).thenReturn(todos);

        List<TreinoExercicio> resultado =
            treinoExercicioService.listarExerciciosAquecimento(1L);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getAquecimento());
    }

    @Test
    void listarExerciciosPrincipais_DeveRetornarApenasPrincipais() {
        TreinoExercicio aquecimento = new TreinoExercicio();
        aquecimento.setAquecimento(true);
        TreinoExercicio principal = new TreinoExercicio();
        principal.setAquecimento(false);

        List<TreinoExercicio> todos = Arrays.asList(aquecimento, principal);
        when(treinoRepository.existsById(1L)).thenReturn(true);
        when(treinoExercicioRepository.findByTreinoIdOrderById(1L)).thenReturn(todos);

        List<TreinoExercicio> resultado =
            treinoExercicioService.listarExerciciosPrincipais(1L);

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).getAquecimento());
    }

    @Test
    void contarExerciciosPorTreino_DeveRetornarQuantidadeCorreta() {
        List<TreinoExercicio> exercicios = Arrays.asList(treinoExercicio, treinoExercicio);
        when(treinoRepository.existsById(1L)).thenReturn(true);
        when(treinoExercicioRepository.findByTreinoIdOrderById(1L)).thenReturn(exercicios);

        long resultado = treinoExercicioService.contarExerciciosPorTreino(1L);

        assertEquals(2, resultado);
    }

    @Test
    void exercicioExisteNoTreino_DeveRetornarTrue_QuandoExiste() {
        when(treinoExercicioRepository.existsByTreinoIdAndExercicioId(1L, 1L))
            .thenReturn(true);

        boolean resultado =
            treinoExercicioService.exercicioExisteNoTreino(1L, 1L);

        assertTrue(resultado);
    }

    @Test
    void gerarRelatorioProgressoExercicio_DeveRetornarRelatorio() {
        List<ExercicioProgressoDTO> progresso = Arrays.asList(new ExercicioProgressoDTO());
        when(treinoExercicioRepository.findHistoricoExercicioPorUsuario(1L, 1L))
            .thenReturn(progresso);

        List<ExercicioProgressoDTO> resultado =
            treinoExercicioService.gerarRelatorioProgressoExercicio(1L, 1L);

        assertEquals(1, resultado.size());
    }
}