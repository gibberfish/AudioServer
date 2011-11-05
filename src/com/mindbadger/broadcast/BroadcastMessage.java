package com.mindbadger.broadcast;

import com.mindbadger.jukebox.PlayerStatus;

public class BroadcastMessage {
  private int currentTrackId;
  private PlayerStatus playerStatus;
  private boolean shuffle;
  private boolean repeat;
  private String artworkUrl;
  public int getCurrentTrackId() {
    return currentTrackId;
  }
  public void setCurrentTrackId(int currentTrackId) {
    this.currentTrackId = currentTrackId;
  }
  public PlayerStatus getPlayerStatus() {
    return playerStatus;
  }
  public void setPlayerStatus(PlayerStatus playerStatus) {
    this.playerStatus = playerStatus;
  }
  public boolean isShuffle() {
    return shuffle;
  }
  public void setShuffle(boolean shuffle) {
    this.shuffle = shuffle;
  }
  public boolean isRepeat() {
    return repeat;
  }
  public void setRepeat(boolean repeat) {
    this.repeat = repeat;
  }
  public void setArtworkUrl(String artworkUrl) {
    this.artworkUrl = artworkUrl;
  }
  public String getArtworkUrl() {
    return artworkUrl;
  }
}
