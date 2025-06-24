package br.ifg.gymbro.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import br.ifg.gymbro.dto.LoginDTO;
import br.ifg.gymbro.dto.UsuarioDTO;
import br.ifg.gymbro.model.Usuario;
import br.ifg.gymbro.repository.UsuarioRepository;

public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(UsuarioDTO usuarioDTO) throws SQLException, IllegalArgumentException {
        // Validações
        if (usuarioDTO.getNome() == null || usuarioDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (usuarioDTO.getSenha() == null || usuarioDTO.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
        
        if (usuarioDTO.getPeso() == null || usuarioDTO.getPeso() <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }

        // Verificar se email já existe
        if (usuarioRepository.emailExiste(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        // Criar hash da senha
        String senhaHash = BCrypt.hashpw(usuarioDTO.getSenha(), BCrypt.gensalt());

        // Criar usuário
        Usuario usuario = new Usuario(
            usuarioDTO.getNome(),
            usuarioDTO.getEmail(),
            senhaHash,
            usuarioDTO.getPeso()
        );

        return usuarioRepository.salvar(usuario);
    }

    public Optional<Usuario> autenticarUsuario(LoginDTO loginDTO) throws SQLException {
        if (loginDTO.getEmail() == null || loginDTO.getSenha() == null) {
            return Optional.empty();
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorEmail(loginDTO.getEmail());
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (BCrypt.checkpw(loginDTO.getSenha(), usuario.getSenhaHash())) {
                return Optional.of(usuario);
            }
        }
        
        return Optional.empty();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) throws SQLException {
        return usuarioRepository.buscarPorId(id);
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email) throws SQLException {
        return usuarioRepository.buscarPorEmail(email);
    }

    public List<Usuario> listarTodosUsuarios() throws SQLException {
        return usuarioRepository.buscarTodos();
    }

    public Usuario atualizarUsuario(Long id, UsuarioDTO usuarioDTO) throws SQLException, IllegalArgumentException {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorId(id);
        
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        
        if (usuarioDTO.getNome() != null && !usuarioDTO.getNome().trim().isEmpty()) {
            usuario.setNome(usuarioDTO.getNome());
        }
        
        if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().trim().isEmpty()) {
            // Verificar se o novo email já existe (exceto para o próprio usuário)
            Optional<Usuario> usuarioComEmail = usuarioRepository.buscarPorEmail(usuarioDTO.getEmail());
            if (usuarioComEmail.isPresent() && !usuarioComEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email já está em uso");
            }
            usuario.setEmail(usuarioDTO.getEmail());
        }
        
        if (usuarioDTO.getPeso() != null && usuarioDTO.getPeso() > 0) {
            usuario.setPeso(usuarioDTO.getPeso());
        }

        usuarioRepository.atualizar(usuario);
        return usuario;
    }

    public void deletarUsuario(Long id) throws SQLException, IllegalArgumentException {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorId(id);
        
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        usuarioRepository.deletar(id);
    }
}
