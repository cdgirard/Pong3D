package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.util.HighScoreListFileManager;

public class PongGlobals
{
    public static int lives = 0;
    public static int score = 0;
    
    public static Array<String> highScores = new Array<String>();
    
    /**
     * Set the initial values for the variables for when we start a new game.
     */
    public static void startGame()
    {
	lives = 1;
	score = 0;
    }
    
    /**
     * A sphere fell into the water, need to update number of lives and put the sphere back
     * in the start place over the middle of the platform.
     */
    public static void lifeLost()
    {
	PongGlobals.lives--;
	if (PongGlobals.lives >= 0)
	    PongObjects.instance.sphere.reset();
	else
	{
	    highScores.add(""+score);
	    HighScoreListFileManager.saveHighScores(highScores);
	    Pong3D.instance.setScreen(new MenuScreen());
	}
    }
    
    
    
    /**
     * A sphere hit a wooden box and scored some points.
     */
    public static void scorePoints()
    {
	PongGlobals.score += 10;
    }

}
