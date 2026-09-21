package br.com.fiap.TodosRestaurantes.repositories;

import br.com.fiap.TodosRestaurantes.entities.Endereco;

import java.util.Optional;

public interface EnderecoRepository {
    Optional<Endereco> findById (Long id);
    Integer save(Endereco endereco);
    Integer update(Endereco Endereco);
    Integer delete(Endereco Endereco);
}
