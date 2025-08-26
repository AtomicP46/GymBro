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

import com.gymbro.dto.EquipamentoDTO;
import com.gymbro.model.Equipamento;
import com.gymbro.repository.EquipamentoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do EquipamentoService")
class EquipamentoServiceTest {

    @Mock
    private EquipamentoRepository repository;

    @InjectMocks
    private EquipamentoService equipamentoService;

    private EquipamentoDTO equipamentoDTO;
    private Equipamento equipamento;

    @BeforeEach
    void setUp() {
        equipamentoDTO = new EquipamentoDTO();
        equipamentoDTO.setNome("Halteres");
        equipamentoDTO.setPesoEquip(10.0f);

        equipamento = new Equipamento();
        // Atribuindo o ID via reflexão sem expor setter na entidade
        ReflectionTestUtils.setField(equipamento, "id", 1L);
        equipamento.setNome("Halteres");
        equipamento.setPesoEquip(10.0f);
    }

    @Test
    @DisplayName("Deve criar equipamento com sucesso")
    void deveCriarEquipamentoComSucesso() {
        when(repository.existsByNomeIgnoreCase("Halteres")).thenReturn(false);
        when(repository.save(any(Equipamento.class))).thenReturn(equipamento);

        EquipamentoDTO resultado = equipamentoService.criarEquipamento(equipamentoDTO);

        assertNotNull(resultado);
        assertEquals("Halteres", resultado.getNome());
        assertEquals(10.0f, resultado.getPesoEquip());
        verify(repository).existsByNomeIgnoreCase("Halteres");
        verify(repository).save(any(Equipamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar equipamento com nome existente")
    void deveLancarExcecaoAoCriarEquipamentoComNomeExistente() {
        when(repository.existsByNomeIgnoreCase("Halteres")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> equipamentoService.criarEquipamento(equipamentoDTO)
        );
        assertEquals("Já existe um equipamento com este nome", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar equipamento por ID")
    void deveBuscarEquipamentoPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(equipamento));

        EquipamentoDTO resultado = equipamentoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Halteres", resultado.getNome());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando equipamento não encontrado")
    void deveLancarExcecaoQuandoEquipamentoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> equipamentoService.buscarPorId(1L)
        );
        assertEquals("Equipamento não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar todos os equipamentos")
    void deveListarTodosOsEquipamentos() {
        List<Equipamento> equipamentos = Arrays.asList(equipamento);
        when(repository.findAll()).thenReturn(equipamentos);

        List<EquipamentoDTO> resultado = equipamentoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Halteres", resultado.get(0).getNome());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve buscar equipamentos por nome")
    void deveBuscarEquipamentosPorNome() {
        List<Equipamento> equipamentos = Arrays.asList(equipamento);
        when(repository.findByNomeContainingIgnoreCaseOrderByNome("Halt"))
            .thenReturn(equipamentos);

        List<EquipamentoDTO> resultado = equipamentoService.buscarPorNome("Halt");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository).findByNomeContainingIgnoreCaseOrderByNome("Halt");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por nome vazio")
    void deveLancarExcecaoAoBuscarPorNomeVazio() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> equipamentoService.buscarPorNome("")
        );
        assertEquals("Nome para busca não pode estar vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar equipamento com sucesso")
    void deveAtualizarEquipamentoComSucesso() {
        EquipamentoDTO novoDTO = new EquipamentoDTO();
        novoDTO.setNome("Halteres 15kg");
        novoDTO.setPesoEquip(15.0f);

        when(repository.findById(1L)).thenReturn(Optional.of(equipamento));
        when(repository.existsByNomeIgnoreCaseAndIdNot("Halteres 15kg", 1L))
            .thenReturn(false);
        when(repository.save(any(Equipamento.class))).thenReturn(equipamento);

        EquipamentoDTO resultado = equipamentoService.atualizarEquipamento(1L, novoDTO);

        assertNotNull(resultado);
        verify(repository).findById(1L);
        verify(repository)
            .existsByNomeIgnoreCaseAndIdNot("Halteres 15kg", 1L);
        verify(repository).save(any(Equipamento.class));
    }

    @Test
    @DisplayName("Deve deletar equipamento com sucesso")
    void deveDeletarEquipamentoComSucesso() {
        when(repository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> equipamentoService.deletarEquipamento(1L));

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve buscar equipamentos por faixa de peso")
    void deveBuscarEquipamentosPorFaixaDePeso() {
        List<Equipamento> equipamentos = Arrays.asList(equipamento);
        when(repository.findByPesoEquipBetweenOrderByPesoEquip(5.0f, 15.0f))
            .thenReturn(equipamentos);

        List<EquipamentoDTO> resultado = equipamentoService.buscarPorFaixaPeso(5.0f, 15.0f);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository)
            .findByPesoEquipBetweenOrderByPesoEquip(5.0f, 15.0f);
    }

    @Test
    @DisplayName("Deve lançar exceção para faixa de peso inválida")
    void deveLancarExcecaoParaFaixaDePesoInvalida() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> equipamentoService.buscarPorFaixaPeso(15.0f, 5.0f)
        );
        assertEquals(
            "Peso mínimo não pode ser maior que o peso máximo",
            exception.getMessage()
        );
    }
}