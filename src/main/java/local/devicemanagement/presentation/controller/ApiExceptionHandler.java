package local.devicemanagement.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import local.devicemanagement.application.exception.ConcurrentUpdateException;
import local.devicemanagement.application.exception.ModificationException;
import local.devicemanagement.application.exception.NotFoundException;
import local.devicemanagement.application.exception.StateException;

import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        return makeItProblem(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({ModificationException.class, StateException.class, ConcurrentUpdateException.class})
    public ProblemDetail handleConflict(RuntimeException ex) {
        return makeItProblem(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.merge(error.getField(), error.getDefaultMessage(), (a, b) -> a + "; " + b);
        }
        ProblemDetail pd = makeItProblem(HttpStatus.BAD_REQUEST, "Request validation failed");
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex) {
        return makeItProblem(HttpStatus.BAD_REQUEST, "Malformed or missing request body");
    }

    //~

    private static ProblemDetail makeItProblem(HttpStatus status, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status.getReasonPhrase());
        return pd;
    }

}
