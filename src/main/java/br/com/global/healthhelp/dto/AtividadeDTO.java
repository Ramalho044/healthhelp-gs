package br.com.global.healthhelp.dto;

import java.time.LocalDateTime;

public record AtividadeDTO(
        Long id,
        Long idCategoria,
        LocalDateTime inicio,
        LocalDateTime fim,
        Integer intensidade1a5,
        Integer qualidade1a5,
        String descricao
) { }
