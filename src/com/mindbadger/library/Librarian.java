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
    
    Map <String, Artist> newMap = merge(mapReadFromWmpLibrary, mapRetrievedFromDisk);
    
    AudioserverDocument newXml = converter.convertMapToXML(newMap);
    
    fileWriter.writeFile(newXml);
    
    cache.setXML(newXml);
    cache.setMap(newMap);
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
  
  private Map <String, Artist> merge (Map <String, Artist> mapReadFromWmpLibrary, Map <String, Artist> mapRetrievedFromDisk) {
    for (Artist artistFromLibrary : mapReadFromWmpLibrary.values()) {
	    String artistName = artistFromLibrary.getName();
	    Artist existingArtist = mapRetrievedFromDisk.get(artistName);
	    if (existingArtist == null) {
	    	artistFromLibrary.setId(idGenerator.getNextId());
	    } else {
	    	artistFromLibrary.setId(existingArtist.getId());
	    }
	    
	    for (Album albumFromLibrary : artistFromLibrary.getAlbums().values()) {
	    	String albumName = albumFromLibrary.getName();
	    	Album existingAlbum = existingArtist == null ? null : existingArtist.getAlbums().get(albumName);
		    if (existingAlbum == null) {
		    	albumFromLibrary.setId(idGenerator.getNextId());
		    } else {
		    	albumFromLibrary.setId(existingAlbum.getId());
		    }

		    for (Track trackFromLibrary : albumFromLibrary.getTracks().values()) {
		    	Integer trackSeq = trackFromLibrary.getTrackNumber();
		    	Track existingTrack = existingAlbum == null ? null : existingAlbum.getTracks().get(trackSeq);

			    if (existingTrack == null) {
			    	trackFromLibrary.setId(idGenerator.getNextId());
			    } else {
			    	trackFromLibrary.setId(existingTrack.getId());
			    }
		    }
	    }
    }
    
    return mapReadFromWmpLibrary;
  }
  
  public AudioserverDocument getXml () {
    return this.cache.getXML();
  }
  
}
