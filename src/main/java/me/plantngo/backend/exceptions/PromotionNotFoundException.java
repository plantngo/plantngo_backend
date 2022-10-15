package me.plantngo.backend.exceptions;

public class PromotionNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public PromotionNotFoundException() {
        super("Promotion Not Found");
    }

    public PromotionNotFoundException(String msg) {
        super("Promotion Not Found:" + msg);
    }
    
}
