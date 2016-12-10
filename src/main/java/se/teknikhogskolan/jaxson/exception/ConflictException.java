package se.teknikhogskolan.jaxson.exception;

public class ConflictException extends RuntimeException {

    private static final long serialVersionUID = 7968389335316189609L;

    protected ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ConflictException(String message) {
        super(message);
    }

    protected ConflictException() {
        super();
    }
}
