package br.com.fiap.TodosRestaurantes.dtos;

import jakarta.validation.constraints.NotNull;

public record ValidaLoginDTO(
        @NotNull(message = "Obrigatório informar os dados do Login")
        LoginDTO login
) {
}
