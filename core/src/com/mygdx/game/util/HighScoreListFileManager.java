package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class HighScoreListFileManager
{
    public static final String fileName = "game_data/highscores.json";

    /**
     * Only used for saving and loading the high score table.
     * @author cdgira
     *
     */
    public static class JsonHighScoreList
    {
        public HighScoreEntry[] highScores;
    }
    
    /**
     * We are not going to be able to stick to internal files if we want to be able for 
     * the world to be created dynamically.
     * @param path
     * @param file
     * @param world
     */
    public static Array<HighScoreEntry> loadHighScores()
    {
        Array<HighScoreEntry> highScores = null;
        String load = readFile(fileName);
        if (!load.isEmpty())
        {
            Json json = new Json();
            JsonHighScoreList jWorld = json.fromJson(JsonHighScoreList.class, load);

            if (jWorld.highScores != null)
            {
                highScores = new Array<HighScoreEntry>();
                for (int x=0;x<jWorld.highScores.length;x++)
                {
                    HighScoreEntry entry = new HighScoreEntry(jWorld.highScores[x].getPlayer(),jWorld.highScores[x].getScore());
                    highScores.add(entry);
                }   
            }
        }
        return highScores;
    }
    
    /**
     * Loads the text file and returns the information in the file as a String.
     * @param fileName
     * @return
     */
    private static String readFile(String fileName)
    {
        FileHandle file = Gdx.files.internal(fileName);
        if (file != null && file.exists())
        {
            String s = file.readString();
            if (!s.isEmpty())
            {
                return s;
            }
        }
        return "";
    }
    
    /**
     * This should save the world to a text file.  Really need to figure out how to save
     * off my simple 10x10 world first.
     */
    public static void saveHighScores(Array<HighScoreEntry> highScores)
    {
        String data = convertHighScores(highScores);
        FileHandle file = Gdx.files.local(fileName);
        file.writeString(data,false);         // True means append, false means overwrite.
    }
    
    /**
     * Convert my storage system into the object structure used by my Json system.
     * @param map
     * @return
     */
    private static String convertHighScores(Array<HighScoreEntry> highScores)
    {
        Json json = new Json();
        JsonHighScoreList jWorld = new JsonHighScoreList();
        jWorld.highScores = new HighScoreEntry[highScores.size];
        for (int x=0;x<jWorld.highScores.length;x++)
        {
            jWorld.highScores[x] = new HighScoreEntry(highScores.get(x).getPlayer(),highScores.get(x).getScore());
        }
        
        return json.prettyPrint(jWorld);
    }



}
