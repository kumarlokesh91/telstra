package com.telstra.codechallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class UrlNulloRNotFoundException extends Exception  {

  private static final long serialVersionUID = 1L;

  public UrlNulloRNotFoundException(String message){
    super(message);
  }
}
