package se.teknikhogskolan.jaxson.exception;

public final class ConflictException extends RuntimeException {

    private static final long serialVersionUID = 7968389335316189609L;

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException() {
        super();
    }
}
