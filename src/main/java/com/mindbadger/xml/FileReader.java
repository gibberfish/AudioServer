package com.mindbadger.xml;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import com.mindbadger.audioserver.schema.AudioserverDocument;

public class FileReader {
  private File file;
  
  public FileReader (File file) {
    this.file = file;
  }
  
  public AudioserverDocument readFile () {
    try {
      return AudioserverDocument.Factory.parse(file);
    } catch (XmlException e) {
      throw new InvalidFileException (e);
    } catch (IOException e) {
      throw new NoMediaFileFoundException (e);
    }
  }
}
