package br.ifg.gymbro.controller;

import br.ifg.gymbro.dto.LoginDTO;
import br.ifg.gymbro.dto.PersonalDTO;
import br.ifg.gymbro.model.Personal;
import br.ifg.gymbro.services.PersonalService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PersonalController {
    private PersonalService personalService;
    private Scanner scanner;

    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
        this.scanner = new Scanner(System.in);
    }

    public void criarPersonal() {
        try {
            System.out.println("=== CRIAR NOVO PERSONAL ===");
            
            System.out.print("Informe seu nome: ");
            String nome = scanner.nextLine();

            System.out.print("Informe seu email: ");
            String email = scanner.nextLine();

            System.out.print("Informe sua senha: ");
            String senha = scanner.nextLine();

            System.out.print("Possui formação em EF? (sim/não): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            boolean formacao = resposta.equals("sim");

            String licenca;
            if (formacao) {
                System.out.print("Informe o número do diploma: ");
                licenca = scanner.nextLine();
            } else {
                System.out.print("Informe sua licença de personal: ");
                licenca = scanner.nextLine();
            }

            PersonalDTO personalDTO = new PersonalDTO(nome, email, senha, licenca, formacao);
            Personal personal = personalService.criarPersonal(personalDTO);

            System.out.println("\n✅ Personal criado com sucesso!");
            System.out.println("ID: " + personal.getId());
            System.out.println("Nome: " + personal.getNome());
            System.out.println("Email: " + personal.getEmail());
            System.out.println("Formação em EF: " + (personal.getFormacao() ? "Sim" : "Não"));
            if (personal.getFormacao()) {
                System.out.println("Número de diploma: " + personal.getLicenca());
            } else {
                System.out.println("Licença de personal: " + personal.getLicenca());
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

    public Personal fazerLogin() {
        try {
            System.out.println("=== LOGIN PERSONAL ===");
            
            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            LoginDTO loginDTO = new LoginDTO(email, senha);
            Optional<Personal> personalOpt = personalService.autenticarPersonal(loginDTO);

            if (personalOpt.isPresent()) {
                Personal personal = personalOpt.get();
                System.out.println("\n✅ Login realizado com sucesso!");
                System.out.println("Bem-vindo(a), Personal " + personal.getNome() + "!\n");
                return personal;
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

    public void exibirPerfilPersonal(Personal personal) {
        if (personal == null) {
            System.out.println("❌ Personal não encontrado.");
            return;
        }

        System.out.println("=== SEU PERFIL PERSONAL ===");
        System.out.println("Nome: " + personal.getNome());
        System.out.println("Email: " + personal.getEmail());
        System.out.println("Formação em EF: " + (personal.getFormacao() ? "Sim" : "Não"));
        if (personal.getFormacao()) {
            System.out.println("Número de diploma: " + personal.getLicenca());
        } else {
            System.out.println("Licença de personal: " + personal.getLicenca());
        }
        System.out.println();
    }

    public void listarTodosPersonais() {
        try {
            System.out.println("=== TODOS OS PERSONAIS ===");
            
            List<Personal> personais = personalService.listarTodosPersonais();
            
            if (personais.isEmpty()) {
                System.out.println("Nenhum personal encontrado.\n");
                return;
            }

            for (Personal personal : personais) {
                System.out.println("ID: " + personal.getId());
                System.out.println("Nome: " + personal.getNome());
                System.out.println("Email: " + personal.getEmail());
                System.out.println("Formação em EF: " + (personal.getFormacao() ? "Sim" : "Não"));
                if (personal.getFormacao()) {
                    System.out.println("Número de diploma: " + personal.getLicenca());
                } else {
                    System.out.println("Licença de personal: " + personal.getLicenca());
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

    public void buscarPersonalPorEmail() {
        try {
            System.out.print("Digite o email do personal: ");
            String email = scanner.nextLine();

            Optional<Personal> personalOpt = personalService.buscarPersonalPorEmail(email);
            
            if (personalOpt.isPresent()) {
                Personal personal = personalOpt.get();
                System.out.println("\n=== PERSONAL ENCONTRADO ===");
                exibirPerfilPersonal(personal);
            } else {
                System.out.println("\n❌ Personal não encontrado com o email: " + email + "\n");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    public void listarPersonaisPorFormacao() {
        try {
            System.out.println("=== FILTRAR PERSONAIS POR FORMAÇÃO ===");
            System.out.println("1. Com formação em EF");
            System.out.println("2. Sem formação em EF");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
            
            boolean temFormacao = (opcao == 1);
            List<Personal> personais = personalService.listarPersonaisPorFormacao(temFormacao);
            
            String titulo = temFormacao ? "PERSONAIS COM FORMAÇÃO EM EF" : "PERSONAIS SEM FORMAÇÃO EM EF";
            System.out.println("\n=== " + titulo + " ===");
            
            if (personais.isEmpty()) {
                System.out.println("Nenhum personal encontrado nesta categoria.\n");
                return;
            }

            for (Personal personal : personais) {
                System.out.println("ID: " + personal.getId());
                System.out.println("Nome: " + personal.getNome());
                System.out.println("Email: " + personal.getEmail());
                if (personal.getFormacao()) {
                    System.out.println("Número de diploma: " + personal.getLicenca());
                } else {
                    System.out.println("Licença de personal: " + personal.getLicenca());
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
}
