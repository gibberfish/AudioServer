package com.mindbadger.jukebox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.library.MediaItem;
import com.mindbadger.library.Track;
import com.mindbadger.player.AudioPlayer;
import com.mindbadger.player.IBroadcastAudioPlayerEvents;

public class Jukebox implements IBroadcastAudioPlayerEvents {
  private AudioPlayer audioPlayer;
  private MediaPlayerCache mediaPlayerCache;
  
  private List<Integer> playlist = new ArrayList<Integer>();
  private int currentlyPlayingIndex;
  private PlayerStatus playerStatus;
  
  public Jukebox (MediaPlayerCache mediaPlayerCache) {
    this.mediaPlayerCache = mediaPlayerCache;
  }
  
  public void addItemToPlaylist(int mediaItemId) {
    System.out.println("Jukebox - addItemToPlaylist: " + mediaItemId);

    boolean emptyPlaylist = (playlist.size() == 0);
    
    MediaItem mediaItem = mediaPlayerCache.getIdMap().get(mediaItemId);
    
    if (mediaItem instanceof Track) {
      playlist.add(mediaItemId);
    }
    
    if (emptyPlaylist) {
      currentlyPlayingIndex = 0;
      playerStatus = PlayerStatus.QUEUED;
      int trackId = playlist.get(currentlyPlayingIndex);
      Track trackToPlay = (Track) mediaPlayerCache.getIdMap().get(trackId);
      File audioFile = new File (trackToPlay.getFullyQualifiedFileName());
      audioPlayer.playAudioFile(audioFile);
    }
  }

  public List<Integer> getPlaylist() {
    return playlist;
  }

  public Object getCurrentlyPlayingIndex() {
    return currentlyPlayingIndex;
  }

  @Override
  public void songStarted() {
    playerStatus = PlayerStatus.PLAYING;
  }

  @Override
  public void songPaused() {
    playerStatus = PlayerStatus.PAUSED;
  }

  @Override
  public void songEnded() {
    // TODO Auto-generated method stub
    
  }

  public PlayerStatus getPlayerStatus() {
    return playerStatus;
  }

  public AudioPlayer getAudioPlayer() {
    return audioPlayer;
  }

  public void setAudioPlayer(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }
}
