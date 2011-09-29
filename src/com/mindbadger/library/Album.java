package com.mindbadger.library;

import java.util.Map;

public class Album {
  private long id;
  private String name;
  private Map <Integer, Track> tracks;
  
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Map<Integer, Track> getTracks() {
    return tracks;
  }
  public void setTracks(Map<Integer, Track> tracks) {
    this.tracks = tracks;
  }
}
