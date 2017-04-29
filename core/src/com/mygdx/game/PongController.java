package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.objects.PongObjects;

public class PongController extends InputAdapter implements Disposable
{
    private static final String TAG = PongController.class.getName();

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
	    PongObjects.instance.ground.moveTo.x = 0f;
	}
	if (keycode == Keys.RIGHT)
	{
	    PongObjects.instance.ground.moveTo.x = 0f;
	}
	if (keycode == Keys.DOWN)
	{
	    PongObjects.instance.ground.moveTo.z = 0f;
	}
	if (keycode == Keys.UP)
	{
	    PongObjects.instance.ground.moveTo.z = 0f;
	}
	return false;
    }

    @Override
    public boolean keyDown(int keycode)
    {
	if (keycode == Keys.LEFT)
	{
	    PongObjects.instance.ground.moveTo.x = -0.1f;
	}
	if (keycode == Keys.RIGHT)
	{
	    PongObjects.instance.ground.moveTo.x = 0.1f;
	}
	if (keycode == Keys.DOWN)
	{
	    PongObjects.instance.ground.moveTo.z = 0.1f;
	}
	if (keycode == Keys.UP)
	{
	    PongObjects.instance.ground.moveTo.z = -0.1f;
	}
	return false;
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub

    }

}
