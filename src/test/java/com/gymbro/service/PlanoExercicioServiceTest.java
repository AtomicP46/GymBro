package com.gymbro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gymbro.dto.PlanoExercicioDTO;
import com.gymbro.exception.BusinessException;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.model.PlanoExercicio;
import com.gymbro.repository.ExercicioRepository;
import com.gymbro.repository.PlanoExercicioRepository;

@ExtendWith(MockitoExtension.class)
class PlanoExercicioServiceTest {

    @Mock
    private PlanoExercicioRepository planoExercicioRepository;

    @Mock
    private ExercicioRepository exercicioRepository;

    @InjectMocks
    private PlanoExercicioService planoExercicioService;

    private PlanoExercicioDTO dto;
    private PlanoExercicio entity;

    @BeforeEach
    void setUp() {
        dto = new PlanoExercicioDTO();
        dto.setPlanoId(1L);
        dto.setExercicioId(1L);
        dto.setSeriesSugeridas(3);
        dto.setObservacoes("Teste");
        dto.setAquecimento(false);

        entity = new PlanoExercicio(1L, 1L, 3, "Teste", false);
        entity.setId(1L);
    }

    @Test
    @DisplayName("adicionarExercicio: sucesso quando dados válidos")
    void adicionarExercicio_sucesso() {
        when(exercicioRepository.existsById(dto.getExercicioId())).thenReturn(true);
        when(planoExercicioRepository.existsByPlanoIdAndExercicioId(dto.getPlanoId(), dto.getExercicioId()))
            .thenReturn(false);
        when(planoExercicioRepository.save(any(PlanoExercicio.class))).thenReturn(entity);

        PlanoExercicio result = planoExercicioService.adicionarExercicio(dto);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());

        verify(exercicioRepository).existsById(dto.getExercicioId());
        verify(planoExercicioRepository).existsByPlanoIdAndExercicioId(dto.getPlanoId(), dto.getExercicioId());
        verify(planoExercicioRepository).save(any(PlanoExercicio.class));
        verifyNoMoreInteractions(exercicioRepository, planoExercicioRepository);
    }

    @Test
    @DisplayName("adicionarExercicio: lança ResourceNotFoundException quando exercício não existe")
    void adicionarExercicio_exercicioNaoExiste() {
        when(exercicioRepository.existsById(dto.getExercicioId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> planoExercicioService.adicionarExercicio(dto));

        verify(exercicioRepository).existsById(dto.getExercicioId());
        verifyNoInteractions(planoExercicioRepository);
    }

    @Test
    @DisplayName("adicionarExercicio: lança BusinessException quando exercício já existe no plano")
    void adicionarExercicio_exercicioExisteNoPlano() {
        when(exercicioRepository.existsById(dto.getExercicioId())).thenReturn(true);
        when(planoExercicioRepository.existsByPlanoIdAndExercicioId(dto.getPlanoId(), dto.getExercicioId()))
            .thenReturn(true);

        assertThrows(BusinessException.class,
                     () -> planoExercicioService.adicionarExercicio(dto));

        verify(exercicioRepository).existsById(dto.getExercicioId());
        verify(planoExercicioRepository).existsByPlanoIdAndExercicioId(dto.getPlanoId(), dto.getExercicioId());
        verify(planoExercicioRepository, never()).save(any());
    }

    @Test
    @DisplayName("buscarPorId: retorna Optional quando existe")
    void buscarPorId_retornaOptional() {
        when(planoExercicioRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<PlanoExercicio> result = planoExercicioService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(entity.getId(), result.get().getId());

        verify(planoExercicioRepository).findById(1L);
    }

    @Test
    @DisplayName("obterPorId: retorna entidade quando existe")
    void obterPorId_retornaEntidade() {
        when(planoExercicioRepository.findById(1L)).thenReturn(Optional.of(entity));

        PlanoExercicio result = planoExercicioService.obterPorId(1L);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());

        verify(planoExercicioRepository).findById(1L);
    }

    @Test
    @DisplayName("obterPorId: lança ResourceNotFoundException quando não existe")
    void obterPorId_naoExiste() {
        when(planoExercicioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> planoExercicioService.obterPorId(1L));

        verify(planoExercicioRepository).findById(1L);
    }

    @Test
    @DisplayName("listarAquecimentoPorPlano: retorna apenas aquecimentos")
    void listarAquecimentoPorPlano_filtraAquecimentos() {
        PlanoExercicio warmUp   = new PlanoExercicio(1L, 1L, 2, "Aquecimento", true);
        PlanoExercicio workout  = new PlanoExercicio(1L, 2L, 3, "Principal", false);
        when(planoExercicioRepository.findByPlanoIdOrderByAquecimentoDescId(1L))
            .thenReturn(List.of(warmUp, workout));

        List<PlanoExercicio> result = planoExercicioService.listarAquecimentoPorPlano(1L);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getAquecimento());

        verify(planoExercicioRepository).findByPlanoIdOrderByAquecimentoDescId(1L);
    }

    @Test
    @DisplayName("listarPrincipaisPorPlano: retorna apenas exercícios principais")
    void listarPrincipaisPorPlano_filtraPrincipais() {
        PlanoExercicio warmUp   = new PlanoExercicio(1L, 1L, 2, "Aquecimento", true);
        PlanoExercicio workout  = new PlanoExercicio(1L, 2L, 3, "Principal", false);
        when(planoExercicioRepository.findByPlanoIdOrderByAquecimentoDescId(1L))
            .thenReturn(List.of(warmUp, workout));

        List<PlanoExercicio> result = planoExercicioService.listarPrincipaisPorPlano(1L);

        assertEquals(1, result.size());
        assertFalse(result.get(0).getAquecimento());

        verify(planoExercicioRepository).findByPlanoIdOrderByAquecimentoDescId(1L);
    }

    @Test
    @DisplayName("atualizarExercicio: atualiza campos corretamente e persiste")
    void atualizarExercicio_atualizaEPersiste() {
        dto.setSeriesSugeridas(4);
        dto.setObservacoes("Atualizado");
        dto.setAquecimento(true);

        when(planoExercicioRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(planoExercicioRepository.save(any(PlanoExercicio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlanoExercicio result = planoExercicioService.atualizarExercicio(1L, dto);

        assertEquals(dto.getSeriesSugeridas(), result.getSeriesSugeridas());
        assertEquals(dto.getObservacoes(), result.getObservacoes());
        assertEquals(dto.getAquecimento(), result.getAquecimento());

        verify(planoExercicioRepository).findById(1L);
        verify(planoExercicioRepository).save(entity);
    }

    @Test
    @DisplayName("removerExercicio: deleta quando encontrado")
    void removerExercicio_deletaQuandoExiste() {
        when(planoExercicioRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> planoExercicioService.removerExercicio(1L));

        verify(planoExercicioRepository).findById(1L);
        verify(planoExercicioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("removerExercicio: lança ResourceNotFoundException quando não encontrado")
    void removerExercicio_naoExiste() {
        when(planoExercicioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> planoExercicioService.removerExercicio(1L));

        verify(planoExercicioRepository).findById(1L);
        verify(planoExercicioRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("contarExerciciosPorPlano: retorna total de exercícios")
    void contarExerciciosPorPlano_contaTotal() {
        when(planoExercicioRepository.findByPlanoIdOrderByAquecimentoDescId(1L))
            .thenReturn(List.of(entity, entity));

        long total = planoExercicioService.contarExerciciosPorPlano(1L);

        assertEquals(2L, total);
        verify(planoExercicioRepository).findByPlanoIdOrderByAquecimentoDescId(1L);
    }

    @Test
    @DisplayName("contarAquecimentosPorPlano: retorna total de aquecimentos")
    void contarAquecimentosPorPlano_contaAquecimentos() {
        PlanoExercicio warmUp   = new PlanoExercicio(1L, 1L, 2, "Aquecimento", true);
        PlanoExercicio workout  = new PlanoExercicio(1L, 2L, 3, "Principal", false);
        when(planoExercicioRepository.findByPlanoIdOrderByAquecimentoDescId(1L))
            .thenReturn(List.of(warmUp, workout));

        long count = planoExercicioService.contarAquecimentosPorPlano(1L);

        assertEquals(1L, count);
        verify(planoExercicioRepository).findByPlanoIdOrderByAquecimentoDescId(1L);
    }
}