package br.com.fiap.TodosRestaurantes.services.exceptions;

public class LoginIncorreto extends RuntimeException {
    public LoginIncorreto(String message) {
        super(message);
    }
}
