package Classes;

import java.util.Scanner;

public class exercicios {
    private String nomeExer;
    private String regiao;
    private String tipo;
    private boolean unilateral;

    public exercicios(){

    }

    public exercicios(String nomeExer, String regiao, String tipo, boolean unilateral){
        this.nomeExer = nomeExer;
        this.regiao = regiao;
        this.tipo = tipo;
        this.unilateral = unilateral;
    }

    public String GetNomeExer(){
        return nomeExer;
    }

     public void setNomeExer(String nomeExer){
        this.nomeExer = nomeExer;
    }

    public String getRegiao(){
        return regiao;
    }

     public void setRegiao(String regiao){
        this.regiao = regiao;
    }

    public String GetTipo() {
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public boolean GetUnilateral(){
        return unilateral;
    }

    public void setUnilateral(boolean unilateral){
        this.unilateral = unilateral;
    }

    public void CadastrarExercicio(Scanner scanner){
        System.out.printf("\nInforme o nome do exercicio: ");
        nomeExer = scanner.nextLine();
        System.out.printf("\nInforme a região trabalhada: ");
        regiao = scanner.nextLine();
        System.out.printf("\nInforme o tipo do exercicio: ");
        tipo = scanner.nextLine();
        System.out.printf("\nO exercicio e unilateral? (Sim : Não): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        unilateral = resposta.equals("sim");
    }

    public void ExibirExercicio(){
        System.out.printf("\nNome do Exercicio: " + nomeExer);
        System.out.printf("\nRegião Trabalhada: " + regiao);
        System.out.printf("\nTipo do Exercicio: " + tipo);
        System.out.printf("\nE unilateral: " + (unilateral ? "Sim" : "Não"));
        System.out.printf("\nEquipamento do Exercicio: (Ainda a ver)\n\n");
    }
}
