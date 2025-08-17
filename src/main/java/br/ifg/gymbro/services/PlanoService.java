package br.ifg.gymbro.services;

import java.sql.SQLException;
import java.util.List;

import br.ifg.gymbro.dto.PlanoDTO;
import br.ifg.gymbro.enums.TipoCriador;
import br.ifg.gymbro.model.Plano;
import br.ifg.gymbro.repository.PlanoRepository;

public class PlanoService {
    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    public Plano criarPlano(PlanoDTO planoDTO) throws SQLException {
        // Validações
        if (planoDTO.getNome() == null || planoDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano é obrigatório");
        }

        if (planoDTO.getDescricao() == null || planoDTO.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do plano é obrigatória");
        }

        if (planoDTO.getCriadorId() == null) {
            throw new IllegalArgumentException("ID do criador é obrigatório");
        }

        if (planoDTO.getTipoCriador() == null) {
            throw new IllegalArgumentException("Tipo do criador é obrigatório");
        }

        // Criar plano
        Plano plano = new Plano(
            planoDTO.getNome().trim(),
            planoDTO.getDescricao().trim(),
            planoDTO.getCriadorId(),
            planoDTO.getTipoCriador(),
            planoDTO.getPublico() != null ? planoDTO.getPublico() : false,
            planoDTO.getObservacoes()
        );

        return planoRepository.criar(plano);
    }

    public Plano buscarPorId(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("ID do plano é obrigatório");
        }
        return planoRepository.buscarPorId(id);
    }

    public List<Plano> listarTodos() throws SQLException {
        return planoRepository.listarTodos();
    }

    public List<Plano> listarPorCriador(Long criadorId, TipoCriador tipoCriador) throws SQLException {
        if (criadorId == null) {
            throw new IllegalArgumentException("ID do criador é obrigatório");
        }
        if (tipoCriador == null) {
            throw new IllegalArgumentException("Tipo do criador é obrigatório");
        }
        return planoRepository.listarPorCriador(criadorId, tipoCriador);
    }

    public List<Plano> listarPublicos() throws SQLException {
        return planoRepository.listarPublicos();
    }

    public List<Plano> buscarPorNome(String nome) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome para busca é obrigatório");
        }
        return planoRepository.buscarPorNome(nome.trim());
    }

    public Plano atualizarPlano(Long id, PlanoDTO planoDTO, Long criadorId, TipoCriador tipoCriador) throws SQLException {
        // Validações
        if (id == null) {
            throw new IllegalArgumentException("ID do plano é obrigatório");
        }

        Plano planoExistente = planoRepository.buscarPorId(id);
        if (planoExistente == null) {
            throw new IllegalArgumentException("Plano não encontrado");
        }

        // Verificar se o usuário tem permissão para editar
        if (!planoExistente.getCriadorId().equals(criadorId) || 
            !planoExistente.getTipoCriador().equals(tipoCriador)) {
            throw new IllegalArgumentException("Você não tem permissão para editar este plano");
        }

        if (planoDTO.getNome() == null || planoDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano é obrigatório");
        }

        if (planoDTO.getDescricao() == null || planoDTO.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do plano é obrigatória");
        }

        // Atualizar dados
        planoExistente.setNome(planoDTO.getNome().trim());
        planoExistente.setDescricao(planoDTO.getDescricao().trim());
        planoExistente.setPublico(planoDTO.getPublico() != null ? planoDTO.getPublico() : false);
        planoExistente.setObservacoes(planoDTO.getObservacoes());

        return planoRepository.atualizar(planoExistente);
    }

    public boolean excluirPlano(Long id, Long criadorId, TipoCriador tipoCriador) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("ID do plano é obrigatório");
        }

        Plano plano = planoRepository.buscarPorId(id);
        if (plano == null) {
            throw new IllegalArgumentException("Plano não encontrado");
        }

        // Verificar se o usuário tem permissão para excluir
        if (!plano.getCriadorId().equals(criadorId) || 
            !plano.getTipoCriador().equals(tipoCriador)) {
            throw new IllegalArgumentException("Você não tem permissão para excluir este plano");
        }

        return planoRepository.excluir(id);
    }

    public boolean podeEditarPlano(Long planoId, Long criadorId, TipoCriador tipoCriador) throws SQLException {
        Plano plano = planoRepository.buscarPorId(planoId);
        if (plano == null) {
            return false;
        }
        return plano.getCriadorId().equals(criadorId) && plano.getTipoCriador().equals(tipoCriador);
    }
}
