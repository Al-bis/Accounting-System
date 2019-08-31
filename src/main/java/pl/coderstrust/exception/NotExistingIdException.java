package pl.coderstrust.exception;

public class NotExistingIdException extends RuntimeException {

    public NotExistingIdException(String message) {
        super(message);
    }
}
