package br.com.fiap.TodosRestaurantes.dtos;

public record ResponseErroDTO(
        String mensagem,
        Integer status
) {
}
