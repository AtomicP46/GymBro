package br.ifg.gymbro.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.ifg.gymbro.model.Personal;

public class PersonalRepository {
    private Connection connection;

    public PersonalRepository(Connection connection) {
        this.connection = connection;
    }

    public Personal salvar(Personal personal) throws SQLException {
        String sql = "INSERT INTO Personal (nome, email, senha, licenca, formacao) VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, personal.getNome());
            stmt.setString(2, personal.getEmail());
            stmt.setString(3, personal.getSenhaHash());
            stmt.setString(4, personal.getLicenca());
            stmt.setBoolean(5, personal.getFormacao());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                personal.setId(rs.getLong("id"));
            }
            
            return personal;
        }
    }

    public Optional<Personal> buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Personal WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Personal personal = new Personal(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("licenca"),
                    rs.getBoolean("formacao")
                );
                return Optional.of(personal);
            }
            
            return Optional.empty();
        }
    }

    public Optional<Personal> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM Personal WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Personal personal = new Personal(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("licenca"),
                    rs.getBoolean("formacao")
                );
                return Optional.of(personal);
            }
            
            return Optional.empty();
        }
    }

    public List<Personal> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM Personal";
        List<Personal> personais = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Personal personal = new Personal(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("licenca"),
                    rs.getBoolean("formacao")
                );
                personais.add(personal);
            }
        }
        
        return personais;
    }

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Personal WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        }
    }

    public void atualizar(Personal personal) throws SQLException {
        String sql = "UPDATE Personal SET nome = ?, email = ?, licenca = ?, formacao = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, personal.getNome());
            stmt.setString(2, personal.getEmail());
            stmt.setString(3, personal.getLicenca());
            stmt.setBoolean(4, personal.getFormacao());
            stmt.setLong(5, personal.getId());
            
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM Personal WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Personal> buscarPorFormacao(boolean temFormacao) throws SQLException {
        String sql = "SELECT * FROM Personal WHERE formacao = ?";
        List<Personal> personais = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, temFormacao);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Personal personal = new Personal(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("licenca"),
                    rs.getBoolean("formacao")
                );
                personais.add(personal);
            }
        }
        
        return personais;
    }
}
