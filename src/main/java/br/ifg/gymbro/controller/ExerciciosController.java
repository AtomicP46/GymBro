package br.ifg.gymbro.controller;

import br.ifg.gymbro.dto.ExerciciosDTO;
import br.ifg.gymbro.enums.RegiaoCorpo;
import br.ifg.gymbro.enums.TipoExercicio;
import br.ifg.gymbro.model.Exercicios;
import br.ifg.gymbro.model.Equipamento;
import br.ifg.gymbro.services.ExerciciosService;
import br.ifg.gymbro.services.EquipamentoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ExerciciosController {
    private ExerciciosService exerciciosService;
    private EquipamentoService equipamentoService;
    private Scanner scanner;

    public ExerciciosController(ExerciciosService exerciciosService, EquipamentoService equipamentoService) {
        this.exerciciosService = exerciciosService;
        this.equipamentoService = equipamentoService;
        this.scanner = new Scanner(System.in);
    }

    public void criarExercicio() {
        try {
            System.out.println("=== CADASTRAR NOVO EXERCÍCIO ===");
            
            System.out.print("Informe o nome do exercício: ");
            String nome = scanner.nextLine();

            // Selecionar região
            String regiao = selecionarRegiao();
            if (regiao == null) return;

            // Selecionar tipo
            String tipo = selecionarTipo();
            if (tipo == null) return;

            System.out.print("O exercício é unilateral? (sim/não): ");
            String respostaUnilateral = scanner.nextLine().trim().toLowerCase();
            boolean unilateral = respostaUnilateral.equals("sim") || respostaUnilateral.equals("s");

            // Selecionar equipamento (opcional)
            Long equipamentoId = selecionarEquipamento();

            ExerciciosDTO exercicioDTO = new ExerciciosDTO(nome, regiao, tipo, unilateral, equipamentoId);
            Exercicios exercicio = exerciciosService.criarExercicio(exercicioDTO);

            System.out.println("\n✅ Exercício cadastrado com sucesso!");
            exibirExercicio(exercicio);

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private String selecionarRegiao() {
        System.out.println("\n=== SELECIONAR REGIÃO ===");
        String[] regioes = RegiaoCorpo.getDescricoes();
        
        for (int i = 0; i < regioes.length; i++) {
            System.out.println((i + 1) + ". " + regioes[i]);
        }
        
        System.out.print("Escolha a região (1-" + regioes.length + "): ");
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
            
            if (opcao >= 1 && opcao <= regioes.length) {
                return regioes[opcao - 1];
            } else {
                System.err.println("❌ Opção inválida!");
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Entrada inválida!");
            scanner.nextLine(); // limpar buffer
            return null;
        }
    }

    private String selecionarTipo() {
        System.out.println("\n=== SELECIONAR TIPO ===");
        String[] tipos = TipoExercicio.getDescricoes();
        
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i]);
        }
        
        System.out.print("Escolha o tipo (1-" + tipos.length + "): ");
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
            
            if (opcao >= 1 && opcao <= tipos.length) {
                return tipos[opcao - 1];
            } else {
                System.err.println("❌ Opção inválida!");
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Entrada inválida!");
            scanner.nextLine(); // limpar buffer
            return null;
        }
    }

    private Long selecionarEquipamento() {
        try {
            System.out.print("\nDeseja associar um equipamento? (sim/não): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            
            if (!resposta.equals("sim") && !resposta.equals("s")) {
                return null;
            }

            List<Equipamento> equipamentos = equipamentoService.listarTodosEquipamentos();
            
            if (equipamentos.isEmpty()) {
                System.out.println("❌ Nenhum equipamento cadastrado.");
                return null;
            }

            System.out.println("\n=== EQUIPAMENTOS DISPONÍVEIS ===");
            for (int i = 0; i < equipamentos.size(); i++) {
                Equipamento eq = equipamentos.get(i);
                System.out.println((i + 1) + ". " + eq.getNome() + 
                    (eq.getPesoEquip() != null ? " (" + eq.getPesoEquip() + "kg)" : ""));
            }
            
            System.out.print("Escolha o equipamento (1-" + equipamentos.size() + ") ou 0 para pular: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
            
            if (opcao == 0) {
                return null;
            } else if (opcao >= 1 && opcao <= equipamentos.size()) {
                return equipamentos.get(opcao - 1).getId();
            } else {
                System.err.println("❌ Opção inválida!");
                return null;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar equipamentos: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Entrada inválida!");
            scanner.nextLine(); // limpar buffer
            return null;
        }
    }

    public void listarTodosExercicios() {
        try {
            System.out.println("=== TODOS OS EXERCÍCIOS ===");
            
            List<Exercicios> exercicios = exerciciosService.listarTodosExercicios();
            
            if (exercicios.isEmpty()) {
                System.out.println("Nenhum exercício encontrado.\n");
                return;
            }

            System.out.println("Total de exercícios: " + exercicios.size());
            System.out.println("---");

            for (Exercicios exercicio : exercicios) {
                exibirExercicioResumido(exercicio);
                System.out.println("---");
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void buscarExercicioPorId() {
        try {
            System.out.print("Digite o ID do exercício: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Optional<Exercicios> exercicioOpt = exerciciosService.buscarExercicioPorId(id);
            
            if (exercicioOpt.isPresent()) {
                System.out.println("\n=== EXERCÍCIO ENCONTRADO ===");
                exibirExercicio(exercicioOpt.get());
            } else {
                System.out.println("\n❌ Exercício não encontrado com o ID: " + id + "\n");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            scanner.nextLine(); // limpar buffer em caso de erro
        }
    }

    public void buscarExerciciosPorNome() {
        try {
            System.out.print("Digite o nome do exercício (busca parcial): ");
            String nome = scanner.nextLine();

            List<Exercicios> exercicios = exerciciosService.buscarExerciciosPorNome(nome);
            
            if (exercicios.isEmpty()) {
                System.out.println("\n❌ Nenhum exercício encontrado com o nome: " + nome + "\n");
                return;
            }

            System.out.println("\n=== EXERCÍCIOS ENCONTRADOS ===");
            System.out.println("Total encontrado: " + exercicios.size());
            System.out.println("---");

            for (Exercicios exercicio : exercicios) {
                exibirExercicioResumido(exercicio);
                System.out.println("---");
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void buscarExerciciosPorRegiao() {
        try {
            String regiao = selecionarRegiao();
            if (regiao == null) return;

            List<Exercicios> exercicios = exerciciosService.buscarExerciciosPorRegiao(regiao);
            
            if (exercicios.isEmpty()) {
                System.out.println("\n❌ Nenhum exercício encontrado para a região: " + regiao + "\n");
                return;
            }

            System.out.println("\n=== EXERCÍCIOS PARA " + regiao.toUpperCase() + " ===");
            System.out.println("Total encontrado: " + exercicios.size());
            System.out.println("---");

            for (Exercicios exercicio : exercicios) {
                exibirExercicioResumido(exercicio);
                System.out.println("---");
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void editarExercicio() {
        try {
            System.out.println("=== EDITAR EXERCÍCIO ===");
            
            System.out.print("Digite o ID do exercício a ser editado: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Optional<Exercicios> exercicioOpt = exerciciosService.buscarExercicioPorId(id);
            
            if (exercicioOpt.isEmpty()) {
                System.out.println("\n❌ Exercício não encontrado com o ID: " + id + "\n");
                return;
            }

            Exercicios exercicioAtual = exercicioOpt.get();
            System.out.println("\n=== EXERCÍCIO ATUAL ===");
            exibirExercicio(exercicioAtual);

            System.out.println("=== NOVOS DADOS (Enter para manter o atual) ===");
            
            System.out.print("Novo nome [" + exercicioAtual.getNome() + "]: ");
            String novoNome = scanner.nextLine();
            if (novoNome.trim().isEmpty()) {
                novoNome = exercicioAtual.getNome();
            }

            System.out.print("Deseja alterar a região? (sim/não): ");
            String alterarRegiao = scanner.nextLine().trim().toLowerCase();
            String novaRegiao = exercicioAtual.getRegiao();
            if (alterarRegiao.equals("sim") || alterarRegiao.equals("s")) {
                String regiaoSelecionada = selecionarRegiao();
                if (regiaoSelecionada != null) {
                    novaRegiao = regiaoSelecionada;
                }
            }

            System.out.print("Deseja alterar o tipo? (sim/não): ");
            String alterarTipo = scanner.nextLine().trim().toLowerCase();
            String novoTipo = exercicioAtual.getTipo();
            if (alterarTipo.equals("sim") || alterarTipo.equals("s")) {
                String tipoSelecionado = selecionarTipo();
                if (tipoSelecionado != null) {
                    novoTipo = tipoSelecionado;
                }
            }

            System.out.print("É unilateral? [" + (exercicioAtual.getUnilateral() ? "Sim" : "Não") + "] (sim/não): ");
            String unilateralStr = scanner.nextLine().trim().toLowerCase();
            Boolean novoUnilateral = exercicioAtual.getUnilateral();
            if (!unilateralStr.isEmpty()) {
                novoUnilateral = unilateralStr.equals("sim") || unilateralStr.equals("s");
            }

            System.out.print("Deseja alterar o equipamento? (sim/não): ");
            String alterarEquipamento = scanner.nextLine().trim().toLowerCase();
            Long novoEquipamentoId = exercicioAtual.getEquipamentoId();
            if (alterarEquipamento.equals("sim") || alterarEquipamento.equals("s")) {
                novoEquipamentoId = selecionarEquipamento();
            }

            ExerciciosDTO exercicioDTO = new ExerciciosDTO(novoNome, novaRegiao, novoTipo, novoUnilateral, novoEquipamentoId);
            Exercicios exercicioAtualizado = exerciciosService.atualizarExercicio(id, exercicioDTO);

            System.out.println("\n✅ Exercício atualizado com sucesso!");
            exibirExercicio(exercicioAtualizado);

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            scanner.nextLine(); // limpar buffer em caso de erro
        }
    }

    public void excluirExercicio() {
        try {
            System.out.println("=== EXCLUIR EXERCÍCIO ===");
            
            System.out.print("Digite o ID do exercício a ser excluído: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Optional<Exercicios> exercicioOpt = exerciciosService.buscarExercicioPorId(id);
            
            if (exercicioOpt.isEmpty()) {
                System.out.println("\n❌ Exercício não encontrado com o ID: " + id + "\n");
                return;
            }

            Exercicios exercicio = exercicioOpt.get();
            System.out.println("\n=== EXERCÍCIO A SER EXCLUÍDO ===");
            exibirExercicio(exercicio);

            System.out.print("Tem certeza que deseja excluir este exercício? (sim/não): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();

            if (confirmacao.equals("sim") || confirmacao.equals("s")) {
                exerciciosService.deletarExercicio(id);
                System.out.println("\n✅ Exercício excluído com sucesso!\n");
            } else {
                System.out.println("\n❌ Exclusão cancelada.\n");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            scanner.nextLine(); // limpar buffer em caso de erro
        }
    }

    public void exibirEstatisticas() {
        try {
            System.out.println("=== ESTATÍSTICAS DOS EXERCÍCIOS ===");
            
            long totalExercicios = exerciciosService.contarExercicios();
            System.out.println("Total de exercícios cadastrados: " + totalExercicios);
            
            if (totalExercicios > 0) {
                System.out.println("\n=== EXERCÍCIOS POR REGIÃO ===");
                String[] regioes = RegiaoCorpo.getDescricoes();
                
                for (String regiao : regioes) {
                    long count = exerciciosService.contarExerciciosPorRegiao(regiao);
                    System.out.println(regiao + ": " + count + " exercício(s)");
                }

                // Estatísticas de exercícios unilaterais
                List<Exercicios> unilaterais = exerciciosService.buscarExerciciosPorUnilateral(true);
                List<Exercicios> bilaterais = exerciciosService.buscarExerciciosPorUnilateral(false);
                
                System.out.println("\n=== TIPO DE EXERCÍCIO ===");
                System.out.println("Exercícios unilaterais: " + unilaterais.size());
                System.out.println("Exercícios bilaterais: " + bilaterais.size());
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private void exibirExercicio(Exercicios exercicio) {
        System.out.println("ID: " + exercicio.getId());
        System.out.println("Nome: " + exercicio.getNome());
        System.out.println("Região: " + exercicio.getRegiao());
        System.out.println("Tipo: " + exercicio.getTipo());
        System.out.println("Unilateral: " + (exercicio.getUnilateral() ? "Sim" : "Não"));
        System.out.println("Equipamento: " + (exercicio.getEquipamentoNome() != null ? exercicio.getEquipamentoNome() : "Nenhum"));
        System.out.println();
    }

    private void exibirExercicioResumido(Exercicios exercicio) {
        System.out.println("ID: " + exercicio.getId() + " | " + exercicio.getNome() + 
                          " (" + exercicio.getRegiao() + ") - " + 
                          (exercicio.getUnilateral() ? "Unilateral" : "Bilateral"));
    }

    public void menuExercicios() {
        boolean executando = true;
        
        while (executando) {
            System.out.println("=== GERENCIAMENTO DE EXERCÍCIOS ===");
            System.out.println("1. Cadastrar novo exercício");
            System.out.println("2. Listar todos os exercícios");
            System.out.println("3. Buscar exercício por ID");
            System.out.println("4. Buscar exercícios por nome");
            System.out.println("5. Buscar exercícios por região");
            System.out.println("6. Editar exercício");
            System.out.println("7. Excluir exercício");
            System.out.println("8. Ver estatísticas");
            System.out.println("0. Voltar ao menu anterior");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // limpar buffer

                switch (opcao) {
                    case 1:
                        criarExercicio();
                        break;
                    case 2:
                        listarTodosExercicios();
                        break;
                    case 3:
                        buscarExercicioPorId();
                        break;
                    case 4:
                        buscarExerciciosPorNome();
                        break;
                    case 5:
                        buscarExerciciosPorRegiao();
                        break;
                    case 6:
                        editarExercicio();
                        break;
                    case 7:
                        excluirExercicio();
                        break;
                    case 8:
                        exibirEstatisticas();
                        break;
                    case 0:
                        executando = false;
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

