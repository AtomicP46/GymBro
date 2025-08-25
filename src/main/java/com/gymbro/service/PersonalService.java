package com.gymbro.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbro.dto.PersonalDTO;
import com.gymbro.model.Personal;
import com.gymbro.repository.PersonalRepository;

@Service
@Transactional
public class PersonalService {

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PersonalDTO criarPersonal(PersonalDTO personalDTO) {
        if (personalRepository.existsByEmail(personalDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        if (Boolean.FALSE.equals(personalDTO.getFormado())
            && (personalDTO.getLicenca() == null || personalDTO.getLicenca().trim().isEmpty())) {
            throw new IllegalArgumentException(
                "Licença é obrigatória para personal trainers não formados em educação física");
        }

        Personal personal = modelMapper.map(personalDTO, Personal.class);
        personal.setSenhaHash(passwordEncoder.encode(personalDTO.getSenha()));

        Personal salvo = personalRepository.save(personal);
        return toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<PersonalDTO> listarTodos() {
        return personalRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PersonalDTO> buscarPorId(Long id) {
        return personalRepository.findById(id)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<PersonalDTO> buscarPorEmail(String email) {
        return personalRepository.findByEmail(email)
                .map(this::toDTO);
    }

    public PersonalDTO atualizarPersonal(Long id, PersonalDTO personalDTO) {
        Personal personal = personalRepository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Personal não encontrado"));

        if (!personal.getEmail().equals(personalDTO.getEmail())
            && personalRepository.existsByEmail(personalDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        if (Boolean.FALSE.equals(personalDTO.getFormado())
            && (personalDTO.getLicenca() == null || personalDTO.getLicenca().trim().isEmpty())) {
            throw new IllegalArgumentException(
                "Licença é obrigatória para personal trainers não formados em educação física");
        }

        personal.setNome(personalDTO.getNome());
        personal.setEmail(personalDTO.getEmail());
        personal.setDataNascimento(personalDTO.getDataNascimento());
        personal.setFormado(personalDTO.getFormado());
        personal.setCodigoValidacao(personalDTO.getCodigoValidacao());
        personal.setLinkValidacao(personalDTO.getLinkValidacao());
        personal.setLicenca(personalDTO.getLicenca());

        if (personalDTO.getSenha() != null && !personalDTO.getSenha().isBlank()) {
            personal.setSenhaHash(passwordEncoder.encode(personalDTO.getSenha()));
        }

        Personal atualizado = personalRepository.save(personal);
        return toDTO(atualizado);
    }

    public void deletarPersonal(Long id) {
        if (!personalRepository.existsById(id)) {
            throw new java.util.NoSuchElementException("Personal não encontrado");
        }
        personalRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PersonalDTO> buscarPorFormacao(Boolean formado) {
        return personalRepository.findByFormado(formado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Expor entidade Personal para autenticação
     */
    @Transactional(readOnly = true)
    public Optional<Personal> buscarEntidadePorEmail(String email) {
        return personalRepository.findByEmail(email);
    }

    private PersonalDTO toDTO(Personal personal) {
        PersonalDTO dto = modelMapper.map(personal, PersonalDTO.class);
        dto.setId(personal.getId());
        dto.setIdade(personal.getIdade());
        return dto;
    }
}
