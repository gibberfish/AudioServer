package com.mindbadger.player;

import java.io.File;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;

import org.apache.log4j.Logger;

public class AudioPlayer implements IPlayAudio, ControllerListener {
  Logger logger = Logger.getLogger(AudioPlayer.class);
  
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
    
    logger.debug("AUDIO PLAYER: about to start " + player + " to play " + audioFile);
    player.start();
  }

  @Override
  public void pause(boolean pause) {
    if (pause) {
      logger.debug("AUDIO PLAYER: about to pause player");
      player.stop();
    } else {
      logger.debug("AUDIO PLAYER: about to restart player");
      player.start();
    }
  }

  @Override
  public void controllerUpdate(ControllerEvent event) {
    logger.debug("*** AUDIO PLAYER EVENT: receiving audio player event: " + event);
    
    if (event instanceof javax.media.EndOfMediaEvent) {
      broadcaster.songEnded();
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
    logger.debug("AUDIO PLAYER: about to destroy player " + player);
    player.close();
    player.deallocate();
  }
}
