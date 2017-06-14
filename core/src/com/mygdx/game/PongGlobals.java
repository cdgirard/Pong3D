package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.util.HighScoreEntry;
import com.mygdx.game.util.HighScoreListFileManager;

public class PongGlobals
{
    public static int lives = 0;
    public static int score = 0;
    
    /**
     * TODO: Need to finish up High Score System so that it functions properly.
     */
    public static Array<HighScoreEntry> highScores = new Array<HighScoreEntry>();
    
    /**
     * Set the initial values for the variables for when we start a new game.
     */
    public static void startGame()
    {
	lives = 1;
	score = 0;
    }
    
    public static void sortHighScoreList()
    {
	for (int x=0;x<highScores.size-1;x++)
	{
	    int y = x+1;
	    int z = x;
	    while ((y > 0) && (highScores.get(y).score > highScores.get(z).score))
	    {
		HighScoreEntry entry = highScores.removeIndex(y);
		highScores.insert(z, entry);
		y--;
		z--;
	    }
	}
	
	while (highScores.size > 10)
	    highScores.removeIndex(10);
    }
    
    /**
     * A sphere fell into the water, need to update number of lives and put the sphere back
     * in the start place over the middle of the platform.
     */
    public static void lifeLost()
    {
	PongGlobals.lives--;
	if (PongGlobals.lives > 0)
	    PongObjects.instance.sphere.reset();
	else
	{
	    
	//    Pong3D.instance.setScreen(new MenuScreen());
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
