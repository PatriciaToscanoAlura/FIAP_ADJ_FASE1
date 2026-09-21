package br.com.fiap.TodosRestaurantes.repositories;

import br.com.fiap.TodosRestaurantes.entities.Login;

import java.util.Optional;

public interface LoginRepository {
    Optional<Login> findByEmail(String email);
    Optional<Login> findLoginById(Long id);
    Integer save(Login login);
    Integer update(Login login);
    Integer delete(Long id);
}
