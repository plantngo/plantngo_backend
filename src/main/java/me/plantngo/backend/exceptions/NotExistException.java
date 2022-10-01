package me.plantngo.backend.exceptions;

public class NotExistException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public NotExistException() {
        super("Item doesn't exist!");
    }

}
