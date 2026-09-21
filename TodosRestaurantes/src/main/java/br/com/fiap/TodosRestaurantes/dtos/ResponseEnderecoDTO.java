package br.com.fiap.TodosRestaurantes.dtos;

public record ResponseEnderecoDTO(
        String tipoLogradouro,
        String nomeLogradouro,
        String numero,
        String bairro,
        String cepCompleto,
        String cidade,
        String uf
) {
}
