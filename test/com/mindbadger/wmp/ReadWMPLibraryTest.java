package com.mindbadger.wmp;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.mindbadger.library.Album;
import com.mindbadger.library.Artist;
import com.mindbadger.library.Track;

public class ReadWMPLibraryTest {
	private static final String VIDEO_1_TITLE = "myVideo1";
  private static final String VIDEO_2_URL = "C:\\videos\\myVid2.wmv";
  private static final String VIDEO_1_URL = "C:\\videos\\myVid.wmv";
  private static final String MEDIA_TYPE_AUDIO = "audio";
  private static final String MEDIA_TYPE_VIDEO = "video";

  private IReadTheWmpLibrary reader;
	
	@Mock private JacobAdapter mockAdapter;
	@Mock	private Dispatch mockMediaCollectionDispatch;
	@Mock private Dispatch mockPlaylistDispatch;
	@Mock private Variant mockPlaylistCountVariant;
	
	@Mock private Dispatch mockPlaylistItem1;
	@Mock private Variant mockPlaylistItemInfo1;
	@Mock private Variant mockPlaylistName1;
	@Mock private Variant mockPlaylistSourceUrl1;
	@Mock private Variant mockPlaylistArtist1;
	@Mock private Variant mockPlaylistTitle1;
	@Mock private Variant mockPlaylistTrack1;

  @Mock private Dispatch mockPlaylistItem2;
  @Mock private Variant mockPlaylistItemInfo2;
  @Mock private Variant mockPlaylistName2;
  @Mock private Variant mockPlaylistSourceUrl2;
  @Mock private Variant mockPlaylistArtist2;
  @Mock private Variant mockPlaylistTitle2;
  @Mock private Variant mockPlaylistTrack2;
	
  @Mock private Dispatch mockPlaylistItem3;
  @Mock private Variant mockPlaylistItemInfo3;
  @Mock private Variant mockPlaylistName3;
  @Mock private Variant mockPlaylistSourceUrl3;
  @Mock private Variant mockPlaylistArtist3;
  @Mock private Variant mockPlaylistTitle3;
  @Mock private Variant mockPlaylistTrack3;
  
  @Mock private Dispatch mockPlaylistItem4;
  @Mock private Variant mockPlaylistItemInfo4;
  @Mock private Variant mockPlaylistName4;
  @Mock private Variant mockPlaylistSourceUrl4;
  @Mock private Variant mockPlaylistArtist4;
  @Mock private Variant mockPlaylistTitle4;
  @Mock private Variant mockPlaylistTrack4;
  
  @Mock private Dispatch mockPlaylistItem5;
  @Mock private Variant mockPlaylistItemInfo5;
  @Mock private Variant mockPlaylistName5;
  @Mock private Variant mockPlaylistSourceUrl5;
  @Mock private Variant mockPlaylistArtist5;
  @Mock private Variant mockPlaylistTitle5;
  @Mock private Variant mockPlaylistTrack5;
  
  @Mock private Dispatch mockPlaylistItem6;
  @Mock private Variant mockPlaylistItemInfo6;
  @Mock private Variant mockPlaylistName6;
  @Mock private Variant mockPlaylistSourceUrl6;
  @Mock private Variant mockPlaylistArtist6;
  @Mock private Variant mockPlaylistTitle6;
  @Mock private Variant mockPlaylistTrack6;
  
  @Mock private Dispatch mockPlaylistItem7;
  @Mock private Variant mockPlaylistItemInfo7;
  @Mock private Variant mockPlaylistName7;
  @Mock private Variant mockPlaylistSourceUrl7;
  @Mock private Variant mockPlaylistArtist7;
  @Mock private Variant mockPlaylistTitle7;
  @Mock private Variant mockPlaylistTrack7;
  
  @Mock private Dispatch mockPlaylistItem8;
  @Mock private Variant mockPlaylistItemInfo8;
  @Mock private Variant mockPlaylistName8;
  @Mock private Variant mockPlaylistSourceUrl8;
  @Mock private Variant mockPlaylistArtist8;
  @Mock private Variant mockPlaylistTitle8;
  @Mock private Variant mockPlaylistTrack8;
  
  @Mock private Dispatch mockPlaylistItem9;
  @Mock private Variant mockPlaylistItemInfo9;
  @Mock private Variant mockPlaylistName9;
  @Mock private Variant mockPlaylistSourceUrl9;
  @Mock private Variant mockPlaylistArtist9;
  @Mock private Variant mockPlaylistTitle9;
  @Mock private Variant mockPlaylistTrack9;
  
  @Mock private Dispatch mockPlaylistItem10;
  @Mock private Variant mockPlaylistItemInfo10;
  @Mock private Variant mockPlaylistName10;
  @Mock private Variant mockPlaylistSourceUrl10;
  @Mock private Variant mockPlaylistArtist10;
  @Mock private Variant mockPlaylistTitle10;
  @Mock private Variant mockPlaylistTrack10;
  
  @Mock private Dispatch mockPlaylistItem11;
  @Mock private Variant mockPlaylistItemInfo11;
  @Mock private Variant mockPlaylistName11;
  @Mock private Variant mockPlaylistSourceUrl11;
  @Mock private Variant mockPlaylistArtist11;
  @Mock private Variant mockPlaylistTitle11;
  @Mock private Variant mockPlaylistTrack11;
  
	@Before
	public void setup () {
		MockitoAnnotations.initMocks(this);
		
		reader = new ReadWMPLibrary ();
	}
	
	@Test
	public void shouldReturnAMapRepresentingTheLibrary () {
		// Given
		givenIHaveAWMPLibrary();
		givenIHaveMediaItem1(MEDIA_TYPE_VIDEO, VIDEO_1_URL, VIDEO_1_TITLE, "videoartist", "my video album", "1");
		givenIHaveMediaItem2 (MEDIA_TYPE_VIDEO, VIDEO_2_URL, "myVideo2", "videoartist", "my video album", "2");
		givenIHaveMediaItem3 (MEDIA_TYPE_AUDIO, "C:\\artist1\\album1\\1_1_track1.mp3", "1_1_track1", "artist1", "album1", "1");
		givenIHaveMediaItem4 (MEDIA_TYPE_AUDIO, "C:\\artist1\\album1\\1_1_track2.mp3", "1_1_track2", "artist1", "album1", "2");
		givenIHaveMediaItem5 (MEDIA_TYPE_AUDIO, "C:\\artist1\\album2\\1_2_track1.mp3", "1_2_track1", "artist1", "album2", "1");
		givenIHaveMediaItem6 (MEDIA_TYPE_AUDIO, "C:\\artist1\\album2\\1_2_track2.mp3", "1_2_track2", "artist1", "album2", "2");
		givenIHaveMediaItem7 (MEDIA_TYPE_AUDIO, "C:\\artist1\\album2\\1_2_track3.mp3", "1_2_track3", "artist1", "album2", "3");
		givenIHaveMediaItem8 (MEDIA_TYPE_AUDIO, "C:\\artist2\\album3\\2_3_track1.mp3", "2_3_track1", "artist2", "album3", "1");
		givenIHaveMediaItem9 (MEDIA_TYPE_AUDIO, "C:\\artist2\\album3\\2_3_track2.mp3", "2_3_track2", "artist2", "album3", "2");
		givenIHaveMediaItem10 (MEDIA_TYPE_AUDIO, "C:\\artist2\\album3\\2_3_track3.mp3", "2_3_track3", "artist2", "album3", "3");
		givenIHaveMediaItem11 (MEDIA_TYPE_AUDIO, "C:\\artist2\\album3\\2_3_track4.mp3", "2_3_track4", "artist2", "album3", "4");
		
		// When
		Map<String, Artist> resultsMap = reader.readLibrary(mockAdapter);
		
		// Then
		assertEquals (2, resultsMap.size());
		assertArtist1AndHisAlbumsAreInTheMap(resultsMap);
		assertArtist2AndHisAlbumsAreInTheMap(resultsMap);
		assertThatTheVideoArtistIsNotContainedInTheMap(resultsMap);
	}

  private void assertThatTheVideoArtistIsNotContainedInTheMap(Map<String, Artist> results) {
    assertNull (results.get("videoartist"));
  }

	private void assertArtist1AndHisAlbumsAreInTheMap(Map<String, Artist> results) {
	  Artist artist1 = results.get("artist1");
	  assertEquals ("artist1", artist1.getName());
	  assertEquals (1, artist1.getId());
	  validateArtist1Albums(artist1.getAlbums());
	}
	
	private void validateArtist1Albums(Map<String, Album> artist1albums) {
	  assertEquals (2, artist1albums.size());
	  validateAlbum1(artist1albums.get("album1"));
	  validateAlbum2(artist1albums.get("album2"));
	}

	private void validateAlbum1(Album artist1album1) {
	  assertEquals ("album1", artist1album1.getName());
	  assertEquals (2, artist1album1.getId());
	  validateAlbum1Tracks(artist1album1.getTracks());
	}

  private void validateAlbum1Tracks(Map<Integer, Track> artist1album1tracks) {
    assertEquals (2, artist1album1tracks.size());
    
    Track track1 = artist1album1tracks.get(1);
    assertEquals("1_1_track1", track1.getName());
    assertEquals("C:\\artist1\\album1\\1_1_track1.mp3", track1.getFullyQualifiedFileName());
    assertEquals(3, track1.getId());
    assertEquals(1, track1.getTrackNumber());
    
    Track track2 = artist1album1tracks.get(2);
    assertEquals("1_1_track2", track2.getName());
    assertEquals("C:\\artist1\\album1\\1_1_track2.mp3", track2.getFullyQualifiedFileName());
    assertEquals(4, track2.getId());
    assertEquals(2, track2.getTrackNumber());
  }
	
	private void validateAlbum2(Album artist1album2) {
	  assertEquals ("album2", artist1album2.getName());
	  assertEquals (5, artist1album2.getId());
	  validateAlbum2Tracks(artist1album2.getTracks());
	}
	
	private void validateAlbum2Tracks(Map<Integer, Track> tracks) {
	  assertEquals (3, tracks.size());
	  
	  Track track1 = tracks.get(1);
	  assertEquals("1_2_track1", track1.getName());
	  assertEquals("C:\\artist1\\album2\\1_2_track1.mp3", track1.getFullyQualifiedFileName());
	  assertEquals(6, track1.getId());
	  assertEquals(1, track1.getTrackNumber());
	  
	  Track track2 = tracks.get(2);
	  assertEquals("1_2_track2", track2.getName());
	  assertEquals("C:\\artist1\\album2\\1_2_track2.mp3", track2.getFullyQualifiedFileName());
	  assertEquals(7, track2.getId());
	  assertEquals(2, track2.getTrackNumber());
	  
	  Track track3 = tracks.get(3);
	  assertEquals("1_2_track3", track3.getName());
	  assertEquals("C:\\artist1\\album2\\1_2_track3.mp3", track3.getFullyQualifiedFileName());
	  assertEquals(8, track3.getId());
	  assertEquals(3, track3.getTrackNumber());
	}

	 private void assertArtist2AndHisAlbumsAreInTheMap(Map<String, Artist> results) {
	   Artist artist2 = results.get("artist2");
	    assertEquals ("artist2", artist2.getName());
	    assertEquals (9, artist2.getId());
	    validateArtist2Albums(artist2.getAlbums());
	  }

	private void validateArtist2Albums(Map<String, Album> albums) {
    assertEquals (1, albums.size());
    validateAlbum3(albums.get("album3"));
  }

  private void validateAlbum3(Album album3) {
    assertEquals ("album3", album3.getName());
    assertEquals (10, album3.getId());
    validateAlbum3Tracks(album3.getTracks());
  }

  private void validateAlbum3Tracks(Map<Integer, Track> tracks) {
    assertEquals (4, tracks.size());
    
    Track track1 = tracks.get(1);
    assertEquals("2_3_track1", track1.getName());
    assertEquals("C:\\artist2\\album3\\2_3_track1.mp3", track1.getFullyQualifiedFileName());
    assertEquals(11, track1.getId());
    assertEquals(1, track1.getTrackNumber());
    
    Track track2 = tracks.get(2);
    assertEquals("2_3_track2", track2.getName());
    assertEquals("C:\\artist2\\album3\\2_3_track2.mp3", track2.getFullyQualifiedFileName());
    assertEquals(12, track2.getId());
    assertEquals(2, track2.getTrackNumber());
    
    Track track3 = tracks.get(3);
    assertEquals("2_3_track3", track3.getName());
    assertEquals("C:\\artist2\\album3\\2_3_track3.mp3", track3.getFullyQualifiedFileName());
    assertEquals(13, track3.getId());
    assertEquals(3, track3.getTrackNumber());
    
    Track track4 = tracks.get(4);
    assertEquals("2_3_track4", track4.getName());
    assertEquals("C:\\artist2\\album3\\2_3_track4.mp3", track4.getFullyQualifiedFileName());
    assertEquals(14, track4.getId());
    assertEquals(4, track4.getTrackNumber());
  }
	
  
  
  
  
	private void givenIHaveMediaItem1(String type, String url, String title, String artist, String album, String track) {
	  when (mockAdapter.call(mockPlaylistDispatch, "item", 0)).thenReturn(mockPlaylistItem1);
		when (mockAdapter.getVariant(mockPlaylistItem1, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo1);
		when (mockPlaylistItemInfo1.toString()).thenReturn(type);
		when (mockAdapter.getVariant(mockPlaylistItem1, "name")).thenReturn(mockPlaylistName1);
		when (mockPlaylistName1.toString()).thenReturn(title);
		when (mockAdapter.getVariant(mockPlaylistItem1, "sourceURL")).thenReturn(mockPlaylistSourceUrl1);
		when (mockPlaylistSourceUrl1.toString()).thenReturn(url);
		when (mockAdapter.getVariant(mockPlaylistItem1, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist1);
		when (mockPlaylistArtist1.toString()).thenReturn(artist);
		when (mockAdapter.getVariant(mockPlaylistItem1, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle1);
		when (mockPlaylistTitle1.toString()).thenReturn(album);
		when (mockAdapter.getVariant(mockPlaylistItem1, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack1);
		when (mockPlaylistTrack1.toString()).thenReturn(track);
  }

   private void givenIHaveMediaItem2(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 1)).thenReturn(mockPlaylistItem2);
      when (mockAdapter.getVariant(mockPlaylistItem2, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo2);
      when (mockPlaylistItemInfo2.toString()).thenReturn(type);
      when (mockAdapter.getVariant(mockPlaylistItem2, "name")).thenReturn(mockPlaylistName2);
      when (mockPlaylistName2.toString()).thenReturn(title);
      when (mockAdapter.getVariant(mockPlaylistItem2, "sourceURL")).thenReturn(mockPlaylistSourceUrl2);
      when (mockPlaylistSourceUrl2.toString()).thenReturn(url);
      when (mockAdapter.getVariant(mockPlaylistItem2, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist2);
      when (mockPlaylistArtist2.toString()).thenReturn(artist);
      when (mockAdapter.getVariant(mockPlaylistItem2, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle2);
      when (mockPlaylistTitle2.toString()).thenReturn(album);
      when (mockAdapter.getVariant(mockPlaylistItem2, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack2);
      when (mockPlaylistTrack2.toString()).thenReturn(track);
    }
   
   private void givenIHaveMediaItem3(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 2)).thenReturn(mockPlaylistItem3);
     when (mockAdapter.getVariant(mockPlaylistItem3, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo3);
     when (mockPlaylistItemInfo3.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem3, "name")).thenReturn(mockPlaylistName3);
     when (mockPlaylistName3.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem3, "sourceURL")).thenReturn(mockPlaylistSourceUrl3);
     when (mockPlaylistSourceUrl3.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem3, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist3);
     when (mockPlaylistArtist3.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem3, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle3);
     when (mockPlaylistTitle3.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem3, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack3);
     when (mockPlaylistTrack3.toString()).thenReturn(track);
   }
   
   private void givenIHaveMediaItem4(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 3)).thenReturn(mockPlaylistItem4);
     when (mockAdapter.getVariant(mockPlaylistItem4, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo4);
     when (mockPlaylistItemInfo4.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem4, "name")).thenReturn(mockPlaylistName4);
     when (mockPlaylistName4.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem4, "sourceURL")).thenReturn(mockPlaylistSourceUrl4);
     when (mockPlaylistSourceUrl4.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem4, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist4);
     when (mockPlaylistArtist4.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem4, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle4);
     when (mockPlaylistTitle4.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem4, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack4);
     when (mockPlaylistTrack4.toString()).thenReturn(track);
   }
   
   private void givenIHaveMediaItem5(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 4)).thenReturn(mockPlaylistItem5);
     when (mockAdapter.getVariant(mockPlaylistItem5, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo5);
     when (mockPlaylistItemInfo5.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem5, "name")).thenReturn(mockPlaylistName5);
     when (mockPlaylistName5.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem5, "sourceURL")).thenReturn(mockPlaylistSourceUrl5);
     when (mockPlaylistSourceUrl5.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem5, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist5);
     when (mockPlaylistArtist5.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem5, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle5);
     when (mockPlaylistTitle5.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem5, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack5);
     when (mockPlaylistTrack5.toString()).thenReturn(track);
   }
   
   private void givenIHaveMediaItem6(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 5)).thenReturn(mockPlaylistItem6);
     when (mockAdapter.getVariant(mockPlaylistItem6, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo6);
     when (mockPlaylistItemInfo6.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem6, "name")).thenReturn(mockPlaylistName6);
     when (mockPlaylistName6.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem6, "sourceURL")).thenReturn(mockPlaylistSourceUrl6);
     when (mockPlaylistSourceUrl6.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem6, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist6);
     when (mockPlaylistArtist6.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem6, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle6);
     when (mockPlaylistTitle6.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem6, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack6);
     when (mockPlaylistTrack6.toString()).thenReturn(track);
   }
   
   private void givenIHaveMediaItem7(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 6)).thenReturn(mockPlaylistItem7);
     when (mockAdapter.getVariant(mockPlaylistItem7, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo7);
     when (mockPlaylistItemInfo7.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem7, "name")).thenReturn(mockPlaylistName7);
     when (mockPlaylistName7.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem7, "sourceURL")).thenReturn(mockPlaylistSourceUrl7);
     when (mockPlaylistSourceUrl7.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem7, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist7);
     when (mockPlaylistArtist7.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem7, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle7);
     when (mockPlaylistTitle7.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem7, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack7);
     when (mockPlaylistTrack7.toString()).thenReturn(track);
   }
   
   private void givenIHaveMediaItem8(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 7)).thenReturn(mockPlaylistItem8);
     when (mockAdapter.getVariant(mockPlaylistItem8, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo8);
     when (mockPlaylistItemInfo8.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem8, "name")).thenReturn(mockPlaylistName8);
     when (mockPlaylistName8.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem8, "sourceURL")).thenReturn(mockPlaylistSourceUrl8);
     when (mockPlaylistSourceUrl8.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem8, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist8);
     when (mockPlaylistArtist8.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem8, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle8);
     when (mockPlaylistTitle8.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem8, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack8);
     when (mockPlaylistTrack8.toString()).thenReturn(track);
   }
   
   private void givenIHaveMediaItem9(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 8)).thenReturn(mockPlaylistItem9);
     when (mockAdapter.getVariant(mockPlaylistItem9, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo9);
     when (mockPlaylistItemInfo9.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem9, "name")).thenReturn(mockPlaylistName9);
     when (mockPlaylistName9.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem9, "sourceURL")).thenReturn(mockPlaylistSourceUrl9);
     when (mockPlaylistSourceUrl9.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem9, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist9);
     when (mockPlaylistArtist9.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem9, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle9);
     when (mockPlaylistTitle9.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem9, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack9);
     when (mockPlaylistTrack9.toString()).thenReturn(track);
   }
   
   private void givenIHaveMediaItem10(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 9)).thenReturn(mockPlaylistItem10);
     when (mockAdapter.getVariant(mockPlaylistItem10, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo10);
     when (mockPlaylistItemInfo10.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem10, "name")).thenReturn(mockPlaylistName10);
     when (mockPlaylistName10.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem10, "sourceURL")).thenReturn(mockPlaylistSourceUrl10);
     when (mockPlaylistSourceUrl10.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem10, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist10);
     when (mockPlaylistArtist10.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem10, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle10);
     when (mockPlaylistTitle10.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem10, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack10);
     when (mockPlaylistTrack10.toString()).thenReturn(track);
   }
   
   private void givenIHaveMediaItem11(String type, String url, String title, String artist, String album, String track) {
     when (mockAdapter.call(mockPlaylistDispatch, "item", 10)).thenReturn(mockPlaylistItem11);
     when (mockAdapter.getVariant(mockPlaylistItem11, "getItemInfo", "MediaType")).thenReturn(mockPlaylistItemInfo11);
     when (mockPlaylistItemInfo11.toString()).thenReturn(type);
     when (mockAdapter.getVariant(mockPlaylistItem11, "name")).thenReturn(mockPlaylistName11);
     when (mockPlaylistName11.toString()).thenReturn(title);
     when (mockAdapter.getVariant(mockPlaylistItem11, "sourceURL")).thenReturn(mockPlaylistSourceUrl11);
     when (mockPlaylistSourceUrl11.toString()).thenReturn(url);
     when (mockAdapter.getVariant(mockPlaylistItem11, "getItemInfo", "WM/AlbumArtist")).thenReturn(mockPlaylistArtist11);
     when (mockPlaylistArtist11.toString()).thenReturn(artist);
     when (mockAdapter.getVariant(mockPlaylistItem11, "getItemInfo", "WM/AlbumTitle")).thenReturn(mockPlaylistTitle11);
     when (mockPlaylistTitle11.toString()).thenReturn(album);
     when (mockAdapter.getVariant(mockPlaylistItem11, "getItemInfo", "WM/TrackNumber")).thenReturn(mockPlaylistTrack11);
     when (mockPlaylistTrack11.toString()).thenReturn(track);
   }
   
	private void givenIHaveAWMPLibrary() {
	  when (mockAdapter.getDispatch("mediaCollection")).thenReturn(mockMediaCollectionDispatch);
		when (mockAdapter.call(mockMediaCollectionDispatch, "getAll")).thenReturn(mockPlaylistDispatch);
		when (mockAdapter.getVariant(mockPlaylistDispatch, "count")).thenReturn(mockPlaylistCountVariant);
		when (mockPlaylistCountVariant.getInt()).thenReturn(11);
  }
}
