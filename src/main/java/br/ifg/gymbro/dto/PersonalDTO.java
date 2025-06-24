package br.ifg.gymbro.dto;

public class PersonalDTO {
    private String nome;
    private String email;
    private String senha;
    private String licenca;
    private Boolean formacao;

    // Construtores
    public PersonalDTO() {}

    public PersonalDTO(String nome, String email, String senha, String licenca, Boolean formacao) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.licenca = licenca;
        this.formacao = formacao;
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

    public String getLicenca() {
        return licenca;
    }

    public void setLicenca(String licenca) {
        this.licenca = licenca;
    }

    public Boolean getFormacao() {
        return formacao;
    }

    public void setFormacao(Boolean formacao) {
        this.formacao = formacao;
    }
}
