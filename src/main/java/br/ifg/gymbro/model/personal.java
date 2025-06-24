package br.ifg.gymbro.model;

public class Personal extends Usuario {
    private String licenca;
    private Boolean formacao;

    // Construtores
    public Personal() {
        super();
    }

    public Personal(String nome, String email, String senhaHash, String licenca, Boolean formacao) {
        super(nome, email, senhaHash, null); // Personal não tem peso
        this.licenca = licenca;
        this.formacao = formacao;
    }

    public Personal(Long id, String nome, String email, String senhaHash, String licenca, Boolean formacao) {
        super(id, nome, email, senhaHash, null); // Personal não tem peso
        this.licenca = licenca;
        this.formacao = formacao;
    }

    // Getters e Setters específicos do Personal
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

    // Método para verificar se tem formação em EF
    public boolean temFormacaoEF() {
        return formacao != null && formacao;
    }

    @Override
    public String toString() {
        return "Personal{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", licenca='" + licenca + '\'' +
                ", formacao=" + formacao +
                '}';
    }
}
