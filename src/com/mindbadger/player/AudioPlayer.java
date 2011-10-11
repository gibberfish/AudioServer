package com.mindbadger.player;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.CannotRealizeException;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

public class AudioPlayer implements IPlayAudio {
  private Player player;
  private JavaxPlayerFactory factory;
  
  @Override
  public void playAudioFile(String fullyQualifiedAudioFileName) {
    File audioFile = new File(fullyQualifiedAudioFileName);
    player = factory.getNewPlayer(audioFile);
    player.addControllerListener(new ControllerListener () {
      @Override
      public void controllerUpdate(ControllerEvent event) {
        if (event instanceof javax.media.EndOfMediaEvent) {
          player.close();
        }
        
        System.out.println("Event: " + event);
      }
    });
    player.start();
  }

  @Override
  public void pause(boolean pause) {
    if (pause)
      player.stop();
    else
      player.start();
  }
  
  public static void main(String[] args) throws InterruptedException {
    String url = "C:\\Music\\Texas\\White on Blonde\\12 Ticket to Lie.mp3";
    AudioPlayer player = new AudioPlayer ();
    player.playAudioFile(url);
    
    Thread.sleep(10000);
    
    player.pause(true);
    
    Thread.sleep(1000);
    
    player.pause(false);
  }

  public void setFactory(JavaxPlayerFactory factory) {
    this.factory = factory;
  }

  public JavaxPlayerFactory getFactory() {
    return factory;
  }
}
