package me.plantngo.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotExistException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public NotExistException() {
        super("Item doesn't exist!");
    }

}
