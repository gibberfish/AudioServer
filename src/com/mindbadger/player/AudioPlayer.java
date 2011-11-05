package com.mindbadger.player;

import java.io.File;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;

public class AudioPlayer implements IPlayAudio, ControllerListener {
  private Player player;
  private JavaxPlayerFactory factory;
  private IBroadcastAudioPlayerEvents broadcaster;
  
  public AudioPlayer () {
    
  }
  
  public AudioPlayer (JavaxPlayerFactory factory) {
    this.factory = factory;
  }
  
  @Override
  public void playAudioFile(File audioFile) {
    player = factory.getNewPlayer(audioFile);
    player.addControllerListener(this);
    
    player.start();
  }

  @Override
  public void pause(boolean pause) {
    if (pause)
      player.stop();
    else
      player.start();
  }

  @Override
  public void controllerUpdate(ControllerEvent event) {
    if (event instanceof javax.media.EndOfMediaEvent) {
      destroyPlayer();
    } else if (event instanceof javax.media.StopByRequestEvent) {
      broadcaster.songPaused();
    } else if (event instanceof javax.media.StartEvent) {
      broadcaster.songStarted();
    }
  }

  public void setBroadcaster(IBroadcastAudioPlayerEvents broadcaster) {
    this.broadcaster = broadcaster;
  }

  public IBroadcastAudioPlayerEvents getBroadcaster() {
    return broadcaster;
  }

  @Override
  public void destroyPlayer() {
    player.stop();
    player.close();
    player.deallocate();
  }
}
