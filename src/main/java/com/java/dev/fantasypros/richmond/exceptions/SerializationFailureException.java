package com.java.dev.fantasypros.richmond.exceptions;

/*
 * This is a custom exception to catch errors during the JSON loading
 */

public class SerializationFailureException extends Exception {

    public SerializationFailureException() {
        super("Failed to serialize data to Endpoint");
    }

    public SerializationFailureException(String message) {
        super("Failed to serialize endpoint:" + message);
    }
    
}
