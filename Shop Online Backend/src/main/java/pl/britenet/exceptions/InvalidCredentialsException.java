package pl.britenet.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException(String msg) {
        super(msg);
    }

    public InvalidCredentialsException() {
        this("");
    }
}
