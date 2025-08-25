package com.gymbro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import com.gymbro.dto.PlanoDTO;
import com.gymbro.enums.TipoCriador;
import com.gymbro.model.Plano;
import com.gymbro.repository.PlanoRepository;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.exception.UnauthorizedException;

@Service
@Validated
@Transactional
public class PlanoService {
    
    @Autowired
    private PlanoRepository planoRepository;

    @Transactional
    public Plano criarPlano(@Valid PlanoDTO planoDTO) {
        Plano plano = new Plano(
            planoDTO.getNome().trim(),
            planoDTO.getDescricao().trim(),
            planoDTO.getCriadorId(),
            planoDTO.getTipoCriador(),
            planoDTO.getPublico() != null ? planoDTO.getPublico() : false,
            planoDTO.getObservacoes()
        );

        return planoRepository.save(plano);
    }

    @Transactional(readOnly = true)
    public Plano buscarPorId(@NotNull Long id) {
        return planoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Plano> listarTodos() {
        return planoRepository.findAllByOrderByDataCriacaoDesc();
    }

    @Transactional(readOnly = true)
    public List<Plano> listarPorCriador(@NotNull Long criadorId, @NotNull TipoCriador tipoCriador) {
        return planoRepository.findByCriadorIdAndTipoCriadorOrderByDataCriacaoDesc(criadorId, tipoCriador);
    }

    @Transactional(readOnly = true)
    public List<Plano> listarPublicos() {
        return planoRepository.findByPublicoTrueOrderByDataCriacaoDesc();
    }

    @Transactional(readOnly = true)
    public List<Plano> buscarPorNome(@NotNull String nome) {
        return planoRepository.findByNomeContainingIgnoreCaseOrderByDataCriacaoDesc(nome.trim());
    }

    @Transactional
    public Plano atualizarPlano(@NotNull Long id, @Valid PlanoDTO planoDTO, @NotNull Long criadorId, @NotNull TipoCriador tipoCriador) {
        Plano planoExistente = buscarPorId(id);

        // Verificar se o usuário tem permissão para editar
        if (!planoExistente.getCriadorId().equals(criadorId) || 
            !planoExistente.getTipoCriador().equals(tipoCriador)) {
            throw new UnauthorizedException("Você não tem permissão para editar este plano");
        }

        // Atualizar dados
        planoExistente.setNome(planoDTO.getNome().trim());
        planoExistente.setDescricao(planoDTO.getDescricao().trim());
        planoExistente.setPublico(planoDTO.getPublico() != null ? planoDTO.getPublico() : false);
        planoExistente.setObservacoes(planoDTO.getObservacoes());

        return planoRepository.save(planoExistente);
    }

    @Transactional
    public void excluirPlano(@NotNull Long id, @NotNull Long criadorId, @NotNull TipoCriador tipoCriador) {
        Plano plano = buscarPorId(id);

        // Verificar se o usuário tem permissão para excluir
        if (!plano.getCriadorId().equals(criadorId) || 
            !plano.getTipoCriador().equals(tipoCriador)) {
            throw new UnauthorizedException("Você não tem permissão para excluir este plano");
        }

        planoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean podeEditarPlano(@NotNull Long planoId, @NotNull Long criadorId, @NotNull TipoCriador tipoCriador) {
        Optional<Plano> planoOpt = planoRepository.findById(planoId);
        if (planoOpt.isEmpty()) {
            return false;
        }
        Plano plano = planoOpt.get();
        return plano.getCriadorId().equals(criadorId) && plano.getTipoCriador().equals(tipoCriador);
    }
}
