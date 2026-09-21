package br.com.fiap.TodosRestaurantes.services.exceptions;

public class RecursoNaoEncontrado extends RuntimeException {
    public RecursoNaoEncontrado(String message) {
        super(message);
    }
}
