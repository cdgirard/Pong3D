package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.util.HighScoreListFileManager;

public class Pong3D extends Game 
{
	private static final String TAG = Pong3D.class.getName();
	
	public static Pong3D instance = null;
	
	@Override
	public void create() 
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		instance = this;
		
		Assets.instance.init();
		PongGlobals.highScores = HighScoreListFileManager.loadHighScores();
		
		//GamePreferences.instance.load();
		//AudioManager.instance.play(Assets.instance.music.song01);
		
		setScreen(new MenuScreen());
		
	}
}
