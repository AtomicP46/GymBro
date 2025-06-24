package br.ifg.gymbro.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.ifg.gymbro.dto.TreinoDTO;
import br.ifg.gymbro.model.Treino;
import br.ifg.gymbro.repository.PersonalRepository;
import br.ifg.gymbro.repository.TreinoRepository;
import br.ifg.gymbro.repository.UsuarioRepository;

public class TreinoService {
    private TreinoRepository treinoRepository;
    private UsuarioRepository usuarioRepository;
    private PersonalRepository personalRepository;

    public TreinoService(TreinoRepository treinoRepository, UsuarioRepository usuarioRepository, 
                        PersonalRepository personalRepository) {
        this.treinoRepository = treinoRepository;
        this.usuarioRepository = usuarioRepository;
        this.personalRepository = personalRepository;
    }

    public Treino criarTreino(TreinoDTO treinoDTO) throws SQLException, IllegalArgumentException {
        // Validações
        if (treinoDTO.getNome() == null || treinoDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do treino é obrigatório");
        }

        if (treinoDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("Usuário é obrigatório");
        }

        // Verificar se usuário existe
        if (usuarioRepository.buscarPorId(treinoDTO.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        // Verificar se personal existe (se informado)
        if (treinoDTO.getPersonalId() != null) {
            if (personalRepository.buscarPorId(treinoDTO.getPersonalId()).isEmpty()) {
                throw new IllegalArgumentException("Personal não encontrado");
            }
        }

        // Criar treino
        Treino treino = new Treino(
            treinoDTO.getNome().trim(),
            treinoDTO.getDataHoraInicio(),
            treinoDTO.getUsuarioId(),
            treinoDTO.getPersonalId()
        );

        return treinoRepository.salvar(treino);
    }

    public Optional<Treino> buscarTreinoPorId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        return treinoRepository.buscarPorId(id);
    }

    public List<Treino> listarTodosTreinos() throws SQLException {
        return treinoRepository.buscarTodos();
    }

    public List<Treino> listarTreinosPorUsuario(Long usuarioId) throws SQLException {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("ID do usuário deve ser um número positivo");
        }

        // Verificar se usuário existe
        if (usuarioRepository.buscarPorId(usuarioId).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        return treinoRepository.buscarPorUsuario(usuarioId);
    }

    public List<Treino> listarTreinosPorPersonal(Long personalId) throws SQLException {
        if (personalId == null || personalId <= 0) {
            throw new IllegalArgumentException("ID do personal deve ser um número positivo");
        }

        // Verificar se personal existe
        if (personalRepository.buscarPorId(personalId).isEmpty()) {
            throw new IllegalArgumentException("Personal não encontrado");
        }

        return treinoRepository.buscarPorPersonal(personalId);
    }

    public List<Treino> listarTreinosEmAndamento() throws SQLException {
        return treinoRepository.buscarEmAndamento();
    }

    public Treino iniciarTreino(Long treinoId) throws SQLException, IllegalArgumentException {
        if (treinoId == null || treinoId <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<Treino> treinoOpt = treinoRepository.buscarPorId(treinoId);
        
        if (treinoOpt.isEmpty()) {
            throw new IllegalArgumentException("Treino não encontrado");
        }

        Treino treino = treinoOpt.get();

        if (treino.isIniciado()) {
            throw new IllegalArgumentException("Treino já foi iniciado");
        }

        LocalDateTime agora = LocalDateTime.now();
        treinoRepository.iniciarTreino(treinoId, agora);
        
        // Buscar treino atualizado
        return treinoRepository.buscarPorId(treinoId).orElse(treino);
    }

    public Treino finalizarTreino(Long treinoId) throws SQLException, IllegalArgumentException {
        if (treinoId == null || treinoId <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<Treino> treinoOpt = treinoRepository.buscarPorId(treinoId);
        
        if (treinoOpt.isEmpty()) {
            throw new IllegalArgumentException("Treino não encontrado");
        }

        Treino treino = treinoOpt.get();

        if (!treino.isIniciado()) {
            throw new IllegalArgumentException("Treino não foi iniciado ainda");
        }

        if (treino.isFinalizado()) {
            throw new IllegalArgumentException("Treino já foi finalizado");
        }

        LocalDateTime agora = LocalDateTime.now();
        treinoRepository.finalizarTreino(treinoId, agora);
        
        // Buscar treino atualizado
        return treinoRepository.buscarPorId(treinoId).orElse(treino);
    }

    public Treino atualizarTreino(Long id, TreinoDTO treinoDTO) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<Treino> treinoOpt = treinoRepository.buscarPorId(id);
        
        if (treinoOpt.isEmpty()) {
            throw new IllegalArgumentException("Treino não encontrado");
        }

        Treino treino = treinoOpt.get();

        // Não permitir editar treino já iniciado
        if (treino.isIniciado()) {
            throw new IllegalArgumentException("Não é possível editar um treino já iniciado");
        }
        
        // Validar e atualizar nome
        if (treinoDTO.getNome() != null && !treinoDTO.getNome().trim().isEmpty()) {
            treino.setNome(treinoDTO.getNome().trim());
        }
        
        // Validar e atualizar usuário
        if (treinoDTO.getUsuarioId() != null) {
            if (usuarioRepository.buscarPorId(treinoDTO.getUsuarioId()).isEmpty()) {
                throw new IllegalArgumentException("Usuário não encontrado");
            }
            treino.setUsuarioId(treinoDTO.getUsuarioId());
        }

        // Validar e atualizar personal
        if (treinoDTO.getPersonalId() != null) {
            if (treinoDTO.getPersonalId() > 0) {
                if (personalRepository.buscarPorId(treinoDTO.getPersonalId()).isEmpty()) {
                    throw new IllegalArgumentException("Personal não encontrado");
                }
                treino.setPersonalId(treinoDTO.getPersonalId());
            } else {
                treino.setPersonalId(null); // Remove personal
            }
        }

        treinoRepository.atualizar(treino);
        return treinoRepository.buscarPorId(id).orElse(treino);
    }

    public void deletarTreino(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<Treino> treinoOpt = treinoRepository.buscarPorId(id);
        
        if (treinoOpt.isEmpty()) {
            throw new IllegalArgumentException("Treino não encontrado");
        }

        Treino treino = treinoOpt.get();

        // Não permitir excluir treino em andamento
        if (treino.isEmAndamento()) {
            throw new IllegalArgumentException("Não é possível excluir um treino em andamento. Finalize o treino primeiro.");
        }

        treinoRepository.deletar(id);
    }

    public long contarTreinos() throws SQLException {
        return treinoRepository.contarTreinos();
    }

    public long contarTreinosFinalizados() throws SQLException {
        return treinoRepository.contarTreinosFinalizados();
    }

    public boolean treinoExiste(Long id) throws SQLException {
        if (id == null || id <= 0) {
            return false;
        }
        return treinoRepository.buscarPorId(id).isPresent();
    }

    public boolean usuarioPodeAcessarTreino(Long treinoId, Long usuarioId) throws SQLException {
        Optional<Treino> treinoOpt = treinoRepository.buscarPorId(treinoId);
        
        if (treinoOpt.isEmpty()) {
            return false;
        }

        Treino treino = treinoOpt.get();
        return treino.getUsuarioId().equals(usuarioId);
    }

    public boolean personalPodeAcessarTreino(Long treinoId, Long personalId) throws SQLException {
        Optional<Treino> treinoOpt = treinoRepository.buscarPorId(treinoId);
        
        if (treinoOpt.isEmpty()) {
            return false;
        }

        Treino treino = treinoOpt.get();
        return treino.getPersonalId() != null && treino.getPersonalId().equals(personalId);
    }
}
