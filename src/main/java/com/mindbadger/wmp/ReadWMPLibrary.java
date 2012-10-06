package com.mindbadger.wmp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.Track;
import com.mindbadger.spring.controller.RegisterController;

public class ReadWMPLibrary implements IReadTheWmpLibrary {
  Logger logger = Logger.getLogger(ReadWMPLibrary.class);
  
  public ReadWMPLibrary () {
    logger.debug("In the constructor of the ReadWMPLibrary");
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
        
        trackName = removeTroublesomeCharacters(trackName);
        artistName = removeTroublesomeCharacters(artistName);
        albumName = removeTroublesomeCharacters(albumName);
        
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
  
  public String removeTroublesomeCharacters(String inString)
  {
      if (inString == null) return null;

      StringBuilder newString = new StringBuilder();
      char ch;

      for (int i = 0; i < inString.length(); i++)
      {

          ch = inString.charAt(i);
          // remove any characters outside the valid UTF-8 range as well as all control characters
          // except tabs and new lines
          if ((ch < 0x00FD && ch > 0x001F) || ch == '\t' || ch == '\n' || ch == '\r')
          {
              newString.append(ch);
          }
      }
      return newString.toString();
  }
}
