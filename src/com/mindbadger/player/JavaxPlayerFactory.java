package com.mindbadger.player;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

public class JavaxPlayerFactory {
  public Player getNewPlayer (File audioFile) {
    try {
      return Manager.createRealizedPlayer(audioFile.toURI().toURL());
    } catch (NoPlayerException e) {
      throw new AudioPlayerException(e);
    } catch (CannotRealizeException e) {
      throw new AudioPlayerException(e);
    } catch (MalformedURLException e) {
      throw new AudioPlayerException(e);
    } catch (IOException e) {
      throw new AudioPlayerException(e);
    }
  }
}
