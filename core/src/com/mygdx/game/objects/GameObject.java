package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
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
    public float bounceTimer = 0;
    public btRigidBody body;
    public btCollisionShape shape;
    public ModelInstance instance;
    // Only for objects that move.
    public btMotionState motionState;
    public Vector3 position; // Not really position
    public Vector3 moveTo;  // Not really moveto
    
    public void update (float delta)
    {
	//position.lerp(moveTo,delta);
	//body.setWorldTransform(instance.transform.trn(position));
	
	if (bounceTimer > 0)
	{
	    bounceTimer = bounceTimer - delta;
	    if (bounceTimer < 0)
	        bounceTimer = 0;
	}
	if (this == PongObjects.instance.ground)
	{
	    //body.setLinearVelocity(moveTo);
	    body.applyCentralImpulse(moveTo);
	    
	    Vector3 tmp = body.getCenterOfMassPosition();
	    if (moveTo.y > 10)
	        Gdx.app.error("Tag",""+tmp.y+" : "+moveTo.y+" : "+bounceTimer);
	    //body.clearForces();
	    if ((tmp.y > 1) || (tmp.y < -0.5))
	    {
	        float amt = -(tmp.y*2);
	        body.activate();
	        Vector3 tmp2 = body.getLinearVelocity();
	        body.setLinearVelocity(new Vector3(tmp2.x,amt,tmp2.z));
	        moveTo.y = 0;
	    }
	    else if (bounceTimer == 0)
	    {
		Vector3 tmp2 = body.getLinearVelocity();
		body.setLinearVelocity(new Vector3(tmp2.x,0,tmp2.z));
	    }
	   
	}
    }
    
    public void render(ModelBatch batch)
    {
	batch.render(instance);
    }
    
    public void render(ModelBatch batch, Environment env)
    {
	batch.render(instance, env);
    }
    
    public void bounce()
    {
	if (bounceTimer == 0)
	{
	    moveTo.y = 15;
	    bounceTimer = 2;
	    float groundY = body.getCenterOfMassPosition().y;
	    float sphereY = PongObjects.instance.sphere.body.getCenterOfMassPosition().y;
	    if (sphereY < groundY+1.5f)
		PongObjects.instance.sphere.body.applyCentralImpulse(new Vector3(0,5,0));
	}
	
    }
    
    @Override
    public void dispose()
    {
	shape.dispose();
	
    }

}
