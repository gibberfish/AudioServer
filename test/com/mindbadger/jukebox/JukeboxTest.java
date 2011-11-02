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
    
  }
  
  @Test
  public void testAddingAnArtist () {
    
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
    
  }

  @Test
  public void shouldSetTheStatusToIdleWhenTheJukeboxReceivesASongEndedMessageAndThereAreNoMoreTracks () {
    
  }

}
