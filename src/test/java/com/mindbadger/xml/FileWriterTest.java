package com.mindbadger.xml;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindbadger.audioserver.schema.AudioserverDocument;

public class FileWriterTest {
  private FileWriter writer;
  
  @Mock private File mockFile;
  @Mock private AudioserverDocument doc;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    
    writer = new FileWriter (mockFile);
  }
  
  @Test
  public void shouldThrowAnExceptionIfTheFileCantBeWritten () throws IOException {
    // Given
    doThrow(new IOException()).when(doc).save(mockFile);
    
    // When
    try {
      writer.writeFile(doc);
      fail ("Should have thrown an IllegalStateException");
    } catch (IllegalStateException e) {
      // OK
    }
    
    // Then
    verify(doc).save(mockFile);
  }
  
  @Test
  public void shouldWriteAFile () throws IOException {
    // When
    writer.writeFile(doc);
    
    // Then
    verify(doc).save(mockFile);
  }
}
