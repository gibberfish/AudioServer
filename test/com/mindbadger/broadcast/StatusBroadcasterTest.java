package com.mindbadger.broadcast;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindbadger.jukebox.PlayerStatus;

public class StatusBroadcasterTest {
  StatusBroadcaster statusBroadcaster;
  
  @Mock AndroidHttpClient mockAndroidHttpClient;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    
    statusBroadcaster = new StatusBroadcaster ();
    statusBroadcaster.setAndroidHttpClient(mockAndroidHttpClient);
  }
  
  @Test
  public void shouldCreateABroadcastMessage () {
    // When
    BroadcastMessage message = statusBroadcaster.generateBroadcastMessage (PlayerStatus.PAUSED, 14, false, true, "C:\\hello");
    
    // Then
    assertEquals (PlayerStatus.PAUSED, message.getPlayerStatus());
    assertEquals ("C:\\hello", message.getArtworkUrl());
    assertEquals (14, message.getCurrentTrackId());
    assertEquals (false, message.isRepeat());
    assertEquals (true, message.isShuffle());
  }
  
  @Test
  public void shouldBroadcastToAllRegisteredSubscribers () {
    // Given
    statusBroadcaster.register("Address1");
    statusBroadcaster.register("Address2");
    statusBroadcaster.register("Address3");
    
    // When
    statusBroadcaster.broadcast(PlayerStatus.PAUSED, 14, false, true, "C:\\hello");

    // Then
    verify (mockAndroidHttpClient).sendHttpBroadcast(eq("Address1"), (BroadcastMessage) anyObject());
    verify (mockAndroidHttpClient).sendHttpBroadcast(eq("Address2"), (BroadcastMessage) anyObject());
    verify (mockAndroidHttpClient).sendHttpBroadcast(eq("Address3"), (BroadcastMessage) anyObject());
  }
}
