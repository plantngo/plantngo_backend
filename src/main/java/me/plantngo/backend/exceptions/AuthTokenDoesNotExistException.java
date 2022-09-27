package me.plantngo.backend.exceptions;

public class AuthTokenDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthTokenDoesNotExistException() {
        super("Token does not exists");
    }

}
