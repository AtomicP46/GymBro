package br.ifg.gymbro.model;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senhaHash;
    private Float peso;

    // Construtores
    public Usuario() {}

    public Usuario(String nome, String email, String senhaHash, Float peso) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.peso = peso;
    }

    public Usuario(Long id, String nome, String email, String senhaHash, Float peso) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.peso = peso;
    }

    // Construtor protegido para herança
    protected Usuario(Long id, String nome, String email, String senhaHash) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", peso=" + peso +
                '}';
    }
}
