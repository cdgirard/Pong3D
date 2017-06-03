package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.assets.Assets;

public abstract class AbstractGameScreen implements Screen
{
	protected Game game;
	
	public AbstractGameScreen(Game g)
	{
		game = g;
	}

	@Override
	public abstract void show();

	@Override
	public abstract void render(float delta);

	@Override
	public abstract void resize(int width, int height);

	@Override
	public abstract void pause();

	@Override
	public abstract void hide();
	
	@Override
	public void resume()
	{
		Assets.instance.init();
	}

	@Override
	public void dispose()
	{
		Assets.instance.dispose();
	}

}

