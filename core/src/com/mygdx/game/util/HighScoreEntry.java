package com.mygdx.game.util;

public class HighScoreEntry
{
    private String player;
    private int score;
    
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
