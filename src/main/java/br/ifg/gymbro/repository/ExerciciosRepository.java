package br.ifg.gymbro.repository;

import br.ifg.gymbro.model.Exercicios;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExerciciosRepository {
    private Connection connection;

    public ExerciciosRepository(Connection connection) {
        this.connection = connection;
    }

    public Exercicios salvar(Exercicios exercicio) throws SQLException {
        String sql = "INSERT INTO Exercicios (nome, regiao, tipo, unilateral, equipamento_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, exercicio.getNome());
            stmt.setString(2, exercicio.getRegiao());
            stmt.setString(3, exercicio.getTipo());
            stmt.setBoolean(4, exercicio.getUnilateral());
            
            if (exercicio.getEquipamentoId() != null) {
                stmt.setLong(5, exercicio.getEquipamentoId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exercicio.setId(rs.getLong("id"));
            }
            
            return exercicio;
        }
    }

    public Optional<Exercicios> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT e.*, eq.nome as equipamento_nome " +
                    "FROM Exercicios e " +
                    "LEFT JOIN Equipamento eq ON e.equipamento_id = eq.id " +
                    "WHERE e.id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Exercicios exercicio = new Exercicios(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("regiao"),
                    rs.getString("tipo"),
                    rs.getBoolean("unilateral"),
                    rs.getObject("equipamento_id") != null ? rs.getLong("equipamento_id") : null
                );
                exercicio.setEquipamentoNome(rs.getString("equipamento_nome"));
                return Optional.of(exercicio);
            }
            
            return Optional.empty();
        }
    }

    public List<Exercicios> buscarTodos() throws SQLException {
        String sql = "SELECT e.*, eq.nome as equipamento_nome " +
                    "FROM Exercicios e " +
                    "LEFT JOIN Equipamento eq ON e.equipamento_id = eq.id " +
                    "ORDER BY e.nome";
        List<Exercicios> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Exercicios exercicio = new Exercicios(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("regiao"),
                    rs.getString("tipo"),
                    rs.getBoolean("unilateral"),
                    rs.getObject("equipamento_id") != null ? rs.getLong("equipamento_id") : null
                );
                exercicio.setEquipamentoNome(rs.getString("equipamento_nome"));
                exercicios.add(exercicio);
            }
        }
        
        return exercicios;
    }

    public List<Exercicios> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT e.*, eq.nome as equipamento_nome " +
                    "FROM Exercicios e " +
                    "LEFT JOIN Equipamento eq ON e.equipamento_id = eq.id " +
                    "WHERE LOWER(e.nome) LIKE LOWER(?) " +
                    "ORDER BY e.nome";
        List<Exercicios> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Exercicios exercicio = new Exercicios(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("regiao"),
                    rs.getString("tipo"),
                    rs.getBoolean("unilateral"),
                    rs.getObject("equipamento_id") != null ? rs.getLong("equipamento_id") : null
                );
                exercicio.setEquipamentoNome(rs.getString("equipamento_nome"));
                exercicios.add(exercicio);
            }
        }
        
        return exercicios;
    }

    public List<Exercicios> buscarPorRegiao(String regiao) throws SQLException {
        String sql = "SELECT e.*, eq.nome as equipamento_nome " +
                    "FROM Exercicios e " +
                    "LEFT JOIN Equipamento eq ON e.equipamento_id = eq.id " +
                    "WHERE LOWER(e.regiao) = LOWER(?) " +
                    "ORDER BY e.nome";
        List<Exercicios> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, regiao);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Exercicios exercicio = new Exercicios(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("regiao"),
                    rs.getString("tipo"),
                    rs.getBoolean("unilateral"),
                    rs.getObject("equipamento_id") != null ? rs.getLong("equipamento_id") : null
                );
                exercicio.setEquipamentoNome(rs.getString("equipamento_nome"));
                exercicios.add(exercicio);
            }
        }
        
        return exercicios;
    }

    public List<Exercicios> buscarPorTipo(String tipo) throws SQLException {
        String sql = "SELECT e.*, eq.nome as equipamento_nome " +
                    "FROM Exercicios e " +
                    "LEFT JOIN Equipamento eq ON e.equipamento_id = eq.id " +
                    "WHERE LOWER(e.tipo) = LOWER(?) " +
                    "ORDER BY e.nome";
        List<Exercicios> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Exercicios exercicio = new Exercicios(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("regiao"),
                    rs.getString("tipo"),
                    rs.getBoolean("unilateral"),
                    rs.getObject("equipamento_id") != null ? rs.getLong("equipamento_id") : null
                );
                exercicio.setEquipamentoNome(rs.getString("equipamento_nome"));
                exercicios.add(exercicio);
            }
        }
        
        return exercicios;
    }

    public List<Exercicios> buscarPorEquipamento(Long equipamentoId) throws SQLException {
        String sql = "SELECT e.*, eq.nome as equipamento_nome " +
                    "FROM Exercicios e " +
                    "LEFT JOIN Equipamento eq ON e.equipamento_id = eq.id " +
                    "WHERE e.equipamento_id = ? " +
                    "ORDER BY e.nome";
        List<Exercicios> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, equipamentoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Exercicios exercicio = new Exercicios(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("regiao"),
                    rs.getString("tipo"),
                    rs.getBoolean("unilateral"),
                    rs.getLong("equipamento_id")
                );
                exercicio.setEquipamentoNome(rs.getString("equipamento_nome"));
                exercicios.add(exercicio);
            }
        }
        
        return exercicios;
    }

    public List<Exercicios> buscarPorUnilateral(boolean unilateral) throws SQLException {
        String sql = "SELECT e.*, eq.nome as equipamento_nome " +
                    "FROM Exercicios e " +
                    "LEFT JOIN Equipamento eq ON e.equipamento_id = eq.id " +
                    "WHERE e.unilateral = ? " +
                    "ORDER BY e.nome";
        List<Exercicios> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, unilateral);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Exercicios exercicio = new Exercicios(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("regiao"),
                    rs.getString("tipo"),
                    rs.getBoolean("unilateral"),
                    rs.getObject("equipamento_id") != null ? rs.getLong("equipamento_id") : null
                );
                exercicio.setEquipamentoNome(rs.getString("equipamento_nome"));
                exercicios.add(exercicio);
            }
        }
        
        return exercicios;
    }

    public boolean nomeExiste(String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Exercicios WHERE LOWER(nome) = LOWER(?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        }
    }

    public boolean nomeExisteExcetoId(String nome, Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Exercicios WHERE LOWER(nome) = LOWER(?) AND id != ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setLong(2, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        }
    }

    public void atualizar(Exercicios exercicio) throws SQLException {
        String sql = "UPDATE Exercicios SET nome = ?, regiao = ?, tipo = ?, unilateral = ?, equipamento_id = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, exercicio.getNome());
            stmt.setString(2, exercicio.getRegiao());
            stmt.setString(3, exercicio.getTipo());
            stmt.setBoolean(4, exercicio.getUnilateral());
            
            if (exercicio.getEquipamentoId() != null) {
                stmt.setLong(5, exercicio.getEquipamentoId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            
            stmt.setLong(6, exercicio.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM Exercicios WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public long contarExercicios() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Exercicios";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        }
    }

    public long contarPorRegiao(String regiao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Exercicios WHERE LOWER(regiao) = LOWER(?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, regiao);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        }
    }
}
