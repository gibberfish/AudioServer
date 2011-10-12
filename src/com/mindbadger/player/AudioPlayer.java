package com.mindbadger.player;

import java.io.File;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;

public class AudioPlayer implements IPlayAudio, ControllerListener {
  private Player player;
  private JavaxPlayerFactory factory;
  private IBroadcastAudioPlayerEvents broadcaster;
  
  public AudioPlayer (JavaxPlayerFactory factory, IBroadcastAudioPlayerEvents broadcaster) {
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
      player.close();
      //broadcaster.songEnded();
    }
    
    System.out.println("Event: " + event);
  }
  
//  public static void main(String[] args) throws InterruptedException {
//    String url = "C:\\Music\\Texas\\White on Blonde\\12 Ticket to Lie.mp3";
//    File file = new File (url);
//    JavaxPlayerFactory factory = new JavaxPlayerFactory ();
//    AudioPlayer player = new AudioPlayer (factory);
//    player.playAudioFile(file);
//    
//    Thread.sleep(10000);
//    
//    player.pause(true);
//    
//    Thread.sleep(1000);
//    
//    player.pause(false);
//  }
}
