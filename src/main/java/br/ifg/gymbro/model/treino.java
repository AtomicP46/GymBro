package Classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class treino {
    private float pesoTreino;
    private int repeticoes;
    private LocalDate dataExer;
    private int series;
    private boolean aqueciemnto;
    private String anotacao;

    public treino() {

    }

    public treino(float pesoTreino, int repeticoes, int series, boolean aqueciemnto, String anotacao,
            LocalDate dataExer) {
        this.pesoTreino = pesoTreino;
        this.repeticoes = repeticoes;
        this.dataExer = dataExer;
        this.series = series;
        this.aqueciemnto = aqueciemnto;
        this.anotacao = anotacao;
    }

    public float getPesoTreino() {
        return pesoTreino;
    }

    public void setPesoTreino(float pesoTreino) {
        this.pesoTreino = pesoTreino;
    }

    public int getRepeticao() {
        return repeticoes;
    }

    public void setRepeticao(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public LocalDate getData() {
        return dataExer;
    }

    public void setData(LocalDate dataExer) {
        this.dataExer = dataExer;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public boolean getAquecimento() {
        return aqueciemnto;
    }

    public void setAquecimento(boolean aqueciemnto) {
        this.aqueciemnto = aqueciemnto;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    public void RegistrarTreino(Scanner scanner) {

        System.out.printf("\nInforme a data do treino formato (dd/mm/yyyy): ");
        String dataInformada = scanner.nextLine();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            dataExer = LocalDate.parse(dataInformada, formato);
            System.out.println("Data registrada com sucesso!");
        } catch (DateTimeParseException e) {
            System.out.printf("Formato de data incorreta ");
            return;
        }

        System.out.printf("\nInforme o peso do treino: ");
        pesoTreino = scanner.nextFloat();
        System.out.printf("\nInforme o numero de repetições: ");
        repeticoes = scanner.nextInt();
        System.out.printf("\nInforme a quantidade de series: ");
        series = scanner.nextInt();
        scanner.nextLine();

        System.out.printf("\nInforme se é necessário um aquecimento (Sim ou Não): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        aqueciemnto = resposta.equals("sim");

        System.out.printf("\nAnotações extrar: ");
        anotacao = scanner.nextLine();
    }

    public void ExibirTreino() {
        System.out.printf("\nData do treino: " + dataExer);
        System.out.printf("\nPeso do treino: " + pesoTreino);
        System.out.printf("\nNumero de repetições: " + repeticoes);
        System.out.printf("\nQuatidade de series: " + series);
        System.out.printf("\nNecesario aquecimento: " + (aqueciemnto ? "sim" : "não"));
        System.out.printf("\nAnotações: " + anotacao + "\n\n");
    }

    public void EdiçãoTreino(Scanner scanner){
        boolean rodEdicao = true;
        boolean respostaConfirm = false;

        while (rodEdicao) {
            System.out.printf("\nO que deseja editar?\n");
            System.out.printf("1: Data\n2: Peso\n3: Repetições\n4: Series\n5: Nececidade de aquecimento\n6: Anotações\n7: Encerrar\n");
            int opModificacao = scanner.nextInt(); 
            switch (opModificacao) {
                case 1:
                    System.out.printf("\nInforme a nova data formato (dd/MM/yyyy): ");
                    String novaDataInformada = scanner.nextLine();
                    DateTimeFormatter novadataForm = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    
                    try {
                        LocalDate novaData = LocalDate.parse(novaDataInformada, novadataForm);
                        System.out.printf("Antiga data: " + dataExer);
                        System.out.printf("\nNova data: " + novaData);
                        System.out.printf("Confirmar modificação? (Sim : Não)");
                        String resp = scanner.nextLine().trim().toLowerCase();
                        respostaConfirm = resp.equals("sim");

                        if (respostaConfirm) {
                            dataExer = novaData;
                            respostaConfirm = false;
                            System.out.printf("\n- Edição realizada com sucesso -\n");
                        } else {
                            System.out.printf("\n- Edição de data cancelada -\n");
                        }

                    } catch (DateTimeParseException e) {
                        System.out.printf("\nERRO! - Formato de data incorreto\nTente novamente...\n");
                        continue;
                    }
                break;

                case 2:
                    System.out.printf("\nInforme o novo peso: ");
                    float novoPeso = scanner.nextFloat();
                    System.out.printf("\nPeso antigo: " + pesoTreino);
                    System.out.printf("\nNovo peso: " + novoPeso);
                    System.out.printf("Confirmar modificação? (Sim : Não)");
                    String resp2 = scanner.nextLine().trim().toLowerCase();
                    respostaConfirm = resp2.equals("sim");

                    if (respostaConfirm) {
                        pesoTreino = novoPeso;
                        respostaConfirm = false;
                        System.out.printf("\n- Edição realizada com sucesso -\n");
                    } else {
                        System.out.printf("\n- Edição de data cancelada -\n");
                    }

                break;

                case 3:
                    System.out.printf("\nInforme o novo valor de repetição: ");
                    int novaRepetição = scanner.nextInt();
                    System.out.printf("\nAntigo valor de repetição: " + repeticoes);
                    System.out.printf("\nNovo valor de repetição: " + novaRepetição);
                    System.out.printf("Confirmar modificação? (Sim : Não)");
                    String resp3 = scanner.nextLine().trim().toLowerCase();
                    respostaConfirm = resp3.equals("sim");

                    if (respostaConfirm) {
                        repeticoes = novaRepetição;
                        respostaConfirm = false;
                        System.out.printf("\n- Edição realizada com sucesso -\n");
                    } else {
                        System.out.printf("\n- Edição de data cancelada -\n");
                    }
                break;

                case 4:
                    System.out.printf("\nInforme a nova quantidade de series: ");
                    int novaSerie = scanner.nextInt();
                    System.out.printf("\nAntigo valor de repetição: " + series);
                    System.out.printf("\nNovo valor de repetição: " + novaSerie);
                    System.out.printf("Confirmar modificação? (Sim : Não)");
                    String resp4 = scanner.nextLine().trim().toLowerCase();
                    respostaConfirm = resp4.equals("sim");

                    if (respostaConfirm) {
                        series = novaSerie;
                        respostaConfirm = false;
                        System.out.printf("\n- Edição realizada com sucesso -\n");
                    } else {
                        System.out.printf("\n- Edição de data cancelada -\n");
                    }
                break;

                case 5:
                    System.out.printf("\nInforme a modificação da necessidade de aqueciemento (Sim ou Não): ");
                    String resposta1 = scanner.nextLine().trim().toLowerCase();
                    boolean novoAqueciemnto = resposta1.equals("sim");

                    System.out.printf("\nAntigo valor de nececidade de aquecimento " + (aqueciemnto ? "sim" : "não"));
                    System.out.printf("\nNovo valor de nececidade de aquecimento: " + (novoAqueciemnto ? "sim" : "não"));
                    System.out.printf("Confirmar modificação? (Sim : Não)");
                    String resp5 = scanner.nextLine().trim().toLowerCase();
                    respostaConfirm = resp5.equals("sim");

                    if (respostaConfirm) {
                        aqueciemnto = novoAqueciemnto;
                        respostaConfirm = false;
                        System.out.printf("\n- Edição realizada com sucesso -\n");
                    } else {
                        System.out.printf("\n- Edição de data cancelada -\n");
                    }
                break;

                case 6:
                    System.out.printf("\nInforme a nova anotação: ");
                    String novaAnotacao = scanner.nextLine();
                    System.out.printf("\nAntiga anotação: " + anotacao);
                    System.out.printf("\nNova anotação: " + novaAnotacao);
                    System.out.printf("Confirmar modificação? (Sim : Não)");
                    String resp6 = scanner.nextLine().trim().toLowerCase();
                    respostaConfirm = resp6.equals("sim");

                    if (respostaConfirm) {
                        anotacao = novaAnotacao;
                        respostaConfirm = false;
                        System.out.printf("\n- Edição realizada com sucesso -\n");
                    } else {
                        System.out.printf("\n- Edição de data cancelada -\n");
                    }
                break;

                case 7:
                    rodEdicao = false;
                    System.out.printf("- Edição terminada -");
                break;
            
                default:
                    break;
            }  
        }
    }
}
