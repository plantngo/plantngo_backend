package me.plantngo.backend.exceptions;

public class InvalidUserTypeException extends RuntimeException{
    public InvalidUserTypeException() {
        super("User Type can only be 'M' or 'C'.");
    }
    
}
