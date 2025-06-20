package Classes;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        boolean rodando = true;
        Scanner scanner = new Scanner(System.in);
        usuario usuario = new usuario();
        equipamento equipamento = new equipamento();
        personal personal = new personal();
        exercicios exercicios = new exercicios();
        treino treino = new treino();

        System.out.printf("Bem vindo ao gerenciador de treino!\n");
        System.out.printf("Selecione se ira entrar como usuario(1), personal(2) ou sair(3)\n");
        int opcao = scanner.nextInt();
        // System.out.printf("opção selecionada: " + opcao);

        while (rodando) {
            switch (opcao) {
                case 1:
                    Scanner scanner2 = new Scanner(System.in);
                    System.out.printf("Bem Vindo ao Perfil de Usuario!\n");
                    System.out.printf("Selecione a opção de Usuario");
                    System.out.printf(
                            "\n0: Mostrar informações de usuarios. \n1: Criar usuario.\n2: Criar treino. \n3: Editar treino. \n4: Sair\n");
                    int usuario1 = scanner2.nextInt();
                    switch (usuario1) {
                        case 0:
                            System.out.printf(" - Informações do usuario - \n");
                            usuario.ExibirUsuarios();
                            break;

                        case 1:
                            System.out.printf("Bem vindo a criação de usuario:\n");
                            usuario.CriarUsuario(scanner);
                            break;

                        case 2:
                            System.out.printf("Bem vindo a criação de treino:\n");
                            treino.RegistrarTreino(scanner);
                            System.out.printf(" - Treino criado - ");
                            treino.ExibirTreino();
                            break;

                        case 3:
                            System.out.printf("Edição de treino:\n - Treino Atual - \n");
                            treino.ExibirTreino();
                            System.out.printf("Deseja editar o treino? (Sim : Não)\n");
                            String resposta2 = scanner.nextLine().trim().toLowerCase();
                            boolean edicao = resposta2.equals("sim");

                            if (edicao) {

                            } else {

                            }
                            break;

                        case 4:
                            rodando = false;
                            break;
                        default:
                            System.out.printf("ERRO!\nOpção selecionada não existe");
                            break;
                    }
                    break;

                case 2:
                    Scanner scanner3 = new Scanner(System.in);
                    System.out.printf("Bem Vindo ao Perfil de Personal!\n");
                    System.out.printf("Selecione a opção de Personal");
                    System.out.printf(
                            "\n0: Mostrar informações de Personal. \n1: Criar perfil de personal. \n2: Editar treino de usuario. \n3: Registrar exercicio. \n4: Registrar equipamento.\n5: Registrar treino \n6: Sair\n");
                    int personalOp = scanner3.nextInt();
                    switch (personalOp) {
                        case 0:
                            System.out.printf(" - Exibição de perfil de Personal - \n");
                            personal.ExibirPerfilPersonal();
                            break;

                        case 1:
                            System.out.printf("Bem vindo a criação de perfil de personal!\n");
                            personal.CriarPerfilPersonal(scanner);
                            break;

                        case 2:
                            System.out.printf("Bem vindo a edição de treino de usuario.\n");
                            System.out.printf(" - Treino do usuario - \n");
                            treino.ExibirTreino();
                            System.out.printf(" - Edição do treino - \n");
                            treino.EdiçãoTreino(scanner);
                            break;

                        case 3:
                            System.out.printf("Bem vindo ao registro de exercicio.\n");
                            exercicios.CadastrarExercicio(scanner);
                            System.out.printf("\n- Exercicio Registrado -");
                            exercicios.ExibirExercicio();
                            break;

                        case 4:
                            System.out.printf("Bem vindo ao registro de equipamento.\n");
                            equipamento.RegistrarEquipamento(scanner);
                            System.out.printf("\n- Equipamento Registrado -");
                            equipamento.ExibirEquipamento();
                            break;

                        case 5:
                            System.out.printf("Bem vindo ao registro de treino. \n");
                            treino.EdiçãoTreino(scanner);
                            break;
                            
                        case 6:
                            rodando = false;
                            break;

                        default:
                            System.out.printf("ERRO!\nOpção selecionada não existe");
                            break;
                    }
                    break;

                case 3:
                    rodando = false;
                    break;

                default:
                    System.out.printf("ERRO!\nOpção selecionada não existe");
                    break;
            }
        }

        scanner.close();
    }
}