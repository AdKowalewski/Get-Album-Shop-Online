package pl.britenet.exceptions;

public class EmailTakenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailTakenException(String msg) {
        super(msg);
    }

    public EmailTakenException() {
        this("");
    }
}
