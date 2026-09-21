package br.com.fiap.TodosRestaurantes.dtos;

import lombok.AllArgsConstructor;

public record ResponseLoginDTO(
        Integer expriraEm,
        String chaveAcesso,
        Long idUsuario,
        String tipoUsuario
) {
}
