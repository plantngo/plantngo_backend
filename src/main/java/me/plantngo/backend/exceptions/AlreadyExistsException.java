package me.plantngo.backend.exceptions;

public class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlreadyExistsException() {
        super("Item already exists");
    }
}
