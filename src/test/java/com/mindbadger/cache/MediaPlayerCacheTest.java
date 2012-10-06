package com.mindbadger.cache;

import static org.junit.Assert.*;

import java.util.HashMap;
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
import com.mindbadger.library.MediaItem;
import com.mindbadger.library.Track;

public class MediaPlayerCacheTest {
	
	private MediaPlayerCache cache;
	
	@Before
	public void setup () {
		MockitoAnnotations.initMocks(this);
		
		cache = new MediaPlayerCache ();
	}

	@Test
	public void shouldReturnTheXMLWhenItHasBeenInitialised () {
		// Given
		AudioserverDocument doc = AudioserverDocument.Factory.newInstance();
		cache.setXML (doc);
		
		// When
		AudioserverDocument returnedDoc = cache.getXML ();

		// Then
		assertEquals (doc, returnedDoc);
	}

	
	@Test
	public void shouldThrowAnExceptionWhenGettingTheXMLBeforeItHasBeenInitialised () {
		// Given
		
		// When
		try {
			cache.getXML ();
			fail ("Expecting an Illegal State Exception");
		} catch (IllegalStateException e) {
			// Then
			assertEquals ("The Media Library XML has not been loaded", e.getMessage());
		}
	}
	
	@Test
	public void shouldReturnTheMapWhenItHasBeenInitialised () {
		// Given
		Map<String, Artist> map = new HashMap<String, Artist> ();
		cache.setMap (map);
		
		// When
		Map<String, Artist> returnedMap = cache.getMap ();

		// Then
		assertEquals (map, returnedMap);
	}

	
	@Test
	public void shouldThrowAnExceptionWhenGettingTheMapBeforeItHasBeenInitialised () {
		// Given
		
		// When
		try {
			cache.getMap ();
			fail ("Expecting an Illegal State Exception");
		} catch (IllegalStateException e) {
			// Then
			assertEquals ("The Media Library Map has not been loaded", e.getMessage());
		}
	}

	@Test
	public void shouldConvertMapIntoAnIdBasedMap () {
    // Given
	  Map<String, Artist> artists = new HashMap<String, Artist> ();
	  Artist artist = new Artist ();
	  artist.setId(1);
	  artists.put("Artist", artist);
	  Map<String, Album> albums = new HashMap<String, Album> ();
	  artist.setAlbums(albums);
	  Album album = new Album ();
	  album.setId(2);
	  albums.put("Album", album);
	  Map<Integer, Track> tracks = new HashMap<Integer, Track> ();
	  album.setTracks(tracks);
	  Track track1 = new Track ();
	  track1.setId(3);
	  tracks.put(3, track1);
    Track track2 = new Track ();
    track2.setId(4);
    tracks.put(4, track2);
	  
    cache.setMap(artists);
    
    // When
    Map<Integer, MediaItem> map = cache.getIdMap ();

    // Then
    assertEquals (4, map.size());
    assertEquals (artist, map.get(1));
    assertEquals (album, map.get(2));
    assertEquals (track1, map.get(3));
    assertEquals (track2, map.get(4));
	}
	
}
