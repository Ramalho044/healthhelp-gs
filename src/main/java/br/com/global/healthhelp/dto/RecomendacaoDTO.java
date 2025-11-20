package br.com.global.healthhelp.dto;

import java.time.LocalDateTime;

public record RecomendacaoDTO(

        Long id,
        String mensagem,
        Integer pontuacaoRelevancia,
        LocalDateTime criadaEm

) { }
