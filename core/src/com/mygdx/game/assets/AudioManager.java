package com.mygdx.game.assets;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

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

}
