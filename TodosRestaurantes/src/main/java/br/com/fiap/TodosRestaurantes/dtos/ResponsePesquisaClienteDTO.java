package br.com.fiap.TodosRestaurantes.dtos;

import java.util.List;

public record ResponsePesquisaClienteDTO(
        List<ClienteDTO> usuarios
) {
}
