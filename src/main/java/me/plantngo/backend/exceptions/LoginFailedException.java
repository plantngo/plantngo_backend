package me.plantngo.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoginFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LoginFailedException() {
        super("Incorrect username or password");
    }

}