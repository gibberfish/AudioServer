package com.mindbadger.library;

public class Track {
  private long id;
  private String name;
  private String fullyQualifiedFileName;
  private int trackNumber;
  
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
  public String getFullyQualifiedFileName() {
    return fullyQualifiedFileName;
  }
  public void setFullyQualifiedFileName(String fullyQualifiedFileName) {
    this.fullyQualifiedFileName = fullyQualifiedFileName;
  }
  public void setTrackNumber(int trackNumber) {
    this.trackNumber = trackNumber;
  }
  public int getTrackNumber() {
    return trackNumber;
  }
}
