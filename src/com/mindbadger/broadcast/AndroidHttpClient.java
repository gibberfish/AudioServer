package com.mindbadger.broadcast;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class AndroidHttpClient {
  public String sendHttpBroadcast (String ipAddress, BroadcastMessage message) {
    
    StringBuffer buffer = new StringBuffer();
    buffer.append("http://");
    buffer.append(ipAddress);
    buffer.append(":8080/updateStatus");
    buffer.append("?trackId=");
    buffer.append(message.getCurrentTrackId());
    buffer.append("&status=");
    buffer.append(message.getPlayerStatus());
    buffer.append("&isShuffle=");
    buffer.append(message.isShuffle());
    buffer.append("&isRepeat=");
    buffer.append(message.isRepeat());
    buffer.append("&artwork=");
    buffer.append(message.getArtworkUrl());
    
    HttpClient client = new DefaultHttpClient ();
    HttpGet httpget = new HttpGet(buffer.toString());
    
    try {
      HttpResponse response = client.execute(httpget);
      System.out.println("ALL GOOD: " + response.getStatusLine());
    } catch (ClientProtocolException e) {
      System.err.println("ClientProtocolException: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }
    
    return null;
  }
  
//  public static void main(String[] args) {
//    AndroidHttpClient client = new AndroidHttpClient ();
//    BroadcastMessage message = new BroadcastMessage ();
//    message.setCurrentTrackId(18);
//    message.setPlayerStatus(PlayerStatus.PLAYING);
//    message.setRepeat(false);
//    message.setShuffle(true);
//    client.sendHttpBroadcast("192.168.2.200", message);
//  }
}
