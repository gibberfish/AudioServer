package com.mindbadger.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.MediaItem;
import com.mindbadger.library.Track;

public class MediaPlayerCache {
	private AudioserverDocument xmlDocument;
	private Map<String, Artist> libraryMap;
	private Map<Integer, MediaItem> idMap;
	
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
		
		idMap = new HashMap<Integer, MediaItem> ();
		
		List<Artist> artists = new ArrayList<Artist>(libraryMap.values());
		for (Artist artist : artists) {
		  idMap.put(artist.getId(), artist);
		  List<Album> albums = new ArrayList<Album>(artist.getAlbums().values());
		  for (Album album : albums) {
	      idMap.put(album.getId(), album);
	      List<Track> tracks = new ArrayList<Track>(album.getTracks().values());
	      for (Track track : tracks) {
	        idMap.put(track.getId(), track);
	      }
		  }
		}
  }

	public Map<String, Artist> getMap() {
		if (this.libraryMap == null) {
			throw new IllegalStateException ("The Media Library Map has not been loaded");
		}
	  return this.libraryMap;
  }

  public Map<Integer, MediaItem> getIdMap() {
    return idMap;
  }
}
