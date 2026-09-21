package br.com.fiap.TodosRestaurantes.dtos;

import br.com.fiap.TodosRestaurantes.model.TipoLogradouro;
import br.com.fiap.TodosRestaurantes.model.Uf;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record EnderecoDTO(
        @Schema(description = "Tipo de logradouro")
        @NotNull(message = "Obrigatório informar o tipo de logradouro")
        TipoLogradouro tipoLogradouro,
        @Schema(description = "Nome do logradouro")
        @NotNull(message = "Obrigatório informar o nome do logradouro")
        String nomeLogradouro,
        @Schema(description = "Número que identifica o imóvel do endereço")
        @NotNull(message = "Obrigatório informar número")
        String numero,
        String bairro,
        @Schema(description = "CEP no formato 99999-999")
        String cepCompleto,
        @NotNull(message = "Obrigatório informar a cidade")
        String cidade,
        @NotNull(message = "Obrigatório informar o Estado (uf)")
        Uf uf
) {
}
