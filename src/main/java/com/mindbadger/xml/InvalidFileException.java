package com.mindbadger.xml;

public class InvalidFileException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidFileException() {
    super();
  }

  public InvalidFileException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidFileException(String message) {
    super(message);
  }

  public InvalidFileException(Throwable cause) {
    super(cause);
  }
}
