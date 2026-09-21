package br.com.fiap.TodosRestaurantes.entities;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class DonoRestaurante implements Usuario {
    private Long idUsuario;
    private String nome;
    private Endereco endereco;
    private LocalDateTime ultimaAlteracao;

    public DonoRestaurante(Long id, String nome, Endereco endereco) {
        this.setNome(nome);
        this.setEndereco(endereco);
        this.setIdUsuario(id);
        this.setUltimaAlteracao(new Timestamp(0).toLocalDateTime());
    }

    public DonoRestaurante(Long id, String nome, Endereco endereco, LocalDateTime ultimaAlteracao) {
        this.setNome(nome);
        this.setEndereco(endereco);
        this.setIdUsuario(id);
        this.setUltimaAlteracao(ultimaAlteracao);
    }
}
