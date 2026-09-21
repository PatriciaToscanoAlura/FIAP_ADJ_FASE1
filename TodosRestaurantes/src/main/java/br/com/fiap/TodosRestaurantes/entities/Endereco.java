package br.com.fiap.TodosRestaurantes.entities;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Endereco {
    private Long idEndereco;
    private String tipoLogradouro;
    private String nomeLogradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;

  public Endereco(String tipoLogradouro, String nomeLogradouro, String numero, String bairro, String cidade, String cep, String uf) {
        this.setIdEndereco(null);
        this.setTipoLogradouro(tipoLogradouro);
        this.setNomeLogradouro(nomeLogradouro);
        this.setNumero(numero);
        this.setBairro(bairro);
        this.setCidade(cidade);
        this.setCep(cep);
        this.setUf(uf);
    }

}
