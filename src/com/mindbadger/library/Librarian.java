package com.mindbadger.library;

import java.util.HashMap;
import java.util.Map;

import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.wmp.IReadTheWmpLibrary;
import com.mindbadger.wmp.IdGenerator;
import com.mindbadger.wmp.JacobAdapter;
import com.mindbadger.xml.FileReader;
import com.mindbadger.xml.FileWriter;
import com.mindbadger.xml.InvalidFileException;
import com.mindbadger.xml.NoMediaFileFoundException;
import com.mindbadger.xml.XMLConverter;

public class Librarian {
  private IReadTheWmpLibrary libraryReader;
  private MediaPlayerCache cache;
  private JacobAdapter adapter;
  private XMLConverter converter;
  private FileReader fileReader;
  private FileWriter fileWriter;
  private IdGenerator idGenerator;

  public Librarian (IReadTheWmpLibrary libraryReader, MediaPlayerCache cache, JacobAdapter adapter, XMLConverter converter, FileReader fileReader, FileWriter fileWriter, IdGenerator idGenerator) {
    System.out.println("In the arg constructor of the Librarian");
    this.idGenerator = idGenerator;
    this.cache = cache;

    Map <String, Artist> mapReadFromWmpLibrary = libraryReader.readLibrary(adapter);
    
    Map <String, Artist> mapRetrievedFromDisk = null;
    try {
      AudioserverDocument doc = fileReader.readFile();
      mapRetrievedFromDisk = converter.convertXMLToMap(doc);
    } catch (NoMediaFileFoundException e) {
      mapRetrievedFromDisk = new HashMap <String, Artist> ();
    } catch (InvalidFileException e) {
      mapRetrievedFromDisk = new HashMap <String, Artist> ();
    }
    
    ensureNextIdIsGreaterThanLargestValueAlreadyInFile (mapRetrievedFromDisk);
    
    boolean libraryHasChanged = doesMergingTheMapsShowAnyChanges(mapReadFromWmpLibrary, mapRetrievedFromDisk);
    
    AudioserverDocument xmlRepresentationOfCurrentLibrary = converter.convertMapToXML(mapReadFromWmpLibrary);

    if (libraryHasChanged) {
      fileWriter.writeFile(xmlRepresentationOfCurrentLibrary);
    }
    
    cache.setXML(xmlRepresentationOfCurrentLibrary);
    cache.setMap(mapReadFromWmpLibrary);
  }
  
  public AudioserverDocument getXml () {
    return this.cache.getXML();
  }
  
  private void ensureNextIdIsGreaterThanLargestValueAlreadyInFile (Map <String, Artist> mapRetrievedFromDisk) {
  	long largestId = 0;
  	
    for (Artist artist : mapRetrievedFromDisk.values()) {
    	long artistId = artist.getId();
    	largestId = largestId < artistId ? artistId : largestId;
    	
    	for (Album album : artist.getAlbums().values()) {
    		long albumId = album.getId();
    		largestId = largestId < albumId ? albumId : largestId;
    		
    		for (Track track : album.getTracks().values()) {
    			long trackId = track.getId();
    			largestId = largestId < trackId ? trackId : largestId;
    		}
    	}
    }
    
    idGenerator.seedCurrentValue (largestId);
  }
  
  private boolean doesMergingTheMapsShowAnyChanges (Map <String, Artist> mapReadFromWmpLibrary, Map <String, Artist> mapRetrievedFromDisk) {
    boolean libraryChanged = mergeArtistsInLibrary(mapReadFromWmpLibrary, mapRetrievedFromDisk);
    return libraryChanged || areThereAnyLibraryItemsThatHaveBeenRemoved (mapReadFromWmpLibrary, mapRetrievedFromDisk);
    //return libraryChanged;
  }

  private boolean mergeArtistsInLibrary(Map<String, Artist> mapReadFromWmpLibrary, Map<String, Artist> mapRetrievedFromDisk) {
    boolean changes = false;
    for (Artist artistFromLibrary : mapReadFromWmpLibrary.values()) {
	    String artistName = artistFromLibrary.getName();
	    Artist existingArtist = mapRetrievedFromDisk.get(artistName);
	    if (existingArtist == null) {
	    	long nextId = idGenerator.getNextId();
        artistFromLibrary.setId(nextId);
	    	changes = true;
	    	System.out.println("Setting new ID " + nextId + " for artist " + artistName);
	    } else {
	    	artistFromLibrary.setId(existingArtist.getId());
	    }
	    
	    changes = mergeAlbumsForArtist(artistFromLibrary, existingArtist) || changes;
    }
    
    return changes;
  }

  private boolean mergeAlbumsForArtist(Artist artistFromLibrary, Artist existingArtist) {
    boolean changes = false;
    for (Album albumFromLibrary : artistFromLibrary.getAlbums().values()) {
    	String albumName = albumFromLibrary.getName();
    	Album existingAlbum = existingArtist == null ? null : existingArtist.getAlbums().get(albumName);
      if (existingAlbum == null) {
      	long nextId = idGenerator.getNextId();
        albumFromLibrary.setId(nextId);
      	changes = true;
      	System.out.println("Setting new ID " + nextId + " for artist " + albumName);
      } else {
      	albumFromLibrary.setId(existingAlbum.getId());
      }

      changes = mergeTracksForAlbum(albumFromLibrary, existingAlbum) || changes;
    }
    return changes;
  }

  private boolean mergeTracksForAlbum(Album albumFromLibrary, Album existingAlbum) {
    boolean changes = false;
    for (Track trackFromLibrary : albumFromLibrary.getTracks().values()) {
    	Integer trackSeq = trackFromLibrary.getTrackNumber();
    	Track existingTrack = existingAlbum == null ? null : existingAlbum.getTracks().get(trackSeq);

      if (existingTrack == null) {
      	trackFromLibrary.setId(idGenerator.getNextId());
      	changes = true;
      } else {
      	trackFromLibrary.setId(existingTrack.getId());
      }
    }
    return changes;
  }
  
  private boolean areThereAnyLibraryItemsThatHaveBeenRemoved(Map<String, Artist> mapReadFromWmpLibrary, Map<String, Artist> mapRetrievedFromDisk) {
    for (Artist artistFromDisk : mapRetrievedFromDisk.values()) {
      String artistName = artistFromDisk.getName();
      Artist artistFromLibrary = mapReadFromWmpLibrary.get(artistName);
      if (artistFromLibrary == null) {
        return true;
      }
      
      if (areThereAnyAlbumsThatHaveBeenRemovedFromArtist(artistFromDisk, artistFromLibrary)) return true;
    }
    return false;
  }

  private boolean areThereAnyAlbumsThatHaveBeenRemovedFromArtist(Artist artistFromDisk, Artist artistFromLibrary) {
    for (Album albumFromDisk : artistFromDisk.getAlbums().values()) {
      String albumName = albumFromDisk.getName();
      Album albumFromLibrary = artistFromLibrary == null ? null : artistFromLibrary.getAlbums().get(albumName);
      if (albumFromLibrary == null) {
        return true;
      }

      if (areThereAnyTracksThatHaveBeenRemovedFromAlbum(albumFromDisk, albumFromLibrary)) return true;
    }
    return false;
  }

  private boolean areThereAnyTracksThatHaveBeenRemovedFromAlbum(Album albumFromDisk, Album albumFromLibrary) {
    for (Track trackFromDisk : albumFromDisk.getTracks().values()) {
      Integer trackSeq = trackFromDisk.getTrackNumber();
      Track existingTrack = albumFromLibrary == null ? null : albumFromLibrary.getTracks().get(trackSeq);

      if (existingTrack == null) {
        return true;
      }
    }
    return false;
  }

}

