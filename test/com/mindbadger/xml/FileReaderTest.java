package com.mindbadger.xml;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FileReaderTest {
  private FileReader reader;
  
  @Mock private File mockFile;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    
    reader = new FileReader (mockFile);
  }
  
  @Test
  public void shouldThrowAnExceptionWhenTheFileIsNotFound () {
    // Given
    //doThrow(new IOException()).when(doc).save(mockFile);
    
    // Can't test anything becuase of the static methods on XMLBeans
  }
}
