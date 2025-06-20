package Classes;

import java.util.Scanner;

public class personal {
    private String nome;
    private String email;
    private boolean formaçãoEF;
    private String licensa;

    public personal(){

    }

    public personal (String nome, String email, boolean formaçãoEF, String licensa){
        this.nome = nome;
        this.email = email;
        this.formaçãoEF = formaçãoEF;
        this.licensa = licensa;
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

    public boolean getFormaçãoEF(){
        return formaçãoEF;
    }

    public void setFormacaoEF(boolean formaçãoEF){
        this.formaçãoEF = formaçãoEF;
    }

    public String getLicensa() {
        return licensa;
    }

    public void setLicenca(String licensa) {
        this.licensa = licensa;
    }

    public void CriarPerfilPersonal(Scanner scanner){
        System.out.printf("Informe seu nome: ");
        nome = scanner.nextLine();
        System.out.printf("Informe seu email: ");
        email = scanner.nextLine();
        System.out.printf("Possue informação em EF? (sim/não): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        formaçãoEF = resposta.equals("sim");

        if (formaçãoEF) {
            System.out.printf("Informe o numero do diploma:");
            licensa = scanner.nextLine();
        } else {
            System.out.printf("Informe sua licença: ");
            licensa = scanner.nextLine();
        }
    }

    public void ExibirPerfilPersonal(){
        System.out.printf("Nome: " + nome);
        System.out.printf("\nEmail: " + email);
        System.out.printf("\nFormação em EF: " + (formaçãoEF ? "Sim" : "Não"));
        if (formaçãoEF) {
            System.out.printf("\nNumero de diploma: " + licensa + "\n");
        } else {
            System.out.printf("\nLicença de personal: " + licensa + "\n\n");
        }
    }
}
