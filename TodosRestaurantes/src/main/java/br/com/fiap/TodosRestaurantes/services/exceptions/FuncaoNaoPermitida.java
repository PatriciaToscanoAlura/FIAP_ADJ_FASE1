package br.com.fiap.TodosRestaurantes.services.exceptions;

public class FuncaoNaoPermitida extends RuntimeException {
    public FuncaoNaoPermitida(String message) {
        super(message);
    }
}
