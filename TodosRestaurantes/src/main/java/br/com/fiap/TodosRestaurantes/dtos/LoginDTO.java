package br.com.fiap.TodosRestaurantes.dtos;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull(message = "Obrigatório informar um e-mail")
        String email,
        @NotNull(message = "Obrigatório informar uma senha")
        String senha
) {
}
