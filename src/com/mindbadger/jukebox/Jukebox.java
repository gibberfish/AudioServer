package com.mindbadger.jukebox;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mindbadger.broadcast.StatusBroadcaster;
import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.library.Artist;
import com.mindbadger.library.MediaItem;
import com.mindbadger.library.Track;
import com.mindbadger.library.Album;
import com.mindbadger.player.AudioPlayer;
import com.mindbadger.player.IBroadcastAudioPlayerEvents;

public class Jukebox implements IBroadcastAudioPlayerEvents {
  Logger logger = Logger.getLogger(Jukebox.class);
  
  static final int START_OF_PLAYLIST = -1;
  static final int END_OF_PLAYLIST = -2;
  static final int NO_PLAYLIST = -3;
  
  private AudioPlayer audioPlayer;
  private MediaPlayerCache mediaPlayerCache;
  private PlaylistRandomiser playlistRandomiser;
  private StatusBroadcaster statusBroadcaster;
  
  private List<Integer> playlist = new ArrayList<Integer>();
  private int currentlyPlayingIndex = NO_PLAYLIST;
  private PlayerStatus playerStatus = PlayerStatus.IDLE;
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
    if (currentlyPlayingIndex > playlist.size()) {
      currentlyPlayingIndex = END_OF_PLAYLIST;
    }
    playTrackIfOneAvailable();
    broadcastStatus();
  }
  
  public void addItemToPlaylist(int mediaItemId) {
    logger.debug("Jukebox - addItemToPlaylist: " + mediaItemId);

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
    
    logger.debug("Jukebox - current playlist: " + playlist);
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
    logger.debug("clearPlaylist");
    currentlyPlayingIndex = NO_PLAYLIST;
    playlist.clear();
    audioPlayer.destroyPlayer();
    broadcastStatus();
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
    return (currentlyPlayingIndex < 0 ? currentlyPlayingIndex : playlist.get(currentlyPlayingIndex));
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
    
    if (currentlyPlayingIndex < 0) {
      playerStatus = PlayerStatus.IDLE;
      currentlyPlayingIndex = START_OF_PLAYLIST;
    } else if (currentlyPlayingIndex >= playlist.size()) {
      playerStatus = PlayerStatus.IDLE;
      currentlyPlayingIndex = END_OF_PLAYLIST;
    } else {
      playTrack();
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
    
    statusBroadcaster.broadcast(playerStatus.toString(), getCurrentTrackId(), repeat, shuffle, "");
  }

  public String getArtworkForTrack(int trackId) {
    String artworkUrl = null;
    logger.debug("...getArtworkForTrack " + trackId);
    
    Track trackToPlay = (Track) mediaPlayerCache.getIdMap().get(trackId);
    
    if (trackToPlay != null) {
      String trackFileName = trackToPlay.getFullyQualifiedFileName();
      int lastSlash = trackFileName.lastIndexOf("\\");
      artworkUrl = trackFileName.substring(0, lastSlash) + "\\AlbumArtSmall.jpg";
      logger.debug("Artwork URL: " + artworkUrl);
    }
    return artworkUrl;
  }
}
