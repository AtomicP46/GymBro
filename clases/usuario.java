package clases;

public class usuario{
    private String nome;
    private String email;
    private float peso;

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
}