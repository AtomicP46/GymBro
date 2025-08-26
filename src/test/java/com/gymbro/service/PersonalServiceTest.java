package com.gymbro.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.gymbro.dto.PersonalDTO;
import com.gymbro.model.Personal;
import com.gymbro.repository.PersonalRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do PersonalService")
class PersonalServiceTest {

    @Mock
    private PersonalRepository personalRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonalService personalService;

    private PersonalDTO personalDTO;
    private Personal personal;

    @BeforeEach
    void setUp() {
        personalDTO = new PersonalDTO();
        personalDTO.setNome("João Personal");
        personalDTO.setEmail("joao.personal@email.com");
        personalDTO.setSenha("123456");
        personalDTO.setDataNascimento(LocalDate.of(1985, 5, 15));
        personalDTO.setFormado(true);
        personalDTO.setCodigoValidacao("CREF123456");

        personal = new Personal();
        personal.setNome("João Personal");
        personal.setEmail("joao.personal@email.com");
        personal.setSenhaHash("hashedPassword");
        personal.setDataNascimento(LocalDate.of(1985, 5, 15));
        personal.setFormado(true);
        personal.setCodigoValidacao("CREF123456");
    }

    @Test
    @DisplayName("Deve criar personal com sucesso")
    void deveCriarPersonalComSucesso() {
        // Given
        when(personalRepository.existsByEmail(personalDTO.getEmail())).thenReturn(false);
        when(modelMapper.map(personalDTO, Personal.class)).thenReturn(personal);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(personalRepository.save(any(Personal.class))).thenReturn(personal);
        when(modelMapper.map(personal, PersonalDTO.class)).thenReturn(personalDTO);

        // When
        PersonalDTO resultado = personalService.criarPersonal(personalDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(personalDTO.getNome(), resultado.getNome());
        assertEquals(personalDTO.getEmail(), resultado.getEmail());
        verify(personalRepository).existsByEmail(personalDTO.getEmail());
        verify(passwordEncoder).encode(anyString());
        verify(personalRepository).save(any(Personal.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar personal com email existente")
    void deveLancarExcecaoAoCriarPersonalComEmailExistente() {
        // Given
        when(personalRepository.existsByEmail(personalDTO.getEmail())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personalService.criarPersonal(personalDTO)
        );
        assertEquals("Email já está em uso", exception.getMessage());
        verify(personalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção para personal não formado sem licença")
    void deveLancarExcecaoParaPersonalNaoFormadoSemLicenca() {
        // Given
        personalDTO.setFormado(false);
        personalDTO.setLicenca(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personalService.criarPersonal(personalDTO)
        );
        assertEquals("Licença é obrigatória para personal trainers não formados em educação física",
                    exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar todos os personals")
    void deveListarTodosOsPersonals() {
        // Given
        List<Personal> personals = Arrays.asList(personal);
        when(personalRepository.findAll()).thenReturn(personals);
        when(modelMapper.map(any(Personal.class), eq(PersonalDTO.class))).thenReturn(personalDTO);

        // When
        List<PersonalDTO> resultado = personalService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(personalRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar personal por ID")
    void deveBuscarPersonalPorId() {
        // Given
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(modelMapper.map(personal, PersonalDTO.class)).thenReturn(personalDTO);

        // When
        Optional<PersonalDTO> resultado = personalService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(personalDTO.getNome(), resultado.get().getNome());
        verify(personalRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve buscar personal por email")
    void deveBuscarPersonalPorEmail() {
        // Given
        when(personalRepository.findByEmail("joao.personal@email.com")).thenReturn(Optional.of(personal));
        when(modelMapper.map(personal, PersonalDTO.class)).thenReturn(personalDTO);

        // When
        Optional<PersonalDTO> resultado = personalService.buscarPorEmail("joao.personal@email.com");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(personalDTO.getEmail(), resultado.get().getEmail());
        verify(personalRepository).findByEmail("joao.personal@email.com");
    }

    @Test
    @DisplayName("Deve atualizar personal com sucesso")
    void deveAtualizarPersonalComSucesso() {
        // Given
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(personalRepository.save(any(Personal.class))).thenReturn(personal);
        when(modelMapper.map(personal, PersonalDTO.class)).thenReturn(personalDTO);

        // When
        PersonalDTO resultado = personalService.atualizarPersonal(1L, personalDTO);

        // Then
        assertNotNull(resultado);
        verify(personalRepository).findById(1L);
        verify(personalRepository).save(any(Personal.class));
    }

    @Test
    @DisplayName("Deve deletar personal com sucesso")
    void deveDeletarPersonalComSucesso() {
        // Given
        when(personalRepository.existsById(1L)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> personalService.deletarPersonal(1L));

        // Then
        verify(personalRepository).existsById(1L);
        verify(personalRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve buscar personals por formação")
    void deveBuscarPersonalsPorFormacao() {
        // Given
        List<Personal> personals = Arrays.asList(personal);
        when(personalRepository.findByFormado(true)).thenReturn(personals);
        when(modelMapper.map(any(Personal.class), eq(PersonalDTO.class))).thenReturn(personalDTO);

        // When
        List<PersonalDTO> resultado = personalService.buscarPorFormacao(true);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(personalRepository).findByFormado(true);
    }
}