package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;

public class GameObject implements Disposable 
{
    public btRigidBody body;
    public btCollisionShape shape;
    public ModelInstance instance;
    // Only for objects that move.
    public btMotionState motionState;
    
    public void update (float deltaTime)
    {
    }
    
    public void render(ModelBatch batch)
    {
	batch.render(instance);
    }
    
    public void render(ModelBatch batch, Environment env)
    {
	batch.render(instance, env);
    }
    
    @Override
    public void dispose()
    {
	shape.dispose();
	
    }

}
