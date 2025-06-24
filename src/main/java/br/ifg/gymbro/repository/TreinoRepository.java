package br.ifg.gymbro.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.ifg.gymbro.model.Treino;

public class TreinoRepository {
    private Connection connection;

    public TreinoRepository(Connection connection) {
        this.connection = connection;
    }

    public Treino salvar(Treino treino) throws SQLException {
        String sql = "INSERT INTO Treino (nome, data_hora_inicio, data_hora_fim, usuario_id, personal_id) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, treino.getNome());
            stmt.setTimestamp(2, treino.getDataHoraInicio() != null ? 
                Timestamp.valueOf(treino.getDataHoraInicio()) : null);
            stmt.setTimestamp(3, treino.getDataHoraFim() != null ? 
                Timestamp.valueOf(treino.getDataHoraFim()) : null);
            stmt.setLong(4, treino.getUsuarioId());
            
            if (treino.getPersonalId() != null) {
                stmt.setLong(5, treino.getPersonalId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                treino.setId(rs.getLong("id"));
            }
            
            return treino;
        }
    }

    public Optional<Treino> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT t.*, u.nome as usuario_nome, p.nome as personal_nome " +
                    "FROM Treino t " +
                    "LEFT JOIN \"User\" u ON t.usuario_id = u.id " +
                    "LEFT JOIN Personal p ON t.personal_id = p.id " +
                    "WHERE t.id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Treino treino = new Treino(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getTimestamp("data_hora_inicio") != null ? 
                        rs.getTimestamp("data_hora_inicio").toLocalDateTime() : null,
                    rs.getTimestamp("data_hora_fim") != null ? 
                        rs.getTimestamp("data_hora_fim").toLocalDateTime() : null,
                    rs.getLong("usuario_id"),
                    rs.getObject("personal_id") != null ? rs.getLong("personal_id") : null
                );
                treino.setUsuarioNome(rs.getString("usuario_nome"));
                treino.setPersonalNome(rs.getString("personal_nome"));
                return Optional.of(treino);
            }
            
            return Optional.empty();
        }
    }

    public List<Treino> buscarTodos() throws SQLException {
        String sql = "SELECT t.*, u.nome as usuario_nome, p.nome as personal_nome " +
                    "FROM Treino t " +
                    "LEFT JOIN \"User\" u ON t.usuario_id = u.id " +
                    "LEFT JOIN Personal p ON t.personal_id = p.id " +
                    "ORDER BY t.data_hora_inicio DESC";
        List<Treino> treinos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Treino treino = new Treino(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getTimestamp("data_hora_inicio") != null ? 
                        rs.getTimestamp("data_hora_inicio").toLocalDateTime() : null,
                    rs.getTimestamp("data_hora_fim") != null ? 
                        rs.getTimestamp("data_hora_fim").toLocalDateTime() : null,
                    rs.getLong("usuario_id"),
                    rs.getObject("personal_id") != null ? rs.getLong("personal_id") : null
                );
                treino.setUsuarioNome(rs.getString("usuario_nome"));
                treino.setPersonalNome(rs.getString("personal_nome"));
                treinos.add(treino);
            }
        }
        
        return treinos;
    }

    public List<Treino> buscarPorUsuario(Long usuarioId) throws SQLException {
        String sql = "SELECT t.*, u.nome as usuario_nome, p.nome as personal_nome " +
                    "FROM Treino t " +
                    "LEFT JOIN \"User\" u ON t.usuario_id = u.id " +
                    "LEFT JOIN Personal p ON t.personal_id = p.id " +
                    "WHERE t.usuario_id = ? " +
                    "ORDER BY t.data_hora_inicio DESC";
        List<Treino> treinos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Treino treino = new Treino(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getTimestamp("data_hora_inicio") != null ? 
                        rs.getTimestamp("data_hora_inicio").toLocalDateTime() : null,
                    rs.getTimestamp("data_hora_fim") != null ? 
                        rs.getTimestamp("data_hora_fim").toLocalDateTime() : null,
                    rs.getLong("usuario_id"),
                    rs.getObject("personal_id") != null ? rs.getLong("personal_id") : null
                );
                treino.setUsuarioNome(rs.getString("usuario_nome"));
                treino.setPersonalNome(rs.getString("personal_nome"));
                treinos.add(treino);
            }
        }
        
        return treinos;
    }

    public List<Treino> buscarPorPersonal(Long personalId) throws SQLException {
        String sql = "SELECT t.*, u.nome as usuario_nome, p.nome as personal_nome " +
                    "FROM Treino t " +
                    "LEFT JOIN \"User\" u ON t.usuario_id = u.id " +
                    "LEFT JOIN Personal p ON t.personal_id = p.id " +
                    "WHERE t.personal_id = ? " +
                    "ORDER BY t.data_hora_inicio DESC";
        List<Treino> treinos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, personalId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Treino treino = new Treino(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getTimestamp("data_hora_inicio") != null ? 
                        rs.getTimestamp("data_hora_inicio").toLocalDateTime() : null,
                    rs.getTimestamp("data_hora_fim") != null ? 
                        rs.getTimestamp("data_hora_fim").toLocalDateTime() : null,
                    rs.getLong("usuario_id"),
                    rs.getLong("personal_id")
                );
                treino.setUsuarioNome(rs.getString("usuario_nome"));
                treino.setPersonalNome(rs.getString("personal_nome"));
                treinos.add(treino);
            }
        }
        
        return treinos;
    }

    public List<Treino> buscarEmAndamento() throws SQLException {
        String sql = "SELECT t.*, u.nome as usuario_nome, p.nome as personal_nome " +
                    "FROM Treino t " +
                    "LEFT JOIN \"User\" u ON t.usuario_id = u.id " +
                    "LEFT JOIN Personal p ON t.personal_id = p.id " +
                    "WHERE t.data_hora_inicio IS NOT NULL AND t.data_hora_fim IS NULL " +
                    "ORDER BY t.data_hora_inicio DESC";
        List<Treino> treinos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Treino treino = new Treino(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getTimestamp("data_hora_inicio").toLocalDateTime(),
                    null,
                    rs.getLong("usuario_id"),
                    rs.getObject("personal_id") != null ? rs.getLong("personal_id") : null
                );
                treino.setUsuarioNome(rs.getString("usuario_nome"));
                treino.setPersonalNome(rs.getString("personal_nome"));
                treinos.add(treino);
            }
        }
        
        return treinos;
    }

    public void atualizar(Treino treino) throws SQLException {
        String sql = "UPDATE Treino SET nome = ?, data_hora_inicio = ?, data_hora_fim = ?, " +
                    "usuario_id = ?, personal_id = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, treino.getNome());
            stmt.setTimestamp(2, treino.getDataHoraInicio() != null ? 
                Timestamp.valueOf(treino.getDataHoraInicio()) : null);
            stmt.setTimestamp(3, treino.getDataHoraFim() != null ? 
                Timestamp.valueOf(treino.getDataHoraFim()) : null);
            stmt.setLong(4, treino.getUsuarioId());
            
            if (treino.getPersonalId() != null) {
                stmt.setLong(5, treino.getPersonalId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            
            stmt.setLong(6, treino.getId());
            stmt.executeUpdate();
        }
    }

    public void iniciarTreino(Long treinoId, LocalDateTime dataHoraInicio) throws SQLException {
        String sql = "UPDATE Treino SET data_hora_inicio = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(dataHoraInicio));
            stmt.setLong(2, treinoId);
            stmt.executeUpdate();
        }
    }

    public void finalizarTreino(Long treinoId, LocalDateTime dataHoraFim) throws SQLException {
        String sql = "UPDATE Treino SET data_hora_fim = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(dataHoraFim));
            stmt.setLong(2, treinoId);
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM Treino WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public long contarTreinos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Treino";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        }
    }

    public long contarTreinosFinalizados() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Treino WHERE data_hora_fim IS NOT NULL";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        }
    }
}

