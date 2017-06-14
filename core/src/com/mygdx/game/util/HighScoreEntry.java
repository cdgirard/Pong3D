package com.mygdx.game.util;

public class HighScoreEntry
{
    /**
     * Needs to be public to work with the Json file system.
     */
    public String player;
    public int score;
    
    /**
     * Default constructor needed for the Json file system.
     */
    public HighScoreEntry()
    {
    }
    
    public HighScoreEntry(String p, int s)
    {
	player = p;
	score = s;
    }
    
    public String getPlayer()
    {
	return player;
    }
    
    public int getScore()
    {
	return score;
    }

}
