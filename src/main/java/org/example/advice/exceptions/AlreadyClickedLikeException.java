package org.example.advice.exceptions;

public class AlreadyClickedLikeException extends RuntimeException{
    public AlreadyClickedLikeException() {
        super("You have already clicked like!");
    }
}
