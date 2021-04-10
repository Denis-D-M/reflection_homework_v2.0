package Exceptions;

public class NoValueAnnotationException extends RuntimeException{
    public NoValueAnnotationException() {
        super();
    }

    public NoValueAnnotationException(String message) {
        super(message);
    }
    public NoValueAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }
}
