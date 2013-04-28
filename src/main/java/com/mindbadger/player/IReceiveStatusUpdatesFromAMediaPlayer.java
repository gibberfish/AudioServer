package com.mindbadger.player;

public interface IReceiveStatusUpdatesFromAMediaPlayer {
  public void songStarted();
  public void songPaused();
  public void songEnded();
}
