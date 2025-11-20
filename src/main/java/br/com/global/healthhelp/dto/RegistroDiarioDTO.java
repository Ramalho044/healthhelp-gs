package br.com.global.healthhelp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record RegistroDiarioDTO(

        Long id,

        @NotNull
        LocalDate dataRegistro,

        Integer pontuacaoEquilibrio,

        @Size(max = 500)
        String observacoes,

        @NotNull
        List<AtividadeDTO> atividades

) { }
