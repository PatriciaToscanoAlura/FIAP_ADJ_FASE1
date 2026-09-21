package br.com.fiap.TodosRestaurantes.services.exceptions;

public class ErroNaAlteracaoEmail extends RuntimeException {
    public ErroNaAlteracaoEmail(String message) {
        super(message);
    }
}
