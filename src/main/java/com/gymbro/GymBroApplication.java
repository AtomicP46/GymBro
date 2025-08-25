package com.gymbro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gymbro.dto.AlunoDTO;
import com.gymbro.dto.LoginRequestDTO;
import com.gymbro.dto.LoginResponseDTO;
import com.gymbro.dto.PersonalDTO;
import com.gymbro.service.AlunoService;
import com.gymbro.service.AuthService;
import com.gymbro.service.PersonalService;

@SpringBootApplication
public class GymBroApplication implements CommandLineRunner {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private PersonalService personalService;

    @Autowired
    private AuthService authService;

    private final Scanner scanner = new Scanner(System.in);
    private LoginResponseDTO usuarioLogado = null;

    public static void main(String[] args) {
        SpringApplication.run(GymBroApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== BEM-VINDO AO GYMBRO SYSTEM ===");

        while (true) {
            if (usuarioLogado == null) {
                mostrarMenuPrincipal();
            } else {
                mostrarMenuUsuario();
            }
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Cadastrar como Aluno");
        System.out.println("2. Cadastrar como Personal");
        System.out.println("3. Fazer Login");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
        case 1:
            cadastrarAluno();
            break;
        case 2:
            cadastrarPersonal();
            break;
        case 3:
            fazerLogin();
            break;
        case 4:
            System.out.println("Obrigado por usar o GymBro System!");
            System.exit(0);
            break;
        default:
            System.out.println("Opção inválida!");
        }
    }

    private void mostrarMenuUsuario() {
        System.out.println("\n--- MENU DO USUÁRIO ---");
        System.out.println("Bem-vindo, " + usuarioLogado.getNome() + "!");
        System.out.println("Tipo de usuário: " + usuarioLogado.getTipoUsuario());
        System.out.println("1. Visualizar meus dados");
        System.out.println("2. Fazer Logoff");
        System.out.println("3. Excluir minha conta");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
        case 1:
            visualizarDados();
            break;
        case 2:
            fazerLogoff();
            break;
        case 3:
            excluirConta();
            break;
        default:
            System.out.println("Opção inválida!");
        }
    }

    private void cadastrarAluno() {
        System.out.println("\n--- CADASTRO DE ALUNO ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Data de Nascimento (YYYY-MM-DD): ");
        LocalDate dataNascimento = LocalDate.parse(scanner.nextLine());
        System.out.print("Peso (kg): ");
        BigDecimal peso = BigDecimal.valueOf(scanner.nextDouble());
        scanner.nextLine();

        AlunoDTO alunoDTO = new AlunoDTO();
        alunoDTO.setNome(nome);
        alunoDTO.setEmail(email);
        alunoDTO.setSenha(senha);
        alunoDTO.setDataNascimento(dataNascimento);
        alunoDTO.setPeso(peso);

        try {
            AlunoDTO criado = alunoService.criarAluno(alunoDTO);
            System.out.println("Aluno cadastrado com sucesso! ID: " + criado.getId());
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    private void cadastrarPersonal() {
        System.out.println("\n--- CADASTRO DE PERSONAL ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Data de Nascimento (YYYY-MM-DD): ");
        LocalDate dataNascimento = LocalDate.parse(scanner.nextLine());
        System.out.print("É formado em Educação Física? (true/false): ");
        Boolean formado = scanner.nextBoolean();
        scanner.nextLine();

        PersonalDTO dto = new PersonalDTO();
        dto.setNome(nome);
        dto.setEmail(email);
        dto.setSenha(senha);
        dto.setDataNascimento(dataNascimento);
        dto.setFormado(formado);

        if (formado) {
            System.out.print("Código de Validação: ");
            dto.setCodigoValidacao(scanner.nextLine());
            System.out.print("Link de Validação: ");
            dto.setLinkValidacao(scanner.nextLine());
        } else {
            System.out.print("Licença (obrigatório): ");
            dto.setLicenca(scanner.nextLine());
        }

        try {
            PersonalDTO criado = personalService.criarPersonal(dto);
            System.out.println("Personal cadastrado com sucesso! ID: " + criado.getId());
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar personal: " + e.getMessage());
        }
    }

    private void fazerLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(email);
        request.setSenha(senha);

        try {
            LoginResponseDTO response = authService.login(request);
            usuarioLogado = response;
            System.out.println("Login realizado com sucesso!");
            System.out.println("Bem-vindo, " + response.getNome() + "!");
        } catch (Exception e) {
            System.out.println("Erro no login: " + e.getMessage());
        }
    }

    private void visualizarDados() {
        System.out.println("\n--- MEUS DADOS ---");
        System.out.println("ID: " + usuarioLogado.getId());
        System.out.println("Nome: " + usuarioLogado.getNome());
        System.out.println("Email: " + usuarioLogado.getEmail());
        System.out.println("Tipo: " + usuarioLogado.getTipoUsuario());

        // busca dados completos via serviço
        if ("Aluno".equals(usuarioLogado.getTipoUsuario())) {
            Optional<AlunoDTO> opt = alunoService.buscarPorId(usuarioLogado.getId());
            opt.ifPresent(aluno -> {
                System.out.println("Data Nascimento: " + aluno.getDataNascimento());
                System.out.println("Idade: " + aluno.getIdade() + " anos");
                System.out.println("Peso: " + aluno.getPeso() + " kg");
            });
        } else if ("Personal".equals(usuarioLogado.getTipoUsuario())) {
            Optional<PersonalDTO> opt = personalService.buscarPorId(usuarioLogado.getId());
            opt.ifPresent(personal -> {
                System.out.println("Data Nascimento: " + personal.getDataNascimento());
                System.out.println("Idade: " + personal.getIdade() + " anos");
                System.out.println("Formado: " + (personal.getFormado() ? "Sim" : "Não"));
                if (personal.getFormado()) {
                    System.out.println("Código Validação: " + personal.getCodigoValidacao());
                    System.out.println("Link Validação: " + personal.getLinkValidacao());
                } else {
                    System.out.println("Licença: " + personal.getLicenca());
                }
            });
        }
    }

    private void fazerLogoff() {
        usuarioLogado = null;
        System.out.println("Logoff realizado com sucesso!");
    }

    private void excluirConta() {
        System.out.print("Tem certeza que deseja excluir sua conta? (sim/não): ");
        String confirm = scanner.nextLine();
        if ("sim".equalsIgnoreCase(confirm)) {
            try {
                if ("Aluno".equals(usuarioLogado.getTipoUsuario())) {
                    alunoService.deletarAluno(usuarioLogado.getId());
                } else {
                    personalService.deletarPersonal(usuarioLogado.getId());
                }
                System.out.println("Conta excluída com sucesso!");
                usuarioLogado = null;
            } catch (Exception e) {
                System.out.println("Erro ao excluir conta: " + e.getMessage());
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}