package com.mindbadger.player;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import javax.media.Player;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindbadger.jukebox.PlayerStatus;

public class AudioPlayerTest {
  private AudioPlayer audioPlayer;
  
  @Mock private JavaxPlayerFactory mockFactory;
  @Mock private File mockFile;
  @Mock private Player mockPlayer;
  @Mock private IBroadcastAudioPlayerEvents mockBroadcaster;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    
    audioPlayer = new AudioPlayer (mockFactory);
    audioPlayer.setBroadcaster(mockBroadcaster);
  }
  
  @Test
  public void shouldBeIdleAtStartup () {
	  // Given
	  
	  // When
	  PlayerStatus status = audioPlayer.getStatus ();
	  
	  // Then
	  assertEquals (PlayerStatus.IDLE, status);
  }
  
  @Test
  public void shouldPlayAFile () {
    // Given
    when (mockFactory.getNewPlayer(mockFile)).thenReturn(mockPlayer);
    
    // When
    audioPlayer.playAudioFile(mockFile);
    
    // Then
    verify (mockFactory).getNewPlayer(mockFile);
    verify (mockPlayer).start();
    verify (mockPlayer).addControllerListener(audioPlayer);
    assertEquals (PlayerStatus.QUEUED, audioPlayer.getStatus());
  }
  
  @Test
  public void shouldStopPlayingAFile () {
	  // Given
	  when (mockFactory.getNewPlayer(mockFile)).thenReturn(mockPlayer);
	  audioPlayer.playAudioFile(mockFile);
	  
	  // When
	  audioPlayer.stopPlayingAudioFile();
	  
	  // Then
	  verify (mockPlayer).close();
	  verify (mockPlayer).deallocate();
	  assertEquals (PlayerStatus.IDLE, audioPlayer.getStatus());
  }
}
