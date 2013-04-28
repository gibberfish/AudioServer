package com.mindbadger.player;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import javax.media.EndOfMediaEvent;
import javax.media.Player;
import javax.media.StartEvent;
import javax.media.StopByRequestEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AudioPlayerTest {
	private AudioPlayer audioPlayer;

	@Mock
	private JavaxPlayerFactory mockFactory;
	@Mock
	private File mockFile;
	@Mock
	private Player mockPlayer;
	@Mock
	private IReceiveStatusUpdatesFromAMediaPlayer mockAudioPlayerStatusListener;
	@Mock
	private EndOfMediaEvent mockEndOfMediaEvent;
	@Mock
	private StopByRequestEvent mockStopByRequestEvent;
	@Mock
	private StartEvent mockStartEvent;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		audioPlayer = new AudioPlayer(mockFactory);
		audioPlayer.setBroadcaster(mockAudioPlayerStatusListener);
	}

	@Test
	public void shouldBeIdleAtStartup() {
		// Given

		// When
		PlayerStatus status = audioPlayer.getAudioPlayerStatus();

		// Then
		assertEquals(PlayerStatus.IDLE, status);
	}

	@Test
	public void shouldPlayAFile() {
		// Given
		when(mockFactory.getNewPlayer(mockFile)).thenReturn(mockPlayer);

		// When
		audioPlayer.playAudioFile(mockFile);

		// Then
		verify(mockFactory).getNewPlayer(mockFile);
		verify(mockPlayer).start();
		verify(mockPlayer).addControllerListener(audioPlayer);
		assertEquals(PlayerStatus.QUEUED, audioPlayer.getAudioPlayerStatus());
	}

	@Test
	public void shouldEnsurePreviousPlayerIsStoppedBeforeYouPlayANewFile() {
		// Given
		when(mockFactory.getNewPlayer(mockFile)).thenReturn(mockPlayer);
		audioPlayer.playAudioFile(mockFile);

		// When
		audioPlayer.playAudioFile(mockFile);

		// Then
		verify(mockFactory,times(2)).getNewPlayer(mockFile);
		verify(mockPlayer,times(2)).start();
		verify(mockPlayer,times(2)).addControllerListener(audioPlayer);
		verify(mockPlayer,times(1)).close();
		verify(mockPlayer,times(1)).deallocate();
	}

	@Test
	public void shouldStopPlayingAFile() {
		// Given
		when(mockFactory.getNewPlayer(mockFile)).thenReturn(mockPlayer);
		audioPlayer.playAudioFile(mockFile);

		// When
		audioPlayer.stopPlayingAudioFile();

		// Then
		verify(mockPlayer).close();
		verify(mockPlayer).deallocate();
		assertEquals(PlayerStatus.IDLE, audioPlayer.getAudioPlayerStatus());
	}

	@Test
	public void shouldNotRepeatStopPlayingAFileIfItsAlreadyStopped() {
		// Given
		when(mockFactory.getNewPlayer(mockFile)).thenReturn(mockPlayer);
		audioPlayer.playAudioFile(mockFile);
		audioPlayer.stopPlayingAudioFile();

		// When
		audioPlayer.stopPlayingAudioFile();

		// Then
		verify(mockPlayer, times(1)).close();
		verify(mockPlayer, times(1)).deallocate();
		assertEquals(PlayerStatus.IDLE, audioPlayer.getAudioPlayerStatus());
	}

	@Test
	public void shouldPause () {
		// Given
		when(mockFactory.getNewPlayer(mockFile)).thenReturn(mockPlayer);
		audioPlayer.playAudioFile(mockFile);
		
		// When
		audioPlayer.pause(true);
		
		// Then
		verify (mockPlayer).stop();
		assertEquals (PlayerStatus.PAUSED, audioPlayer.getAudioPlayerStatus());
	}
	
	@Test
	public void shouldUnpause () {
		// Given
		when(mockFactory.getNewPlayer(mockFile)).thenReturn(mockPlayer);
		audioPlayer.playAudioFile(mockFile);
		audioPlayer.pause(true);
		
		// When
		audioPlayer.pause(false);
		
		// Then
		verify (mockPlayer, times(2)).start();
		assertEquals (PlayerStatus.QUEUED, audioPlayer.getAudioPlayerStatus());
		
	}
	
	@Test
	public void shouldNotPauseIfAlreadyStopped () {
		// Given
		
		// When
		audioPlayer.pause(true);
		
		// Then
		verify (mockPlayer, never()).stop();
	}

	@Test
	public void shouldNotUnpauseIfAlreadyStopped () {
		// Given
		
		// When
		audioPlayer.pause(false);
		
		// Then
		verify (mockPlayer, never()).start();
	}

	
	@Test
	public void shouldHandleEndOfMediaEvent () {
		// Given
		
		// When
		audioPlayer.controllerUpdate(mockEndOfMediaEvent);
		
		// Then
		verify (mockAudioPlayerStatusListener).songEnded();
	}
	
	@Test
	public void shouldHandleStopByRequestEvent () {
		// Given
		
		// When
		audioPlayer.controllerUpdate(mockStopByRequestEvent);
		
		// Then
		verify (mockAudioPlayerStatusListener).songPaused();
	}
	
	@Test
	public void shouldHandleStartEvent () {
		// Given
		
		// When
		audioPlayer.controllerUpdate(mockStartEvent);
		
		// Then
		verify (mockAudioPlayerStatusListener).songStarted();
	}
	
}
