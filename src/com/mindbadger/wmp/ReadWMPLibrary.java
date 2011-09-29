package com.mindbadger.wmp;

import java.util.HashMap;
import java.util.Map;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.Track;

public class ReadWMPLibrary implements IReadTheWmpLibrary {

  public ReadWMPLibrary () {
    System.out.println("In the constructor of the ReadWMPLibrary");
  }
  
  public Map<String, Artist> readLibrary (JacobAdapter adapter) {
    
    Map<String, Artist> library = new HashMap<String, Artist> ();
    IdGenerator idGenerator = new IdGenerator();
    
    Dispatch mediaCollection = adapter.getDispatch("mediaCollection");
    Dispatch playlist = adapter.call(mediaCollection, "getAll");
    Variant countVariant = adapter.getVariant(playlist, "count"); 
    
    int count = countVariant.getInt();
    
    for (int i=0; i < count; i++) {
      Dispatch playlistItem = adapter.call(playlist, "item", i);
      Variant mediaType = adapter.getVariant(playlistItem, "getItemInfo", "MediaType");
      
      if ("audio".equals(mediaType.toString())) {
        String trackName = adapter.getVariant(playlistItem, "name").toString();
        String sourceUrl = adapter.getVariant(playlistItem, "sourceURL").toString ();
        String artistName = adapter.getVariant(playlistItem, "getItemInfo", "WM/AlbumArtist").toString();
        String albumName = adapter.getVariant(playlistItem, "getItemInfo", "WM/AlbumTitle").toString();
        String trackNumberString = adapter.getVariant(playlistItem, "getItemInfo", "WM/TrackNumber").toString();
        Integer trackNumber = null;
        try {
          trackNumber = Integer.parseInt(trackNumberString);
        } catch (NumberFormatException e) {
          trackNumber = 0;
        }
        
        Artist artist = library.get(artistName);
        if (artist == null) {
          artist = new Artist ();
          artist.setName(artistName);
          artist.setId(idGenerator.getNextId());
          artist.setAlbums(new HashMap<String, Album> ());
          library.put(artistName, artist);
        }
        
        Album album = artist.getAlbums().get(albumName);
        if (album == null) {
          album = new Album ();
          album.setName(albumName);
          album.setId(idGenerator.getNextId());
          album.setTracks(new HashMap<Integer, Track> ());
          artist.getAlbums().put(albumName, album);
        }
        
        Track track = new Track ();
        track.setTrackNumber(trackNumber);
        track.setName(trackName);
        track.setId(idGenerator.getNextId());
        track.setFullyQualifiedFileName(sourceUrl);
        album.getTracks().put(trackNumber, track);
      }
    }
      
    return library;
  }
}
