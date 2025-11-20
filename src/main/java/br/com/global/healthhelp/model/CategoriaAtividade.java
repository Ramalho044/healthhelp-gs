package br.com.global.healthhelp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIA_ATIVIDADE", schema = "RM558024")
public class CategoriaAtividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORIA_ID")
    private Long id;

    @Column(name = "NOME_CATEGORIA", nullable = false, length = 60)
    private String nome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
