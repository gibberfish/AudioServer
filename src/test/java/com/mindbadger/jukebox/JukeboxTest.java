package com.mindbadger.jukebox;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindbadger.broadcast.StatusBroadcaster;
import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.Track;
import com.mindbadger.player.AudioPlayer;
import com.mindbadger.player.PlayerStatus;

public class JukeboxTest {

	private static final String ART_FILE = "C:\\myfolder\\albumArtFile";

	private Jukebox jukebox;

	@Mock
	AudioPlayer mockAudioPlayer;
	@Mock
	MediaPlayerCache mockMediaPlayerCache;
	@Mock
	PlayList mockPlayList;
	@Mock
	StatusBroadcaster mockStatusBroadcaster;
	@Mock
	Track mockTrack1;
	@Mock
	Track mockTrack2;
	@Mock
	Album mockAlbum1;
	@Mock
	Album mockAlbum2;
	@Mock
	Artist mockArtist;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		jukebox = new Jukebox(mockMediaPlayerCache);
		jukebox.setAudioPlayer(mockAudioPlayer);
		jukebox.setStatusBroadcaster(mockStatusBroadcaster);
		jukebox.setPlayList(mockPlayList);

		when(mockAudioPlayer.getAudioPlayerStatus()).thenReturn(PlayerStatus.IDLE);
	}

	@Test
	public void shouldBroadcastAMessageWhenASongStartsToPlay() {
		// Given
		when(mockAudioPlayer.getAudioPlayerStatus()).thenReturn(PlayerStatus.PLAYING);
		when(mockTrack1.getArtworkFile()).thenReturn(ART_FILE);
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(12);

		// When
		jukebox.songStarted();

		// Then
		verify(mockStatusBroadcaster).broadcast(eq(PlayerStatus.PLAYING.toString()), eq(12), eq(false), eq(false), eq(""));
	}

	@Test
	public void shouldBroadcastAMessageWhenASongIsPaused() {
		// Given
		when(mockAudioPlayer.getAudioPlayerStatus()).thenReturn(PlayerStatus.PAUSED);
		when(mockTrack1.getArtworkFile()).thenReturn(ART_FILE);
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(14);

		// When
		jukebox.songPaused();

		// Then
		verify(mockStatusBroadcaster).broadcast(eq(PlayerStatus.PAUSED.toString()), eq(14), eq(false), eq(false), eq(""));
	}

	@Test
	public void shouldBroadcastAMessageWhenASongEnds() {
		// Given
		when(mockAudioPlayer.getAudioPlayerStatus()).thenReturn(PlayerStatus.IDLE);
		when(mockTrack1.getArtworkFile()).thenReturn(ART_FILE);
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(14);
		when(mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile");

		// When
		jukebox.songEnded();

		// Then
		verify(mockStatusBroadcaster).broadcast(eq(PlayerStatus.IDLE.toString()), eq(14), eq(false), eq(false), eq(""));
	}
	
	@Test
	public void shouldPlayNextTrackWhenASongEnds() {
		// Given
		when(mockAudioPlayer.getAudioPlayerStatus()).thenReturn(PlayerStatus.IDLE);
		when(mockTrack1.getArtworkFile()).thenReturn(ART_FILE);
		
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1).thenReturn(mockTrack2);
		when(mockPlayList.getCurrentIndex()).thenReturn(3);
		
		when(mockTrack1.getId()).thenReturn(14);
		when(mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile1");
		when(mockTrack2.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile2");

		// When
		jukebox.songEnded();

		// Then
		verify(mockPlayList).nextTrack();
		verify(mockAudioPlayer).playAudioFile(any(File.class));
	}
	
	@Test
	public void shouldNotPlayNextTrackAtTheEndOfThePlaylistWhenASongEnds() {
		// Given
		when(mockAudioPlayer.getAudioPlayerStatus()).thenReturn(PlayerStatus.IDLE);
		when(mockTrack1.getArtworkFile()).thenReturn(ART_FILE);
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1).thenReturn(mockTrack2);
		when(mockPlayList.getCurrentIndex()).thenReturn(PlayList.POSITION_END_OF_PLAYLIST);

		when(mockTrack1.getId()).thenReturn(14);
		when(mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile1");
		when(mockTrack2.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile2");

		// When
		jukebox.songEnded();

		// Then
		verify(mockPlayList).nextTrack();
		verify(mockAudioPlayer,never()).playAudioFile(any(File.class));
	}
	
	@Test
	public void shouldAddFirstTrackToPlaylistAndStartPlaying () {
		// Given
		when (mockPlayList.getSize()).thenReturn(0);
		when (mockMediaPlayerCache.getMediaItemWithId(14)).thenReturn(mockTrack1);
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(14);
		when(mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile1");
		
		// When
		jukebox.addItemToPlaylist(14);
		
		// Then
		verify(mockPlayList).nextTrack();
		verify(mockPlayList).addTrack(mockTrack1);
		verify(mockAudioPlayer).playAudioFile(any(File.class));
	}

	@Test
	public void shouldAddTrackToPlaylistWhileAnotherIsAlreadyPlaying () {
		// Given
		when (mockPlayList.getSize()).thenReturn(1);
		when (mockMediaPlayerCache.getMediaItemWithId(14)).thenReturn(mockTrack1);
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(14);
		when(mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile1");
		
		// When
		jukebox.addItemToPlaylist(14);
		
		// Then
		verify(mockPlayList,never()).nextTrack();
		verify(mockPlayList).addTrack(mockTrack1);
		verify(mockAudioPlayer,never()).playAudioFile(any(File.class));
	}

	@Test
	public void shouldAddAlbumToPlaylist () {
		// Given
		when (mockPlayList.getSize()).thenReturn(1);
		when (mockMediaPlayerCache.getMediaItemWithId(14)).thenReturn(mockAlbum1);
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		
		when(mockAlbum1.getId()).thenReturn(14);
		Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
		tracks.put(15, mockTrack1);
		tracks.put(16, mockTrack2);
		when(mockAlbum1.getTracks()).thenReturn(tracks);
		
		//when(mockTrack1.getId()).thenReturn(15);
		//when(mockTrack2.getId()).thenReturn(16);
		//when(mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile1");
		
		// When
		jukebox.addItemToPlaylist(14);
		
		// Then
		verify(mockPlayList).addTrack(mockTrack1);
		verify(mockPlayList).addTrack(mockTrack2);
	}

	@Test
	public void shouldAddArtistToPlaylist () {
		// Given
		when (mockPlayList.getSize()).thenReturn(1);
		when (mockMediaPlayerCache.getMediaItemWithId(14)).thenReturn(mockArtist);
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		
		when(mockArtist.getId()).thenReturn(14);
		
		Map<String,Album> albums = new HashMap<String, Album> ();
		albums.put("17", mockAlbum1);
		albums.put("18", mockAlbum2);
		
		Map<Integer, Track> tracks1 = new HashMap<Integer, Track> ();
		tracks1.put(15, mockTrack1);
		Map<Integer, Track> tracks2 = new HashMap<Integer, Track> ();
		tracks2.put(16, mockTrack2);
		
		when(mockAlbum1.getTracks()).thenReturn(tracks1);
		when(mockAlbum2.getTracks()).thenReturn(tracks2);
		
		when(mockArtist.getAlbums()).thenReturn(albums);
		//when(mockTrack1.getId()).thenReturn(15);
		//when(mockTrack2.getId()).thenReturn(16);
		//when(mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile1");
		
		// When
		jukebox.addItemToPlaylist(14);
		
		// Then
		verify(mockPlayList).addTrack(mockTrack1);
		verify(mockPlayList).addTrack(mockTrack2);
	}

	@Test
	public void shouldPlayIfPaused () {
		// Given
		when (mockAudioPlayer.getAudioPlayerStatus()).thenReturn(PlayerStatus.PAUSED);
		
		// When
		jukebox.playOrPause();
		
		// Then
		verify (mockAudioPlayer).pause(false);
	}

	@Test
	public void shouldPauseIfPlaying () {
		// Given
		when (mockAudioPlayer.getAudioPlayerStatus()).thenReturn(PlayerStatus.PLAYING);
		
		// When
		jukebox.playOrPause();
		
		// Then
		verify (mockAudioPlayer).pause(true);
	}

	@Test
	public void shouldPlayNextTrackIfOneAvailable () {
		// Given
		when (mockPlayList.getCurrentIndex()).thenReturn(4);
		when (mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when (mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile");
		
		// When
		jukebox.nextTrack();
		
		// Then
		verify (mockPlayList).nextTrack();
		verify (mockAudioPlayer).stopPlayingAudioFile();
		verify (mockAudioPlayer).playAudioFile(any(File.class));
	}

	@Test
	public void shouldNotPlayNextTrackIfAtEndOfPlaylist () {
		// Given
		when (mockPlayList.getCurrentIndex()).thenReturn(PlayList.POSITION_END_OF_PLAYLIST);
		
		// When
		jukebox.nextTrack();
		
		// Then
		verify (mockPlayList).nextTrack();
		verify (mockAudioPlayer).stopPlayingAudioFile();
		verify (mockAudioPlayer,never()).playAudioFile(any(File.class));		
	}

	@Test
	public void shouldPlayPreviousTrackIfOneAvailable () {
		// Given
		when (mockPlayList.getCurrentIndex()).thenReturn(4);
		when (mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when (mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile");
		
		// When
		jukebox.previousTrack();
		
		// Then
		verify (mockPlayList).previousTrack();
		verify (mockAudioPlayer).stopPlayingAudioFile();
		verify (mockAudioPlayer).playAudioFile(any(File.class));		
	}

	@Test
	public void shouldNotPlayPreviousTrackIfAtStartOfPlaylist () {
		// Given
		when (mockPlayList.getCurrentIndex()).thenReturn(PlayList.POSITION_START_OF_PLAYLIST);
		
		// When
		jukebox.previousTrack();
		
		// Then
		verify (mockPlayList).previousTrack();
		verify (mockAudioPlayer).stopPlayingAudioFile();
		verify (mockAudioPlayer,never()).playAudioFile(any(File.class));		
	}
	
	@Test
	public void shouldToggleRandomOn () {
		// Given
		jukebox.shuffle = false;
		when (mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when (mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile");
		
		// When
		jukebox.toggleShuffle();
		
		// Then
		verify (mockPlayList).randomise();
		verify (mockAudioPlayer).stopPlayingAudioFile();
		verify (mockAudioPlayer).playAudioFile(any(File.class));		
	}

	@Test
	public void shouldToggleRandomOff () {
		// Given
		jukebox.shuffle = true;
		when (mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when (mockTrack1.getFullyQualifiedFileName()).thenReturn("C:\\myfolder\\myfile");
		
		// When
		jukebox.toggleShuffle();
		
		// Then
		verify (mockPlayList).unRandomise();
		verify (mockAudioPlayer).stopPlayingAudioFile();
		verify (mockAudioPlayer).playAudioFile(any(File.class));		
	}

	@Test
	public void shouldToggleRepeatOn () {
		// Given
		jukebox.repeat = false;
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(12);
				
		// When
		jukebox.toggleRepeat();
		
		// Then
		assertEquals (true, jukebox.repeat);
		verify(mockStatusBroadcaster).broadcast(eq(PlayerStatus.IDLE.toString()), eq(12), eq(true), eq(false), eq(""));
	}

	@Test
	public void shouldToggleRepeatOff () {
		// Given
		jukebox.repeat = true;
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(12);
				
		// When
		jukebox.toggleRepeat();
		
		// Then
		assertEquals (false, jukebox.repeat);
		verify(mockStatusBroadcaster).broadcast(eq(PlayerStatus.IDLE.toString()), eq(12), eq(false), eq(false), eq(""));
	}

	@Test
	public void shouldClearPlayList () {
		// Given
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(12);
		
		// When
		jukebox.clearPlaylist();
		
		// Then
		verify (mockPlayList).clear();
		verify (mockAudioPlayer).stopPlayingAudioFile();
		verify(mockStatusBroadcaster).broadcast(eq(PlayerStatus.IDLE.toString()), eq(12), eq(false), eq(false), eq(""));
	}

	@Test
	public void shouldBroadcastStatus () {
		// Given
		when(mockPlayList.getCurrentTrack()).thenReturn(mockTrack1);
		when(mockTrack1.getId()).thenReturn(12);
		
		// When
		jukebox.broadcastStatus();
		
		// Then
		verify(mockStatusBroadcaster).broadcast(eq(PlayerStatus.IDLE.toString()), eq(12), eq(false), eq(false), eq(""));
	}

	@Test
	public void shouldThrowExceptionIfAttemptToGetArtworkForAlbum() {
		// Given
		when(mockMediaPlayerCache.getMediaItemWithId(23)).thenReturn(mockAlbum1);

		try {
			// When
			jukebox.getArtworkForTrack(23);

			fail("Should have got an illegal argument exception");
		} catch (IllegalArgumentException e) {
			assertEquals("You can only retrieve artwork for a Track", e.getMessage());
		}
	}

	@Test
	public void shouldThrowExceptionIfAttemptToGetArtworkForArtist() {
		// Given
		when(mockMediaPlayerCache.getMediaItemWithId(23)).thenReturn(mockArtist);

		try {
			// When
			jukebox.getArtworkForTrack(23);

			fail("Should have got an illegal argument exception");
		} catch (IllegalArgumentException e) {
			assertEquals("You can only retrieve artwork for a Track", e.getMessage());
		}
	}

	@Test
	public void shouldReturnArtworkForTrack() {
		// Given
		when(mockMediaPlayerCache.getMediaItemWithId(23)).thenReturn(mockTrack1);
		when(mockTrack1.getArtworkFile()).thenReturn(ART_FILE);

		// When
		String artFile = jukebox.getArtworkForTrack(23);

		// Then
		assertEquals(ART_FILE, artFile);
	}
}
