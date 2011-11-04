package com.mindbadger.jukebox;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.library.Artist;
import com.mindbadger.library.MediaItem;
import com.mindbadger.library.Track;
import com.mindbadger.library.Album;
import com.mindbadger.player.AudioPlayer;
import com.mindbadger.player.IBroadcastAudioPlayerEvents;

public class Jukebox implements IBroadcastAudioPlayerEvents {
  private AudioPlayer audioPlayer;
  private MediaPlayerCache mediaPlayerCache;
  private PlaylistRandomiser playlistRandomiser;
  
  private List<Integer> playlist = new ArrayList<Integer>();
  private int currentlyPlayingIndex;
  private PlayerStatus playerStatus;
  private boolean repeat = false;
  private boolean shuffle = false;
  
  public Jukebox (MediaPlayerCache mediaPlayerCache) {
    this.mediaPlayerCache = mediaPlayerCache;
  }
  
  public void addItemToPlaylist(int mediaItemId) {
    System.out.println("Jukebox - addItemToPlaylist: " + mediaItemId);

    boolean emptyPlaylist = (playlist.size() == 0);
    
    MediaItem mediaItem = mediaPlayerCache.getIdMap().get(mediaItemId);
    
    if (mediaItem instanceof Track) {
      playlist.add(mediaItemId);
    } else if (mediaItem instanceof Album) {
      Album album = (Album) mediaItem;
      Map<Integer, Track> tracks = album.getTracks();
      List<Track> trackList = new ArrayList<Track> (tracks.values());
      //Collections.sort(trackList);
      
      for (Track track: trackList) {
        playlist.add(track.getId());
      }
    } else if (mediaItem instanceof Artist) {
      Artist artist = (Artist) mediaItem;
      
      Map<String, Album> albums = artist.getAlbums();
      List<Album> albumList = new ArrayList<Album> (albums.values());
      Collections.sort(albumList);
      for (Album album : albumList) {
        Map<Integer, Track> tracks = album.getTracks();
        List<Track> trackList = new ArrayList<Track> (tracks.values());
        
        for (Track track: trackList) {
          playlist.add(track.getId());
        }
      }
    }
    
    if (emptyPlaylist) {
      currentlyPlayingIndex = 0;
      playTrack();
    }
  }

  private void playTrack() {
    playerStatus = PlayerStatus.QUEUED;
    int trackId = getCurrentTrackId();
    Track trackToPlay = (Track) mediaPlayerCache.getIdMap().get(trackId);
    File audioFile = new File (trackToPlay.getFullyQualifiedFileName());
    audioPlayer.playAudioFile(audioFile);
  }

  public int getCurrentTrackId() {
    return playlist.get(currentlyPlayingIndex);
  }

  public List<Integer> getPlaylist() {
    return playlist;
  }

  public int getCurrentlyPlayingIndex() {
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
    currentlyPlayingIndex++;
    playTrackIfOneAvailable();
  }

  private void playTrackIfOneAvailable() {
    if (currentlyPlayingIndex < playlist.size() && currentlyPlayingIndex >= 0) {
      playTrack();
    } else {
      playerStatus = PlayerStatus.IDLE;
    }
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
  
  public void playOrPause () {
    boolean pause = (getPlayerStatus() == PlayerStatus.PLAYING);
    audioPlayer.pause(pause);
  }

  public void nextTrack() {
    audioPlayer.destroyPlayer();
    currentlyPlayingIndex++;
    playTrackIfOneAvailable();
  }

  public void previousTrack() {
    audioPlayer.destroyPlayer();
    currentlyPlayingIndex--;
    playTrackIfOneAvailable();
  }

  public void toggleShuffle() {
    shuffle = !shuffle;
    
    if (shuffle) {
      
    }
  }

  public void toggleRepeat() {
    repeat = !repeat;
  }

  public void clearPlaylist() {
    System.out.println("clearPlaylist");
  }

  public boolean isRepeat() {
    return repeat;
  }

  public void setPlaylistRandomiser(PlaylistRandomiser playlistRandomiser) {
    this.playlistRandomiser = playlistRandomiser;
  }

  public PlaylistRandomiser getPlaylistRandomiser() {
    return playlistRandomiser;
  }
}
