package com.mindbadger.jukebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindbadger.broadcast.BroadcastMessage;
import com.mindbadger.broadcast.StatusBroadcaster;
import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.MediaItem;
import com.mindbadger.library.Track;
import com.mindbadger.player.AudioPlayer;

public class JukeboxTest {

  private Jukebox jukebox;
  
  @Mock AudioPlayer mockAudioPlayer;
  @Mock MediaPlayerCache mockMediaPlayerCache;
  @Mock PlaylistRandomiser mockPlaylistRandomiser;
  @Mock StatusBroadcaster mockStatusBroadcaster;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    
    jukebox = new Jukebox (mockMediaPlayerCache);
    jukebox.setAudioPlayer(mockAudioPlayer);
    jukebox.setPlaylistRandomiser(mockPlaylistRandomiser);
    jukebox.setStatusBroadcaster(mockStatusBroadcaster);
  }

  @Test
  public void testAddingATrackToAnEmptyPlaylistCausesThePlayerToStart () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album);
    
    map.put(1, artist);
    map.put(2, album);
    map.put(3, track1);
    
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
    
    Artist artist = newArtist(1);
    Album album = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album);
    Track track2 = addTrackToAlbum(4, album);
    Track track3 = addTrackToAlbum(5, album);    
    
    map.put(1, artist);
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
    
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);    
    Album album2 = addAlbumToArtist(6, artist);
    Track track4 = addTrackToAlbum(7, album2);
    
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
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album);
    
    map.put(1, artist);
    map.put(2, album);
    map.put(3, track1);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(3);
    
    // When
    jukebox.songStarted();
    
    // Then
    assertEquals(PlayerStatus.PLAYING, jukebox.getPlayerStatus());
    //verify(mockStatusBroadcaster).broadcast((BroadcastMessage) anyObject());
  }
  
  @Test
  public void shouldSetTheStatusToPausedWhenTheJukeboxReceivesAPausedMessage () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album);
    
    map.put(1, artist);
    map.put(2, album);
    map.put(3, track1);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(3);
    
    // When
    jukebox.songPaused();
    
    // Then
    assertEquals(PlayerStatus.PAUSED, jukebox.getPlayerStatus());
  }

  @Test
  public void shouldMoveToTheNextTrackWhenTheJukeboxReceivesASongEndedMessage () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();

    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
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
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(3);
    
    // When
    jukebox.songEnded();
    
    // Then
    assertEquals (Jukebox.END_OF_PLAYLIST, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.IDLE, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer, times(1)).playAudioFile((File) anyObject());
  }

  @Test
  public void shouldPauseAPlayingTrack () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
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
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
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

  @Test
  public void shouldMoveToNextTrackWhenNextTrackCalledIfOneAvailable () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(2);
    jukebox.songStarted();
    
    // When
    jukebox.nextTrack();
    
    // Then
    assertEquals (1, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.QUEUED, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer, times(2)).playAudioFile((File) anyObject());
    verify(mockAudioPlayer).stopPlayingAudioFile();
  }

  @Test
  public void shouldFinishWhenNextTrackCalledIfNoMoreAreAvailable () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(2);
    jukebox.songStarted();
    
    // When
    jukebox.nextTrack();
    
    // Then
    assertEquals (Jukebox.END_OF_PLAYLIST, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.IDLE, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer, times(1)).playAudioFile((File) anyObject());
    verify(mockAudioPlayer).stopPlayingAudioFile();
  }

  @Test
  public void shouldMoveToPreviousTrackWhenPreviousTrackCalledIfOneAvailable () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(2);
    jukebox.songStarted();
    jukebox.nextTrack();
    jukebox.songStarted();
    
    // When
    jukebox.previousTrack();
    
    // Then
    assertEquals (0, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.QUEUED, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer, times(3)).playAudioFile((File) anyObject());
    verify(mockAudioPlayer, times(2)).stopPlayingAudioFile();
  }

  @Test
  public void shouldFinishWhenPreviousTrackCalledIfNoMoreAreAvailable () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(2);
    jukebox.songStarted();
    
    // When
    jukebox.previousTrack();
    
    // Then
    assertEquals (-1, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.IDLE, jukebox.getPlayerStatus ());
    
    verify(mockAudioPlayer, times(1)).playAudioFile((File) anyObject());
    verify(mockAudioPlayer, times(1)).stopPlayingAudioFile();
  }

  @Test
  public void shouldToggleRepeat () {
    // Given
    assertEquals(false, jukebox.isRepeat());
    
    // When
    jukebox.toggleRepeat();
    
    // Then
    assertEquals(true, jukebox.isRepeat());
    
    // When
    jukebox.toggleRepeat();
    
    // Then
    assertEquals(false, jukebox.isRepeat());
  }

  @Test
  public void shouldRandomiseThePlaylistWhenShuffleSelected () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(2);
    List<Integer> originalPlaylist = jukebox.getPlaylist();
    
    List<Integer> mixedUpPlaylist = new ArrayList<Integer> ();
    mixedUpPlaylist.add(4);
    mixedUpPlaylist.add(3);
    mixedUpPlaylist.add(5);

    when(mockPlaylistRandomiser.randomise(originalPlaylist)).thenReturn(mixedUpPlaylist);
    assertEquals (3, jukebox.getCurrentTrackId());
    
    // When
    jukebox.toggleShuffle();
    
    // Then
    assertEquals (0, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.QUEUED, jukebox.getPlayerStatus ());
    assertEquals (4, jukebox.getCurrentTrackId());
    assertEquals (true, jukebox.isShuffle());
    
    verify(mockAudioPlayer, times(2)).playAudioFile((File) anyObject());
    verify(mockAudioPlayer, times(1)).stopPlayingAudioFile();
    verify(mockPlaylistRandomiser).randomise(originalPlaylist);
  }

  @Test
  public void shouldGoBackToOriginalPlaylistWhenShuffleDeselected () {
    // Given
    Map<Integer, MediaItem> map = new HashMap<Integer, MediaItem> ();
    
    Artist artist = newArtist(1);
    Album album1 = addAlbumToArtist(2, artist);
    Track track1 = addTrackToAlbum(3, album1);
    Track track2 = addTrackToAlbum(4, album1);
    Track track3 = addTrackToAlbum(5, album1);

    map.put(1, artist);
    map.put(2, album1);
    map.put(3, track1);
    map.put(4, track2);
    map.put(5, track3);
    
    when(mockMediaPlayerCache.getIdMap()).thenReturn(map);
    jukebox.addItemToPlaylist(2);
    List<Integer> originalPlaylist = jukebox.getPlaylist();
    
    List<Integer> mixedUpPlaylist = new ArrayList<Integer> ();
    mixedUpPlaylist.add(4);
    mixedUpPlaylist.add(3);
    mixedUpPlaylist.add(5);

    when(mockPlaylistRandomiser.randomise(originalPlaylist)).thenReturn(mixedUpPlaylist);
    when(mockPlaylistRandomiser.backToOriginalState()).thenReturn(originalPlaylist);
    jukebox.toggleShuffle();
    assertEquals (4, jukebox.getCurrentTrackId());
    
    // When
    jukebox.toggleShuffle();
    
    // Then
    assertEquals (0, jukebox.getCurrentlyPlayingIndex());
    assertEquals (PlayerStatus.QUEUED, jukebox.getPlayerStatus ());
    assertEquals (3, jukebox.getCurrentTrackId());
    assertEquals (false, jukebox.isShuffle());
    
    verify(mockAudioPlayer, times(3)).playAudioFile((File) anyObject());
    verify(mockAudioPlayer, times(2)).stopPlayingAudioFile();
    verify(mockPlaylistRandomiser).backToOriginalState();
  }

  private Artist newArtist (int id) {
    Artist artist = new Artist ();
    Map<String, Album> albums = new HashMap<String, Album> ();
    artist.setAlbums(albums);
    artist.setId(id);
    artist.setName("Artist"+id);
    return artist;
  }
  
  private Album addAlbumToArtist (int id, Artist artist) {
    Album album = new Album ();
    Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
    album.setTracks(tracks);
    album.setId(id);
    album.setName("Album"+id);
    artist.getAlbums().put("Album"+id, album);
    return album;
  }
  
  private Track addTrackToAlbum(int id, Album album) {
    Track track = new Track ();
    track.setFullyQualifiedFileName("C:\\location\\myfile"+id+".mp3");
    track.setName("Track"+id);
    track.setId(id);
    Map<Integer, Track> tracks = album.getTracks();
    int seq = (tracks.size() + 1);
    track.setTrackNumber(seq);
    tracks.put(seq, track);
    return track;
  }
}
