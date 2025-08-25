package com.gymbro.dto;

public class LoginResponseDTO {

    private String token;
    private String tipoUsuario;
    private Long id;
    private String nome;
    private String email;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, String tipoUsuario, Long id, String nome, String email) {
        this.token = token;
        this.tipoUsuario = tipoUsuario;
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

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
}