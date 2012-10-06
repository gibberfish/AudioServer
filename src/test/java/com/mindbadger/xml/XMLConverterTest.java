package com.mindbadger.xml;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.mindbadger.audioserver.schema.AlbumType;
import com.mindbadger.audioserver.schema.ArtistType;
import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.audioserver.schema.AudioserverType;
import com.mindbadger.audioserver.schema.TrackType;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.Track;

public class XMLConverterTest {
  private XMLConverter converter;
  
  @Before
  public void setup () {
    MockitoAnnotations.initMocks(this);
    converter = new XMLConverter ();
  }
  
  @Test
  public void shouldConvertMapToXML () {
    // Given
    Map<String, Artist> map = new HashMap<String, Artist> ();
    
    map.put("Artist1", getArtist1());
    map.put("Artist2", getArtist2());
    
    // When
    AudioserverDocument doc = converter.convertMapToXML (map);
    
    // Then
    AudioserverType audioServerType = doc.getAudioserver();
    List<ArtistType> artists = audioServerType.getArtistList();
    ArtistType artist1 = artists.get(0);
    assertEquals ("Artist1", artist1.getName());
    assertEquals (1, artist1.getId());
    List<AlbumType> artist1Albums = artist1.getAlbumList();
    AlbumType artist1Album1 = artist1Albums.get(1);
    assertEquals ("Album1", artist1Album1.getName());
    assertEquals (3, artist1Album1.getId());
    
    List<TrackType> artist1Album1Tracks = artist1Album1.getTrackList();
    TrackType artist1Album1Track1 = artist1Album1Tracks.get(0);
    assertEquals ("Track1", artist1Album1Track1.getName());
    assertEquals (1, artist1Album1Track1.getSeq());
    assertEquals (6, artist1Album1Track1.getId());
    assertEquals ("C:\\track1_1.mp3", artist1Album1Track1.getLocation());

    TrackType artist1Album1Track2 = artist1Album1Tracks.get(1);
    assertEquals ("Track2", artist1Album1Track2.getName());
    assertEquals (2, artist1Album1Track2.getSeq());
    assertEquals (7, artist1Album1Track2.getId());
    assertEquals ("C:\\track1_2.mp3", artist1Album1Track2.getLocation());

    TrackType artist1Album1Track3 = artist1Album1Tracks.get(2);
    assertEquals ("Track3", artist1Album1Track3.getName());
    assertEquals (3, artist1Album1Track3.getSeq());
    assertEquals (8, artist1Album1Track3.getId());
    assertEquals ("C:\\track1_3.mp3", artist1Album1Track3.getLocation());
    
    AlbumType artist1Album2 = artist1Albums.get(0);
    assertEquals ("Album2", artist1Album2.getName());
    assertEquals (4, artist1Album2.getId());

    List<TrackType> artist1Album2Tracks = artist1Album2.getTrackList();
    TrackType artist1Album2Track1 = artist1Album2Tracks.get(0);
    assertEquals ("Track1", artist1Album2Track1.getName());
    assertEquals (1, artist1Album2Track1.getSeq());
    assertEquals (9, artist1Album2Track1.getId());
    assertEquals ("C:\\track2_1.mp3", artist1Album2Track1.getLocation());

    TrackType artist1Album2Track2 = artist1Album2Tracks.get(1);
    assertEquals ("Track2", artist1Album2Track2.getName());
    assertEquals (2, artist1Album2Track2.getSeq());
    assertEquals (10, artist1Album2Track2.getId());
    assertEquals ("C:\\track2_2.mp3", artist1Album2Track2.getLocation());
    
    ArtistType artist2 = artists.get(1);
    assertEquals ("Artist2", artist2.getName());
    assertEquals (2, artist2.getId());

    List<AlbumType> artist2Albums = artist2.getAlbumList();
    AlbumType artist2Album1 = artist2Albums.get(0);
    assertEquals ("Album3", artist2Album1.getName());
    assertEquals (5, artist2Album1.getId());
    
    List<TrackType> artist2Album1Tracks = artist2Album1.getTrackList();
    TrackType artist2Album1Track1 = artist2Album1Tracks.get(0);
    assertEquals ("Track1", artist2Album1Track1.getName());
    assertEquals (1, artist2Album1Track1.getSeq());
    assertEquals (11, artist2Album1Track1.getId());
    assertEquals ("C:\\track3_1.mp3", artist2Album1Track1.getLocation());
  }

  private Artist getArtist1() {
    Artist artist1 = new Artist ();
    artist1.setId(1);
    artist1.setName("Artist1");
    
    Map<String, Album> albums = new HashMap<String, Album> ();
    albums.put("Album1", getAlbum1());
    albums.put("Album2", getAlbum2());
    
    artist1.setAlbums(albums);
    return artist1;
  }

	private Artist getArtist2() {
    Artist artist2 = new Artist ();
    artist2.setId(2);
    artist2.setName("Artist2");
    
    Map<String, Album> albums = new HashMap<String, Album> ();
    albums.put("Album3", getAlbum3());
    
    artist2.setAlbums(albums);
    return artist2;
  }
	
	private Album getAlbum1() {
		Album album1 = new Album ();
		album1.setId(3);
		album1.setName("Album1");
		
		Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
		tracks.put(1, getTrack1_1());
		tracks.put(2, getTrack1_2());
		tracks.put(3, getTrack1_3());
		
		album1.setTracks(tracks);
		return album1;
	}

	private Album getAlbum2() {
		Album album2 = new Album ();
		album2.setId(4);
		album2.setName("Album2");
		
		Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
		tracks.put(1, getTrack2_1());
		tracks.put(2, getTrack2_2());
		
		album2.setTracks(tracks);
		return album2;
	}

	private Album getAlbum3() {
		Album album3 = new Album ();
		album3.setId(5);
		album3.setName("Album3");
		
		Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
		tracks.put(1, getTrack3_1());
		
		album3.setTracks(tracks);
		return album3;
  }

  private Track getTrack1_1() {
  	Track track1 = new Track ();
  	track1.setId(6);
  	track1.setName("Track1");
  	track1.setTrackNumber(1);
  	track1.setFullyQualifiedFileName("C:\\track1_1.mp3");
  	return track1;
  }
  
  private Track getTrack1_2() {
  	Track track2 = new Track ();
  	track2.setId(7);
  	track2.setName("Track2");
  	track2.setTrackNumber(2);
  	track2.setFullyQualifiedFileName("C:\\track1_2.mp3");
  	return track2;
  }
  
  private Track getTrack1_3() {
  	Track track3 = new Track ();
  	track3.setId(8);
  	track3.setName("Track3");
  	track3.setTrackNumber(3);
  	track3.setFullyQualifiedFileName("C:\\track1_3.mp3");
  	return track3;
  }
  
  private Track getTrack2_1() {
  	Track track1 = new Track ();
  	track1.setId(9);
  	track1.setName("Track1");
  	track1.setTrackNumber(1);
  	track1.setFullyQualifiedFileName("C:\\track2_1.mp3");
  	return track1;
  }
  
  private Track getTrack2_2() {
  	Track track2 = new Track ();
  	track2.setId(10);
  	track2.setName("Track2");
  	track2.setTrackNumber(2);
  	track2.setFullyQualifiedFileName("C:\\track2_2.mp3");
  	return track2;
  }
  
  private Track getTrack3_1() {
  	Track track1 = new Track ();
  	track1.setId(11);
  	track1.setName("Track1");
  	track1.setTrackNumber(1);
  	track1.setFullyQualifiedFileName("C:\\track3_1.mp3");
  	return track1;
  }

	@Test
  public void shouldConvertXMLToMap () {
    // Given
    AudioserverDocument doc = AudioserverDocument.Factory.newInstance();
    AudioserverType type = doc.addNewAudioserver();
    ArtistType artistXml = type.addNewArtist();
    artistXml.setName("Artist1");
    artistXml.setId(1);
    AlbumType albumXml = artistXml.addNewAlbum();
    albumXml.setName("Album1");
    albumXml.setId(2);
    TrackType trackXml = albumXml.addNewTrack();
    trackXml.setName("Track1");
    trackXml.setSeq(1);
    trackXml.setId(3);
    trackXml.setLocation("C:\\track1.mp3");
    
    // When
     Map<String, Artist> map = converter.convertXMLToMap (doc);
    
    // Then
    assertEquals (1, map.size());
    Artist artist = map.get("Artist1");
    assertEquals ("Artist1", artist.getName());
    assertEquals (1, artist.getId());
    
    Map<String, Album> albums = artist.getAlbums();
    assertEquals (1, albums.size());
    Album album = albums.get("Album1");
    
    assertEquals ("Album1", album.getName());
    assertEquals (2, album.getId());
    
    Map<Integer, Track> tracks = album.getTracks();
    assertEquals (1, tracks.size());
    
    Track track = tracks.get(1);
    assertEquals ("Track1", track.getName());
    assertEquals (1, track.getTrackNumber());
    assertEquals (3, track.getId());
    assertEquals ("C:\\track1.mp3", track.getFullyQualifiedFileName());
    assertEquals ("Artist1", track.getArtist());
    assertEquals ("Album1", track.getAlbum());
  }
}
