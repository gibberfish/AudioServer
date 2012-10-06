package com.mindbadger.library;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.audioserver.schema.AudioserverType;
import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.wmp.IReadTheWmpLibrary;
import com.mindbadger.wmp.IdGenerator;
import com.mindbadger.wmp.JacobAdapter;
import com.mindbadger.xml.FileReader;
import com.mindbadger.xml.FileWriter;
import com.mindbadger.xml.InvalidFileException;
import com.mindbadger.xml.NoMediaFileFoundException;
import com.mindbadger.xml.XMLConverter;

public class LibrarianTest {
  @Mock private IReadTheWmpLibrary mockLibraryReader;
  private MediaPlayerCache cache;
  @Mock private JacobAdapter mockAdapter;
  @Mock private XMLConverter mockConverter;
  @Mock private FileReader mockFileReader;
  @Mock private FileWriter mockFileWriter;
  private IdGenerator idGenerator;
  
  private Librarian librarian;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void shouldLoadTheLibraryIntoANewFileWhenAFileDoesntAlreadyExist () {
    // Given
    Map<String, Artist> mapReadFromLibrary = getTheMapReadFromTheWmpLibrary();

    cache = new MediaPlayerCache();
    idGenerator = new IdGenerator();
    
    AudioserverDocument convertedXml = getConvertedXml();
    
    when (mockLibraryReader.readLibrary(mockAdapter)).thenReturn(mapReadFromLibrary);
    doThrow(new NoMediaFileFoundException()).when(mockFileReader).readFile();
    when (mockConverter.convertMapToXML((Map<String, Artist>)anyObject())).thenReturn (convertedXml);
    
    // When
    librarian = new Librarian (mockLibraryReader, cache, mockAdapter, mockConverter, mockFileReader, mockFileWriter, idGenerator);
    
    // Then
    verify(mockLibraryReader).readLibrary(mockAdapter);
    verify(mockFileReader).readFile();
    verify(mockConverter, never()).convertXMLToMap((AudioserverDocument) anyObject());
    verify(mockConverter).convertMapToXML((Map<String, Artist>)anyObject());
    verify(mockFileWriter).writeFile(convertedXml);
    
    assertEquals(convertedXml, cache.getXML());
    assertTrue (cache.getXML().getAudioserver().getLastUpdated().get(Calendar.YEAR) > 2010);
    
    Map<String, Artist> newMap = cache.getMap();
    
    assertEquals (2, newMap.size());
    
    Artist artist1 = newMap.get("artist1");
    assertEquals(4, artist1.getId());
    assertEquals("artist1", artist1.getName());
    
    Map<String, Album> artist1Albums = artist1.getAlbums();
    assertEquals (1, artist1Albums.size());
    
    Album album1 = artist1Albums.get("album1");
    assertEquals(5, album1.getId());
    assertEquals("album1", album1.getName());

    Map<Integer, Track> album1Tracks = album1.getTracks();
    assertEquals (1, album1Tracks.size());

    Track track1 = album1Tracks.get(1);
    assertEquals(6, track1.getId());
    assertEquals("track1", track1.getName());
    
    assertNull (newMap.get("artist2"));

    Artist artist3 = newMap.get("artist3");
    assertEquals(1, artist3.getId());
    assertEquals("artist3", artist3.getName());

    Map<String, Album> artist3Albums = artist3.getAlbums();
    assertEquals (1, artist3Albums.size());
    
    Album album3 = artist3Albums.get("album3");
    assertEquals(2, album3.getId());
    assertEquals("album3", album3.getName());

    Map<Integer, Track> album3Tracks = album3.getTracks();
    assertEquals (1, album3Tracks.size());

    Track track3 = album3Tracks.get(1);
    assertEquals(3, track3.getId());
    assertEquals("track3", track3.getName());
  }

  @Test
  public void shouldLoadTheLibraryIntoANewFileWhenACorruptFileExists () {
    // Given
    Map<String, Artist> mapReadFromLibrary = getTheMapReadFromTheWmpLibrary();

    cache = new MediaPlayerCache();
    idGenerator = new IdGenerator();
    
    AudioserverDocument convertedXml = getConvertedXml();
    
    when (mockLibraryReader.readLibrary(mockAdapter)).thenReturn(mapReadFromLibrary);
    doThrow(new InvalidFileException()).when(mockFileReader).readFile();
    when (mockConverter.convertMapToXML((Map<String, Artist>)anyObject())).thenReturn (convertedXml);
    
    // When
    librarian = new Librarian (mockLibraryReader, cache, mockAdapter, mockConverter, mockFileReader, mockFileWriter, idGenerator);
    
    // Then
    verify(mockLibraryReader).readLibrary(mockAdapter);
    verify(mockFileReader).readFile();
    verify(mockConverter, never()).convertXMLToMap((AudioserverDocument) anyObject());
    verify(mockConverter).convertMapToXML((Map<String, Artist>)anyObject());
    verify(mockFileWriter).writeFile(convertedXml);
    
    assertEquals(convertedXml, cache.getXML());
    assertTrue (cache.getXML().getAudioserver().getLastUpdated().get(Calendar.YEAR) > 2010);
    
    Map<String, Artist> newMap = cache.getMap();
    
    assertEquals (2, newMap.size());
    
    Artist artist1 = newMap.get("artist1");
    assertEquals(4, artist1.getId());
    assertEquals("artist1", artist1.getName());
    
    Map<String, Album> artist1Albums = artist1.getAlbums();
    assertEquals (1, artist1Albums.size());
    
    Album album1 = artist1Albums.get("album1");
    assertEquals(5, album1.getId());
    assertEquals("album1", album1.getName());

    Map<Integer, Track> album1Tracks = album1.getTracks();
    assertEquals (1, album1Tracks.size());

    Track track1 = album1Tracks.get(1);
    assertEquals(6, track1.getId());
    assertEquals("track1", track1.getName());
    
    assertNull (newMap.get("artist2"));

    Artist artist3 = newMap.get("artist3");
    assertEquals(1, artist3.getId());
    assertEquals("artist3", artist3.getName());

    Map<String, Album> artist3Albums = artist3.getAlbums();
    assertEquals (1, artist3Albums.size());
    
    Album album3 = artist3Albums.get("album3");
    assertEquals(2, album3.getId());
    assertEquals("album3", album3.getName());

    Map<Integer, Track> album3Tracks = album3.getTracks();
    assertEquals (1, album3Tracks.size());

    Track track3 = album3Tracks.get(1);
    assertEquals(3, track3.getId());
    assertEquals("track3", track3.getName());
  }

  @Test
  public void shouldCombineChangesToTheLibraryIntoAnExistingFile () {
    // Given
    Map<String, Artist> mapReadFromLibrary = getTheMapReadFromTheWmpLibrary();
    Map<String, Artist> mapConvertedFromFile = getTheMapConvertedFromTheFile();

    cache = new MediaPlayerCache();
    idGenerator = new IdGenerator();
    
    AudioserverDocument fileOnDiskDoc = getXmlFileFromDisk();
    AudioserverDocument convertedXml = getConvertedXml();
    
    when (mockLibraryReader.readLibrary(mockAdapter)).thenReturn(mapReadFromLibrary);
    when (mockFileReader.readFile()).thenReturn(fileOnDiskDoc);
    when (mockConverter.convertXMLToMap(fileOnDiskDoc)).thenReturn(mapConvertedFromFile);
    when (mockConverter.convertMapToXML((Map<String, Artist>)anyObject())).thenReturn (convertedXml);
    
    // When
    librarian = new Librarian (mockLibraryReader, cache, mockAdapter, mockConverter, mockFileReader, mockFileWriter, idGenerator);
    
    // Then
    verify(mockLibraryReader).readLibrary(mockAdapter);
    verify(mockFileReader).readFile();
    verify(mockConverter).convertXMLToMap(fileOnDiskDoc);
    verify(mockConverter).convertMapToXML((Map<String, Artist>)anyObject());
    verify(mockFileWriter).writeFile(convertedXml);
    
    assertEquals(convertedXml, cache.getXML());
    assertTrue (cache.getXML().getAudioserver().getLastUpdated().get(Calendar.YEAR) > 2010);
    
    Map<String, Artist> newMap = cache.getMap();
    
    assertEquals (2, newMap.size());
    
    Artist artist1 = newMap.get("artist1");
    assertEquals(10, artist1.getId());
    assertEquals("artist1", artist1.getName());
    
    Map<String, Album> artist1Albums = artist1.getAlbums();
    assertEquals (1, artist1Albums.size());
    
    Album album1 = artist1Albums.get("album1");
    assertEquals(20, album1.getId());
    assertEquals("album1", album1.getName());

    Map<Integer, Track> album1Tracks = album1.getTracks();
    assertEquals (1, album1Tracks.size());

    Track track1 = album1Tracks.get(1);
    assertEquals(30, track1.getId());
    assertEquals("track1", track1.getName());
    
    assertNull (newMap.get("artist2"));

    Artist artist3 = newMap.get("artist3");
    assertEquals(61, artist3.getId());
    assertEquals("artist3", artist3.getName());

    Map<String, Album> artist3Albums = artist3.getAlbums();
    assertEquals (1, artist3Albums.size());
    
    Album album3 = artist3Albums.get("album3");
    assertEquals(62, album3.getId());
    assertEquals("album3", album3.getName());

    Map<Integer, Track> album3Tracks = album3.getTracks();
    assertEquals (1, album3Tracks.size());

    Track track3 = album3Tracks.get(1);
    assertEquals(63, track3.getId());
    assertEquals("track3", track3.getName());
  }

  @Test
  public void shouldNotWriteTheFileIfItHasntChanged () {
    // Given
    Map<String, Artist> mapReadFromLibrary = getTheMapReadFromTheWmpLibrary();
    Map<String, Artist> mapConvertedFromFile = getTheMapReadFromTheWmpLibrary(); // Ensures the maps are identical
    mapConvertedFromFile.get("artist1").setId(1);
    mapConvertedFromFile.get("artist1").getAlbums().get("album1").setId(2);
    mapConvertedFromFile.get("artist1").getAlbums().get("album1").getTracks().get(1).setId(3);
    mapConvertedFromFile.get("artist3").setId(4);
    mapConvertedFromFile.get("artist3").getAlbums().get("album3").setId(5);
    mapConvertedFromFile.get("artist3").getAlbums().get("album3").getTracks().get(1).setId(6);
    

    cache = new MediaPlayerCache();
    idGenerator = new IdGenerator();
    
    AudioserverDocument fileOnDiskDoc = getXmlFileFromDisk();
    AudioserverDocument convertedXml = getConvertedXml();
    
    when (mockLibraryReader.readLibrary(mockAdapter)).thenReturn(mapReadFromLibrary);
    when (mockFileReader.readFile()).thenReturn(fileOnDiskDoc);
    when (mockConverter.convertXMLToMap(fileOnDiskDoc)).thenReturn(mapConvertedFromFile);
    when (mockConverter.convertMapToXML((Map<String, Artist>)anyObject())).thenReturn (convertedXml);
    
    // When
    librarian = new Librarian (mockLibraryReader, cache, mockAdapter, mockConverter, mockFileReader, mockFileWriter, idGenerator);
    
    // Then
    verify(mockLibraryReader).readLibrary(mockAdapter);
    verify(mockFileReader).readFile();
    verify(mockConverter).convertXMLToMap(fileOnDiskDoc);
    verify(mockConverter).convertMapToXML((Map<String, Artist>)anyObject());
    verify(mockFileWriter, never()).writeFile(convertedXml);
    
    assertEquals(convertedXml, cache.getXML());
    assertTrue (cache.getXML().getAudioserver().getLastUpdated().get(Calendar.YEAR) == 2010);
    
    Map<String, Artist> newMap = cache.getMap();
    
    assertEquals (2, newMap.size());
    
    Artist artist1 = newMap.get("artist1");
    assertEquals(1, artist1.getId());
    assertEquals("artist1", artist1.getName());
    
    Map<String, Album> artist1Albums = artist1.getAlbums();
    assertEquals (1, artist1Albums.size());
    
    Album album1 = artist1Albums.get("album1");
    assertEquals(2, album1.getId());
    assertEquals("album1", album1.getName());

    Map<Integer, Track> album1Tracks = album1.getTracks();
    assertEquals (1, album1Tracks.size());

    Track track1 = album1Tracks.get(1);
    assertEquals(3, track1.getId());
    assertEquals("track1", track1.getName());
    
    assertNull (newMap.get("artist2"));

    Artist artist2 = newMap.get("artist3");
    assertEquals(4, artist2.getId());
    assertEquals("artist3", artist2.getName());

    Map<String, Album> artist2Albums = artist2.getAlbums();
    assertEquals (1, artist2Albums.size());
    
    Album album2 = artist2Albums.get("album3");
    assertEquals(5, album2.getId());
    assertEquals("album3", album2.getName());

    Map<Integer, Track> album2Tracks = album2.getTracks();
    assertEquals (1, album2Tracks.size());

    Track track2 = album2Tracks.get(1);
    assertEquals(6, track2.getId());
    assertEquals("track3", track2.getName());
  }

  @Test
  public void shouldWriteTheFileIfATrackHasBeenRemoved () {
    // Given
    Map<String, Artist> mapReadFromLibrary = getTheMapReadFromTheWmpLibrary();
    mapReadFromLibrary.get("artist1").getAlbums().get("album1").getTracks().remove(1);
    
    Map<String, Artist> mapConvertedFromFile = getTheMapReadFromTheWmpLibrary();
    mapConvertedFromFile.get("artist1").setId(1);
    mapConvertedFromFile.get("artist1").getAlbums().get("album1").setId(2);
    mapConvertedFromFile.get("artist1").getAlbums().get("album1").getTracks().get(1).setId(3);
    mapConvertedFromFile.get("artist3").setId(4);
    mapConvertedFromFile.get("artist3").getAlbums().get("album3").setId(5);
    mapConvertedFromFile.get("artist3").getAlbums().get("album3").getTracks().get(1).setId(6);
    

    cache = new MediaPlayerCache();
    idGenerator = new IdGenerator();
    
    AudioserverDocument fileOnDiskDoc = getXmlFileFromDisk();
    AudioserverDocument convertedXml = getConvertedXml();
    
    when (mockLibraryReader.readLibrary(mockAdapter)).thenReturn(mapReadFromLibrary);
    when (mockFileReader.readFile()).thenReturn(fileOnDiskDoc);
    when (mockConverter.convertXMLToMap(fileOnDiskDoc)).thenReturn(mapConvertedFromFile);
    when (mockConverter.convertMapToXML((Map<String, Artist>)anyObject())).thenReturn (convertedXml);
    
    // When
    librarian = new Librarian (mockLibraryReader, cache, mockAdapter, mockConverter, mockFileReader, mockFileWriter, idGenerator);
    
    // Then
    verify(mockLibraryReader).readLibrary(mockAdapter);
    verify(mockFileReader).readFile();
    verify(mockConverter).convertXMLToMap(fileOnDiskDoc);
    verify(mockConverter).convertMapToXML((Map<String, Artist>)anyObject());
    verify(mockFileWriter).writeFile(convertedXml);
    
    assertEquals(convertedXml, cache.getXML());
    assertTrue (cache.getXML().getAudioserver().getLastUpdated().get(Calendar.YEAR) > 2010);
    
    Map<String, Artist> newMap = cache.getMap();
    
    assertEquals (2, newMap.size());
    
    Artist artist1 = newMap.get("artist1");
    assertEquals(1, artist1.getId());
    assertEquals("artist1", artist1.getName());
    
    Map<String, Album> artist1Albums = artist1.getAlbums();
    assertEquals (1, artist1Albums.size());
    
    Album album1 = artist1Albums.get("album1");
    assertEquals(2, album1.getId());
    assertEquals("album1", album1.getName());

    Map<Integer, Track> album1Tracks = album1.getTracks();
    assertEquals (0, album1Tracks.size());

    assertNull (newMap.get("artist2"));

    Artist artist2 = newMap.get("artist3");
    assertEquals(4, artist2.getId());
    assertEquals("artist3", artist2.getName());

    Map<String, Album> artist2Albums = artist2.getAlbums();
    assertEquals (1, artist2Albums.size());
    
    Album album2 = artist2Albums.get("album3");
    assertEquals(5, album2.getId());
    assertEquals("album3", album2.getName());

    Map<Integer, Track> album2Tracks = album2.getTracks();
    assertEquals (1, album2Tracks.size());

    Track track2 = album2Tracks.get(1);
    assertEquals(6, track2.getId());
    assertEquals("track3", track2.getName());
  }

	private Map<String, Artist> getTheMapConvertedFromTheFile() {
	  Map<String, Artist> mapConvertedFromTheFile = new HashMap<String, Artist> ();
	  
	  Artist artist1 = new Artist ();
	  artist1.setId(10);
	  artist1.setName("artist1");
	  mapConvertedFromTheFile.put("artist1", artist1);
	  
	  Map<String, Album> albums = new HashMap<String, Album> ();
	  artist1.setAlbums(albums);
	  Album album1 = new Album ();
	  album1.setId(20);
	  album1.setName("album1");
	  albums.put("album1", album1);
	  
	  Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
	  album1.setTracks(tracks);
	  Track track1 = new Track ();
	  track1.setId(30);
	  track1.setName("track1");
	  track1.setFullyQualifiedFileName("C:\\track1.mp3");
	  track1.setTrackNumber(1);
	  tracks.put(1, track1);
	  
	  Artist artist2 = new Artist ();
	  artist2.setId(40);
	  artist2.setName("artist2");
	  mapConvertedFromTheFile.put("artist2", artist2);
	  
	  Map<String, Album> albums2 = new HashMap<String, Album> ();
	  artist2.setAlbums(albums2);
	  Album album2 = new Album ();
	  album2.setId(50);
	  album2.setName("album2");
	  albums.put("album2", album2);
	  
	  Map<Integer, Track> tracks2 = new HashMap<Integer, Track> ();
	  album2.setTracks(tracks2);
	  Track track2 = new Track ();
	  track2.setId(60);
	  track2.setName("track2");
	  track2.setFullyQualifiedFileName("C:\\track2.mp3");
	  track2.setTrackNumber(1);
	  tracks2.put(1, track2);
	  
	  return mapConvertedFromTheFile;
  }

	private Map<String, Artist> getTheMapReadFromTheWmpLibrary() {
	  Map<String, Artist> mapReadFromLibrary = new HashMap<String, Artist> ();
	  
	  Artist artist1 = new Artist ();
	  artist1.setName("artist1");
	  mapReadFromLibrary.put("artist1", artist1);
	  
	  Map<String, Album> albums = new HashMap<String, Album> ();
	  artist1.setAlbums(albums);
	  Album album1 = new Album ();
	  album1.setName("album1");
	  albums.put("album1", album1);
	  
	  Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
	  album1.setTracks(tracks);
	  Track track1 = new Track ();
	  track1.setName("track1");
	  track1.setFullyQualifiedFileName("C:\\track1.mp3");
	  track1.setTrackNumber(1);
	  tracks.put(1, track1);
	  
	  Artist artist3 = new Artist ();
	  artist3.setName("artist3");
	  mapReadFromLibrary.put("artist3", artist3);
	  
	  Map<String, Album> albums3 = new HashMap<String, Album> ();
	  artist3.setAlbums(albums3);
	  Album album3 = new Album ();
	  album3.setName("album3");
	  albums3.put("album3", album3);
	  
	  Map<Integer, Track> tracks3 = new HashMap<Integer, Track> ();
	  album3.setTracks(tracks3);
	  Track track3 = new Track ();
	  track3.setName("track3");
	  track3.setFullyQualifiedFileName("C:\\track3.mp3");
	  track3.setTrackNumber(1);
	  tracks3.put(1, track3);
	  
	  return mapReadFromLibrary;
  }
	
  private AudioserverDocument getXmlFileFromDisk() {
    AudioserverDocument fileOnDiskDoc = AudioserverDocument.Factory.newInstance();
    AudioserverType type = fileOnDiskDoc.addNewAudioserver();
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2010);
    type.setLastUpdated(calendar);
    return fileOnDiskDoc;
  }

  private AudioserverDocument getConvertedXml() {
    AudioserverDocument convertedXmlDoc = AudioserverDocument.Factory.newInstance();
    AudioserverType type = convertedXmlDoc.addNewAudioserver();
    return convertedXmlDoc;
  }
}
