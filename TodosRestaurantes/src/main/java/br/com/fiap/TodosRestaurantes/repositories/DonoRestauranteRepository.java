package br.com.fiap.TodosRestaurantes.repositories;

import br.com.fiap.TodosRestaurantes.dtos.LinhaDonoRestauranteDTO;
import br.com.fiap.TodosRestaurantes.entities.DonoRestaurante;

import java.util.Optional;

public interface DonoRestauranteRepository {
    Optional<LinhaDonoRestauranteDTO> findById (Long id);
    Integer save(DonoRestaurante donoRestaurante);
    Integer update(DonoRestaurante donoRestaurante);
    Integer delete(DonoRestaurante donoRestaurante);
}
