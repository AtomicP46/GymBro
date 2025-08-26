package com.gymbro.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.gymbro.dto.PlanoDTO;
import com.gymbro.enums.TipoCriador;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.exception.UnauthorizedException;
import com.gymbro.model.Plano;
import com.gymbro.repository.PlanoRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
@Transactional
public class PlanoService {
    
    @Autowired
    private PlanoRepository planoRepository;

    private final Predicate<String> nomeValido = nome -> nome != null && !nome.trim().isEmpty();
    private final Predicate<String> descricaoValida = desc -> desc != null && !desc.trim().isEmpty();

    @Transactional
    public Plano criarPlano(@Valid PlanoDTO planoDTO) {
        return Optional.of(planoDTO)
            .filter(dto -> nomeValido.test(dto.getNome()))
            .filter(dto -> descricaoValida.test(dto.getDescricao()))
            .map(this::construirPlano)
            .map(planoRepository::save)
            .orElseThrow(() -> new IllegalArgumentException("Dados do plano inválidos"));
    }

    private Plano construirPlano(PlanoDTO dto) {
        return new Plano(
            dto.getNome().trim(),
            dto.getDescricao().trim(),
            dto.getCriadorId(),
            dto.getTipoCriador(),
            Optional.ofNullable(dto.getPublico()).orElse(false),
            dto.getObservacoes()
        );
    }

    @Transactional(readOnly = true)
    public Optional<Plano> buscarPorId(@NotNull Long id) {
        return planoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Plano obterPorId(@NotNull Long id) {
        return buscarPorId(id)
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
        return listarTodos().stream()
            .filter(Plano::getPublico)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Plano> buscarPorNome(@NotNull String nome) {
        return Optional.ofNullable(nome)
            .filter(n -> !n.trim().isEmpty())
            .map(String::trim)
            .map(n -> planoRepository.findByNomeContainingIgnoreCaseOrderByDataCriacaoDesc(n))
            .orElse(List.of());
    }

    @Transactional
    public Plano atualizarPlano(@NotNull Long id, @Valid PlanoDTO planoDTO, @NotNull Long criadorId, @NotNull TipoCriador tipoCriador) {
        return buscarPorId(id)
            .filter(plano -> podeEditarPlano(plano, criadorId, tipoCriador))
            .map(plano -> atualizarCampos(plano, planoDTO))
            .map(planoRepository::save)
            .orElseThrow(() -> {
                if (buscarPorId(id).isEmpty()) {
                    return new ResourceNotFoundException("Plano não encontrado com ID: " + id);
                }
                return new UnauthorizedException("Você não tem permissão para editar este plano");
            });
    }

    private Plano atualizarCampos(Plano plano, PlanoDTO dto) {
        plano.setNome(dto.getNome().trim());
        plano.setDescricao(dto.getDescricao().trim());
        plano.setPublico(Optional.ofNullable(dto.getPublico()).orElse(false));
        plano.setObservacoes(dto.getObservacoes());
        return plano;
    }

    @Transactional
    public void excluirPlano(@NotNull Long id, @NotNull Long criadorId, @NotNull TipoCriador tipoCriador) {
        buscarPorId(id)
            .filter(plano -> podeEditarPlano(plano, criadorId, tipoCriador))
            .ifPresentOrElse(
                plano -> planoRepository.deleteById(id),
                () -> {
                    if (buscarPorId(id).isEmpty()) {
                        throw new ResourceNotFoundException("Plano não encontrado com ID: " + id);
                    }
                    throw new UnauthorizedException("Você não tem permissão para excluir este plano");
                }
            );
    }

    @Transactional(readOnly = true)
    public boolean podeEditarPlano(@NotNull Long planoId, @NotNull Long criadorId, @NotNull TipoCriador tipoCriador) {
        return buscarPorId(planoId)
            .map(plano -> podeEditarPlano(plano, criadorId, tipoCriador))
            .orElse(false);
    }

    private boolean podeEditarPlano(Plano plano, Long criadorId, TipoCriador tipoCriador) {
        return plano.getCriadorId().equals(criadorId) && plano.getTipoCriador().equals(tipoCriador);
    }

    @Transactional(readOnly = true)
    public long contarPlanosPorCriador(@NotNull Long criadorId, @NotNull TipoCriador tipoCriador) {
        return listarPorCriador(criadorId, tipoCriador).stream().count();
    }

    @Transactional(readOnly = true)
    public long contarPlanosPublicos() {
        return listarPublicos().stream().count();
    }

    @Transactional(readOnly = true)
    public List<Plano> listarPlanosRecentes(int limite) {
        return listarTodos().stream()
            .limit(limite)
            .collect(Collectors.toList());
    }
}
