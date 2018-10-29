package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.AudioManager;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MenuScreen;

/**
 * This class manages the input from the user while they still have lives.  Once all 
 * the lives are used, input control is handed over to the High Score Name window.
 * 
 * @author cdgira
 *
 */
public class PongController extends InputAdapter implements Disposable
{
    private static final String TAG = PongController.class.getName();
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;
    
    // TODO Quick fix, clean up later.
    GameScreen screen;

    public PongController(GameScreen gs)
    {
	screen = gs;
	init();
    }

    private void init()
    {
	Gdx.input.setInputProcessor(this);
    }
    
    /**
     * Allows the user to move the platform around.
     */
    @Override
    public boolean keyUp(int keycode)
    {
	if (keycode == Keys.LEFT)
	{
	    if (!right)
	        PongObjects.instance.ground.impulseForce.x = 0f;
	    left = false;
	}
	if (keycode == Keys.RIGHT)
	{
	    if (!left)
	        PongObjects.instance.ground.impulseForce.x = 0f;
	    right = false;
	}
	if (keycode == Keys.DOWN)
	{
	    if (!up)
	        PongObjects.instance.ground.impulseForce.z = 0f;
	    down = false;
	}
	if (keycode == Keys.UP)
	{
	    if (!down)
	        PongObjects.instance.ground.impulseForce.z = 0f;
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
	    PongObjects.instance.ground.impulseForce.x = -5f;
	    left = true;
	}
	if (keycode == Keys.RIGHT)
	{
	    PongObjects.instance.ground.impulseForce.x = 5f;
	    right = true;
	}
	if (keycode == Keys.DOWN)
	{
	    PongObjects.instance.ground.impulseForce.z = 5f;
	    down = true;
	}
	if (keycode == Keys.UP)
	{
	    PongObjects.instance.ground.impulseForce.z = -5f;
	    up = true;
	}
	if (keycode == Keys.SPACE)
	{
	    Gdx.app.error("Tag",""+PongObjects.instance.ground.bounceTimer);
	    PongObjects.instance.ground.bounce();
	}
	if (keycode == Keys.ESCAPE)
	{
	    screen.dispose();
	    AudioManager.instance.stopMusic();
	    Pong3D.instance.setScreen(new MenuScreen());
	}
	return false;
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub

    }

}
