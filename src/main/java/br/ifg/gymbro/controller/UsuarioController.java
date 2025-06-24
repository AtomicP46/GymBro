package br.ifg.gymbro.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import br.ifg.gymbro.dto.LoginDTO;
import br.ifg.gymbro.dto.UsuarioDTO;
import br.ifg.gymbro.model.Usuario;
import br.ifg.gymbro.services.UsuarioService;

public class UsuarioController {
    private UsuarioService usuarioService;
    private Scanner scanner;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.scanner = new Scanner(System.in);
    }

    public void criarUsuario() {
        try {
            System.out.println("=== CRIAR NOVO USUÁRIO ===");
            
            System.out.print("Informe seu nome: ");
            String nome = scanner.nextLine();

            System.out.print("Informe seu email: ");
            String email = scanner.nextLine();

            System.out.print("Informe sua senha: ");
            String senha = scanner.nextLine();

            System.out.print("Informe seu peso: ");
            float peso = scanner.nextFloat();
            scanner.nextLine(); // limpar buffer

            UsuarioDTO usuarioDTO = new UsuarioDTO(nome, email, senha, peso);
            Usuario usuario = usuarioService.criarUsuario(usuarioDTO);

            System.out.println("\n✅ Usuário criado com sucesso!");
            System.out.println("ID: " + usuario.getId());
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Peso: " + usuario.getPeso() + " kg\n");

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public Usuario fazerLogin() {
        try {
            System.out.println("=== LOGIN ===");
            
            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            LoginDTO loginDTO = new LoginDTO(email, senha);
            Optional<Usuario> usuarioOpt = usuarioService.autenticarUsuario(loginDTO);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                System.out.println("\n✅ Login realizado com sucesso!");
                System.out.println("Bem-vindo(a), " + usuario.getNome() + "!\n");
                return usuario;
            } else {
                System.out.println("\n❌ Email ou senha incorretos.\n");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            return null;
        }
    }

    public void exibirPerfilUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("❌ Usuário não encontrado.");
            return;
        }

        System.out.println("=== SEU PERFIL ===");
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Peso: " + usuario.getPeso() + " kg\n");
    }

    public void listarTodosUsuarios() {
        try {
            System.out.println("=== TODOS OS USUÁRIOS ===");
            
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            
            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário encontrado.\n");
                return;
            }

            for (Usuario usuario : usuarios) {
                System.out.println("ID: " + usuario.getId());
                System.out.println("Nome: " + usuario.getNome());
                System.out.println("Email: " + usuario.getEmail());
                System.out.println("Peso: " + usuario.getPeso() + " kg");
                System.out.println("---");
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void buscarUsuarioPorEmail() {
        try {
            System.out.print("Digite o email do usuário: ");
            String email = scanner.nextLine();

            Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorEmail(email);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                System.out.println("\n=== USUÁRIO ENCONTRADO ===");
                exibirPerfilUsuario(usuario);
            } else {
                System.out.println("\n❌ Usuário não encontrado com o email: " + email + "\n");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }
}
