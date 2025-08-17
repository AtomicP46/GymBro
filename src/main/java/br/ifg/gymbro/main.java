package br.ifg.gymbro;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import br.ifg.gymbro.config.DatabaseConfig;
import br.ifg.gymbro.controller.EquipamentoController;
import br.ifg.gymbro.controller.ExerciciosController;
import br.ifg.gymbro.controller.PersonalController;
import br.ifg.gymbro.controller.PlanoController;
import br.ifg.gymbro.controller.TreinoController;
import br.ifg.gymbro.controller.UsuarioController;
import br.ifg.gymbro.enums.TipoCriador;
import br.ifg.gymbro.model.Personal;
import br.ifg.gymbro.model.Usuario;
import br.ifg.gymbro.repository.EquipamentoRepository;
import br.ifg.gymbro.repository.ExerciciosRepository;
import br.ifg.gymbro.repository.PersonalRepository;
import br.ifg.gymbro.repository.PlanoExercicioRepository;
import br.ifg.gymbro.repository.PlanoRepository;
import br.ifg.gymbro.repository.TreinoExercicioRepository;
import br.ifg.gymbro.repository.TreinoRepository;
import br.ifg.gymbro.repository.UsuarioRepository;
import br.ifg.gymbro.services.EquipamentoService;
import br.ifg.gymbro.services.ExerciciosService;
import br.ifg.gymbro.services.PersonalService;
import br.ifg.gymbro.services.PlanoExercicioService;
import br.ifg.gymbro.services.PlanoService;
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

            // Inicializar camadas do Plano
            PlanoRepository planoRepository = new PlanoRepository(connection);
            PlanoExercicioRepository planoExercicioRepository = new PlanoExercicioRepository(connection);
            PlanoService planoService = new PlanoService(planoRepository);
            PlanoExercicioService planoExercicioService = new PlanoExercicioService(planoExercicioRepository, exerciciosRepository);
            PlanoController planoController = new PlanoController(planoService, planoExercicioService, exerciciosService);

            // Menu principal
            Usuario usuarioLogado = null;
            Personal personalLogado = null;
            boolean executando = true;

            while (executando) {
                if (usuarioLogado == null && personalLogado == null) {
                    // TELA INICIAL - Apenas cadastro e login
                    System.out.println("=== BEM-VINDO AO SISTEMA GYMBRO ===");
                    System.out.println("1. Cadastrar Usuário");
                    System.out.println("2. Cadastrar Personal");
                    System.out.println("3. Login Usuário");
                    System.out.println("4. Login Personal");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");

                    int opcao = scanner.nextInt();
                    scanner.nextLine(); // limpar buffer

                    switch (opcao) {
                        case 1:
                            usuarioController.criarUsuario();
                            break;
                        case 2:
                            personalController.criarPersonal();
                            break;
                        case 3:
                            usuarioLogado = usuarioController.fazerLogin();
                            break;
                        case 4:
                            personalLogado = personalController.fazerLogin();
                            break;
                        case 0:
                            executando = false;
                            System.out.println("👋 Até logo!");
                            break;
                        default:
                            System.out.println("❌ Opção inválida!\n");
                    }
                } else if (usuarioLogado != null) {
                    // MENU PRINCIPAL DO USUÁRIO LOGADO
                    System.out.println("=== SISTEMA GYMBRO - " + usuarioLogado.getNome().toUpperCase() + " ===");
                    System.out.println("1. Usuário");
                    System.out.println("2. Treinos");
                    System.out.println("3. Exercícios");
                    System.out.println("4. Equipamentos");
                    System.out.println("5. Planos");
                    System.out.println("0. Logout");
                    System.out.print("Escolha uma opção: ");

                    int opcao = scanner.nextInt();
                    scanner.nextLine(); // limpar buffer

                    switch (opcao) {
                        case 1:
                            menuUsuario(usuarioController, personalController, usuarioLogado, scanner);
                            break;
                        case 2:
                            treinoController.menuTreinos();
                            break;
                        case 3:
                            exerciciosController.menuExercicios();
                            break;
                        case 4:
                            equipamentoController.menuEquipamentos();
                            break;
                        case 5:
                            planoController.menuPrincipal(usuarioLogado.getId(), TipoCriador.USUARIO);
                            break;
                        case 0:
                            usuarioLogado = null;
                            System.out.println("✅ Logout realizado com sucesso!\n");
                            break;
                        default:
                            System.out.println("❌ Opção inválida!\n");
                    }
                } else if (personalLogado != null) {
                    // MENU PRINCIPAL DO PERSONAL LOGADO
                    System.out.println("=== SISTEMA GYMBRO - PERSONAL " + personalLogado.getNome().toUpperCase() + " ===");
                    System.out.println("1. Personal");
                    System.out.println("2. Treinos");
                    System.out.println("3. Exercícios");
                    System.out.println("4. Equipamentos");
                    System.out.println("5. Planos");
                    System.out.println("0. Logout");
                    System.out.print("Escolha uma opção: ");

                    int opcao = scanner.nextInt();
                    scanner.nextLine(); // limpar buffer

                    switch (opcao) {
                        case 1:
                            menuPersonal(personalController, usuarioController, personalLogado, scanner);
                            break;
                        case 2:
                            treinoController.menuTreinos();
                            break;
                        case 3:
                            exerciciosController.menuExercicios();
                            break;
                        case 4:
                            equipamentoController.menuEquipamentos();
                            break;
                        case 5:
                            planoController.menuPrincipal(personalLogado.getId(), TipoCriador.PERSONAL);
                            break;
                        case 0:
                            personalLogado = null;
                            System.out.println("✅ Logout realizado com sucesso!\n");
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

    // Menu específico para funcionalidades do usuário
    private static void menuUsuario(UsuarioController usuarioController, PersonalController personalController, 
                                   Usuario usuarioLogado, Scanner scanner) {
        boolean voltarMenu = false;
        
        while (!voltarMenu) {
            System.out.println("=== MENU USUÁRIO ===");
            System.out.println("1. Ver meu perfil");
            System.out.println("2. Buscar usuário por email");
            System.out.println("3. Listar todos os usuários");
            System.out.println("4. Listar todos os personais");
            System.out.println("5. Filtrar personais por formação");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // limpar buffer

                switch (opcao) {
                    case 1:
                        usuarioController.exibirPerfilUsuario(usuarioLogado);
                        break;
                    case 2:
                        usuarioController.buscarUsuarioPorEmail();
                        break;
                    case 3:
                        usuarioController.listarTodosUsuarios();
                        break;
                    case 4:
                        personalController.listarTodosPersonais();
                        break;
                    case 5:
                        personalController.listarPersonaisPorFormacao();
                        break;
                    case 0:
                        voltarMenu = true;
                        break;
                    default:
                        System.out.println("❌ Opção inválida!\n");
                }
            } catch (Exception e) {
                System.err.println("❌ Entrada inválida. Digite um número.\n");
                scanner.nextLine(); // limpar buffer
            }
        }
    }

    // Menu específico para funcionalidades do personal
    private static void menuPersonal(PersonalController personalController, UsuarioController usuarioController,
                                    Personal personalLogado, Scanner scanner) {
        boolean voltarMenu = false;
        
        while (!voltarMenu) {
            System.out.println("=== MENU PERSONAL ===");
            System.out.println("1. Ver meu perfil");
            System.out.println("2. Buscar personal por email");
            System.out.println("3. Listar todos os personais");
            System.out.println("4. Listar todos os usuários");
            System.out.println("5. Filtrar personais por formação");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // limpar buffer

                switch (opcao) {
                    case 1:
                        personalController.exibirPerfilPersonal(personalLogado);
                        break;
                    case 2:
                        personalController.buscarPersonalPorEmail();
                        break;
                    case 3:
                        personalController.listarTodosPersonais();
                        break;
                    case 4:
                        usuarioController.listarTodosUsuarios();
                        break;
                    case 5:
                        personalController.listarPersonaisPorFormacao();
                        break;
                    case 0:
                        voltarMenu = true;
                        break;
                    default:
                        System.out.println("❌ Opção inválida!\n");
                }
            } catch (Exception e) {
                System.err.println("❌ Entrada inválida. Digite um número.\n");
                scanner.nextLine(); // limpar buffer
            }
        }
    }
}
