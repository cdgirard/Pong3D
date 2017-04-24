package com.mygdx.game.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.objects.PongObjects;

public class MovingPointShadowLight extends PointShadowLight
{
    private boolean needsUpdate = false;
    public Vector3 originalPosition = new Vector3();
    public float angle = 0;
    public float distance = 20f;

    public MovingPointShadowLight(final Vector3 position)
    {
	super(position);
	originalPosition.set(position);
    }

    @Override
    public void render()
    {
	if (!needsUpdate)
	{  
	    return;
	}
	needsUpdate = false;
	super.render();
    }

    @Override
    public void act(final float delta)
    {
	angle += delta / 1f;
	position.set(originalPosition.x + MathUtils.cos(angle) * distance, originalPosition.y, originalPosition.z + MathUtils.sin(angle) * distance);
	camera.position.set(position);
	needsUpdate = true;
    }

}
