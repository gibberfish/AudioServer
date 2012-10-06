package com.mindbadger.jukebox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

public class PlaylistRandomiser {
  Logger logger = Logger.getLogger(PlaylistRandomiser.class);
  
  List<Integer> savedPlaylist;
  
  public List<Integer> randomise (List<Integer> playlist) {
    savedPlaylist = new ArrayList<Integer> (playlist);
    Collections.copy(savedPlaylist, playlist);
    
    List<Integer> newPlaylist = new ArrayList<Integer> ();
    int playlistSize = playlist.size();
    
    Random generator = null;
    
    
    while (playlistSize > 0) {
      int randomTime = (int) (((new Date()).getTime()) % playlistSize);
      generator = new Random(randomTime);
      int randomIndex = generator.nextInt(playlistSize);
      
      Integer trackId = playlist.get(randomIndex);
      newPlaylist.add(trackId);
      playlist.remove(randomIndex);
      
      playlistSize--;
    }
    
    return newPlaylist;
  }
  
  public List<Integer> backToOriginalState () {
    return savedPlaylist;
  }
  
  // TEST MAIN METHOD
//  public static void main(String[] args) {
//    List<Integer> myPlaylist = new ArrayList<Integer> ();
//    for (int i=1; i<31; i++) {
//      myPlaylist.add(i);
//    }
//    logger.debug(myPlaylist);
//    
//    PlaylistRandomiser rand = new PlaylistRandomiser();
//    
//    List<Integer> randomPlaylist = rand.randomise(myPlaylist);
//    logger.debug(randomPlaylist);
//    
//    List<Integer> originalPlaylist = rand.backToOriginalState();
//    logger.debug(originalPlaylist);
//  }
}
