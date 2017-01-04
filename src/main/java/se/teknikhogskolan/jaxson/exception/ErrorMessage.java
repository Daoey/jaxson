package se.teknikhogskolan.jaxson.exception;

public final class ErrorMessage {
    private int code;
    private String status;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ErrorMessage{");
        sb.append("code=").append(code);
        sb.append(", status='").append(status).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
