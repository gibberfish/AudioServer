package com.mindbadger.player;

import java.io.File;

public interface IPlayAudio {
  public void playAudioFile (File audioFile);
  
  public void pause (boolean pause);
}
