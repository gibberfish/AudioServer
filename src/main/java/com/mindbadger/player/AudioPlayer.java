package com.mindbadger.player;

import java.io.File;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;

import org.apache.log4j.Logger;

import com.mindbadger.jukebox.PlayerStatus;

public class AudioPlayer implements IPlayAudio, ControllerListener {
	Logger logger = Logger.getLogger(AudioPlayer.class);

	private Player player;
	private JavaxPlayerFactory factory;
	private IBroadcastAudioPlayerEvents broadcaster;
	private PlayerStatus status = PlayerStatus.IDLE;

	public AudioPlayer() {
	}

	public AudioPlayer(JavaxPlayerFactory factory) {
		this.factory = factory;
	}

	@Override
	public void playAudioFile(File audioFile) {
		player = factory.getNewPlayer(audioFile);
		player.addControllerListener(this);
		
		status = PlayerStatus.QUEUED;

		logger.debug("AUDIO PLAYER: about to start " + player + " to play " + audioFile);
		player.start();
	}

	@Override
	public void pause(boolean pause) {
		if (pause) {
			logger.debug("AUDIO PLAYER: about to pause player");
			status = PlayerStatus.PAUSED;
			player.stop();
		} else {
			logger.debug("AUDIO PLAYER: about to restart player");
			status = PlayerStatus.QUEUED;
			player.start();
		}
	}

	@Override
	public void controllerUpdate(ControllerEvent event) {
		logger.debug("*** AUDIO PLAYER EVENT: receiving audio player event: " + event);

		if (event instanceof javax.media.EndOfMediaEvent) {
			broadcaster.songEnded();
		} else if (event instanceof javax.media.StopByRequestEvent) {
			status = PlayerStatus.PAUSED;
			broadcaster.songPaused();
		} else if (event instanceof javax.media.StartEvent) {
			status = PlayerStatus.PLAYING;
			broadcaster.songStarted();
		}
	}

	public void setBroadcaster(IBroadcastAudioPlayerEvents broadcaster) {
		this.broadcaster = broadcaster;
	}

	public IBroadcastAudioPlayerEvents getBroadcaster() {
		return broadcaster;
	}

	@Override
	public void stopPlayingAudioFile() {
		logger.debug("AUDIO PLAYER: about to stop playing " + player);
		player.close();
		player.deallocate();
		status = PlayerStatus.IDLE;
	}

	public PlayerStatus getStatus() {
		return status;
	}
}
