package br.ifg.gymbro.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import br.ifg.gymbro.dto.PlanoDTO;
import br.ifg.gymbro.dto.PlanoExercicioDTO;
import br.ifg.gymbro.enums.TipoCriador;
import br.ifg.gymbro.model.Exercicios;
import br.ifg.gymbro.model.Plano;
import br.ifg.gymbro.model.PlanoExercicio;
import br.ifg.gymbro.services.ExerciciosService;
import br.ifg.gymbro.services.PlanoExercicioService;
import br.ifg.gymbro.services.PlanoService;

public class PlanoController {
    private final PlanoService planoService;
    private final PlanoExercicioService planoExercicioService;
    private final ExerciciosService exerciciosService;
    private final Scanner scanner;

    public PlanoController(PlanoService planoService, PlanoExercicioService planoExercicioService,
                          ExerciciosService exerciciosService) {
        this.planoService = planoService;
        this.planoExercicioService = planoExercicioService;
        this.exerciciosService = exerciciosService;
        this.scanner = new Scanner(System.in);
    }

    public void menuPrincipal(Long usuarioId, TipoCriador tipoUsuario) {
        int opcao;
        do {
            System.out.println("\n=== GERENCIAMENTO DE PLANOS ===");
            System.out.println("1. Criar novo plano");
            System.out.println("2. Meus planos");
            System.out.println("3. Planos públicos");
            System.out.println("4. Buscar planos");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha

            switch (opcao) {
                case 1:
                    criarPlano(usuarioId, tipoUsuario);
                    break;
                case 2:
                    listarMeusPlanos(usuarioId, tipoUsuario);
                    break;
                case 3:
                    listarPlanosPublicos();
                    break;
                case 4:
                    buscarPlanos();
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void criarPlano(Long usuarioId, TipoCriador tipoUsuario) {
        try {
            System.out.println("\n=== CRIAR NOVO PLANO ===");
            
            System.out.print("Nome do plano: ");
            String nome = scanner.nextLine();
            
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            
            System.out.print("Plano público? (s/n): ");
            boolean publico = scanner.nextLine().toLowerCase().startsWith("s");
            
            System.out.print("Observações (opcional): ");
            String observacoes = scanner.nextLine();
            if (observacoes.trim().isEmpty()) {
                observacoes = null;
            }

            PlanoDTO planoDTO = new PlanoDTO(nome, descricao, usuarioId, tipoUsuario, publico, observacoes);
            Plano plano = planoService.criarPlano(planoDTO);

            System.out.println("Plano criado com sucesso! ID: " + plano.getId());
            
            System.out.print("Deseja adicionar exercícios agora? (s/n): ");
            if (scanner.nextLine().toLowerCase().startsWith("s")) {
                gerenciarExerciciosDoPlano(plano.getId(), usuarioId, tipoUsuario);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao criar plano: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarMeusPlanos(Long usuarioId, TipoCriador tipoUsuario) {
        try {
            System.out.println("\n=== MEUS PLANOS ===");
            List<Plano> planos = planoService.listarPorCriador(usuarioId, tipoUsuario);

            if (planos.isEmpty()) {
                System.out.println("Você ainda não criou nenhum plano.");
                return;
            }

            for (int i = 0; i < planos.size(); i++) {
                Plano plano = planos.get(i);
                System.out.printf("%d. %s - %s (%s)\n", 
                    i + 1, 
                    plano.getNome(), 
                    plano.getDescricao(),
                    plano.isPublico() ? "Público" : "Privado"
                );
            }

            System.out.print("\nSelecione um plano para gerenciar (0 para voltar): ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao > 0 && opcao <= planos.size()) {
                Plano planoSelecionado = planos.get(opcao - 1);
                menuGerenciarPlano(planoSelecionado, usuarioId, tipoUsuario);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar planos: " + e.getMessage());
        }
    }

    private void listarPlanosPublicos() {
        try {
            System.out.println("\n=== PLANOS PÚBLICOS ===");
            List<Plano> planos = planoService.listarPublicos();

            if (planos.isEmpty()) {
                System.out.println("Nenhum plano público encontrado.");
                return;
            }

            for (int i = 0; i < planos.size(); i++) {
                Plano plano = planos.get(i);
                System.out.printf("%d. %s - %s\n", 
                    i + 1, 
                    plano.getNome(), 
                    plano.getDescricao()
                );
            }

            System.out.print("\nSelecione um plano para visualizar (0 para voltar): ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao > 0 && opcao <= planos.size()) {
                Plano planoSelecionado = planos.get(opcao - 1);
                visualizarPlano(planoSelecionado);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar planos públicos: " + e.getMessage());
        }
    }

    private void buscarPlanos() {
        try {
            System.out.print("Digite o nome do plano para buscar: ");
            String nome = scanner.nextLine();

            List<Plano> planos = planoService.buscarPorNome(nome);

            if (planos.isEmpty()) {
                System.out.println("Nenhum plano encontrado com esse nome.");
                return;
            }

            System.out.println("\n=== RESULTADOS DA BUSCA ===");
            for (int i = 0; i < planos.size(); i++) {
                Plano plano = planos.get(i);
                System.out.printf("%d. %s - %s (%s)\n", 
                    i + 1, 
                    plano.getNome(), 
                    plano.getDescricao(),
                    plano.isPublico() ? "Público" : "Privado"
                );
            }

            System.out.print("\nSelecione um plano para visualizar (0 para voltar): ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao > 0 && opcao <= planos.size()) {
                Plano planoSelecionado = planos.get(opcao - 1);
                visualizarPlano(planoSelecionado);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar planos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuGerenciarPlano(Plano plano, Long usuarioId, TipoCriador tipoUsuario) {
        int opcao;
        do {
            System.out.println("\n=== GERENCIAR PLANO: " + plano.getNome() + " ===");
            System.out.println("1. Visualizar plano");
            System.out.println("2. Editar plano");
            System.out.println("3. Gerenciar exercícios");
            System.out.println("4. Excluir plano");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    visualizarPlano(plano);
                    break;
                case 2:
                    editarPlano(plano, usuarioId, tipoUsuario);
                    break;
                case 3:
                    gerenciarExerciciosDoPlano(plano.getId(), usuarioId, tipoUsuario);
                    break;
                case 4:
                    if (excluirPlano(plano, usuarioId, tipoUsuario)) {
                        return; // Sair do menu se o plano foi excluído
                    }
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void visualizarPlano(Plano plano) {
        try {
            System.out.println("\n=== DETALHES DO PLANO ===");
            System.out.println("Nome: " + plano.getNome());
            System.out.println("Descrição: " + plano.getDescricao());
            System.out.println("Visibilidade: " + (plano.isPublico() ? "Público" : "Privado"));
            if (plano.getObservacoes() != null) {
                System.out.println("Observações: " + plano.getObservacoes());
            }

            List<PlanoExercicio> exercicios = planoExercicioService.listarPorPlano(plano.getId());
            
            if (exercicios.isEmpty()) {
                System.out.println("\nNenhum exercício adicionado a este plano.");
            } else {
                System.out.println("\n=== EXERCÍCIOS DO PLANO ===");
                
                // Listar exercícios de aquecimento
                List<PlanoExercicio> aquecimento = planoExercicioService.listarAquecimentoPorPlano(plano.getId());
                if (!aquecimento.isEmpty()) {
                    System.out.println("\n--- AQUECIMENTO ---");
                    for (PlanoExercicio pe : aquecimento) {
                        System.out.printf("• Exercício ID: %d", pe.getExercicioId());
                        if (pe.getSeriesSugeridas() != null) {
                            System.out.printf(" - %d séries", pe.getSeriesSugeridas());
                        }
                        if (pe.getObservacoes() != null) {
                            System.out.printf(" - %s", pe.getObservacoes());
                        }
                        System.out.println();
                    }
                }
                
                // Listar exercícios principais
                List<PlanoExercicio> principais = planoExercicioService.listarPrincipaisPorPlano(plano.getId());
                if (!principais.isEmpty()) {
                    System.out.println("\n--- EXERCÍCIOS PRINCIPAIS ---");
                    for (PlanoExercicio pe : principais) {
                        System.out.printf("• Exercício ID: %d", pe.getExercicioId());
                        if (pe.getSeriesSugeridas() != null) {
                            System.out.printf(" - %d séries", pe.getSeriesSugeridas());
                        }
                        if (pe.getObservacoes() != null) {
                            System.out.printf(" - %s", pe.getObservacoes());
                        }
                        System.out.println();
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao visualizar plano: " + e.getMessage());
        }
    }

    private void editarPlano(Plano plano, Long usuarioId, TipoCriador tipoUsuario) {
        try {
            System.out.println("\n=== EDITAR PLANO ===");
            
            System.out.print("Nome atual: " + plano.getNome() + "\nNovo nome (Enter para manter): ");
            String nome = scanner.nextLine();
            if (nome.trim().isEmpty()) {
                nome = plano.getNome();
            }
            
            System.out.print("Descrição atual: " + plano.getDescricao() + "\nNova descrição (Enter para manter): ");
            String descricao = scanner.nextLine();
            if (descricao.trim().isEmpty()) {
                descricao = plano.getDescricao();
            }
            
            System.out.print("Público atual: " + (plano.isPublico() ? "Sim" : "Não") + "\nTornar público? (s/n/Enter para manter): ");
            String publicoStr = scanner.nextLine();
            Boolean publico = plano.getPublico();
            if (!publicoStr.trim().isEmpty()) {
                publico = publicoStr.toLowerCase().startsWith("s");
            }
            
            System.out.print("Observações atuais: " + (plano.getObservacoes() != null ? plano.getObservacoes() : "Nenhuma") + 
                            "\nNovas observações (Enter para manter): ");
            String observacoes = scanner.nextLine();
            if (observacoes.trim().isEmpty()) {
                observacoes = plano.getObservacoes();
            }

            PlanoDTO planoDTO = new PlanoDTO(nome, descricao, usuarioId, tipoUsuario, publico, observacoes);
            planoService.atualizarPlano(plano.getId(), planoDTO, usuarioId, tipoUsuario);

            System.out.println("Plano atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao editar plano: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private boolean excluirPlano(Plano plano, Long usuarioId, TipoCriador tipoUsuario) {
        try {
            System.out.print("Tem certeza que deseja excluir o plano '" + plano.getNome() + "'? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.toLowerCase().startsWith("s")) {
                if (planoService.excluirPlano(plano.getId(), usuarioId, tipoUsuario)) {
                    System.out.println("Plano excluído com sucesso!");
                    return true;
                } else {
                    System.out.println("Erro ao excluir plano.");
                }
            }
            return false;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir plano: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    private void gerenciarExerciciosDoPlano(Long planoId, Long usuarioId, TipoCriador tipoUsuario) {
        int opcao;
        do {
            System.out.println("\n=== GERENCIAR EXERCÍCIOS DO PLANO ===");
            System.out.println("1. Adicionar exercício");
            System.out.println("2. Listar exercícios");
            System.out.println("3. Editar exercício");
            System.out.println("4. Remover exercício");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarExercicioAoPlano(planoId);
                    break;
                case 2:
                    listarExerciciosDoPlano(planoId);
                    break;
                case 3:
                    editarExercicioDoPlano(planoId);
                    break;
                case 4:
                    removerExercicioDoPlano(planoId);
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void adicionarExercicioAoPlano(Long planoId) {
        try {
            // Listar exercícios disponíveis
            List<Exercicios> exercicios = exerciciosService.listarTodosExercicios();
            if (exercicios.isEmpty()) {
                System.out.println("Nenhum exercício cadastrado no sistema.");
                return;
            }

            System.out.println("\n=== EXERCÍCIOS DISPONÍVEIS ===");
            for (int i = 0; i < exercicios.size(); i++) {
                Exercicios ex = exercicios.get(i);
                System.out.printf("%d. %s - %s\n", i + 1, ex.getNome(), ex.getRegiao());
            }

            System.out.print("Selecione um exercício: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao < 1 || opcao > exercicios.size()) {
                System.out.println("Opção inválida!");
                return;
            }

            Exercicios exercicioSelecionado = exercicios.get(opcao - 1);

            System.out.print("Número de séries sugeridas (opcional, Enter para pular): ");
            String seriesStr = scanner.nextLine();
            Integer series = null;
            if (!seriesStr.trim().isEmpty()) {
                series = Integer.parseInt(seriesStr);
            }

            System.out.print("É exercício de aquecimento? (s/n): ");
            boolean aquecimento = scanner.nextLine().toLowerCase().startsWith("s");

            System.out.print("Observações (opcional): ");
            String observacoes = scanner.nextLine();
            if (observacoes.trim().isEmpty()) {
                observacoes = null;
            }

            PlanoExercicioDTO dto = new PlanoExercicioDTO(planoId, exercicioSelecionado.getId(), series, observacoes, aquecimento);
            planoExercicioService.adicionarExercicio(dto);

            System.out.println("Exercício adicionado ao plano com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar exercício: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarExerciciosDoPlano(Long planoId) {
        try {
            List<PlanoExercicio> exercicios = planoExercicioService.listarPorPlano(planoId);
            
            if (exercicios.isEmpty()) {
                System.out.println("Nenhum exercício adicionado a este plano.");
                return;
            }

            System.out.println("\n=== EXERCÍCIOS DO PLANO ===");
            for (int i = 0; i < exercicios.size(); i++) {
                PlanoExercicio pe = exercicios.get(i);
                System.out.printf("%d. Exercício ID: %d", i + 1, pe.getExercicioId());
                if (pe.getSeriesSugeridas() != null) {
                    System.out.printf(" - %d séries", pe.getSeriesSugeridas());
                }
                System.out.printf(" - %s", pe.isAquecimento() ? "AQUECIMENTO" : "PRINCIPAL");
                if (pe.getObservacoes() != null) {
                    System.out.printf(" - %s", pe.getObservacoes());
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar exercícios: " + e.getMessage());
        }
    }

    private void editarExercicioDoPlano(Long planoId) {
        try {
            List<PlanoExercicio> exercicios = planoExercicioService.listarPorPlano(planoId);
            
            if (exercicios.isEmpty()) {
                System.out.println("Nenhum exercício adicionado a este plano.");
                return;
            }

            System.out.println("\n=== EXERCÍCIOS DO PLANO ===");
            for (int i = 0; i < exercicios.size(); i++) {
                PlanoExercicio pe = exercicios.get(i);
                System.out.printf("%d. Exercício ID: %d - %s\n", 
                    i + 1, pe.getExercicioId(), pe.isAquecimento() ? "AQUECIMENTO" : "PRINCIPAL");
            }

            System.out.print("Selecione um exercício para editar: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao < 1 || opcao > exercicios.size()) {
                System.out.println("Opção inválida!");
                return;
            }

            PlanoExercicio exercicioSelecionado = exercicios.get(opcao - 1);

            System.out.print("Séries atuais: " + exercicioSelecionado.getSeriesSugeridas() + 
                            "\nNovas séries (Enter para manter): ");
            String seriesStr = scanner.nextLine();
            Integer series = exercicioSelecionado.getSeriesSugeridas();
            if (!seriesStr.trim().isEmpty()) {
                series = Integer.parseInt(seriesStr);
            }

            System.out.print("Aquecimento atual: " + (exercicioSelecionado.isAquecimento() ? "Sim" : "Não") + 
                            "\nÉ aquecimento? (s/n/Enter para manter): ");
            String aquecimentoStr = scanner.nextLine();
            Boolean aquecimento = exercicioSelecionado.getAquecimento();
            if (!aquecimentoStr.trim().isEmpty()) {
                aquecimento = aquecimentoStr.toLowerCase().startsWith("s");
            }

            System.out.print("Observações atuais: " + (exercicioSelecionado.getObservacoes() != null ? exercicioSelecionado.getObservacoes() : "Nenhuma") + 
                            "\nNovas observações (Enter para manter): ");
            String observacoes = scanner.nextLine();
            if (observacoes.trim().isEmpty()) {
                observacoes = exercicioSelecionado.getObservacoes();
            }

            PlanoExercicioDTO dto = new PlanoExercicioDTO(planoId, exercicioSelecionado.getExercicioId(), series, observacoes, aquecimento);
            planoExercicioService.atualizarExercicio(exercicioSelecionado.getId(), dto);

            System.out.println("Exercício atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao editar exercício: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void removerExercicioDoPlano(Long planoId) {
        try {
            List<PlanoExercicio> exercicios = planoExercicioService.listarPorPlano(planoId);
            
            if (exercicios.isEmpty()) {
                System.out.println("Nenhum exercício adicionado a este plano.");
                return;
            }

            System.out.println("\n=== EXERCÍCIOS DO PLANO ===");
            for (int i = 0; i < exercicios.size(); i++) {
                PlanoExercicio pe = exercicios.get(i);
                System.out.printf("%d. Exercício ID: %d - %s\n", 
                    i + 1, pe.getExercicioId(), pe.isAquecimento() ? "AQUECIMENTO" : "PRINCIPAL");
            }

            System.out.print("Selecione um exercício para remover: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao < 1 || opcao > exercicios.size()) {
                System.out.println("Opção inválida!");
                return;
            }

            PlanoExercicio exercicioSelecionado = exercicios.get(opcao - 1);

            System.out.print("Tem certeza que deseja remover este exercício? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.toLowerCase().startsWith("s")) {
                if (planoExercicioService.removerExercicio(exercicioSelecionado.getId())) {
                    System.out.println("Exercício removido com sucesso!");
                } else {
                    System.out.println("Erro ao remover exercício.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao remover exercício: " + e.getMessage());
        }
    }
}
