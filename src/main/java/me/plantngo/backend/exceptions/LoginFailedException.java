package me.plantngo.backend.exceptions;

public class LoginFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LoginFailedException() {
        super("Incorrect username or password");
    }

}