package com.mindbadger.jukebox;

public enum PlayerStatus {
  PLAYING("Playing"), PAUSED("Paused"), IDLE("Idle"), QUEUED("Queued");
  private String statusText;
  private PlayerStatus (String statusText) {
    this.statusText = statusText;
  }
  public String toString () {
    return statusText;
  }
}
