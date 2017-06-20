package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.PongGlobals;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.PlatformGameObject;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.objects.SphereGameObject;
import com.mygdx.game.objects.TargetGameObject;
import com.mygdx.game.objects.WallGameObject;

public class LevelLoader
{
    private static final String TAG = "LevelLoader";

    public enum BLOCK_TYPE
    {
	EMPTY(0, 0, 0), // black
	SCORE_TARGET(0, 255, 0), // green
	PLAYER_SPAWNPOINT(255, 255, 255), // white
	WALL_A(255, 0, 255), // purple
	WALL_B(0, 0, 255), // blue
	SOLID_TARGET(255, 0, 0); // red

	private int color;

	private BLOCK_TYPE(int r, int g, int b)
	{
	    color = r << 24 | g << 16 | b << 8 | 0xff;
	}

	public boolean sameColor(int color)
	{
	    return this.color == color;
	}

	public int getColor()
	{
	    return color;
	}
    }

    /**
     * Loads in a level for the player.  Needs the camera due to the special effects.
     * @param fileName
     * @param cam
     */
    public static void loadLevel(String fileName, Camera cam)
    {

	Pixmap pixmap = new Pixmap(Gdx.files.internal(fileName));
	for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
	{
	    for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
	    {
		GameObject obj = null;
		int currentPixel = pixmap.getPixel(pixelX, pixelY);

		if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
		{
		    // do nothing
		}
		else if (BLOCK_TYPE.WALL_A.sameColor(currentPixel))
		{
		    Vector3 position = new Vector3(pixelX, 0, pixelY);
		    obj = new WallGameObject(position, WallGameObject.EAST_WEST);
		}
		else if (BLOCK_TYPE.WALL_B.sameColor(currentPixel))
		{
		    Vector3 position = new Vector3(pixelX, 0, pixelY);
		    obj = new WallGameObject(position, WallGameObject.NORTH_SOUTH);

		}
		else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
		{
		    Vector3 position = new Vector3(pixelX, 7, pixelY);
		    obj = PongObjects.instance.sphere = new SphereGameObject(position, cam);
		    PongObjects.instance.ground = new PlatformGameObject(new Vector3(pixelX,0,pixelY));
		    // TODO: Make this a method for better design?
		    PongObjects.instance.objects.add(PongObjects.instance.ground);
		}
		else if (BLOCK_TYPE.SCORE_TARGET.sameColor(currentPixel))
		{
		    Vector3 position = new Vector3(pixelX, 5, pixelY);
		    obj = new TargetGameObject(position, GameObject.SCORE_TARGET);
		    PongGlobals.numScoreBlocks++;
		}
		else if (BLOCK_TYPE.SOLID_TARGET.sameColor(currentPixel))
		{
		    Vector3 position = new Vector3(pixelX, 5, pixelY);
		    obj = new TargetGameObject(position, GameObject.SOLID_TARGET);
		}
		else
		{
		    int r = 0xff & (currentPixel >>> 24); // red
		    int g = 0xff & (currentPixel >>> 16); // green
		    int b = 0xff & (currentPixel >>> 8); // blue
		    int a = 0xff & currentPixel; // alpha
		    Gdx.app.error(TAG, "Unknown object at x <" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
		}
		if (obj != null)
		    PongObjects.instance.objects.add(obj);
	    }
	}

	pixmap.dispose();
	Gdx.app.debug(TAG, "level '" + fileName + "' loaded");
    }

}
