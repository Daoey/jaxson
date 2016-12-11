package se.teknikhogskolan.jaxson.exception;

public final class IncompleteException extends RuntimeException {

    private static final long serialVersionUID = -7716548302889493612L;

    public IncompleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteException(String message) {
        super(message);
    }

    public IncompleteException() {
        super();
    }
}