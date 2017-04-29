package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
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
    public Vector3 position; // Not really position
    public Vector3 moveTo;  // Not really moveto
    
    public void update (float delta)
    {
	position.lerp(moveTo,delta);
	body.setWorldTransform(instance.transform.trn(position));
	Matrix4 worldTrans = body.getWorldTransform();
	instance.transform.set(worldTrans);
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
