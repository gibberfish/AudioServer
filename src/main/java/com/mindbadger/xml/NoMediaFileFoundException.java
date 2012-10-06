package com.mindbadger.xml;

public class NoMediaFileFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NoMediaFileFoundException() {
  }

  public NoMediaFileFoundException(String message) {
    super(message);
  }

  public NoMediaFileFoundException(Throwable cause) {
    super(cause);
  }

  public NoMediaFileFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
