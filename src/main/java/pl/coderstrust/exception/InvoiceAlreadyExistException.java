package pl.coderstrust.exception;

public class InvoiceAlreadyExistException extends RuntimeException {

    public InvoiceAlreadyExistException(String message) {
        super(message);
    }
}
