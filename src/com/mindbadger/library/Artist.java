package com.mindbadger.library;

import java.util.Map;

public class Artist {
  private long id;
  private String name;
  private Map<String, Album> albums;
  
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
  public Map<String, Album> getAlbums() {
    return albums;
  }
  public void setAlbums(Map<String, Album> albums) {
    this.albums = albums;
  }
}
