package com.mindbadger.broadcast;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class StatusBroadcaster {
  Logger logger = Logger.getLogger(StatusBroadcaster.class);
  
  private AndroidHttpClient androidHttpClient;
  
  List<String> registeredIpAddress = new ArrayList<String> ();
  
  public void register (String ipAddress) {
    registeredIpAddress.add(ipAddress);
  }
  
  public void broadcast (String status, int trackId, boolean repeat, boolean shuffle, String artworkUrl) {
    logger.debug("BROADCAST: Status: "+ status + ", id: " + trackId + ", repeat: " + repeat + ", shuffle: " + shuffle);
    BroadcastMessage message = generateBroadcastMessage(status, trackId, repeat, shuffle, artworkUrl);
    for (String ipAddress : registeredIpAddress) {
      androidHttpClient.sendHttpBroadcast(ipAddress, message);
    }
  }
  
  public void setAndroidHttpClient(AndroidHttpClient androidHttpClient) {
    this.androidHttpClient = androidHttpClient;
  }

  public AndroidHttpClient getAndroidHttpClient() {
    return androidHttpClient;
  }

  protected BroadcastMessage generateBroadcastMessage(String status, int trackId, boolean repeat, boolean shuffle, String artworkUrl) {
    BroadcastMessage message = new BroadcastMessage ();
    message.setPlayerStatus(status);
    message.setCurrentTrackId(trackId);
    message.setArtworkUrl(artworkUrl);
    message.setRepeat(repeat);
    message.setShuffle(shuffle);
    return message;
  }
}
