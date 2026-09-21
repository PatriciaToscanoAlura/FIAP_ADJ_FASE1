package br.com.fiap.TodosRestaurantes.dtos;

import java.time.LocalDateTime;

public record ClienteDTO(
        String nome,
        ResponseEnderecoDTO endereco,
        LocalDateTime ultimaAlteracao,
        String email
) {
}

