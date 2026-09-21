package br.com.fiap.TodosRestaurantes.dtos;

import java.time.LocalDateTime;

public record LinhaClienteDTO(
        Long idUsuario,
        String nome,
        Long idEndereco,
        LocalDateTime ultimaAlteracao
) {
}
