package com.gymbro.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AlunoDTO criarAluno(AlunoDTO alunoDTO) {
        if (alunoRepository.existsByEmail(alunoDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        Aluno aluno = modelMapper.map(alunoDTO, Aluno.class);
        aluno.setSenhaHash(passwordEncoder.encode(alunoDTO.getSenha()));

        Aluno salvo = alunoRepository.save(aluno);
        return toDTO(salvo);
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
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Aluno não encontrado"));

        if (!aluno.getEmail().equals(alunoDTO.getEmail())
            && alunoRepository.existsByEmail(alunoDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        aluno.setNome(alunoDTO.getNome());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setDataNascimento(alunoDTO.getDataNascimento());
        aluno.setPeso(alunoDTO.getPeso());

        if (alunoDTO.getSenha() != null && !alunoDTO.getSenha().isBlank()) {
            aluno.setSenhaHash(passwordEncoder.encode(alunoDTO.getSenha()));
        }

        Aluno atualizado = alunoRepository.save(aluno);
        return toDTO(atualizado);
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

    /**
     * Expor entidade Aluno para autenticação
     */
    @Transactional(readOnly = true)
    public Optional<Aluno> buscarEntidadePorEmail(String email) {
        return alunoRepository.findByEmail(email);
    }

    private AlunoDTO toDTO(Aluno aluno) {
        AlunoDTO dto = modelMapper.map(aluno, AlunoDTO.class);
        dto.setId(aluno.getId());
        dto.setIdade(aluno.getIdade());
        return dto;
    }
}
