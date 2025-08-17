package br.ifg.gymbro.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.ifg.gymbro.enums.TipoCriador;
import br.ifg.gymbro.model.Plano;

public class PlanoRepository {
    private final Connection connection;

    public PlanoRepository(Connection connection) {
        this.connection = connection;
    }

    public Plano criar(Plano plano) throws SQLException {
        String sql = "INSERT INTO Plano (nome, descricao, criador_id, tipo_criador, publico, observacoes) " +
                    "VALUES (?, ?, ?, ?::tipo_criador, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, plano.getNome());
            stmt.setString(2, plano.getDescricao());
            stmt.setLong(3, plano.getCriadorId());
            stmt.setString(4, plano.getTipoCriador().name());
            stmt.setBoolean(5, plano.isPublico());
            stmt.setString(6, plano.getObservacoes());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                plano.setId(rs.getLong("id"));
            }
            return plano;
        }
    }

    public Plano buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM Plano WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearResultSet(rs);
            }
            return null;
        }
    }

    public List<Plano> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Plano ORDER BY data_criacao DESC";
        List<Plano> planos = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                planos.add(mapearResultSet(rs));
            }
        }
        return planos;
    }

    public List<Plano> listarPorCriador(Long criadorId, TipoCriador tipoCriador) throws SQLException {
        String sql = "SELECT * FROM Plano WHERE criador_id = ? AND tipo_criador = ?::tipo_criador " +
                    "ORDER BY data_criacao DESC";
        List<Plano> planos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, criadorId);
            stmt.setString(2, tipoCriador.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                planos.add(mapearResultSet(rs));
            }
        }
        return planos;
    }

    public List<Plano> listarPublicos() throws SQLException {
        String sql = "SELECT * FROM Plano WHERE publico = true ORDER BY data_criacao DESC";
        List<Plano> planos = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                planos.add(mapearResultSet(rs));
            }
        }
        return planos;
    }

    public Plano atualizar(Plano plano) throws SQLException {
        String sql = "UPDATE Plano SET nome = ?, descricao = ?, publico = ?, observacoes = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, plano.getNome());
            stmt.setString(2, plano.getDescricao());
            stmt.setBoolean(3, plano.isPublico());
            stmt.setString(4, plano.getObservacoes());
            stmt.setLong(5, plano.getId());
            
            stmt.executeUpdate();
            return plano;
        }
    }

    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM Plano WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Plano> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM Plano WHERE LOWER(nome) LIKE LOWER(?) ORDER BY data_criacao DESC";
        List<Plano> planos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                planos.add(mapearResultSet(rs));
            }
        }
        return planos;
    }

    private Plano mapearResultSet(ResultSet rs) throws SQLException {
        Plano plano = new Plano();
        plano.setId(rs.getLong("id"));
        plano.setNome(rs.getString("nome"));
        plano.setDescricao(rs.getString("descricao"));
        plano.setCriadorId(rs.getLong("criador_id"));
        plano.setTipoCriador(TipoCriador.valueOf(rs.getString("tipo_criador")));
        plano.setPublico(rs.getBoolean("publico"));
        plano.setObservacoes(rs.getString("observacoes"));
        
        Timestamp timestamp = rs.getTimestamp("data_criacao");
        if (timestamp != null) {
            plano.setDataCriacao(timestamp.toLocalDateTime());
        }
        
        return plano;
    }
}
