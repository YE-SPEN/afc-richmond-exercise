package com.java.dev.fantasypros.richmond.exceptions;

public class SerializationFailureException extends Exception {

    public SerializationFailureException() {
        super("Failed to deserialize data from Endpoint");
    }

    public SerializationFailureException(String message) {
        super("Failed to serialize endpoint:" + message);
    }
    
}
