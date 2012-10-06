package com.mindbadger.xml;

import java.io.File;
import java.io.IOException;

import com.mindbadger.audioserver.schema.AudioserverDocument;

public class FileWriter {
  private File file;
  
  public FileWriter (File file) {
    this.file = file;
  }
  
  public void writeFile (AudioserverDocument doc) {
    try {
      doc.save(file);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
