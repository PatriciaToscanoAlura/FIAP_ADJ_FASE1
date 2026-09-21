package br.com.fiap.TodosRestaurantes.dtos;

import br.com.fiap.TodosRestaurantes.model.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record IncluiUsuarioRequestDTO(
        @Schema(description = "Nome do usuário")
        @NotNull(message = "Obrigatório informar o nome do cliente")
        String nome,
        EnderecoDTO endereco,
        TipoUsuario tipoUsuario,
        @NotNull(message = "Obrigatório informar usuário e senha")
        LoginDTO login
) {
}
