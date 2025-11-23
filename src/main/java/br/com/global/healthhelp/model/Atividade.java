package br.com.global.healthhelp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ATIVIDADE", schema = "RM558024")
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATIVIDADE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGISTRO_ID", nullable = false)
    private RegistroDiario registro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORIA_ID", nullable = false)
    private CategoriaAtividade categoria;

    @Column(name = "DESCRICAO", length = 200)
    private String descricao;

    // ATENÇÃO: nomes das colunas iguais aos do banco
    @Column(name = "INICIO_TS")
    private LocalDateTime inicio;

    @Column(name = "FIM_TS")
    private LocalDateTime fim;

    @Column(name = "INTENSIDADE_1A5")
    private Integer intensidade1a5;

    @Column(name = "QUALIDADE_1A5")
    private Integer qualidade1a5;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegistroDiario getRegistro() {
        return registro;
    }

    public void setRegistro(RegistroDiario registro) {
        this.registro = registro;
    }

    public CategoriaAtividade getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAtividade categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public Integer getIntensidade1a5() {
        return intensidade1a5;
    }

    public void setIntensidade1a5(Integer intensidade1a5) {
        this.intensidade1a5 = intensidade1a5;
    }

    public Integer getQualidade1a5() {
        return qualidade1a5;
    }

    public void setQualidade1a5(Integer qualidade1a5) {
        this.qualidade1a5 = qualidade1a5;
    }
}
