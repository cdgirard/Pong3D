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
        public Entry[] highScores;
    }

    public static class Entry
    {
        public String player = null;

        public int score = 0;
    }
    
    /**
     * We are not going to be able to stick to internal files if we want to be able for 
     * the world to be created dynamically.
     * @param path
     * @param file
     * @param world
     */
    public static Array<String> loadHighScores()
    {
        Array<String> highScores = null;
        String load = readFile(fileName);
        if (!load.isEmpty())
        {
            Json json = new Json();
            JsonHighScoreList jWorld = json.fromJson(JsonHighScoreList.class, load);

            if (jWorld.highScores != null)
            {
                highScores = new Array<String>();
                for (int x=0;x<jWorld.highScores.length;x++)
                {
                    highScores.add(jWorld.highScores[x].player);
                    //highScores.add(jWorld.highScores[x].score);
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
        FileHandle file = Gdx.files.local(fileName);
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
    public static void saveHighScores(Array<String> highScores)
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
    private static String convertHighScores(Array<String> highScores)
    {
        Json json = new Json();
        JsonHighScoreList jWorld = new JsonHighScoreList();
        jWorld.highScores = new Entry[highScores.size];
        for (int x=0;x<jWorld.highScores.length;x++)
        {
            jWorld.highScores[x] = new Entry();
            jWorld.highScores[x].player = highScores.get(x);
            //jWorld.highScores[x].score = ???;
        }
        
        return json.prettyPrint(jWorld);
    }



}
