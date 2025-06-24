package br.ifg.gymbro.controller;

import br.ifg.gymbro.dto.EquipamentoDTO;
import br.ifg.gymbro.model.Equipamento;
import br.ifg.gymbro.services.EquipamentoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class EquipamentoController {
    private EquipamentoService equipamentoService;
    private Scanner scanner;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
        this.scanner = new Scanner(System.in);
    }

    public void criarEquipamento() {
        try {
            System.out.println("=== REGISTRAR NOVO EQUIPAMENTO ===");
            
            System.out.print("Informe o nome do equipamento: ");
            String nome = scanner.nextLine();

            System.out.print("Informe o peso do equipamento (kg) [Enter para pular]: ");
            String pesoStr = scanner.nextLine();
            
            Float peso = null;
            if (!pesoStr.trim().isEmpty()) {
                try {
                    peso = Float.parseFloat(pesoStr);
                } catch (NumberFormatException e) {
                    System.err.println("❌ Peso inválido. Equipamento será criado sem peso.");
                }
            }

            EquipamentoDTO equipamentoDTO = new EquipamentoDTO(nome, peso);
            Equipamento equipamento = equipamentoService.criarEquipamento(equipamentoDTO);

            System.out.println("\n✅ Equipamento registrado com sucesso!");
            System.out.println("ID: " + equipamento.getId());
            System.out.println("Nome: " + equipamento.getNome());
            System.out.println("Peso: " + (equipamento.getPesoEquip() != null ? equipamento.getPesoEquip() + " kg" : "Não informado"));
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void listarTodosEquipamentos() {
        try {
            System.out.println("=== TODOS OS EQUIPAMENTOS ===");
            
            List<Equipamento> equipamentos = equipamentoService.listarTodosEquipamentos();
            
            if (equipamentos.isEmpty()) {
                System.out.println("Nenhum equipamento encontrado.\n");
                return;
            }

            System.out.println("Total de equipamentos: " + equipamentos.size());
            System.out.println("---");

            for (Equipamento equipamento : equipamentos) {
                System.out.println("ID: " + equipamento.getId());
                System.out.println("Nome: " + equipamento.getNome());
                System.out.println("Peso: " + (equipamento.getPesoEquip() != null ? equipamento.getPesoEquip() + " kg" : "Não informado"));
                System.out.println("---");
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void buscarEquipamentoPorId() {
        try {
            System.out.print("Digite o ID do equipamento: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Optional<Equipamento> equipamentoOpt = equipamentoService.buscarEquipamentoPorId(id);
            
            if (equipamentoOpt.isPresent()) {
                Equipamento equipamento = equipamentoOpt.get();
                System.out.println("\n=== EQUIPAMENTO ENCONTRADO ===");
                exibirEquipamento(equipamento);
            } else {
                System.out.println("\n❌ Equipamento não encontrado com o ID: " + id + "\n");
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

    public void buscarEquipamentosPorNome() {
        try {
            System.out.print("Digite o nome do equipamento (busca parcial): ");
            String nome = scanner.nextLine();

            List<Equipamento> equipamentos = equipamentoService.buscarEquipamentosPorNome(nome);
            
            if (equipamentos.isEmpty()) {
                System.out.println("\n❌ Nenhum equipamento encontrado com o nome: " + nome + "\n");
                return;
            }

            System.out.println("\n=== EQUIPAMENTOS ENCONTRADOS ===");
            System.out.println("Total encontrado: " + equipamentos.size());
            System.out.println("---");

            for (Equipamento equipamento : equipamentos) {
                System.out.println("ID: " + equipamento.getId());
                System.out.println("Nome: " + equipamento.getNome());
                System.out.println("Peso: " + (equipamento.getPesoEquip() != null ? equipamento.getPesoEquip() + " kg" : "Não informado"));
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

    public void editarEquipamento() {
        try {
            System.out.println("=== EDITAR EQUIPAMENTO ===");
            
            System.out.print("Digite o ID do equipamento a ser editado: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Optional<Equipamento> equipamentoOpt = equipamentoService.buscarEquipamentoPorId(id);
            
            if (equipamentoOpt.isEmpty()) {
                System.out.println("\n❌ Equipamento não encontrado com o ID: " + id + "\n");
                return;
            }

            Equipamento equipamentoAtual = equipamentoOpt.get();
            System.out.println("\n=== EQUIPAMENTO ATUAL ===");
            exibirEquipamento(equipamentoAtual);

            System.out.println("=== NOVOS DADOS (Enter para manter o atual) ===");
            
            System.out.print("Novo nome [" + equipamentoAtual.getNome() + "]: ");
            String novoNome = scanner.nextLine();
            if (novoNome.trim().isEmpty()) {
                novoNome = equipamentoAtual.getNome();
            }

            System.out.print("Novo peso [" + (equipamentoAtual.getPesoEquip() != null ? equipamentoAtual.getPesoEquip() : "Não informado") + "]: ");
            String novoPesoStr = scanner.nextLine();
            
            Float novoPeso = equipamentoAtual.getPesoEquip();
            if (!novoPesoStr.trim().isEmpty()) {
                try {
                    novoPeso = Float.parseFloat(novoPesoStr);
                } catch (NumberFormatException e) {
                    System.err.println("❌ Peso inválido. Mantendo peso atual.");
                }
            }

            EquipamentoDTO equipamentoDTO = new EquipamentoDTO(novoNome, novoPeso);
            Equipamento equipamentoAtualizado = equipamentoService.atualizarEquipamento(id, equipamentoDTO);

            System.out.println("\n✅ Equipamento atualizado com sucesso!");
            exibirEquipamento(equipamentoAtualizado);

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            scanner.nextLine(); // limpar buffer em caso de erro
        }
    }

    public void excluirEquipamento() {
        try {
            System.out.println("=== EXCLUIR EQUIPAMENTO ===");
            
            System.out.print("Digite o ID do equipamento a ser excluído: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Optional<Equipamento> equipamentoOpt = equipamentoService.buscarEquipamentoPorId(id);
            
            if (equipamentoOpt.isEmpty()) {
                System.out.println("\n❌ Equipamento não encontrado com o ID: " + id + "\n");
                return;
            }

            Equipamento equipamento = equipamentoOpt.get();
            System.out.println("\n=== EQUIPAMENTO A SER EXCLUÍDO ===");
            exibirEquipamento(equipamento);

            System.out.print("Tem certeza que deseja excluir este equipamento? (sim/não): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();

            if (confirmacao.equals("sim") || confirmacao.equals("s")) {
                equipamentoService.deletarEquipamento(id);
                System.out.println("\n✅ Equipamento excluído com sucesso!\n");
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

    public void buscarPorFaixaPeso() {
        try {
            System.out.println("=== BUSCAR POR FAIXA DE PESO ===");
            
            System.out.print("Peso mínimo (kg): ");
            float pesoMin = scanner.nextFloat();
            
            System.out.print("Peso máximo (kg): ");
            float pesoMax = scanner.nextFloat();
            scanner.nextLine(); // limpar buffer

            List<Equipamento> equipamentos = equipamentoService.buscarEquipamentosPorFaixaPeso(pesoMin, pesoMax);
            
            if (equipamentos.isEmpty()) {
                System.out.println("\n❌ Nenhum equipamento encontrado na faixa de peso " + pesoMin + "kg - " + pesoMax + "kg\n");
                return;
            }

            System.out.println("\n=== EQUIPAMENTOS NA FAIXA " + pesoMin + "kg - " + pesoMax + "kg ===");
            System.out.println("Total encontrado: " + equipamentos.size());
            System.out.println("---");

            for (Equipamento equipamento : equipamentos) {
                System.out.println("ID: " + equipamento.getId());
                System.out.println("Nome: " + equipamento.getNome());
                System.out.println("Peso: " + equipamento.getPesoEquip() + " kg");
                System.out.println("---");
            }
            System.out.println();

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
            System.out.println("=== ESTATÍSTICAS DOS EQUIPAMENTOS ===");
            
            long totalEquipamentos = equipamentoService.contarEquipamentos();
            System.out.println("Total de equipamentos cadastrados: " + totalEquipamentos);
            
            if (totalEquipamentos > 0) {
                List<Equipamento> equipamentos = equipamentoService.listarTodosEquipamentos();
                
                long equipamentosComPeso = equipamentos.stream()
                    .mapToLong(e -> e.getPesoEquip() != null ? 1 : 0)
                    .sum();
                
                System.out.println("Equipamentos com peso informado: " + equipamentosComPeso);
                System.out.println("Equipamentos sem peso informado: " + (totalEquipamentos - equipamentosComPeso));
                
                if (equipamentosComPeso > 0) {
                    double pesoMedio = equipamentos.stream()
                        .filter(e -> e.getPesoEquip() != null)
                        .mapToDouble(Equipamento::getPesoEquip)
                        .average()
                        .orElse(0.0);
                    
                    System.out.printf("Peso médio dos equipamentos: %.2f kg%n", pesoMedio);
                }
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private void exibirEquipamento(Equipamento equipamento) {
        System.out.println("ID: " + equipamento.getId());
        System.out.println("Nome: " + equipamento.getNome());
        System.out.println("Peso: " + (equipamento.getPesoEquip() != null ? equipamento.getPesoEquip() + " kg" : "Não informado"));
        System.out.println();
    }

    public void menuEquipamentos() {
        boolean executando = true;
        
        while (executando) {
            System.out.println("=== GERENCIAMENTO DE EQUIPAMENTOS ===");
            System.out.println("1. Registrar novo equipamento");
            System.out.println("2. Listar todos os equipamentos");
            System.out.println("3. Buscar equipamento por ID");
            System.out.println("4. Buscar equipamentos por nome");
            System.out.println("5. Editar equipamento");
            System.out.println("6. Excluir equipamento");
            System.out.println("7. Buscar por faixa de peso");
            System.out.println("8. Ver estatísticas");
            System.out.println("0. Voltar ao menu anterior");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // limpar buffer

                switch (opcao) {
                    case 1:
                        criarEquipamento();
                        break;
                    case 2:
                        listarTodosEquipamentos();
                        break;
                    case 3:
                        buscarEquipamentoPorId();
                        break;
                    case 4:
                        buscarEquipamentosPorNome();
                        break;
                    case 5:
                        editarEquipamento();
                        break;
                    case 6:
                        excluirEquipamento();
                        break;
                    case 7:
                        buscarPorFaixaPeso();
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
