package com.mindbadger.jukebox;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindbadger.library.Track;

public class PlayListTest {
	PlayList playlist;

	@Mock PlaylistRandomiser mockPlaylistRandomiser;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		playlist = new PlayList();
		playlist.setPlaylistRandomiser(mockPlaylistRandomiser);
	}

	@Test
	public void shouldStartWithAnEmptyPlaylist() {
		// Given

		// When
		int size = playlist.getSize ();
		int currentIndex = playlist.getCurrentIndex ();

		// Then
		assertEquals (0, size);
		assertEquals (PlayList.POSITION_START_OF_PLAYLIST, currentIndex);
		assertNull (playlist.getCurrentTrack());
	}
	
	@Test
	public void shouldAddTrackToPlaylist () {
		// Given
		Track track = new Track ();
		
		// When
		playlist.addTrack (track);
		
		// Then
		assertEquals (1, playlist.getSize());
		assertEquals (PlayList.POSITION_START_OF_PLAYLIST, playlist.getCurrentIndex());
		assertNull (playlist.getCurrentTrack());
	}
	
	@Test
	public void shouldAddAnotherTrackToPlaylist () {
		// Given
		Track track1 = new Track ();
		Track track2 = new Track ();
		playlist.addTrack (track1);
		
		// When
		playlist.addTrack (track2);
		
		// Then
		assertEquals (2, playlist.getSize());
		assertEquals (PlayList.POSITION_START_OF_PLAYLIST, playlist.getCurrentIndex());
		assertNull (playlist.getCurrentTrack());
	}
	
	@Test
	public void shouldAdvanceToFirstTrackFromStartOfPlaylist () {
		// Given
		Track track = new Track ();
		playlist.addTrack(track);
		
		// When
		playlist.nextTrack ();
		
		// Then
		assertEquals (0, playlist.getCurrentIndex());
		assertEquals (track, playlist.getCurrentTrack());
	}

	@Test
	public void shouldAdvanceToSecondTrack () {
		// Given
		Track track1 = new Track ();
		Track track2 = new Track ();
		playlist.addTrack(track1);
		playlist.addTrack(track2);
		playlist.nextTrack ();
		
		// When
		playlist.nextTrack ();
		
		// Then
		assertEquals (1, playlist.getCurrentIndex());
		assertEquals (track2, playlist.getCurrentTrack());
	}

	@Test
	public void shouldAdvanceToEndOfPlaylist () {
		// Given
		Track track1 = new Track ();
		playlist.addTrack(track1);
		playlist.nextTrack ();
		
		// When
		playlist.nextTrack ();
		
		// Then
		assertEquals (PlayList.POSITION_END_OF_PLAYLIST, playlist.getCurrentIndex());
		assertNull (playlist.getCurrentTrack());
	}

	@Test
	public void shouldNotAdvanceIfAtEndOfPlaylist () {
		// Given
		Track track1 = new Track ();
		playlist.addTrack(track1);
		playlist.nextTrack ();
		playlist.nextTrack ();
		
		// When
		playlist.nextTrack ();
		
		// Then
		assertEquals (PlayList.POSITION_END_OF_PLAYLIST, playlist.getCurrentIndex());
		assertNull (playlist.getCurrentTrack());
	}

	@Test
	public void shouldGoBackOneTrack () {
		// Given
		Track track1 = new Track ();
		Track track2 = new Track ();
		playlist.addTrack(track1);
		playlist.addTrack(track2);
		playlist.nextTrack ();
		playlist.nextTrack ();
		
		// When
		playlist.previousTrack ();
		
		// Then
		assertEquals (0, playlist.getCurrentIndex());
		assertEquals (track1, playlist.getCurrentTrack());
	}

	@Test
	public void shouldGoBackToStartOfPlaylist () {
		// Given
		Track track1 = new Track ();
		playlist.addTrack(track1);
		playlist.nextTrack ();
		
		// When
		playlist.previousTrack ();
		
		// Then
		assertEquals (PlayList.POSITION_START_OF_PLAYLIST, playlist.getCurrentIndex());
		assertNull (playlist.getCurrentTrack());
	}

	@Test
	public void shouldNotGoBackIfAtStartOfPlaylist () {
		// Given
		Track track1 = new Track ();
		playlist.addTrack(track1);
		playlist.nextTrack ();
		playlist.previousTrack ();
		
		// When
		playlist.previousTrack ();
		
		// Then
		assertEquals (PlayList.POSITION_START_OF_PLAYLIST, playlist.getCurrentIndex());
		assertNull (playlist.getCurrentTrack());
	}

	@Test
	public void shouldGoBackToLastTrackIfAtEndOfPlaylist () {
		// Given
		Track track1 = new Track ();
		playlist.addTrack(track1);
		playlist.nextTrack ();
		playlist.nextTrack ();
		
		// When
		playlist.previousTrack ();
		
		// Then
		assertEquals (0, playlist.getCurrentIndex());
	}

	@Test
	public void shouldClearPlayList () {
		// Given
		Track track1 = new Track ();
		Track track2 = new Track ();
		playlist.addTrack(track1);
		playlist.addTrack(track2);
		
		// When
		playlist.clear ();
		
		// Then
		assertEquals (0, playlist.getSize());
		assertEquals (PlayList.POSITION_START_OF_PLAYLIST, playlist.getCurrentIndex());
		assertNull (playlist.getCurrentTrack());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldRandomiseThePlayList () {
		// Given
		List<Track> randomisedList = new ArrayList<Track> ();
		when(mockPlaylistRandomiser.randomise(any(List.class))).thenReturn(randomisedList);
		
		// When
		playlist.randomise ();
		
		// Then
		verify(mockPlaylistRandomiser).randomise(any(List.class));
		assertEquals (randomisedList, playlist.getTrackList());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldUnRandomiseThePlayList () {
		// Given
		List<Track> originalList = new ArrayList<Track> ();
		when(mockPlaylistRandomiser.randomise(any(List.class))).thenReturn(originalList);
		
		// When
		playlist.unRandomise ();
		
		// Then
		verify(mockPlaylistRandomiser).backToOriginalState();
		assertEquals (originalList, playlist.getTrackList());
	}

}
