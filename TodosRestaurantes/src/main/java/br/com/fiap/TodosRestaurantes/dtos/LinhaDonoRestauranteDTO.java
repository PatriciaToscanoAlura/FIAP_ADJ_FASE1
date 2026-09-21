package br.com.fiap.TodosRestaurantes.dtos;

import java.time.LocalDateTime;

public record LinhaDonoRestauranteDTO(
        Long idUsuario,
        String nome,
        Long idEndereco,
        LocalDateTime ultimaAlteracao
) {
}
