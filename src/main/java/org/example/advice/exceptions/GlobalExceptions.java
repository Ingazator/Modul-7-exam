package org.example.advice.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler(UserAlreadyExists.class)
    public String email(Model model,UserAlreadyExists exists){
        model.addAttribute("emailError",exists.getMessage());
        return "exceptions";
    }
    @ExceptionHandler(AlreadyClickedLikeException.class)
    public String like(Model model,AlreadyClickedLikeException exception){
        model.addAttribute("likeError",exception.getMessage());
        return "exceptions";
    }
}
