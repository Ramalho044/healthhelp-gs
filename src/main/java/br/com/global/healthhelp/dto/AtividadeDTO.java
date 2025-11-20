package br.com.global.healthhelp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AtividadeDTO(

        Long id,

        @NotNull
        Long idCategoria,

        @NotNull
        LocalDateTime inicio,

        @NotNull
        LocalDateTime fim,

        @Size(max = 255)
        String descricao

) { }
