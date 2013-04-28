package com.mindbadger.player;

import java.io.File;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;

import org.apache.log4j.Logger;


public class AudioPlayer implements IPlayAudio, ControllerListener {
	Logger logger = Logger.getLogger(AudioPlayer.class);

	private Player player;
	private JavaxPlayerFactory factory;
	private IReceiveStatusUpdatesFromAMediaPlayer audioPlayerStatusListener;
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
	public void stopPlayingAudioFile() {
		logger.debug("AUDIO PLAYER: about to stop playing " + player);
		player.close();
		player.deallocate();
		status = PlayerStatus.IDLE;
	}

	@Override
	public void controllerUpdate(ControllerEvent event) {
		logger.debug("*** AUDIO PLAYER EVENT: receiving audio player event: " + event);

		if (event instanceof javax.media.EndOfMediaEvent) {
			audioPlayerStatusListener.songEnded();
		} else if (event instanceof javax.media.StopByRequestEvent) {
			status = PlayerStatus.PAUSED;
			audioPlayerStatusListener.songPaused();
		} else if (event instanceof javax.media.StartEvent) {
			status = PlayerStatus.PLAYING;
			audioPlayerStatusListener.songStarted();
		}
	}
	
	@Override
	public PlayerStatus getAudioPlayerStatus() {
		return status;
	}

	public void setBroadcaster(IReceiveStatusUpdatesFromAMediaPlayer broadcaster) {
		this.audioPlayerStatusListener = broadcaster;
	}

	public IReceiveStatusUpdatesFromAMediaPlayer getBroadcaster() {
		return audioPlayerStatusListener;
	}
}
