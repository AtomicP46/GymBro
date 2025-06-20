package Classes;

import java.util.Scanner;

public class equipamento {
    private String nomeEqui;
    private float pesoEqui;

    public equipamento(){

    }

    public equipamento(String nomeEqui, float pesoEqui){
        this.nomeEqui = nomeEqui;
        this.pesoEqui = pesoEqui;
    }

    public String GetNomeEqui(){
        return nomeEqui;
    }

    public void setNomeEqui(String nomeEqui){
        this.nomeEqui = nomeEqui;
    }

    public float GetPesoEqui(){
        return pesoEqui;
    }

    public void setPesoEqui(float pesoEqui){
        this.pesoEqui = pesoEqui;
    }

    public void RegistrarEquipamento(Scanner scanner) {
        System.out.printf("\nInforme o nome do equipamento: ");
        nomeEqui = scanner.nextLine();
        System.out.printf("\nInforme o peso do equipamento: ");
        pesoEqui = scanner.nextFloat();
    }

    public void ExibirEquipamento() {
        System.out.printf("\nNome do Equipamento: " + nomeEqui);
        System.out.printf("\nPeso do Equipamento: " + pesoEqui + "\n\n");
    }
}
