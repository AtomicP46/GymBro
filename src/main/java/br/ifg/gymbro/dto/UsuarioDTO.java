package br.ifg.gymbro.dto;

public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private Float peso;

    // Construtores
    public UsuarioDTO() {}

    public UsuarioDTO(String nome, String email, String senha, Float peso) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.peso = peso;
    }

    // Getters e Setters
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }
}
