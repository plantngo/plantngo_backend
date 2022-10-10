package me.plantngo.backend.exceptions;

public class UserNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("User Not Found");
    }

    public UserNotFoundException(String msg) {
        super("User Not Found:" + msg);
    }
}
