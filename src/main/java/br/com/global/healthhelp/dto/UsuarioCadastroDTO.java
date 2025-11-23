package br.com.global.healthhelp.dto;

import java.time.LocalDate;

public record UsuarioCadastroDTO(
        String nome,
        String email,
        String senha,
        String genero,
        LocalDate dataNascimento,
        Integer alturaCm,
        Double pesoKg
) { }
