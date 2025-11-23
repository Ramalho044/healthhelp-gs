package br.com.global.healthhelp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REGISTRO_DIARIO", schema = "RM558024")
public class RegistroDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REGISTRO_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;

    @Column(name = "DATA_REF")
    private LocalDate dataRef;

    @Column(name = "PONTUACAO_EQUILIBRIO")
    private Double pontuacaoEquilibrio;

    @Column(name = "OBSERVACOES", length = 1000)
    private String observacoes;

    @OneToMany(mappedBy = "registro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atividade> atividades = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataRef() {
        return dataRef;
    }

    public void setDataRef(LocalDate dataRef) {
        this.dataRef = dataRef;
    }

    public Double getPontuacaoEquilibrio() {
        return pontuacaoEquilibrio;
    }

    public void setPontuacaoEquilibrio(Double pontuacaoEquilibrio) {
        this.pontuacaoEquilibrio = pontuacaoEquilibrio;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }
}
