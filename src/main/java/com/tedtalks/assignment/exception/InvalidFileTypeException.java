package com.tedtalks.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidFileTypeException extends RuntimeException {

    public InvalidFileTypeException(String message) {
        super(message);
    }

}
