package br.com.fiap.TodosRestaurantes.dtos;

import jakarta.validation.constraints.NotNull;

public record TrocaSenhaDTO(
        @NotNull(message = "Obrigatório informar os dados do Login atual")
        LoginDTO loginAtual,
        @NotNull(message = "Obrigatório informar os dados do Login")
        String novaSenha
) {
}
