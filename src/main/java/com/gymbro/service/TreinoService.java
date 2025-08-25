package com.gymbro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbro.dto.TreinoDTO;
import com.gymbro.model.Treino;
import com.gymbro.repository.AlunoRepository;
import com.gymbro.repository.PersonalRepository;
import com.gymbro.repository.TreinoRepository;

@Service
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

    public Treino criarTreino(TreinoDTO treinoDTO) {
        if (!alunoRepository.existsById(treinoDTO.getUsuarioId())) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        if (treinoDTO.getPersonalId() != null && !personalRepository.existsById(treinoDTO.getPersonalId())) {
            throw new IllegalArgumentException("Personal não encontrado");
        }

        Treino treino = modelMapper.map(treinoDTO, Treino.class);
        return treinoRepository.save(treino);
    }

    @Transactional(readOnly = true)
    public Optional<Treino> buscarTreinoPorId(Long id) {
        return treinoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTodosTreinos() {
        return treinoRepository.findAllByOrderByDataHoraInicioDesc();
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTreinosPorUsuario(Long usuarioId) {
        if (!alunoRepository.existsById(usuarioId)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return treinoRepository.findByUsuarioIdOrderByDataHoraInicioDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTreinosPorPersonal(Long personalId) {
        if (!personalRepository.existsById(personalId)) {
            throw new IllegalArgumentException("Personal não encontrado");
        }
        return treinoRepository.findByPersonalIdOrderByDataHoraInicioDesc(personalId);
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTreinosEmAndamento() {
        return treinoRepository.findTreinosEmAndamento();
    }

    public Treino iniciarTreino(Long treinoId) {
        Treino treino = treinoRepository.findById(treinoId)
            .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado"));

        if (treino.getDataHoraInicio() != null) {
            throw new IllegalArgumentException("Treino já foi iniciado");
        }

        treino.setDataHoraInicio(LocalDateTime.now());
        return treinoRepository.save(treino);
    }

    public Treino finalizarTreino(Long treinoId) {
        Treino treino = treinoRepository.findById(treinoId)
            .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado"));

        if (treino.getDataHoraInicio() == null) {
            throw new IllegalArgumentException("Treino não foi iniciado ainda");
        }

        if (treino.getDataHoraFim() != null) {
            throw new IllegalArgumentException("Treino já foi finalizado");
        }

        treino.setDataHoraFim(LocalDateTime.now());
        return treinoRepository.save(treino);
    }

    public Treino atualizarTreino(Long id, TreinoDTO treinoDTO) {
        Treino treino = treinoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado"));

        if (treino.getDataHoraInicio() != null) {
            throw new IllegalArgumentException("Não é possível editar um treino já iniciado");
        }

        modelMapper.map(treinoDTO, treino);
        return treinoRepository.save(treino);
    }

    public void deletarTreino(Long id) {
        Treino treino = treinoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado"));

        if (treino.isEmAndamento()) {
            throw new IllegalArgumentException("Não é possível excluir um treino em andamento");
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
}
