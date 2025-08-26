package com.gymbro.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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

    private final PersonalRepository personalRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonalService(PersonalRepository personalRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.personalRepository = personalRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public PersonalDTO criarPersonal(PersonalDTO personalDTO) {
        validarEmailUnico(personalDTO.getEmail());
        validarLicencaSeNecessario(personalDTO);
        
        return Optional.of(personalDTO)
                .map(dto -> modelMapper.map(dto, Personal.class))
                .map(this::criptografarSenha)
                .map(personalRepository::save)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalStateException("Erro ao criar personal"));
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
        return personalRepository.findById(id)
                .map(personal -> {
                    validarEmailUnicoParaAtualizacao(personal, personalDTO.getEmail());
                    validarLicencaSeNecessario(personalDTO);
                    return atualizarDadosPersonal(personal, personalDTO);
                })
                .map(personalRepository::save)
                .map(this::toDTO)
                .orElseThrow(() -> new java.util.NoSuchElementException("Personal não encontrado"));
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

    @Transactional(readOnly = true)
    public List<PersonalDTO> buscarPorCriterio(Predicate<Personal> criterio) {
        return personalRepository.findAll().stream()
                .filter(criterio)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Personal> buscarEntidadePorEmail(String email) {
        return personalRepository.findByEmail(email);
    }

    private void validarEmailUnico(String email) {
        if (personalRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso");
        }
    }

    private void validarEmailUnicoParaAtualizacao(Personal personal, String novoEmail) {
        if (!personal.getEmail().equals(novoEmail) && personalRepository.existsByEmail(novoEmail)) {
            throw new IllegalArgumentException("Email já está em uso");
        }
    }

    private void validarLicencaSeNecessario(PersonalDTO dto) {
        if (Boolean.FALSE.equals(dto.getFormado()) && 
            (dto.getLicenca() == null || dto.getLicenca().trim().isEmpty())) {
            throw new IllegalArgumentException(
                "Licença é obrigatória para personal trainers não formados em educação física");
        }
    }

    private Personal criptografarSenha(Personal personal) {
        personal.setSenhaHash(passwordEncoder.encode(personal.getSenhaHash()));
        return personal;
    }

    private Personal atualizarDadosPersonal(Personal personal, PersonalDTO dto) {
        personal.setNome(dto.getNome());
        personal.setEmail(dto.getEmail());
        personal.setDataNascimento(dto.getDataNascimento());
        personal.setFormado(dto.getFormado());
        personal.setCodigoValidacao(dto.getCodigoValidacao());
        personal.setLinkValidacao(dto.getLinkValidacao());
        personal.setLicenca(dto.getLicenca());

        Optional.ofNullable(dto.getSenha())
                .filter(senha -> !senha.isBlank())
                .ifPresent(senha -> personal.setSenhaHash(passwordEncoder.encode(senha)));

        return personal;
    }

    private PersonalDTO toDTO(Personal personal) {
        PersonalDTO dto = modelMapper.map(personal, PersonalDTO.class);
        dto.setId(personal.getId());
        dto.setIdade(personal.getIdade());
        return dto;
    }
}
