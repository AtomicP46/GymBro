package br.ifg.gymbro.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.ifg.gymbro.model.Equipamento;

public class EquipamentoRepository {
    private Connection connection;

    public EquipamentoRepository(Connection connection) {
        this.connection = connection;
    }

    public Equipamento salvar(Equipamento equipamento) throws SQLException {
        String sql = "INSERT INTO Equipamento (nome, pesoequip) VALUES (?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, equipamento.getNome());
            if (equipamento.getPesoEquip() != null) {
                stmt.setFloat(2, equipamento.getPesoEquip());
            } else {
                stmt.setNull(2, Types.FLOAT);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                equipamento.setId(rs.getLong("id"));
            }
            
            return equipamento;
        }
    }

    public Optional<Equipamento> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM Equipamento WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Equipamento equipamento = new Equipamento(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getObject("pesoequip") != null ? rs.getFloat("pesoequip") : null
                );
                return Optional.of(equipamento);
            }
            
            return Optional.empty();
        }
    }

    public List<Equipamento> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM Equipamento ORDER BY nome";
        List<Equipamento> equipamentos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Equipamento equipamento = new Equipamento(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getObject("pesoequip") != null ? rs.getFloat("pesoequip") : null
                );
                equipamentos.add(equipamento);
            }
        }
        
        return equipamentos;
    }

    public List<Equipamento> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM Equipamento WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";
        List<Equipamento> equipamentos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Equipamento equipamento = new Equipamento(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getObject("pesoequip") != null ? rs.getFloat("pesoequip") : null
                );
                equipamentos.add(equipamento);
            }
        }
        
        return equipamentos;
    }

    public boolean nomeExiste(String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Equipamento WHERE LOWER(nome) = LOWER(?)";
        
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
        String sql = "SELECT COUNT(*) FROM Equipamento WHERE LOWER(nome) = LOWER(?) AND id != ?";
        
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

    public void atualizar(Equipamento equipamento) throws SQLException {
        String sql = "UPDATE Equipamento SET nome = ?, pesoequip = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, equipamento.getNome());
            if (equipamento.getPesoEquip() != null) {
                stmt.setFloat(2, equipamento.getPesoEquip());
            } else {
                stmt.setNull(2, Types.FLOAT);
            }
            stmt.setLong(3, equipamento.getId());
            
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM Equipamento WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Equipamento> buscarPorFaixaPeso(Float pesoMin, Float pesoMax) throws SQLException {
        String sql = "SELECT * FROM Equipamento WHERE pesoequip BETWEEN ? AND ? ORDER BY pesoequip";
        List<Equipamento> equipamentos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setFloat(1, pesoMin);
            stmt.setFloat(2, pesoMax);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Equipamento equipamento = new Equipamento(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getFloat("pesoequip")
                );
                equipamentos.add(equipamento);
            }
        }
        
        return equipamentos;
    }

    public long contarEquipamentos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Equipamento";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        }
    }
}
