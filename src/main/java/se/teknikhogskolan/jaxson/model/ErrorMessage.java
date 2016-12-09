package se.teknikhogskolan.jaxson.model;

public final class ErrorMessage {

    private String errorMessage;
    
    protected ErrorMessage(){}
    
    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
