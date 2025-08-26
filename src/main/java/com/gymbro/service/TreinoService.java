package com.gymbro.service;

import com.gymbro.dto.TreinoDTO;
import com.gymbro.model.Treino;
import com.gymbro.repository.PersonalRepository;
import com.gymbro.repository.TreinoRepository;
import com.gymbro.repository.AlunoRepository;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.exception.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class TreinoService {
    
    @Autowired
    private TreinoRepository treinoRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private PersonalRepository personalRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    // Substituindo por métodos privados que são chamados quando necessário

    private boolean usuarioExiste(Long id) {
        return alunoRepository.existsById(id);
    }

    private boolean personalExiste(Long id) {
        return personalRepository.existsById(id);
    }

    private boolean treinoJaIniciado(Treino treino) {
        return treino.getDataHoraInicio() != null;
    }

    private boolean treinoJaFinalizado(Treino treino) {
        return treino.getDataHoraFim() != null;
    }

    private boolean treinoEmAndamento(Treino treino) {
        return treino.getDataHoraInicio() != null && treino.getDataHoraFim() == null;
    }

    @Transactional
    public Treino criarTreino(@Valid TreinoDTO treinoDTO) {
        if (!usuarioExiste(treinoDTO.getUsuarioId())) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        
        if (treinoDTO.getPersonalId() != null && !personalExiste(treinoDTO.getPersonalId())) {
            throw new ResourceNotFoundException("Personal não encontrado");
        }
        
        Treino treino = modelMapper.map(treinoDTO, Treino.class);
        return treinoRepository.save(treino);
    }

    @Transactional(readOnly = true)
    public Optional<Treino> buscarTreinoPorId(@NotNull Long id) {
        return treinoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Treino obterTreinoPorId(@NotNull Long id) {
        return buscarTreinoPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTodosTreinos() {
        return treinoRepository.findAllByOrderByDataHoraInicioDesc();
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTreinosPorUsuario(@NotNull Long usuarioId) {
        if (!usuarioExiste(usuarioId)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        return treinoRepository.findByUsuarioIdOrderByDataHoraInicioDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTreinosPorPersonal(@NotNull Long personalId) {
        if (!personalExiste(personalId)) {
            throw new ResourceNotFoundException("Personal não encontrado");
        }
        return treinoRepository.findByPersonalIdOrderByDataHoraInicioDesc(personalId);
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTreinosEmAndamento() {
        return listarTodosTreinos().stream()
            .filter(this::treinoEmAndamento)
            .collect(Collectors.toList());
    }

    @Transactional
    public Treino iniciarTreino(@NotNull Long treinoId) {
        Treino treino = obterTreinoPorId(treinoId);
        
        if (treinoJaIniciado(treino)) {
            throw new BusinessException("Treino já foi iniciado");
        }
        
        treino.setDataHoraInicio(LocalDateTime.now());
        return treinoRepository.save(treino);
    }

    @Transactional
    public Treino finalizarTreino(@NotNull Long treinoId) {
        Treino treino = obterTreinoPorId(treinoId);
        
        if (!treinoJaIniciado(treino)) {
            throw new BusinessException("Treino não foi iniciado ainda");
        }
        
        if (treinoJaFinalizado(treino)) {
            throw new BusinessException("Treino já foi finalizado");
        }
        
        treino.setDataHoraFim(LocalDateTime.now());
        return treinoRepository.save(treino);
    }

    @Transactional
    public Treino atualizarTreino(@NotNull Long id, @Valid TreinoDTO treinoDTO) {
        Treino treino = obterTreinoPorId(id);
        
        if (treinoJaIniciado(treino)) {
            throw new BusinessException("Não é possível editar um treino já iniciado");
        }
        
        modelMapper.map(treinoDTO, treino);
        return treinoRepository.save(treino);
    }

    @Transactional
    public void deletarTreino(@NotNull Long id) {
        Treino treino = obterTreinoPorId(id);
        
        if (treinoEmAndamento(treino)) {
            throw new BusinessException("Não é possível excluir um treino em andamento");
        }
        
        treinoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long contarTreinos() {
        return treinoRepository.count();
    }

    @Transactional(readOnly = true)
    public long contarTreinosFinalizados() {
        return treinoRepository.countTreinosFinalizados();
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTreinosFinalizados() {
        return listarTodosTreinos().stream()
            .filter(this::treinoJaFinalizado)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTreinosNaoIniciados() {
        return listarTodosTreinos().stream()
            .filter(treino -> !treinoJaIniciado(treino))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long contarTreinosPorUsuario(@NotNull Long usuarioId) {
        return listarTreinosPorUsuario(usuarioId).stream().count();
    }

    @Transactional(readOnly = true)
    public long contarTreinosPorPersonal(@NotNull Long personalId) {
        return listarTreinosPorPersonal(personalId).stream().count();
    }
}
