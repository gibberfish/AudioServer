package com.mindbadger.jukebox;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mindbadger.broadcast.StatusBroadcaster;
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
  private StatusBroadcaster statusBroadcaster;
  
  private List<Integer> playlist = new ArrayList<Integer>();
  private int currentlyPlayingIndex;
  private PlayerStatus playerStatus;
  private boolean repeat = false;
  private boolean shuffle = false;
  
  public Jukebox (MediaPlayerCache mediaPlayerCache) {
    this.mediaPlayerCache = mediaPlayerCache;
  }
  
  @Override
  public void songStarted() {
    playerStatus = PlayerStatus.PLAYING;
    broadcastStatus();
  }
  
  @Override
  public void songPaused() {
    playerStatus = PlayerStatus.PAUSED;
    broadcastStatus();
  }
  
  @Override
  public void songEnded() {
    currentlyPlayingIndex++;
    playTrackIfOneAvailable();
    broadcastStatus();
  }
  
  public void addItemToPlaylist(int mediaItemId) {
    System.out.println("Jukebox - addItemToPlaylist: " + mediaItemId);

    boolean emptyPlaylist = (playlist.size() == 0);
    
    MediaItem mediaItem = mediaPlayerCache.getIdMap().get(mediaItemId);
    
    if (mediaItem instanceof Track) {
      addTrackWithIdToPlaylist(mediaItemId);
    } else if (mediaItem instanceof Album) {
      Album album = (Album) mediaItem;
      addAlbumToPlaylist(album);
    } else if (mediaItem instanceof Artist) {
      Artist artist = (Artist) mediaItem;
      addArtistToPlaylist(artist);
    }
    
    if (emptyPlaylist) {
      currentlyPlayingIndex = 0;
      playTrack();
    }
    
    System.out.println("Jukebox - current playlist: " + playlist);
  }
  
  public void playOrPause () {
    boolean pause = (getPlayerStatus() == PlayerStatus.PLAYING);
    audioPlayer.pause(pause);
  }

  public void nextTrack() {
    currentlyPlayingIndex++;
    playTrackIfOneAvailable();
  }

  public void previousTrack() {
    currentlyPlayingIndex--;
    playTrackIfOneAvailable();
  }

  public void toggleShuffle() {
    shuffle = !shuffle;
    
    if (shuffle) {
      playlist = playlistRandomiser.randomise(playlist);
    } else {
      playlist = playlistRandomiser.backToOriginalState();
    }
    playTrackIfOneAvailable();
  }

  public void toggleRepeat() {
    repeat = !repeat;
    broadcastStatus();
  }

  public void clearPlaylist() {
    System.out.println("clearPlaylist");
  }

  public boolean isRepeat() {
    return repeat;
  }

  public boolean isShuffle() {
    return shuffle;
  }
  
  public void setPlaylistRandomiser(PlaylistRandomiser playlistRandomiser) {
    this.playlistRandomiser = playlistRandomiser;
  }

  public PlaylistRandomiser getPlaylistRandomiser() {
    return playlistRandomiser;
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

  public PlayerStatus getPlayerStatus() {
    return playerStatus;
  }

  public AudioPlayer getAudioPlayer() {
    return audioPlayer;
  }

  public void setAudioPlayer(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }
  
  private void playTrackIfOneAvailable() {
    audioPlayer.destroyPlayer();
    
    if (currentlyPlayingIndex < playlist.size() && currentlyPlayingIndex >= 0) {
      playTrack();
    } else {
      playerStatus = PlayerStatus.IDLE;
    }
  }

  private void addTrackWithIdToPlaylist(int mediaItemId) {
    playlist.add(mediaItemId);
  }
  
  private void addAlbumToPlaylist(Album album) {
    Map<Integer, Track> tracks = album.getTracks();
    List<Track> trackList = new ArrayList<Track> (tracks.values());
    //Collections.sort(trackList);
    
    for (Track track: trackList) {
      addTrackWithIdToPlaylist(track.getId());
    }
  }
  
  private void addArtistToPlaylist(Artist artist) {
    Map<String, Album> albums = artist.getAlbums();
    List<Album> albumList = new ArrayList<Album> (albums.values());
    Collections.sort(albumList);
    for (Album album : albumList) {
      addAlbumToPlaylist(album);
    }
  }
  
  private void playTrack() {
    playerStatus = PlayerStatus.QUEUED;
    int trackId = getCurrentTrackId();
    Track trackToPlay = (Track) mediaPlayerCache.getIdMap().get(trackId);
    File audioFile = new File (trackToPlay.getFullyQualifiedFileName());
    audioPlayer.playAudioFile(audioFile);
  }

  public void setStatusBroadcaster(StatusBroadcaster statusBroadcaster) {
    this.statusBroadcaster = statusBroadcaster;
  }

  public StatusBroadcaster getStatusBroadcaster() {
    return statusBroadcaster;
  }
  
  public void broadcastStatus () {
    String artworkUrl = "";
    statusBroadcaster.broadcast(playerStatus.toString(), getCurrentTrackId(), repeat, shuffle, artworkUrl);
  }

  public String getArtworkForTrack(int trackId) {
    String artworkUrl = null;
    System.out.println("...getArtworkForTrack " + trackId);
    
    Track trackToPlay = (Track) mediaPlayerCache.getIdMap().get(trackId);
    
    if (trackToPlay != null) {
      String trackFileName = trackToPlay.getFullyQualifiedFileName();
      int lastSlash = trackFileName.lastIndexOf("\\");
      artworkUrl = trackFileName.substring(0, lastSlash) + "\\AlbumArtSmall.jpg";
      System.out.println("Artwork URL: " + artworkUrl);
    }
    return artworkUrl;
  }
}
