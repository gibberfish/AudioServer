package com.mindbadger.cache;

import java.util.Map;

import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.library.Artist;

public class MediaPlayerCache {
	private AudioserverDocument xmlDocument;
	private Map<String, Artist> libraryMap;
	
	public MediaPlayerCache () {
	  System.out.println("In the constructor of the MediaPlayerCache");
	}
	
	public AudioserverDocument getXML() {
		if (xmlDocument == null) {
			throw new IllegalStateException ("The Media Library XML has not been loaded");
		}
		
		return xmlDocument;
  }

	public void setXML(AudioserverDocument doc) {
		this.xmlDocument = doc;
  }

	public void setMap(Map<String, Artist> map) {
		this.libraryMap = map;
  }

	public Map<String, Artist> getMap() {
		if (this.libraryMap == null) {
			throw new IllegalStateException ("The Media Library Map has not been loaded");
		}
	  return this.libraryMap;
  }
}
