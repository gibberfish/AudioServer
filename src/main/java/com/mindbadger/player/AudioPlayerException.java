package com.mindbadger.player;

public class AudioPlayerException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AudioPlayerException() {
  }

  public AudioPlayerException(String message) {
    super(message);
  }

  public AudioPlayerException(Throwable cause) {
    super(cause);
  }

  public AudioPlayerException(String message, Throwable cause) {
    super(message, cause);
  }

}
