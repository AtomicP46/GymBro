package br.ifg.gymbro.controller;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import br.ifg.gymbro.dto.ExercicioProgressoDTO;
import br.ifg.gymbro.dto.TreinoDTO;
import br.ifg.gymbro.dto.TreinoExercicioDTO;
import br.ifg.gymbro.model.Exercicios;
import br.ifg.gymbro.model.Personal;
import br.ifg.gymbro.model.Treino;
import br.ifg.gymbro.model.TreinoExercicio;
import br.ifg.gymbro.model.Usuario;
import br.ifg.gymbro.services.ExerciciosService;
import br.ifg.gymbro.services.PersonalService;
import br.ifg.gymbro.services.TreinoExercicioService;
import br.ifg.gymbro.services.TreinoService;
import br.ifg.gymbro.services.UsuarioService;

public class TreinoController {
    private final TreinoService treinoService;
    private final TreinoExercicioService treinoExercicioService;
    private final UsuarioService usuarioService;
    private final PersonalService personalService;
    private final ExerciciosService exerciciosService;
    private final Scanner scanner;

    public TreinoController(TreinoService treinoService, TreinoExercicioService treinoExercicioService,
                           UsuarioService usuarioService, PersonalService personalService,
                           ExerciciosService exerciciosService) {
        this.treinoService = treinoService;
        this.treinoExercicioService = treinoExercicioService;
        this.usuarioService = usuarioService;
        this.personalService = personalService;
        this.exerciciosService = exerciciosService;
        this.scanner = new Scanner(System.in);
    }

    public void criarTreino() {
        try {
            System.out.println("=== CRIAR NOVO TREINO ===");
            
            System.out.print("Nome do treino: ");
            String nome = scanner.nextLine();

            // Selecionar usuário
            Long usuarioId = selecionarUsuario();
            if (usuarioId == null) return;

            // Selecionar personal (opcional)
            Long personalId = selecionarPersonal();

            TreinoDTO treinoDTO = new TreinoDTO(nome, null, usuarioId, personalId);
            Treino treino = treinoService.criarTreino(treinoDTO);

            System.out.println("\n✅ Treino criado com sucesso!");
            exibirTreino(treino);

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void listarTodosTreinos() {
        try {
            System.out.println("=== TODOS OS TREINOS ===");
            
            List<Treino> treinos = treinoService.listarTodosTreinos();
            
            if (treinos.isEmpty()) {
                System.out.println("Nenhum treino encontrado.\n");
                return;
            }

            System.out.println("Total de treinos: " + treinos.size());
            System.out.println("---");

            for (Treino treino : treinos) {
                exibirTreinoResumido(treino);
                System.out.println("---");
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void buscarTreinoPorId() {
        try {
            System.out.print("Digite o ID do treino: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Optional<Treino> treinoOpt = treinoService.buscarTreinoPorId(id);
            
            if (treinoOpt.isPresent()) {
                Treino treino = treinoOpt.get();
                System.out.println("\n=== TREINO ENCONTRADO ===");
                exibirTreino(treino);
                
                // Mostrar exercícios do treino
                exibirExerciciosDoTreino(id);
            } else {
                System.out.println("\n❌ Treino não encontrado com o ID: " + id + "\n");
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

    public void iniciarTreino() {
        try {
            System.out.println("=== INICIAR TREINO ===");
            
            System.out.print("Digite o ID do treino: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Treino treino = treinoService.iniciarTreino(id);
            
            System.out.println("\n✅ Treino iniciado com sucesso!");
            System.out.println("Hora de início: " + treino.getDataHoraInicio().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            exibirTreino(treino);

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            scanner.nextLine(); // limpar buffer em caso de erro
        }
    }

    public void finalizarTreino() {
        try {
            System.out.println("=== FINALIZAR TREINO ===");
            
            System.out.print("Digite o ID do treino: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Treino treino = treinoService.finalizarTreino(id);
            
            System.out.println("\n✅ Treino finalizado com sucesso!");
            System.out.println("Hora de finalização: " + treino.getDataHoraFim().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            
            // Calcular duração
            if (treino.getDataHoraInicio() != null && treino.getDataHoraFim() != null) {
                Duration duracao = Duration.between(treino.getDataHoraInicio(), treino.getDataHoraFim());
                long horas = duracao.toHours();
                long minutos = duracao.toMinutesPart();
                System.out.println("Duração total: " + horas + "h " + minutos + "min");
            }
            
            exibirTreino(treino);

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            scanner.nextLine(); // limpar buffer em caso de erro
        }
    }

    public void adicionarExercicioAoTreino() {
        try {
            System.out.println("=== ADICIONAR EXERCÍCIO AO TREINO ===");
            
            System.out.print("Digite o ID do treino: ");
            long treinoId = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            // Verificar se treino existe
            Optional<Treino> treinoOpt = treinoService.buscarTreinoPorId(treinoId);
            if (treinoOpt.isEmpty()) {
                System.out.println("❌ Treino não encontrado.");
                return;
            }

            // Selecionar exercício
            Long exercicioId = selecionarExercicio();
            if (exercicioId == null) return;

            System.out.print("Número de séries planejadas: ");
            int series = scanner.nextInt();

            System.out.print("Número de repetições por série: ");
            int repeticoes = scanner.nextInt();

            System.out.print("Peso a ser usado (kg) [0 para pular]: ");
            float peso = scanner.nextFloat();
            scanner.nextLine(); // limpar buffer

            System.out.print("É exercício de aquecimento? (sim/não): ");
            String aquecimentoStr = scanner.nextLine().trim().toLowerCase();
            boolean aquecimento = aquecimentoStr.equals("sim") || aquecimentoStr.equals("s");

            System.out.print("Anotações (opcional): ");
            String anotacoes = scanner.nextLine();

            TreinoExercicioDTO treinoExercicioDTO = new TreinoExercicioDTO(
                treinoId, exercicioId, series, repeticoes, 
                peso > 0 ? peso : null, anotacoes.isEmpty() ? null : anotacoes, aquecimento
            );

            TreinoExercicio treinoExercicio = treinoExercicioService.adicionarExercicioAoTreino(treinoExercicioDTO);

            System.out.println("\n✅ Exercício adicionado ao treino com sucesso!");
            exibirTreinoExercicio(treinoExercicio);

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            scanner.nextLine(); // limpar buffer em caso de erro
        }
    }

    public void registrarExecucaoExercicio() {
        try {
            System.out.println("=== REGISTRAR EXECUÇÃO DO EXERCÍCIO ===");
            
            System.out.print("Digite o ID do treino: ");
            long treinoId = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            // Mostrar exercícios do treino
            List<TreinoExercicio> exercicios = treinoExercicioService.listarExerciciosDoTreino(treinoId);
            
            if (exercicios.isEmpty()) {
                System.out.println("❌ Nenhum exercício encontrado neste treino.");
                return;
            }

            System.out.println("\n=== EXERCÍCIOS DO TREINO ===");
            for (int i = 0; i < exercicios.size(); i++) {
                TreinoExercicio te = exercicios.get(i);
                System.out.println((i + 1) + ". " + te.getExercicioNome() + 
                    " - Séries: " + (te.getSeries() != null ? te.getSeries() : "N/A") +
                    " - Rep: " + (te.getRepeticoes() != null ? te.getRepeticoes() : "N/A") +
                    " - Peso: " + (te.getPesoUsado() != null ? te.getPesoUsado() + "kg" : "N/A"));
            }

            System.out.print("\nEscolha o exercício (1-" + exercicios.size() + "): ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            if (opcao < 1 || opcao > exercicios.size()) {
                System.out.println("❌ Opção inválida!");
                return;
            }

            TreinoExercicio exercicioSelecionado = exercicios.get(opcao - 1);

            System.out.print("Repetições realizadas: ");
            int repeticoesRealizadas = scanner.nextInt();

            System.out.print("Peso usado (kg): ");
            float pesoUsado = scanner.nextFloat();
            scanner.nextLine(); // limpar buffer

            System.out.print("Anotações sobre a execução: ");
            String anotacoes = scanner.nextLine();

            TreinoExercicio atualizado = treinoExercicioService.registrarExecucao(
                exercicioSelecionado.getId(), repeticoesRealizadas, pesoUsado, anotacoes
            );

            System.out.println("\n✅ Execução registrada com sucesso!");
            exibirTreinoExercicio(atualizado);

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            scanner.nextLine(); // limpar buffer em caso de erro
        }
    }

    public void listarTreinosEmAndamento() {
        try {
            System.out.println("=== TREINOS EM ANDAMENTO ===");
            
            List<Treino> treinos = treinoService.listarTreinosEmAndamento();
            
            if (treinos.isEmpty()) {
                System.out.println("Nenhum treino em andamento.\n");
                return;
            }

            for (Treino treino : treinos) {
                exibirTreinoResumido(treino);
                
                // Calcular tempo decorrido
                if (treino.getDataHoraInicio() != null) {
                    Duration duracao = Duration.between(treino.getDataHoraInicio(), LocalDateTime.now());
                    long horas = duracao.toHours();
                    long minutos = duracao.toMinutesPart();
                    System.out.println("Tempo decorrido: " + horas + "h " + minutos + "min");
                }
                
                System.out.println("---");
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void excluirTreino() {
        try {
            System.out.println("=== EXCLUIR TREINO ===");
            
            System.out.print("Digite o ID do treino a ser excluído: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // limpar buffer

            Optional<Treino> treinoOpt = treinoService.buscarTreinoPorId(id);
            
            if (treinoOpt.isEmpty()) {
                System.out.println("\n❌ Treino não encontrado com o ID: " + id + "\n");
                return;
            }

            Treino treino = treinoOpt.get();
            System.out.println("\n=== TREINO A SER EXCLUÍDO ===");
            exibirTreino(treino);

            System.out.print("Tem certeza que deseja excluir este treino? (sim/não): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();

            if (confirmacao.equals("sim") || confirmacao.equals("s")) {
                treinoService.deletarTreino(id);
                System.out.println("\n✅ Treino excluído com sucesso!\n");
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

    /**
     * Permite ao usuário gerar um relatório de progresso para um exercício específico.
     */
    public void gerarRelatorioProgressoExercicio() {
        try {
            System.out.println("=== RELATÓRIO DE PROGRESSO DE EXERCÍCIO ===");

            // Selecionar usuário
            Long usuarioId = selecionarUsuario();
            if (usuarioId == null) return;

            // Selecionar exercício
            Long exercicioId = selecionarExercicio();
            if (exercicioId == null) return;

            // CORREÇÃO: Usar List<ExercicioProgressoDTO> em vez de List<TreinoExercicio>
            List<ExercicioProgressoDTO> historico = treinoExercicioService.gerarRelatorioProgressoExercicio(usuarioId, exercicioId);

            if (historico.isEmpty()) {
                System.out.println("\nNenhum histórico encontrado para este exercício e usuário.\n");
                return;
            }

            System.out.println("\n=== HISTÓRICO DE PROGRESSO PARA " + historico.get(0).getExercicioNome().toUpperCase() + " ===");
            System.out.println("Usuário: " + usuarioService.buscarUsuarioPorId(usuarioId).map(Usuario::getNome).orElse("Desconhecido"));
            System.out.println("---");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // CORREÇÃO: Usar ExercicioProgressoDTO em vez de TreinoExercicio
            for (ExercicioProgressoDTO progresso : historico) {
                System.out.println("Data do Treino: " + (progresso.getDataHoraInicioTreino() != null ? progresso.getDataHoraInicioTreino().format(formatter) : "N/A"));
                System.out.println("Séries: " + (progresso.getSeries() != null ? progresso.getSeries() : "N/A"));
                System.out.println("Repetições: " + (progresso.getRepeticoes() != null ? progresso.getRepeticoes() : "N/A"));
                System.out.println("Peso Usado: " + (progresso.getPesoUsado() != null ? progresso.getPesoUsado() + " kg" : "N/A"));
                if (progresso.getAnotacoes() != null && !progresso.getAnotacoes().trim().isEmpty()) {
                    System.out.println("Anotações: " + progresso.getAnotacoes());
                }
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

    private Long selecionarUsuario() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            
            if (usuarios.isEmpty()) {
                System.out.println("❌ Nenhum usuário cadastrado.");
                return null;
            }

            System.out.println("\n=== SELECIONAR USUÁRIO ===");
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                System.out.println((i + 1) + ". " + u.getNome() + " (" + u.getEmail() + ")");
            }
            
            System.out.print("Escolha o usuário (1-" + usuarios.size() + "): ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
            
            if (opcao >= 1 && opcao <= usuarios.size()) {
                return usuarios.get(opcao - 1).getId();
            } else {
                System.err.println("❌ Opção inválida!");
                return null;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar usuários: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Entrada inválida!");
            scanner.nextLine(); // limpar buffer
            return null;
        }
    }

    private Long selecionarPersonal() {
        try {
            System.out.print("\nDeseja associar um personal? (sim/não): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            
            if (!resposta.equals("sim") && !resposta.equals("s")) {
                return null;
            }

            List<Personal> personais = personalService.listarTodosPersonais();
            
            if (personais.isEmpty()) {
                System.out.println("❌ Nenhum personal cadastrado.");
                return null;
            }

            System.out.println("\n=== SELECIONAR PERSONAL ===");
            for (int i = 0; i < personais.size(); i++) {
                Personal p = personais.get(i);
                System.out.println((i + 1) + ". " + p.getNome() + " (" + p.getEmail() + ")");
            }
            
            System.out.print("Escolha o personal (1-" + personais.size() + ") ou 0 para pular: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
            
            if (opcao == 0) {
                return null;
            } else if (opcao >= 1 && opcao <= personais.size()) {
                return personais.get(opcao - 1).getId();
            } else {
                System.err.println("❌ Opção inválida!");
                return null;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar personais: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Entrada inválida!");
            scanner.nextLine(); // limpar buffer
            return null;
        }
    }

    private Long selecionarExercicio() {
        try {
            List<Exercicios> exercicios = exerciciosService.listarTodosExercicios();
            
            if (exercicios.isEmpty()) {
                System.out.println("❌ Nenhum exercício cadastrado.");
                return null;
            }

            System.out.println("\n=== SELECIONAR EXERCÍCIO ===");
            for (int i = 0; i < exercicios.size(); i++) {
                Exercicios e = exercicios.get(i);
                System.out.println((i + 1) + ". " + e.getNome() + " (" + e.getRegiao() + ")");
            }
            
            System.out.print("Escolha o exercício (1-" + exercicios.size() + "): ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
            
            if (opcao >= 1 && opcao <= exercicios.size()) {
                return exercicios.get(opcao - 1).getId();
            } else {
                System.err.println("❌ Opção inválida!");
                return null;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar exercícios: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Entrada inválida!");
            scanner.nextLine(); // limpar buffer
            return null;
        }
    }

    private void exibirTreino(Treino treino) {
        System.out.println("ID: " + treino.getId());
        System.out.println("Nome: " + treino.getNome());
        System.out.println("Usuário: " + treino.getUsuarioNome());
        System.out.println("Personal: " + (treino.getPersonalNome() != null ? treino.getPersonalNome() : "Nenhum"));
        
        if (treino.getDataHoraInicio() != null) {
            System.out.println("Iniciado em: " + treino.getDataHoraInicio().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        } else {
            System.out.println("Status: Não iniciado");
        }
        
        if (treino.getDataHoraFim() != null) {
            System.out.println("Finalizado em: " + treino.getDataHoraFim().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        } else if (treino.isIniciado()) {
            System.out.println("Status: Em andamento");
        }
        
        System.out.println();
    }

    private void exibirTreinoResumido(Treino treino) {
        String status = treino.isFinalizado() ? "Finalizado" : 
                       treino.isIniciado() ? "Em andamento" : "Não iniciado";
        
        System.out.println("ID: " + treino.getId() + " | " + treino.getNome() + 
                          " - " + treino.getUsuarioNome() + " (" + status + ")");
    }

    private void exibirExerciciosDoTreino(Long treinoId) {
        try {
            List<TreinoExercicio> exercicios = treinoExercicioService.listarExerciciosDoTreino(treinoId);
            
            if (exercicios.isEmpty()) {
                System.out.println("Nenhum exercício adicionado a este treino.");
                return;
            }

            System.out.println("=== EXERCÍCIOS DO TREINO ===");
            for (TreinoExercicio te : exercicios) {
                exibirTreinoExercicio(te);
                System.out.println("---");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar exercícios do treino: " + e.getMessage());
        }
    }

    private void exibirTreinoExercicio(TreinoExercicio treinoExercicio) {
        System.out.println("Exercício: " + treinoExercicio.getExercicioNome() + 
                          " (" + treinoExercicio.getExercicioRegiao() + ")");
        System.out.println("Séries: " + (treinoExercicio.getSeries() != null ? treinoExercicio.getSeries() : "N/A"));
        System.out.println("Repetições: " + (treinoExercicio.getRepeticoes() != null ? treinoExercicio.getRepeticoes() : "N/A"));
        System.out.println("Peso usado: " + (treinoExercicio.getPesoUsado() != null ? treinoExercicio.getPesoUsado() + " kg" : "N/A"));
        System.out.println("Aquecimento: " + (treinoExercicio.isAquecimento() ? "Sim" : "Não"));
        if (treinoExercicio.getAnotacoes() != null && !treinoExercicio.getAnotacoes().trim().isEmpty()) {
            System.out.println("Anotações: " + treinoExercicio.getAnotacoes());
        }
    }

    public void menuTreinos() {
        boolean executando = true;
        
        while (executando) {
            System.out.println("=== GERENCIAMENTO DE TREINOS ===");
            System.out.println("1. Criar novo treino");
            System.out.println("2. Listar todos os treinos");
            System.out.println("3. Buscar treino por ID");
            System.out.println("4. Iniciar treino");
            System.out.println("5. Finalizar treino");
            System.out.println("6. Adicionar exercício ao treino");
            System.out.println("7. Registrar execução de exercício");
            System.out.println("8. Listar treinos em andamento");
            System.out.println("9. Excluir treino");
            System.out.println("10. Gerar Relatório de Progresso de Exercício");
            System.out.println("0. Voltar ao menu anterior");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // limpar buffer

                switch (opcao) {
                    case 1:
                        criarTreino();
                        break;
                    case 2:
                        listarTodosTreinos();
                        break;
                    case 3:
                        buscarTreinoPorId();
                        break;
                    case 4:
                        iniciarTreino();
                        break;
                    case 5:
                        finalizarTreino();
                        break;
                    case 6:
                        adicionarExercicioAoTreino();
                        break;
                    case 7:
                        registrarExecucaoExercicio();
                        break;
                    case 8:
                        listarTreinosEmAndamento();
                        break;
                    case 9:
                        excluirTreino();
                        break;
                    case 10:
                        gerarRelatorioProgressoExercicio();
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
