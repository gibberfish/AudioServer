package com.mindbadger.cache;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.library.Artist;

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

}
