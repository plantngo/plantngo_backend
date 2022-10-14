package me.plantngo.backend.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException() {
        super("Insufficient Green Points");
    }
}
