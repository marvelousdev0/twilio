package com.vamshimaddur.spring.twilio.lookup;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LookupExceptionHandler {

  @ExceptionHandler(LookupParameterException.class)
  public ResponseEntity<String> handleMissingParameterException(LookupParameterException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
