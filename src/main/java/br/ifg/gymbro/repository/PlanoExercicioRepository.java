package br.ifg.gymbro.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ifg.gymbro.model.PlanoExercicio;

public class PlanoExercicioRepository {
    private final Connection connection;

    public PlanoExercicioRepository(Connection connection) {
        this.connection = connection;
    }

    public PlanoExercicio criar(PlanoExercicio planoExercicio) throws SQLException {
        String sql = "INSERT INTO Plano_Exercicio (plano_id, exercicio_id, series_sugeridas, observacoes, aquecimento) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, planoExercicio.getPlanoId());
            stmt.setLong(2, planoExercicio.getExercicioId());
            if (planoExercicio.getSeriesSugeridas() != null) {
                stmt.setInt(3, planoExercicio.getSeriesSugeridas());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setString(4, planoExercicio.getObservacoes());
            stmt.setBoolean(5, planoExercicio.isAquecimento());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                planoExercicio.setId(rs.getLong("id"));
            }
            return planoExercicio;
        }
    }

    public PlanoExercicio buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM Plano_Exercicio WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearResultSet(rs);
            }
            return null;
        }
    }

    public List<PlanoExercicio> listarPorPlano(Long planoId) throws SQLException {
        String sql = "SELECT * FROM Plano_Exercicio WHERE plano_id = ? ORDER BY aquecimento DESC, id";
        List<PlanoExercicio> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, planoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                exercicios.add(mapearResultSet(rs));
            }
        }
        return exercicios;
    }

    public List<PlanoExercicio> listarAquecimentoPorPlano(Long planoId) throws SQLException {
        String sql = "SELECT * FROM Plano_Exercicio WHERE plano_id = ? AND aquecimento = true ORDER BY id";
        List<PlanoExercicio> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, planoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                exercicios.add(mapearResultSet(rs));
            }
        }
        return exercicios;
    }

    public List<PlanoExercicio> listarPrincipaisPorPlano(Long planoId) throws SQLException {
        String sql = "SELECT * FROM Plano_Exercicio WHERE plano_id = ? AND aquecimento = false ORDER BY id";
        List<PlanoExercicio> exercicios = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, planoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                exercicios.add(mapearResultSet(rs));
            }
        }
        return exercicios;
    }

    public PlanoExercicio atualizar(PlanoExercicio planoExercicio) throws SQLException {
        String sql = "UPDATE Plano_Exercicio SET series_sugeridas = ?, observacoes = ?, aquecimento = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (planoExercicio.getSeriesSugeridas() != null) {
                stmt.setInt(1, planoExercicio.getSeriesSugeridas());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setString(2, planoExercicio.getObservacoes());
            stmt.setBoolean(3, planoExercicio.isAquecimento());
            stmt.setLong(4, planoExercicio.getId());
            
            stmt.executeUpdate();
            return planoExercicio;
        }
    }

    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM Plano_Exercicio WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluirPorPlano(Long planoId) throws SQLException {
        String sql = "DELETE FROM Plano_Exercicio WHERE plano_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, planoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existeExercicioNoPlano(Long planoId, Long exercicioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Plano_Exercicio WHERE plano_id = ? AND exercicio_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, planoId);
            stmt.setLong(2, exercicioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private PlanoExercicio mapearResultSet(ResultSet rs) throws SQLException {
        PlanoExercicio planoExercicio = new PlanoExercicio();
        planoExercicio.setId(rs.getLong("id"));
        planoExercicio.setPlanoId(rs.getLong("plano_id"));
        planoExercicio.setExercicioId(rs.getLong("exercicio_id"));
        
        int seriesSugeridas = rs.getInt("series_sugeridas");
        if (!rs.wasNull()) {
            planoExercicio.setSeriesSugeridas(seriesSugeridas);
        }
        
        planoExercicio.setObservacoes(rs.getString("observacoes"));
        planoExercicio.setAquecimento(rs.getBoolean("aquecimento"));
        return planoExercicio;
    }
}
