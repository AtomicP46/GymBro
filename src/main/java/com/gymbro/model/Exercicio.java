package com.gymbro.model;

import com.gymbro.enums.RegiaoCorpo;
import com.gymbro.enums.TipoExercicio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "exercicio")
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RegiaoCorpo regiao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoExercicio tipo;

    @NotNull
    @Column(nullable = false)
    private Boolean unilateral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id")
    private Equipamento equipamento;

    @Transient
    private String equipamentoNome;

    public Exercicio() {}

    public Exercicio(String nome,
                     RegiaoCorpo regiao,
                     TipoExercicio tipo,
                     Boolean unilateral,
                     Equipamento equipamento) {
        this.nome = nome;
        this.regiao = regiao;
        this.tipo = tipo;
        this.unilateral = unilateral;
        this.equipamento = equipamento;
    }

    public Exercicio(Long id,
                     String nome,
                     RegiaoCorpo regiao,
                     TipoExercicio tipo,
                     Boolean unilateral,
                     Equipamento equipamento) {
        this(nome, regiao, tipo, unilateral, equipamento);
        this.id = id;
    }

    // getters/setters…

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public RegiaoCorpo getRegiao() { return regiao; }
    public void setRegiao(RegiaoCorpo regiao) { this.regiao = regiao; }

    public TipoExercicio getTipo() { return tipo; }
    public void setTipo(TipoExercicio tipo) { this.tipo = tipo; }

    public Boolean getUnilateral() { return unilateral; }
    public void setUnilateral(Boolean unilateral) { this.unilateral = unilateral; }

    public Equipamento getEquipamento() { return equipamento; }
    public void setEquipamento(Equipamento equipamento) { this.equipamento = equipamento; }

    public String getEquipamentoNome() {
        return equipamento != null ? equipamento.getNome() : equipamentoNome;
    }
    public void setEquipamentoNome(String equipamentoNome) {
        this.equipamentoNome = equipamentoNome;
    }

    @Override
    public String toString() {
        return "Exercicio{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", regiao=" + regiao +
            ", tipo=" + tipo +
            ", unilateral=" + unilateral +
            ", equipamentoId=" + (equipamento != null ? equipamento.getId() : null) +
            ", equipamentoNome='" + getEquipamentoNome() + '\'' +
            '}';
    }
}