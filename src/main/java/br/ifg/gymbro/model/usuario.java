package Classes;

import java.util.Scanner;

public class usuario{
    private String nome;
    private String email;
    private float peso;

    public usuario(){

    }

    public usuario(String nome, String email, float peso) {
        this.nome = nome;
        this.email = email;
        this.peso = peso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public void CriarUsuario(Scanner scanner){
        System.out.printf("Informe seu nome: ");
        nome = scanner.nextLine();
        System.out.printf("\nInforme seu email: ");
        email = scanner.nextLine();
        System.out.printf("\nInforme seu peso: ");
        peso = scanner.nextFloat();
    }

    public void ExibirUsuarios(){
        System.out.printf("Nome: " + nome + "\n");
        System.out.printf("Email: " + email + "\n");
        System.out.printf("Peso: " + peso + "\n\n");
    }
}