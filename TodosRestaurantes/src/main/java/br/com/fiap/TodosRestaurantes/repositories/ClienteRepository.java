package br.com.fiap.TodosRestaurantes.repositories;

import br.com.fiap.TodosRestaurantes.dtos.LinhaClienteDTO;
import br.com.fiap.TodosRestaurantes.entities.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Optional<LinhaClienteDTO> findById (Long id);
    Integer save(Cliente cliente);
    Integer update(Cliente cliente);
    Integer delete(Cliente cliente);
    List<LinhaClienteDTO> findByNome(String nome);
}
