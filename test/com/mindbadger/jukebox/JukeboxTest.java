package com.mindbadger.jukebox;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.Librarian;
import com.mindbadger.library.MediaItem;
import com.mindbadger.library.Track;
import com.mindbadger.player.AudioPlayer;

public class JukeboxTest {

  private Jukebox jukebox;
  
  @Mock AudioPlayer mockAudioPlayer;
  @Mock MediaPlayerCache mockMediaPlayerCache;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    
    jukebox = new Jukebox (mockMediaPlayerCache);
    jukebox.setAudioPlayer(mockAudioPlayer);
  }

  @Test
  public void testAddingATrackToAnEmptyPlaylistCausesThePlayerToStart () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    Track track = new Track ();
    track.setFullyQualifiedFileName("C:\\location\\myfile.mp3");
    map.put(3, track);
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    
    // When
    jukebox.addItemToPlaylist(3);
    
    // Then
    List<Integer> playlist = jukebox.getPlaylist ();
    
    assertEquals (1, playlist.size());
    assertEquals (new Integer(3), playlist.get(0));
    assertEquals (0, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.QUEUED, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer).playAudioFile((File) anyObject());
  }
  
  @Test
  public void testAddingAnAlbum () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Album album = new Album ();
    Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
    album.setTracks(tracks);
    
    Track track1 = new Track ();
    track1.setFullyQualifiedFileName("C:\\location\\myfile.mp3");
    track1.setId(3);
    tracks.put(3, track1);

    Track track2 = new Track ();
    track2.setFullyQualifiedFileName("C:\\location\\myfile2.mp3");
    track2.setId(4);
    tracks.put(4, track2);

    Track track3 = new Track ();
    track3.setFullyQualifiedFileName("C:\\location\\myfile3.mp3");
    track3.setId(5);
    tracks.put(5, track3);
    
    
    map.put(2, album);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    
    // When
    jukebox.addItemToPlaylist(2);
    
    // Then
    List<Integer> playlist = jukebox.getPlaylist ();
    
    assertEquals (3, playlist.size());
    assertEquals (new Integer(3), playlist.get(0));
    assertEquals (new Integer(4), playlist.get(1));
    assertEquals (new Integer(5), playlist.get(2));
    
    assertEquals (0, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.QUEUED, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer).playAudioFile((File) anyObject());
  }
  
  @Test
  public void testAddingAnArtist () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = new Artist ();
    Map<String, Album> albums = new HashMap<String, Album> ();
    artist.setAlbums(albums);
    
    
    Album album1 = new Album ();
    Map<Integer, Track> tracks1 = new HashMap<Integer, Track> ();
    album1.setTracks(tracks1);
    album1.setName("Album1");
    albums.put("Album1", album1);
    
    Track track1 = new Track ();
    track1.setFullyQualifiedFileName("C:\\location\\myfile.mp3");
    track1.setId(3);
    tracks1.put(3, track1);

    Track track2 = new Track ();
    track2.setFullyQualifiedFileName("C:\\location\\myfile2.mp3");
    track2.setId(4);
    tracks1.put(4, track2);

    Track track3 = new Track ();
    track3.setFullyQualifiedFileName("C:\\location\\myfile3.mp3");
    track3.setId(5);
    tracks1.put(5, track3);

    
    Album album2 = new Album ();
    Map<Integer, Track> tracks2 = new HashMap<Integer, Track> ();
    album2.setTracks(tracks2);
    album2.setName("Album2");
    albums.put("Album2", album2);
    
    Track track4 = new Track ();
    track4.setFullyQualifiedFileName("C:\\location\\myfile4.mp3");
    track4.setId(7);
    tracks2.put(7, track4);

    
    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    map.put(6, album2);
    map.put(7, track4);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    
    // When
    jukebox.addItemToPlaylist(1);
    
    // Then
    List<Integer> playlist = jukebox.getPlaylist ();
    
    assertEquals (4, playlist.size());
    assertEquals (new Integer(3), playlist.get(0));
    assertEquals (new Integer(4), playlist.get(1));
    assertEquals (new Integer(5), playlist.get(2));
    assertEquals (new Integer(7), playlist.get(3));
    
    assertEquals (0, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.QUEUED, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer).playAudioFile((File) anyObject());
  }
  
  @Test
  public void shouldSetTheStatusToPlayingWhenTheJukeboxReceivesAPlayingMessage () {
    // Given
    
    // When
    jukebox.songStarted();
    
    // Then
    assertEquals(PlayerStatus.PLAYING, jukebox.getPlayerStatus());
  }
  
  @Test
  public void shouldSetTheStatusToPausedWhenTheJukeboxReceivesAPausedMessage () {
    // Given
    
    // When
    jukebox.songPaused();
    
    // Then
    assertEquals(PlayerStatus.PAUSED, jukebox.getPlayerStatus());
  }

  @Test
  public void shouldMoveToTheNextTrackWhenTheJukeboxReceivesASongEndedMessage () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Album album = new Album ();
    Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
    album.setTracks(tracks);
    
    Track track1 = new Track ();
    track1.setFullyQualifiedFileName("C:\\location\\myfile.mp3");
    track1.setId(3);
    tracks.put(3, track1);

    Track track2 = new Track ();
    track2.setFullyQualifiedFileName("C:\\location\\myfile2.mp3");
    track2.setId(4);
    tracks.put(4, track2);

    Track track3 = new Track ();
    track3.setFullyQualifiedFileName("C:\\location\\myfile3.mp3");
    track3.setId(5);
    tracks.put(5, track3);
    
    
    map.put(2, album);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(2);
    
    // When
    jukebox.songEnded();
    
    // Then
    assertEquals (1, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.QUEUED, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer, times(2)).playAudioFile((File) anyObject());
  }

  @Test
  public void shouldSetTheStatusToIdleWhenTheJukeboxReceivesASongEndedMessageAndThereAreNoMoreTracks () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Track track1 = new Track ();
    track1.setFullyQualifiedFileName("C:\\location\\myfile.mp3");
    track1.setId(3);
    
    map.put(3, track1);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(3);
    
    // When
    jukebox.songEnded();
    
    // Then
    assertEquals (1, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.IDLE, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer, times(1)).playAudioFile((File) anyObject());
  }

  @Test
  public void shouldPauseAPlayingTrack () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Track track1 = new Track ();
    track1.setFullyQualifiedFileName("C:\\location\\myfile.mp3");
    track1.setId(3);
    
    map.put(3, track1);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(3);
    jukebox.songStarted();
    
    // When
    jukebox.playOrPause();
    
    // Then
    assertEquals (0, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.PLAYING, jukebox.getPlayerStatus ());
    verify(mockAudioPlayer).pause(true);
  }

  @Test
  public void shouldPlayAPausedTrack () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Track track1 = new Track ();
    track1.setFullyQualifiedFileName("C:\\location\\myfile.mp3");
    track1.setId(3);
    
    map.put(3, track1);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(3);
    jukebox.songStarted();
    jukebox.playOrPause();
    jukebox.songPaused();
    
    // When
    jukebox.playOrPause();
    
    // Then
    assertEquals (0, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.PAUSED, jukebox.getPlayerStatus ());
    verify(mockAudioPlayer).pause(false);
  }
}
