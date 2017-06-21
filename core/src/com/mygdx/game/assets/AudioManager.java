package com.mygdx.game.assets;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.util.GamePreferences;

public class AudioManager
{
    public static final AudioManager instance = new AudioManager();
    private Music playingMusic;

    /**
     * singleton: prevent instantiation from other classes
     */
    private AudioManager()
    {
    }

    public void play(Sound sound)
    {
	play(sound, 1);
    }

    public void play(Sound sound, float volume)
    {
	play(sound, volume, 1);
    }

    public void play(Sound sound, float volume, float pitch)
    {
	play(sound, volume, pitch, 0);
    }

    public void play(Sound sound, float volume, float pitch, float pan)
    {
	// TODO: Setup the Game Preferences class once we create the intro
	// screen.
	// if (!GamePreferences.instance.sound) return;
	// sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
	sound.play(volume, pitch, pan);
    }
    
    /**
     * Play some music in the background.
     * @param music
     */
    public void play(Music music)
    {
	stopMusic();
	playingMusic = music;
	if (GamePreferences.instance.music)
	{
	    music.setLooping(true);
	    music.setVolume(GamePreferences.instance.volMusic);
	    music.play();
	}
    }
    
    /**
     * Stop any music we do have playing.
     */
    public void stopMusic()
    {
	if (playingMusic != null)
	    playingMusic.stop();
	playingMusic = null;
    }

    /**
     * Should be called if changes are made to the settings stored in GamePreferences.
     */
    public void onSettingsUpdated()
    {
	if (playingMusic == null)
	    return;
	playingMusic.setVolume(GamePreferences.instance.volMusic);
	if (GamePreferences.instance.music)
	{
	    if (!playingMusic.isPlaying())
		playingMusic.play();
	}
	else
	{
	    playingMusic.pause();
	}
    }

}
