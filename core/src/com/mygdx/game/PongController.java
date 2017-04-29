package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.objects.PongObjects;

public class PongController extends InputAdapter implements Disposable
{
    private static final String TAG = PongController.class.getName();
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;

    public PongController()
    {
	init();
    }

    private void init()
    {
	Gdx.input.setInputProcessor(this);
    }
    
    @Override
    public boolean keyUp(int keycode)
    {
	if (keycode == Keys.LEFT)
	{
	    if (!right)
	        PongObjects.instance.ground.moveTo.x = 0f;
	    left = false;
	}
	if (keycode == Keys.RIGHT)
	{
	    if (!left)
	        PongObjects.instance.ground.moveTo.x = 0f;
	    right = false;
	}
	if (keycode == Keys.DOWN)
	{
	    if (!up)
	        PongObjects.instance.ground.moveTo.z = 0f;
	    down = false;
	}
	if (keycode == Keys.UP)
	{
	    if (!down)
	        PongObjects.instance.ground.moveTo.z = 0f;
	    up = false;
	}
	return false;
    }

    @Override
    public boolean keyDown(int keycode)
    {
	PongObjects.instance.ground.body.activate();
	if (keycode == Keys.LEFT)
	{
	    PongObjects.instance.ground.moveTo.x = -5f;
	    left = true;
	}
	if (keycode == Keys.RIGHT)
	{
	    PongObjects.instance.ground.moveTo.x = 5f;
	    right = true;
	}
	if (keycode == Keys.DOWN)
	{
	    PongObjects.instance.ground.moveTo.z = 5f;
	    down = true;
	}
	if (keycode == Keys.UP)
	{
	    PongObjects.instance.ground.moveTo.z = -5f;
	    up = true;
	}
	if (keycode == Keys.SPACE)
	{
	    Gdx.app.error("Tag",""+PongObjects.instance.ground.bounceTimer);
	    PongObjects.instance.ground.bounce();
	}
	return false;
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub

    }

}
