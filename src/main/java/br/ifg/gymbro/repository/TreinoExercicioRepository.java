package br.ifg.gymbro.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.ifg.gymbro.dto.ExercicioProgressoDTO;
import br.ifg.gymbro.model.TreinoExercicio;

public class TreinoExercicioRepository {
    private Connection connection;

    public TreinoExercicioRepository(Connection connection) {
        this.connection = connection;
    }

    public TreinoExercicio salvar(TreinoExercicio treinoExercicio) throws SQLException {
        String sql = "INSERT INTO Treino_Exercicio (treino_id, exercicio_id, series, repeticoes, " +
                    "peso_usado, anotacoes, aquecimento) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, treinoExercicio.getTreinoId());
            stmt.setLong(2, treinoExercicio.getExercicioId());
            
            if (treinoExercicio.getSeries() != null) {
                stmt.setInt(3, treinoExercicio.getSeries());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            if (treinoExercicio.getRepeticoes() != null) {
                stmt.setInt(4, treinoExercicio.getRepeticoes());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            if (treinoExercicio.getPesoUsado() != null) {
                stmt.setFloat(5, treinoExercicio.getPesoUsado());
            } else {
                stmt.setNull(5, Types.FLOAT);
            }
            
            stmt.setString(6, treinoExercicio.getAnotacoes());
            stmt.setBoolean(7, treinoExercicio.getAquecimento() != null ? 
                treinoExercicio.getAquecimento() : false);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                treinoExercicio.setId(rs.getLong("id"));
            }
            
            return treinoExercicio;
        }
    }

    public Optional<TreinoExercicio> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT te.*, e.nome as exercicio_nome, e.regiao as exercicio_regiao " +
                    "FROM Treino_Exercicio te " +
                    "LEFT JOIN Exercicios e ON te.exercicio_id = e.id " +
                    "WHERE te.id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                TreinoExercicio treinoExercicio = new TreinoExercicio(
                    rs.getLong("id"),
                    rs.getLong("treino_id"),
                    rs.getLong("exercicio_id"),
                    rs.getObject("series") != null ? rs.getInt("series") : null,
                    rs.getObject("repeticoes") != null ? rs.getInt("repeticoes") : null,
                    rs.getObject("peso_usado") != null ? rs.getFloat("peso_usado") : null,
                    rs.getString("anotacoes"),
                    rs.getBoolean("aquecimento")
                );
                treinoExercicio.setExercicioNome(rs.getString("exercicio_nome"));
                treinoExercicio.setExercicioRegiao(rs.getString("exercicio_regiao"));
                return Optional.of(treinoExercicio);
            }
            
            return Optional.empty();
        }
    }

    public List<TreinoExercicio> buscarPorTreino(Long treinoId) throws SQLException {
        String sql = "SELECT te.*, e.nome as exercicio_nome, e.regiao as exercicio_regiao " +
                    "FROM Treino_Exercicio te " +
                    "LEFT JOIN Exercicios e ON te.exercicio_id = e.id " +
                    "WHERE te.treino_id = ? " +
                    "ORDER BY te.id";
        List<TreinoExercicio> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, treinoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TreinoExercicio treinoExercicio = new TreinoExercicio(
                    rs.getLong("id"),
                    rs.getLong("treino_id"),
                    rs.getLong("exercicio_id"),
                    rs.getObject("series") != null ? rs.getInt("series") : null,
                    rs.getObject("repeticoes") != null ? rs.getInt("repeticoes") : null,
                    rs.getObject("peso_usado") != null ? rs.getFloat("peso_usado") : null,
                    rs.getString("anotacoes"),
                    rs.getBoolean("aquecimento")
                );
                treinoExercicio.setExercicioNome(rs.getString("exercicio_nome"));
                treinoExercicio.setExercicioRegiao(rs.getString("exercicio_regiao"));
                exercicios.add(treinoExercicio);
            }
        }
        
        return exercicios;
    }

    public void atualizar(TreinoExercicio treinoExercicio) throws SQLException {
        String sql = "UPDATE Treino_Exercicio SET treino_id = ?, exercicio_id = ?, series = ?, " +
                    "repeticoes = ?, peso_usado = ?, anotacoes = ?, aquecimento = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, treinoExercicio.getTreinoId());
            stmt.setLong(2, treinoExercicio.getExercicioId());
            
            if (treinoExercicio.getSeries() != null) {
                stmt.setInt(3, treinoExercicio.getSeries());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            if (treinoExercicio.getRepeticoes() != null) {
                stmt.setInt(4, treinoExercicio.getRepeticoes());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            if (treinoExercicio.getPesoUsado() != null) {
                stmt.setFloat(5, treinoExercicio.getPesoUsado());
            } else {
                stmt.setNull(5, Types.FLOAT);
            }
            
            stmt.setString(6, treinoExercicio.getAnotacoes());
            stmt.setBoolean(7, treinoExercicio.getAquecimento() != null ? 
                treinoExercicio.getAquecimento() : false);
            stmt.setLong(8, treinoExercicio.getId());
            
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM Treino_Exercicio WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void deletarPorTreino(Long treinoId) throws SQLException {
        String sql = "DELETE FROM Treino_Exercicio WHERE treino_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, treinoId);
            stmt.executeUpdate();
        }
    }

    public boolean existeExercicioNoTreino(Long treinoId, Long exercicioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Treino_Exercicio WHERE treino_id = ? AND exercicio_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, treinoId);
            stmt.setLong(2, exercicioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        }
    }

    public List<ExercicioProgressoDTO> buscarHistoricoExercicioPorUsuario(Long usuarioId, Long exercicioId) throws SQLException {
        String sql = "SELECT te.id as treino_exercicio_id, te.treino_id, te.exercicio_id, " +
                     "te.series, te.repeticoes, te.peso_usado, te.anotacoes, te.aquecimento, " +
                     "e.nome as exercicio_nome, " +
                     "t.data_hora_inicio, t.data_hora_fim " +
                     "FROM Treino_Exercicio te " +
                     "JOIN Treinos t ON te.treino_id = t.id " +
                     "JOIN Exercicios e ON te.exercicio_id = e.id " +
                     "WHERE t.usuario_id = ? AND te.exercicio_id = ? " +
                     "ORDER BY t.data_hora_inicio DESC";

        List<ExercicioProgressoDTO> historico = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.setLong(2, exercicioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExercicioProgressoDTO dto = new ExercicioProgressoDTO(
                    rs.getLong("treino_exercicio_id"),
                    rs.getLong("treino_id"),
                    rs.getLong("exercicio_id"),
                    rs.getString("exercicio_nome"),
                    rs.getObject("series") != null ? rs.getInt("series") : null,
                    rs.getObject("repeticoes") != null ? rs.getInt("repeticoes") : null,
                    rs.getObject("peso_usado") != null ? rs.getFloat("peso_usado") : null,
                    rs.getString("anotacoes"),
                    rs.getBoolean("aquecimento"),
                    rs.getTimestamp("data_hora_inicio") != null ? rs.getTimestamp("data_hora_inicio").toLocalDateTime() : null,
                    rs.getTimestamp("data_hora_fim") != null ? rs.getTimestamp("data_hora_fim").toLocalDateTime() : null
                );
                historico.add(dto);
            }
        }
        return historico;
    }
}
