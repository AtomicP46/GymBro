package com.gymbro.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "personal")
public class Personal extends Usuario {

    @Column(name = "formado", nullable = false)
    @NotNull(message = "Campo 'formado' é obrigatório")
    private Boolean formado = false;

    @Column(name = "codigo_validacao", length = 50, insertable = true, updatable = true)
    private String codigoValidacao;

    @Column(name = "link_validacao", length = 500, insertable = true, updatable = true)
    private String linkValidacao;

    @Column(name = "licenca", length = 100, insertable = true, updatable = true)
    private String licenca;

    public Personal() {
        super();
    }

    public Personal(String nome,
                    String email,
                    String senhaHash,
                    LocalDate dataNascimento,
                    Boolean formado,
                    String codigoValidacao,
                    String linkValidacao,
                    String licenca) {
        super(nome, email, senhaHash, dataNascimento);
        this.formado = formado;
        this.codigoValidacao = codigoValidacao;
        this.linkValidacao = linkValidacao;
        this.licenca = licenca;
    }

    @PrePersist
    @PreUpdate
    private void validarLicenca() {
        if (Boolean.FALSE.equals(formado)
            && (licenca == null || licenca.trim().isEmpty())) {
            throw new IllegalArgumentException(
                "Licença é obrigatória para personal trainers não formados");
        }
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

    @Override
    public String toString() {
        return "Personal{" +
               "id=" + getId() +
               ", nome='" + getNome() + '\'' +
               ", idade=" + getIdade() +
               ", formado=" + formado +
               ", licenca='" + licenca + '\'' +
               '}';
    }
}
