package com.gymbro.service;

import java.time.LocalDateTime;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gymbro.dto.PlanoDTO;
import com.gymbro.enums.TipoCriador;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.exception.UnauthorizedException;
import com.gymbro.model.Plano;
import com.gymbro.repository.PlanoRepository;

@ExtendWith(MockitoExtension.class)
class PlanoServiceTest {

    @Mock
    private PlanoRepository planoRepository;

    @InjectMocks
    private PlanoService planoService;

    private PlanoDTO planoDTO;
    private Plano plano;

    @BeforeEach
    void setUp() {
        planoDTO = new PlanoDTO();
        planoDTO.setNome("Plano Teste");
        planoDTO.setDescricao("Descrição do plano teste");
        planoDTO.setCriadorId(1L);
        planoDTO.setTipoCriador(TipoCriador.PERSONAL);
        planoDTO.setPublico(true);
        planoDTO.setObservacoes("Observações");

        plano = new Plano("Plano Teste", "Descrição do plano teste", 1L, TipoCriador.PERSONAL, true, "Observações");
        plano.setId(1L);
        plano.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void criarPlano_DeveRetornarPlano_QuandoDadosValidos() {
        // Given
        when(planoRepository.save(any(Plano.class))).thenReturn(plano);

        // When
        Plano resultado = planoService.criarPlano(planoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Plano Teste", resultado.getNome());
        verify(planoRepository).save(any(Plano.class));
    }

    @Test
    void criarPlano_DeveLancarIllegalArgumentException_QuandoNomeInvalido() {
        // Given
        planoDTO.setNome("");

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> planoService.criarPlano(planoDTO));
        
        verify(planoRepository, never()).save(any());
    }

    @Test
    void criarPlano_DeveLancarIllegalArgumentException_QuandoDescricaoInvalida() {
        // Given
        planoDTO.setDescricao("");

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> planoService.criarPlano(planoDTO));
        
        verify(planoRepository, never()).save(any());
    }

    @Test
    void buscarPorId_DeveRetornarOptional_QuandoEncontrado() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));

        // When
        Optional<Plano> resultado = planoService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void obterPorId_DeveRetornarPlano_QuandoEncontrado() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));

        // When
        Plano resultado = planoService.obterPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obterPorId_DeveLancarResourceNotFoundException_QuandoNaoEncontrado() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
            () -> planoService.obterPorId(1L));
    }

    @Test
    void listarPublicos_DeveRetornarApenasPublicos() {
        // Given
        Plano planoPublico = new Plano("Público", "Desc", 1L, TipoCriador.PERSONAL, true, null);
        Plano planoPrivado = new Plano("Privado", "Desc", 1L, TipoCriador.PERSONAL, false, null);
        List<Plano> todos = Arrays.asList(planoPublico, planoPrivado);
        
        when(planoRepository.findAllByOrderByDataCriacaoDesc()).thenReturn(todos);

        // When
        List<Plano> resultado = planoService.listarPublicos();

        // Then
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getPublico());
    }

    @Test
    void buscarPorNome_DeveRetornarPlanos_QuandoNomeValido() {
        // Given
        List<Plano> planos = Arrays.asList(plano);
        when(planoRepository.findByNomeContainingIgnoreCaseOrderByDataCriacaoDesc("Teste")).thenReturn(planos);

        // When
        List<Plano> resultado = planoService.buscarPorNome("Teste");

        // Then
        assertEquals(1, resultado.size());
        verify(planoRepository).findByNomeContainingIgnoreCaseOrderByDataCriacaoDesc("Teste");
    }

    @Test
    void buscarPorNome_DeveRetornarListaVazia_QuandoNomeVazio() {
        // When
        List<Plano> resultado = planoService.buscarPorNome("");

        // Then
        assertTrue(resultado.isEmpty());
        verify(planoRepository, never()).findByNomeContainingIgnoreCaseOrderByDataCriacaoDesc(any());
    }

    @Test
    void atualizarPlano_DeveRetornarPlanoAtualizado_QuandoPermissaoValida() {
        // Given
        planoDTO.setNome("Plano Atualizado");
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));
        when(planoRepository.save(any(Plano.class))).thenReturn(plano);

        // When
        Plano resultado = planoService.atualizarPlano(1L, planoDTO, 1L, TipoCriador.PERSONAL);

        // Then
        assertNotNull(resultado);
        verify(planoRepository).save(any(Plano.class));
    }

    @Test
    void atualizarPlano_DeveLancarUnauthorizedException_QuandoSemPermissao() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));

        // When & Then
        assertThrows(UnauthorizedException.class, 
            () -> planoService.atualizarPlano(1L, planoDTO, 2L, TipoCriador.PERSONAL));
        
        verify(planoRepository, never()).save(any());
    }

    @Test
    void excluirPlano_DeveExcluir_QuandoPermissaoValida() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));

        // When
        assertDoesNotThrow(() -> planoService.excluirPlano(1L, 1L, TipoCriador.PERSONAL));

        // Then
        verify(planoRepository).deleteById(1L);
    }

    @Test
    void excluirPlano_DeveLancarUnauthorizedException_QuandoSemPermissao() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));

        // When & Then
        assertThrows(UnauthorizedException.class, 
            () -> planoService.excluirPlano(1L, 2L, TipoCriador.PERSONAL));
        
        verify(planoRepository, never()).deleteById(any());
    }

    @Test
    void podeEditarPlano_DeveRetornarTrue_QuandoPermissaoValida() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));

        // When
        boolean resultado = planoService.podeEditarPlano(1L, 1L, TipoCriador.PERSONAL);

        // Then
        assertTrue(resultado);
    }

    @Test
    void podeEditarPlano_DeveRetornarFalse_QuandoSemPermissao() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));

        // When
        boolean resultado = planoService.podeEditarPlano(1L, 2L, TipoCriador.PERSONAL);

        // Then
        assertFalse(resultado);
    }

    @Test
    void podeEditarPlano_DeveRetornarFalse_QuandoPlanoNaoExiste() {
        // Given
        when(planoRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        boolean resultado = planoService.podeEditarPlano(1L, 1L, TipoCriador.PERSONAL);

        // Then
        assertFalse(resultado);
    }

    @Test
    void contarPlanosPorCriador_DeveRetornarQuantidadeCorreta() {
        // Given
        List<Plano> planos = Arrays.asList(plano, plano);
        when(planoRepository.findByCriadorIdAndTipoCriadorOrderByDataCriacaoDesc(1L, TipoCriador.PERSONAL))
            .thenReturn(planos);

        // When
        long resultado = planoService.contarPlanosPorCriador(1L, TipoCriador.PERSONAL);

        // Then
        assertEquals(2, resultado);
    }

    @Test
    void listarPlanosRecentes_DeveRetornarQuantidadeLimitada() {
        // Given
        List<Plano> planos = Arrays.asList(plano, plano, plano);
        when(planoRepository.findAllByOrderByDataCriacaoDesc()).thenReturn(planos);

        // When
        List<Plano> resultado = planoService.listarPlanosRecentes(2);

        // Then
        assertEquals(2, resultado.size());
    }
}
