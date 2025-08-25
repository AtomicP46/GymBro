package com.gymbro.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Email(message = "Email deve ter formato válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idade;

    @NotNull(message = "Campo 'formado' é obrigatório")
    private Boolean formado;

    private String codigoValidacao;
    private String linkValidacao;
    private String licenca;

    public PersonalDTO() {}

    public PersonalDTO(String nome,
                       String email,
                       String senha,
                       LocalDate dataNascimento,
                       Boolean formado,
                       String codigoValidacao,
                       String linkValidacao,
                       String licenca) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.formado = formado;
        this.codigoValidacao = codigoValidacao;
        this.linkValidacao = linkValidacao;
        this.licenca = licenca;
    }

    public PersonalDTO(Long id,
                       String nome,
                       String email,
                       LocalDate dataNascimento,
                       Integer idade,
                       Boolean formado,
                       String codigoValidacao,
                       String linkValidacao,
                       String licenca) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.idade = idade;
        this.formado = formado;
        this.codigoValidacao = codigoValidacao;
        this.linkValidacao = linkValidacao;
        this.licenca = licenca;
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
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public Integer getIdade() {
        return idade;
    }
    
    public void setIdade(Integer idade) {
        this.idade = idade;
    }
    
    public Boolean getFormado() {
        return formado;
    }
    
    public void setFormado(Boolean formado) {
        this.formado = formado;
    }
    
    public String getCodigoValidacao() {
        return codigoValidacao;
    }
    
    public void setCodigoValidacao(String codigoValidacao) {
        this.codigoValidacao = codigoValidacao;
    }
    
    public String getLinkValidacao() {
        return linkValidacao;
    }
    
    public void setLinkValidacao(String linkValidacao) {
        this.linkValidacao = linkValidacao;
    }
    
    public String getLicenca() {
        return licenca;
    }
    
    public void setLicenca(String licenca) {
        this.licenca = licenca;
    }
    
}
