package org.example.ivoprojekt.api.warning;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
