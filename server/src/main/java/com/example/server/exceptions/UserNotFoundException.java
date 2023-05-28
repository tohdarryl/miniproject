package com.example.server.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
