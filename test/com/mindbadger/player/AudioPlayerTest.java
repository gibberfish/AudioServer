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
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    
    audioPlayer = new AudioPlayer ();
    
    audioPlayer.setFactory(mockFactory);
  }
  
  @Test
  public void shouldPlayAFile () {
    // Given
    when (mockFactory.getNewPlayer((File)anyObject())).thenReturn(mockPlayer);
    
    // When
    audioPlayer.playAudioFile("C:\\myfile.mp3");
    
    // Then
    verify (mockPlayer).start();
  }
}
