package com.mindbadger.player;

import static org.mockito.Mockito.*;

import java.io.File;

import javax.media.Player;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AudioPlayerTest {
  private AudioPlayer audioPlayer;
  
  @Mock private JavaxPlayerFactory mockFactory;
  @Mock private File mockFile;
  @Mock private Player mockPlayer;
  @Mock private IBroadcastAudioPlayerEvents mockBroadcaster;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    
    audioPlayer = new AudioPlayer (mockFactory, mockBroadcaster);
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
  }
}
