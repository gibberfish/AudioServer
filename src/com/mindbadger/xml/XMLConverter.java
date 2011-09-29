package com.mindbadger.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mindbadger.audioserver.schema.AlbumType;
import com.mindbadger.audioserver.schema.ArtistType;
import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.audioserver.schema.AudioserverType;
import com.mindbadger.audioserver.schema.TrackType;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.Track;

public class XMLConverter {

  public AudioserverDocument convertMapToXML(Map<String, Artist> map) {
    AudioserverDocument doc = AudioserverDocument.Factory.newInstance();
    AudioserverType type = doc.addNewAudioserver();
    
    for (Artist artist : map.values()) {
      ArtistType artistXml = type.addNewArtist();
      artistXml.setName(artist.getName());
      artistXml.setId(artist.getId());
      
      for (Album album : artist.getAlbums().values()) {
        AlbumType albumXml = artistXml.addNewAlbum();
        albumXml.setName(album.getName());
        albumXml.setId(album.getId());

        for (Track track : album.getTracks().values()) {
          TrackType trackXml = albumXml.addNewTrack();
          trackXml.setSeq(track.getTrackNumber());
          trackXml.setName(track.getName());
          trackXml.setLocation(track.getFullyQualifiedFileName());
          trackXml.setId(track.getId());
        }
      }
      
    }
    
    return doc;
  }

  public Map<String, Artist> convertXMLToMap(AudioserverDocument doc) {
    Map<String, Artist> map = new HashMap<String, Artist> ();
    AudioserverType type = doc.getAudioserver();
    
    for (ArtistType artistXml : type.getArtistList()) {
      Artist artist = new Artist ();
      String artistName = artistXml.getName();
      map.put(artistName, artist);
      artist.setName(artistName);
      artist.setId(artistXml.getId());
      
      Map<String, Album> albums = new HashMap<String, Album> ();
      artist.setAlbums(albums);
      
      for (AlbumType albumXml : artistXml.getAlbumList()) {
        Album album = new Album ();
        String albumName = albumXml.getName();
        
        albums.put(albumName, album);
        album.setName(albumName);
        album.setId(albumXml.getId());
        
        Map <Integer, Track> tracks = new HashMap <Integer, Track> ();
        album.setTracks(tracks);
        
        for (TrackType trackXml : albumXml.getTrackList()) {
          Track track = new Track ();
          String trackSeq = Long.toString(trackXml.getSeq());
          
          tracks.put(Integer.parseInt(trackSeq), track);
          track.setName(trackXml.getName());
          track.setTrackNumber(Integer.parseInt(trackSeq));
          track.setId(trackXml.getId());
          track.setFullyQualifiedFileName(trackXml.getLocation());
        }
      }
    }
    
    return map;
  }

}
