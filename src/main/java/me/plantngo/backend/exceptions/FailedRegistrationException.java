package me.plantngo.backend.exceptions;

public class FailedRegistrationException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public FailedRegistrationException(){
        super("Registration Failed");
    }
    public FailedRegistrationException(String msg){
        super("Registration Failed: " + msg);
    }
}


