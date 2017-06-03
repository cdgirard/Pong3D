package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.screens.MenuScreen;

public class Pong3D extends Game 
{
	private static final String TAG = Pong3D.class.getName();
	
	@Override
	public void create() 
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		Assets.instance.init();
		
		//GamePreferences.instance.load();
		//AudioManager.instance.play(Assets.instance.music.song01);
		
		setScreen(new MenuScreen(this));
		
	}
}
