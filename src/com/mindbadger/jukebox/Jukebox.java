package com.mindbadger.jukebox;

import com.mindbadger.player.AudioPlayer;

public class Jukebox {
  private AudioPlayer audioPlayer;
  
  public Jukebox (AudioPlayer audioServer) {
    this.audioPlayer = audioServer;
  }
  
  public void addItemToPlaylist(int parseInt) {
    System.out.println("Jukebox - addItemToPlaylist: " + parseInt);
  }
}
