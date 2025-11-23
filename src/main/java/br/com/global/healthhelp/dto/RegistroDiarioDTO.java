package br.com.global.healthhelp.dto;

import java.time.LocalDate;
import java.util.List;

public record RegistroDiarioDTO(
        Long id,
        LocalDate dataRegistro,
        Double pontuacaoEquilibrio,
        String observacoes,
        List<AtividadeDTO> atividades
) {}
