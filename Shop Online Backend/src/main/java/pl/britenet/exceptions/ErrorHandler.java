package pl.britenet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage InvalidCredentialsException(InvalidCredentialsException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                !ex.getMessage().equals("") ? ex.getMessage() : "Wrong username or password",
                request.getDescription(false));
    }

    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage EmailTakenException(EmailTakenException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                !ex.getMessage().equals("") ? ex.getMessage() : "Email already taken",
                request.getDescription(false));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage ResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage ServerExceptionHandler(Exception ex, WebRequest request) {
        System.out.println(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage UnauthorizedExceptionHandler(Exception ex, WebRequest request) {
        System.out.println(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                "Unauthorized",
                request.getDescription(false));
    }

//    @ExceptionHandler(InvalidNameException.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorMessage invalidNameException(InvalidNameException ex, WebRequest request) {
//        return new ErrorMessage(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                new Date(),
//                !ex.getMessage().equals("") ? ex.getMessage() : "Name should have at least 3 letters and should contain only letters!",
//                request.getDescription(false));
//    }
//
//    @ExceptionHandler(InvalidSurnameException.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorMessage invalidSurnameException(InvalidSurnameException ex, WebRequest request) {
//        return new ErrorMessage(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                new Date(),
//                !ex.getMessage().equals("") ? ex.getMessage() : "Surname should have at least 5 letters and should contain only letters!",
//                request.getDescription(false));
//    }
//
//    @ExceptionHandler(InvalidEmailException.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorMessage invalidEmailException(InvalidEmailException ex, WebRequest request) {
//        return new ErrorMessage(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                new Date(),
//                !ex.getMessage().equals("") ? ex.getMessage() : "Invalid email format!",
//                request.getDescription(false));
//    }
//
//    @ExceptionHandler(InvalidPasswordException.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorMessage invalidPasswordException(InvalidPasswordException ex, WebRequest request) {
//        return new ErrorMessage(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                new Date(),
//                !ex.getMessage().equals("") ? ex.getMessage() : "Password should have at least 8 characters!",
//                request.getDescription(false));
//    }
}
