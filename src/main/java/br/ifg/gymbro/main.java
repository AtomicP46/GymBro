package br.ifg.gymbro;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import br.ifg.gymbro.config.DatabaseConfig;
import br.ifg.gymbro.controller.EquipamentoController;
import br.ifg.gymbro.controller.ExerciciosController;
import br.ifg.gymbro.controller.PersonalController;
import br.ifg.gymbro.controller.TreinoController;
import br.ifg.gymbro.controller.UsuarioController;
import br.ifg.gymbro.model.Personal;
import br.ifg.gymbro.model.Usuario;
import br.ifg.gymbro.repository.EquipamentoRepository;
import br.ifg.gymbro.repository.ExerciciosRepository;
import br.ifg.gymbro.repository.PersonalRepository;
import br.ifg.gymbro.repository.TreinoExercicioRepository;
import br.ifg.gymbro.repository.TreinoRepository;
import br.ifg.gymbro.repository.UsuarioRepository;
import br.ifg.gymbro.services.EquipamentoService;
import br.ifg.gymbro.services.ExerciciosService;
import br.ifg.gymbro.services.PersonalService;
import br.ifg.gymbro.services.TreinoExercicioService;
import br.ifg.gymbro.services.TreinoService;
import br.ifg.gymbro.services.UsuarioService;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Conectar ao banco de dados
            connection = DatabaseConfig.getConnection();
            System.out.println("✅ Conectado ao banco de dados PostgreSQL!");

            // Inicializar camadas
            UsuarioRepository usuarioRepository = new UsuarioRepository(connection);
            UsuarioService usuarioService = new UsuarioService(usuarioRepository);
            UsuarioController usuarioController = new UsuarioController(usuarioService);

            // Inicializar camadas do Personal
            PersonalRepository personalRepository = new PersonalRepository(connection);
            PersonalService personalService = new PersonalService(personalRepository);
            PersonalController personalController = new PersonalController(personalService);

            // Inicializar camadas do Equipamento
            EquipamentoRepository equipamentoRepository = new EquipamentoRepository(connection);
            EquipamentoService equipamentoService = new EquipamentoService(equipamentoRepository);
            EquipamentoController equipamentoController = new EquipamentoController(equipamentoService);

            // Inicializar camadas do Exercicios
            ExerciciosRepository exerciciosRepository = new ExerciciosRepository(connection);
            ExerciciosService exerciciosService = new ExerciciosService(exerciciosRepository, equipamentoRepository);
            ExerciciosController exerciciosController = new ExerciciosController(exerciciosService, equipamentoService);

            // Inicializar camadas do Treino
            TreinoRepository treinoRepository = new TreinoRepository(connection);
            TreinoExercicioRepository treinoExercicioRepository = new TreinoExercicioRepository(connection);
            TreinoService treinoService = new TreinoService(treinoRepository, usuarioRepository, personalRepository);
            TreinoExercicioService treinoExercicioService = new TreinoExercicioService(treinoExercicioRepository, treinoRepository, exerciciosRepository);
            TreinoController treinoController = new TreinoController(treinoService, treinoExercicioService, usuarioService, personalService, exerciciosService);

            // Menu principal
            Usuario usuarioLogado = null;
            Personal personalLogado = null;
            boolean executando = true;

            while (executando) {
                if (usuarioLogado == null && personalLogado == null) {
                    System.out.println("=== SISTEMA GYMBRO ===");
                    System.out.println("=== USUÁRIOS ===");
                    System.out.println("1. Criar novo usuário");
                    System.out.println("2. Login usuário");
                    System.out.println("3. Buscar usuário por email");
                    System.out.println("4. Listar todos os usuários");
                    System.out.println("=== PERSONAIS ===");
                    System.out.println("5. Criar novo personal");
                    System.out.println("6. Login personal");
                    System.out.println("7. Buscar personal por email");
                    System.out.println("8. Listar todos os personais");
                    System.out.println("9. Filtrar personais por formação");
                    System.out.println("=== EQUIPAMENTOS ===");
                    System.out.println("10. Gerenciar equipamentos");
                    System.out.println("11. Gerenciar exercícios");
                    System.out.println("12. Gerenciar treinos");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");

                    int opcao = scanner.nextInt();
                    scanner.nextLine(); // limpar buffer

                    switch (opcao) {
                        case 1:
                            usuarioController.criarUsuario();
                            break;
                        case 2:
                            usuarioLogado = usuarioController.fazerLogin();
                            break;
                        case 3:
                            usuarioController.buscarUsuarioPorEmail();
                            break;
                        case 4:
                            usuarioController.listarTodosUsuarios();
                            break;
                        case 5:
                            personalController.criarPersonal();
                            break;
                        case 6:
                            personalLogado = personalController.fazerLogin();
                            break;
                        case 7:
                            personalController.buscarPersonalPorEmail();
                            break;
                        case 8:
                            personalController.listarTodosPersonais();
                            break;
                        case 9:
                            personalController.listarPersonaisPorFormacao();
                            break;
                        case 10:
                            equipamentoController.menuEquipamentos();
                            break;
                        case 11:
                            exerciciosController.menuExercicios();
                            break;
                        case 12:
                            treinoController.menuTreinos();
                            break;
                        case 0:
                            executando = false;
                            System.out.println("👋 Até logo!");
                            break;
                        default:
                            System.out.println("❌ Opção inválida!\n");
                    }
                } else if (usuarioLogado != null) {
                    System.out.println("=== ÁREA DO USUÁRIO ===");
                    System.out.println("1. Ver meu perfil");
                    System.out.println("2. Listar todos os usuários");
                    System.out.println("3. Buscar usuário por email");
                    System.out.println("4. Listar todos os personais");
                    System.out.println("5. Gerenciar exercícios");
                    System.out.println("6. Gerenciar equipamentos");
                    System.out.println("7. Gerenciar treinos");
                    System.out.println("8. Logout");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");

                    int opcao = scanner.nextInt();
                    scanner.nextLine(); // limpar buffer

                    switch (opcao) {
                        case 1:
                            usuarioController.exibirPerfilUsuario(usuarioLogado);
                            break;
                        case 2:
                            usuarioController.listarTodosUsuarios();
                            break;
                        case 3:
                            usuarioController.buscarUsuarioPorEmail();
                            break;
                        case 4:
                            personalController.listarTodosPersonais();
                            break;
                        case 5:
                            exerciciosController.menuExercicios();
                            break;
                        case 6:
                            equipamentoController.menuEquipamentos();
                            break;
                        case 7:
                            treinoController.menuTreinos();
                            break;
                        case 8:
                            usuarioLogado = null;
                            System.out.println("✅ Logout realizado com sucesso!\n");
                            break;
                        case 0:
                            executando = false;
                            System.out.println("👋 Até logo!");
                            break;
                        default:
                            System.out.println("❌ Opção inválida!\n");
                    }
                } else if (personalLogado != null) {
                    System.out.println("=== ÁREA DO PERSONAL ===");
                    System.out.println("1. Ver meu perfil");
                    System.out.println("2. Listar todos os personais");
                    System.out.println("3. Buscar personal por email");
                    System.out.println("4. Listar todos os usuários");
                    System.out.println("5. Filtrar personais por formação");
                    System.out.println("6. Gerenciar exercícios");
                    System.out.println("7. Gerenciar equipamentos");
                    System.out.println("8. Gerenciar treinos");
                    System.out.println("9. Logout");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");

                    int opcao = scanner.nextInt();
                    scanner.nextLine(); // limpar buffer

                    switch (opcao) {
                        case 1:
                            personalController.exibirPerfilPersonal(personalLogado);
                            break;
                        case 2:
                            personalController.listarTodosPersonais();
                            break;
                        case 3:
                            personalController.buscarPersonalPorEmail();
                            break;
                        case 4:
                            usuarioController.listarTodosUsuarios();
                            break;
                        case 5:
                            personalController.listarPersonaisPorFormacao();
                            break;
                        case 6:
                            exerciciosController.menuExercicios();
                            break;
                        case 7:
                            equipamentoController.menuEquipamentos();
                            break;
                        case 8:
                            treinoController.menuTreinos();
                            break;
                        case 9:
                            personalLogado = null;
                            System.out.println("✅ Logout realizado com sucesso!\n");
                            break;
                        case 0:
                            executando = false;
                            System.out.println("👋 Até logo!");
                            break;
                        default:
                            System.out.println("❌ Opção inválida!\n");
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de conexão com o banco de dados: " + e.getMessage());
            System.err.println("Verifique se o PostgreSQL está rodando e as configurações estão corretas.");
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(connection);
            scanner.close();
        }
    }
}
