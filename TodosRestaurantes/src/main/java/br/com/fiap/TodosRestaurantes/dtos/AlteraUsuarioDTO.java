package br.com.fiap.TodosRestaurantes.dtos;

public record AlteraUsuarioDTO(
        String nome,
        ResponseEnderecoDTO endereco,
        String email
) {
}
