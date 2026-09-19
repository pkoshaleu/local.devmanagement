package local.devicemanagement.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import local.devicemanagement.application.exception.IllicitStateException;
import local.devicemanagement.application.exception.NotFoundException;


@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        return makeItProblem(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllicitStateException.class)
    public ProblemDetail handleConflict(IllicitStateException ex) {
        return makeItProblem(HttpStatus.CONFLICT, ex.getMessage());
    }

    //~

    private static ProblemDetail makeItProblem(HttpStatus status, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status.getReasonPhrase());
        return pd;
    }

}
