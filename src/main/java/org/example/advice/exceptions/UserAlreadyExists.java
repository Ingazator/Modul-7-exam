package org.example.advice.exceptions;

public class UserAlreadyExists extends RuntimeException{
    public UserAlreadyExists() {
        super("This email already exists");
    }
}
