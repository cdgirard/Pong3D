package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;

public class GameObject implements Disposable 
{
    public static final int SCORE_TARGET = 1;
    public static final int OBSTACLE_TARGET = 2;
    public static final int SPHERE = 3;
    public static final int PLATFORM = 4;
    public static final int WALL = 5;
    public int objId;
    public boolean visible = true;
    public btRigidBody body;
    public btCollisionShape shape;
    public ModelInstance instance;
    // Only for objects that move.
    public btMotionState motionState;

    /**
     * Updates the state of a game object.
     * @param delta
     */
    public void update (float delta)
    {

    }
    
    
    @Override
    public void dispose()
    {
	if (shape != null)
	    shape.dispose();
	if (motionState != null)
	    motionState.dispose();
	if (body != null)
	    body.dispose();
    }

}
