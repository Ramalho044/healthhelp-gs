package br.com.global.healthhelp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroDTO(

        @NotBlank
        @Size(min = 2, max = 120)
        String nome,

        @NotBlank
        @Email
        @Size(max = 120)
        String email

) { }
