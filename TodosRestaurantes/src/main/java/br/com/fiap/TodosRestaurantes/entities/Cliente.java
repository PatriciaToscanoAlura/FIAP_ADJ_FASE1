package br.com.fiap.TodosRestaurantes.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Cliente implements Usuario {
    private Long idUsuario;
    private String nome;
    private Endereco endereco;
    private LocalDateTime ultimaAlteracao;

    public Cliente(Long id, String nome, Endereco endereco) {
        this.setNome(nome);
        this.setEndereco(endereco);
        this.setIdUsuario(id);
        this.setUltimaAlteracao(new Timestamp(0).toLocalDateTime());
    }

    public Cliente(Long id, String nome, Endereco endereco, LocalDateTime ultimaAlteracao) {
        this.setNome(nome);
        this.setEndereco(endereco);
        this.setIdUsuario(id);
        this.setUltimaAlteracao(ultimaAlteracao);
    }
}
