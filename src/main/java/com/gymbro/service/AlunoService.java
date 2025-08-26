package com.gymbro.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbro.dto.AlunoDTO;
import com.gymbro.model.Aluno;
import com.gymbro.repository.AlunoRepository;

@Service
@Transactional
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AlunoService(AlunoRepository alunoRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AlunoDTO criarAluno(AlunoDTO alunoDTO) {
        validarEmailUnico(alunoDTO.getEmail());
        
        return Optional.of(alunoDTO)
                .map(dto -> modelMapper.map(dto, Aluno.class))
                .map(this::criptografarSenha)
                .map(alunoRepository::save)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalStateException("Erro ao criar aluno"));
    }

    @Transactional(readOnly = true)
    public List<AlunoDTO> listarTodos() {
        return alunoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AlunoDTO> buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<AlunoDTO> buscarPorEmail(String email) {
        return alunoRepository.findByEmail(email)
                .map(this::toDTO);
    }

    public AlunoDTO atualizarAluno(Long id, AlunoDTO alunoDTO) {
        return alunoRepository.findById(id)
                .map(aluno -> {
                    validarEmailUnicoParaAtualizacao(aluno, alunoDTO.getEmail());
                    return atualizarDadosAluno(aluno, alunoDTO);
                })
                .map(alunoRepository::save)
                .map(this::toDTO)
                .orElseThrow(() -> new java.util.NoSuchElementException("Aluno não encontrado"));
    }

    public void deletarAluno(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new java.util.NoSuchElementException("Aluno não encontrado");
        }
        alunoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AlunoDTO> buscarPorFaixaPeso(BigDecimal pesoMin, BigDecimal pesoMax) {
        return alunoRepository.findByPesoBetween(pesoMin, pesoMax).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlunoDTO> buscarPorCriterio(Predicate<Aluno> criterio) {
        return alunoRepository.findAll().stream()
                .filter(criterio)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Aluno> buscarEntidadePorEmail(String email) {
        return alunoRepository.findByEmail(email);
    }

    private void validarEmailUnico(String email) {
        if (alunoRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso");
        }
    }

    private void validarEmailUnicoParaAtualizacao(Aluno aluno, String novoEmail) {
        if (!aluno.getEmail().equals(novoEmail) && alunoRepository.existsByEmail(novoEmail)) {
            throw new IllegalArgumentException("Email já está em uso");
        }
    }

    private Aluno criptografarSenha(Aluno aluno) {
        aluno.setSenhaHash(passwordEncoder.encode(aluno.getSenhaHash()));
        return aluno;
    }

    private Aluno atualizarDadosAluno(Aluno aluno, AlunoDTO dto) {
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setDataNascimento(dto.getDataNascimento());
        aluno.setPeso(dto.getPeso());

        Optional.ofNullable(dto.getSenha())
                .filter(senha -> !senha.isBlank())
                .ifPresent(senha -> aluno.setSenhaHash(passwordEncoder.encode(senha)));

        return aluno;
    }

    private AlunoDTO toDTO(Aluno aluno) {
        AlunoDTO dto = modelMapper.map(aluno, AlunoDTO.class);
        dto.setId(aluno.getId());
        dto.setIdade(aluno.getIdade());
        return dto;
    }
}
