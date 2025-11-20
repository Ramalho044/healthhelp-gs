package br.com.global.healthhelp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "REGISTRO_DIARIO", schema = "RM558024")
public class RegistroDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REGISTRO_ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @Column(name = "DATA_REF", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "PONTUACAO_EQUILIBRIO")
    private Integer pontuacaoEquilibrio;

    @Column(name = "OBSERVACOES", length = 500)
    private String observacoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Integer getPontuacaoEquilibrio() {
        return pontuacaoEquilibrio;
    }

    public void setPontuacaoEquilibrio(Integer pontuacaoEquilibrio) {
        this.pontuacaoEquilibrio = pontuacaoEquilibrio;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
