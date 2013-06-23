package com.mindbadger.jukebox;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mindbadger.broadcast.StatusBroadcaster;
import com.mindbadger.cache.MediaPlayerCache;
import com.mindbadger.library.Artist;
import com.mindbadger.library.MediaItem;
import com.mindbadger.library.Track;
import com.mindbadger.library.Album;
import com.mindbadger.player.AudioPlayer;
import com.mindbadger.player.IPlayAudio;
import com.mindbadger.player.IReceiveStatusUpdatesFromAMediaPlayer;
import com.mindbadger.player.PlayerStatus;

public class Jukebox implements IReceiveStatusUpdatesFromAMediaPlayer {
	Logger logger = Logger.getLogger(Jukebox.class);

	static final int START_OF_PLAYLIST = -1;
	static final int END_OF_PLAYLIST = -2;
	static final int NO_PLAYLIST = -3;

	private IPlayAudio audioPlayer;
	private MediaPlayerCache mediaPlayerCache;
	private PlaylistRandomiser playlistRandomiser;
	private StatusBroadcaster statusBroadcaster;

	private List<Integer> playlist = new ArrayList<Integer>();
	private int currentlyPlayingIndex = NO_PLAYLIST;
	private boolean repeat = false;
	private boolean shuffle = false;

	public Jukebox(MediaPlayerCache mediaPlayerCache) {
		this.mediaPlayerCache = mediaPlayerCache;
	}

	@Override
	public void songStarted() {
		logger.debug("PLAYER STATUS CHANGED: songStarted");
		broadcastStatus();
	}

	@Override
	public void songPaused() {
		logger.debug("PLAYER STATUS CHANGED: songPaused");
		broadcastStatus();
	}

	@Override
	public void songEnded() {
		logger.debug("PLAYER STATUS CHANGED: ended");
		currentlyPlayingIndex++;
		if (currentlyPlayingIndex > playlist.size()) {
			logger.debug("...End of playlist");
			currentlyPlayingIndex = END_OF_PLAYLIST;
		}
		playTrackIfOneAvailable();
		broadcastStatus();
	}

	private void playTrackIfOneAvailable() {
		audioPlayer.stopPlayingAudioFile();

		if (currentlyPlayingIndex < 0) {
			logger.debug("Can't play track - start of playlist");
			currentlyPlayingIndex = START_OF_PLAYLIST;
			broadcastStatus();
		} else if (currentlyPlayingIndex >= playlist.size()) {
			logger.debug("Can't play track - end of playlist");
			currentlyPlayingIndex = END_OF_PLAYLIST;
			broadcastStatus();
		} else {
			playTrack();
		}
	}

	public void addItemToPlaylist(int mediaItemId) {
		logger.debug("Adding Item to playlist: " + mediaItemId);

		boolean emptyPlaylist = (playlist.size() == 0);

		MediaItem mediaItem = mediaPlayerCache.getMediaItemWithId(mediaItemId);

		if (mediaItem instanceof Track) {
			addTrackWithIdToPlaylist(mediaItemId);
		} else if (mediaItem instanceof Album) {
			Album album = (Album) mediaItem;
			addAlbumToPlaylist(album);
		} else if (mediaItem instanceof Artist) {
			Artist artist = (Artist) mediaItem;
			addArtistToPlaylist(artist);
		}

		if (emptyPlaylist) {
			currentlyPlayingIndex = 0;
			playTrack();
		}

		logger.debug("Jukebox - current playlist: " + playlist);
	}

	private void playTrack() {
		int trackId = getCurrentTrackId();
		Track trackToPlay = (Track) mediaPlayerCache.getMediaItemWithId(trackId);
		File audioFile = new File(trackToPlay.getFullyQualifiedFileName());
		logger.debug("Playing track: " + audioFile);
		audioPlayer.playAudioFile(audioFile);
	}

	private void addTrackWithIdToPlaylist(int mediaItemId) {
		playlist.add(mediaItemId);
	}

	private void addAlbumToPlaylist(Album album) {
		Map<Integer, Track> tracks = album.getTracks();
		List<Track> trackList = new ArrayList<Track>(tracks.values());
		// Collections.sort(trackList);

		for (Track track : trackList) {
			addTrackWithIdToPlaylist(track.getId());
		}
	}

	private void addArtistToPlaylist(Artist artist) {
		Map<String, Album> albums = artist.getAlbums();
		List<Album> albumList = new ArrayList<Album>(albums.values());
		Collections.sort(albumList);
		for (Album album : albumList) {
			addAlbumToPlaylist(album);
		}
	}

	public void playOrPause() {
		boolean pause = (getPlayerStatus() == PlayerStatus.PLAYING);
		audioPlayer.pause(pause);
	}

	public void nextTrack() {
		currentlyPlayingIndex++;
		playTrackIfOneAvailable();
	}

	public void previousTrack() {
		currentlyPlayingIndex--;
		playTrackIfOneAvailable();
	}

	public void toggleShuffle() {
		shuffle = !shuffle;

		if (shuffle) {
			playlist = playlistRandomiser.randomise(playlist);
		} else {
			playlist = playlistRandomiser.backToOriginalState();
		}
		playTrackIfOneAvailable();
	}

	public void toggleRepeat() {
		repeat = !repeat;
		broadcastStatus();
	}

	public void clearPlaylist() {
		currentlyPlayingIndex = NO_PLAYLIST;
		playlist.clear();
		audioPlayer.stopPlayingAudioFile();
		broadcastStatus();
	}

	public void broadcastStatus() {

		statusBroadcaster.broadcast(audioPlayer.getAudioPlayerStatus().toString(), getCurrentTrackId(), repeat, shuffle, "");
	}

	public int getCurrentTrackId() {
		return (currentlyPlayingIndex < 0 ? currentlyPlayingIndex : playlist.get(currentlyPlayingIndex));
	}

	public Track getCurrentTrack () {
		return (Track) mediaPlayerCache.getMediaItemWithId(getCurrentTrackId());
	}
	
	public String getArtworkForTrack(int trackId) {
		
		MediaItem mediaItem = mediaPlayerCache.getMediaItemWithId(trackId);
		
		if (!(mediaItem instanceof Track)) {
			throw new IllegalArgumentException("You can only retrieve artwork for a Track");
		}
		
		return ((Track) mediaItem).getArtworkFile();
	}

	public boolean isRepeat() {
		return repeat;
	}

	public boolean isShuffle() {
		return shuffle;
	}

	public List<Integer> getPlaylist() {
		return playlist;
	}

	public int getCurrentlyPlayingIndex() {
		return currentlyPlayingIndex;
	}

	public void setStatusBroadcaster(StatusBroadcaster statusBroadcaster) {
		this.statusBroadcaster = statusBroadcaster;
	}

	public StatusBroadcaster getStatusBroadcaster() {
		return statusBroadcaster;
	}

	public PlayerStatus getPlayerStatus() {
		return audioPlayer.getAudioPlayerStatus();
	}

	public void setAudioPlayer(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	public void setPlaylistRandomiser(PlaylistRandomiser playlistRandomiser) {
		this.playlistRandomiser = playlistRandomiser;
	}

	public PlaylistRandomiser getPlaylistRandomiser() {
		return playlistRandomiser;
	}
	
	
	
	public boolean isStartOfPlaylist () {
		return (currentlyPlayingIndex == NO_PLAYLIST || currentlyPlayingIndex == 0);
	}

	public boolean isEndOfPlaylist() {
		return (currentlyPlayingIndex == NO_PLAYLIST || currentlyPlayingIndex == (playlist.size() -1));
	}
}
