package com.smartlostfound.backend.exception;

public class LostItemNotFoundException extends RuntimeException {

    public LostItemNotFoundException(String message) {
        super(message);
    }
}