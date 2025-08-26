package com.gymbro.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.modelmapper.ModelMapper;

import com.gymbro.dto.TreinoDTO;
import com.gymbro.exception.BusinessException;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.model.Treino;
import com.gymbro.repository.AlunoRepository;
import com.gymbro.repository.PersonalRepository;
import com.gymbro.repository.TreinoRepository;

@ExtendWith(MockitoExtension.class)
class TreinoServiceTest {

    @Mock
    private TreinoRepository treinoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private PersonalRepository personalRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TreinoService treinoService;

    private TreinoDTO treinoDTO;
    private Treino treino;

    @BeforeEach
    void setUp() {
        treinoDTO = new TreinoDTO();
        treinoDTO.setUsuarioId(1L);
        treinoDTO.setPersonalId(1L);
        treinoDTO.setNome("Treino Teste");

        treino = new Treino();
        treino.setId(1L);
        treino.setUsuarioId(1L);
        treino.setPersonalId(1L);
        treino.setNome("Treino Teste");
    }

    @Test
    void criarTreino_DeveRetornarTreino_QuandoDadosValidos() {
        // Given
        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(personalRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(treinoDTO, Treino.class)).thenReturn(treino);
        when(treinoRepository.save(any(Treino.class))).thenReturn(treino);

        // When
        Treino resultado = treinoService.criarTreino(treinoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Treino Teste", resultado.getNome());
        verify(treinoRepository).save(any(Treino.class));
    }

    @Test
    void criarTreino_DeveLancarResourceNotFoundException_QuandoUsuarioNaoExiste() {
        // Given
        when(alunoRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
            () -> treinoService.criarTreino(treinoDTO));
        
        verify(treinoRepository, never()).save(any());
    }

    @Test
    void criarTreino_DeveLancarResourceNotFoundException_QuandoPersonalNaoExiste() {
        // Given
        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(personalRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
            () -> treinoService.criarTreino(treinoDTO));
        
        verify(treinoRepository, never()).save(any());
    }

    @Test
    void criarTreino_DevePermitirPersonalNulo() {
        // Given
        treinoDTO.setPersonalId(null);
        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(treinoDTO, Treino.class)).thenReturn(treino);
        when(treinoRepository.save(any(Treino.class))).thenReturn(treino);

        // When
        Treino resultado = treinoService.criarTreino(treinoDTO);

        // Then
        assertNotNull(resultado);
        verify(treinoRepository).save(any(Treino.class));
    }

    @Test
    void buscarTreinoPorId_DeveRetornarOptional_QuandoEncontrado() {
        // Given
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));

        // When
        Optional<Treino> resultado = treinoService.buscarTreinoPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void obterTreinoPorId_DeveRetornarTreino_QuandoEncontrado() {
        // Given
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));

        // When
        Treino resultado = treinoService.obterTreinoPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obterTreinoPorId_DeveLancarResourceNotFoundException_QuandoNaoEncontrado() {
        // Given
        when(treinoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
            () -> treinoService.obterTreinoPorId(1L));
    }

    @Test
    void listarTreinosPorUsuario_DeveRetornarLista_QuandoUsuarioExiste() {
        // Given
        List<Treino> treinos = Arrays.asList(treino);
        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(treinoRepository.findByUsuarioIdOrderByDataHoraInicioDesc(1L)).thenReturn(treinos);

        // When
        List<Treino> resultado = treinoService.listarTreinosPorUsuario(1L);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    void listarTreinosPorUsuario_DeveLancarResourceNotFoundException_QuandoUsuarioNaoExiste() {
        // Given
        when(alunoRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
            () -> treinoService.listarTreinosPorUsuario(1L));
    }

    @Test
    void iniciarTreino_DeveRetornarTreinoIniciado_QuandoNaoIniciado() {
        // Given
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));
        when(treinoRepository.save(any(Treino.class))).thenReturn(treino);

        // When
        Treino resultado = treinoService.iniciarTreino(1L);

        // Then
        assertNotNull(resultado);
        verify(treinoRepository).save(any(Treino.class));
    }

    @Test
    void iniciarTreino_DeveLancarBusinessException_QuandoJaIniciado() {
        // Given
        treino.setDataHoraInicio(LocalDateTime.now());
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> treinoService.iniciarTreino(1L));
        
        verify(treinoRepository, never()).save(any());
    }

    @Test
    void finalizarTreino_DeveRetornarTreinoFinalizado_QuandoIniciado() {
        // Given
        treino.setDataHoraInicio(LocalDateTime.now());
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));
        when(treinoRepository.save(any(Treino.class))).thenReturn(treino);

        // When
        Treino resultado = treinoService.finalizarTreino(1L);

        // Then
        assertNotNull(resultado);
        verify(treinoRepository).save(any(Treino.class));
    }

    @Test
    void finalizarTreino_DeveLancarBusinessException_QuandoNaoIniciado() {
        // Given
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> treinoService.finalizarTreino(1L));
        
        verify(treinoRepository, never()).save(any());
    }

    @Test
    void finalizarTreino_DeveLancarBusinessException_QuandoJaFinalizado() {
        // Given
        treino.setDataHoraInicio(LocalDateTime.now());
        treino.setDataHoraFim(LocalDateTime.now());
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> treinoService.finalizarTreino(1L));
        
        verify(treinoRepository, never()).save(any());
    }

    @Test
    void atualizarTreino_DeveRetornarTreinoAtualizado_QuandoNaoIniciado() {
        // Given
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));
        when(treinoRepository.save(any(Treino.class))).thenReturn(treino);

        // When
        Treino resultado = treinoService.atualizarTreino(1L, treinoDTO);

        // Then
        assertNotNull(resultado);
        verify(modelMapper).map(treinoDTO, treino);
        verify(treinoRepository).save(treino);
    }

    @Test
    void atualizarTreino_DeveLancarBusinessException_QuandoJaIniciado() {
        // Given
        treino.setDataHoraInicio(LocalDateTime.now());
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> treinoService.atualizarTreino(1L, treinoDTO));
        
        verify(treinoRepository, never()).save(any());
    }

    @Test
    void deletarTreino_DeveExcluir_QuandoNaoEmAndamento() {
        // Given
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));

        // When
        assertDoesNotThrow(() -> treinoService.deletarTreino(1L));

        // Then
        verify(treinoRepository).deleteById(1L);
    }

    @Test
    void deletarTreino_DeveLancarBusinessException_QuandoEmAndamento() {
        // Given
        treino.setDataHoraInicio(LocalDateTime.now());
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> treinoService.deletarTreino(1L));
        
        verify(treinoRepository, never()).deleteById(any());
    }

    @Test
    void listarTreinosEmAndamento_DeveRetornarApenasEmAndamento() {
        // Given
        Treino treinoEmAndamento = new Treino();
        treinoEmAndamento.setDataHoraInicio(LocalDateTime.now());
        
        Treino treinoFinalizado = new Treino();
        treinoFinalizado.setDataHoraInicio(LocalDateTime.now());
        treinoFinalizado.setDataHoraFim(LocalDateTime.now());
        
        List<Treino> todos = Arrays.asList(treinoEmAndamento, treinoFinalizado, treino);
        when(treinoRepository.findAllByOrderByDataHoraInicioDesc()).thenReturn(todos);

        // When
        List<Treino> resultado = treinoService.listarTreinosEmAndamento();

        // Then
        assertEquals(1, resultado.size());
        assertNotNull(resultado.get(0).getDataHoraInicio());
        assertNull(resultado.get(0).getDataHoraFim());
    }

    @Test
    void listarTreinosFinalizados_DeveRetornarApenasFinalizados() {
        // Given
        Treino treinoFinalizado = new Treino();
        treinoFinalizado.setDataHoraInicio(LocalDateTime.now());
        treinoFinalizado.setDataHoraFim(LocalDateTime.now());
        
        List<Treino> todos = Arrays.asList(treinoFinalizado, treino);
        when(treinoRepository.findAllByOrderByDataHoraInicioDesc()).thenReturn(todos);

        // When
        List<Treino> resultado = treinoService.listarTreinosFinalizados();

        // Then
        assertEquals(1, resultado.size());
        assertNotNull(resultado.get(0).getDataHoraFim());
    }

    @Test
    void contarTreinosPorUsuario_DeveRetornarQuantidadeCorreta() {
        // Given
        List<Treino> treinos = Arrays.asList(treino, treino);
        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(treinoRepository.findByUsuarioIdOrderByDataHoraInicioDesc(1L)).thenReturn(treinos);

        // When
        long resultado = treinoService.contarTreinosPorUsuario(1L);

        // Then
        assertEquals(2, resultado);
    }
}
