package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyMotionState;

public class PlatformGameObject extends GameObject
{
    public float bounceTimer = 0;
    public Vector3 impulseForce;
    
    public PlatformGameObject()
    {
	Model model = Assets.assetManager.get(Assets.marble, Model.class);
	
	impulseForce = new Vector3(0, 0, 0);
	instance = new ModelInstance(model);
	motionState = new MyMotionState(instance);

	objId = GameObject.PLATFORM;
	
	float width = 5;
	shape = new btBoxShape(new Vector3(width, 1 / 4f, width));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(100f, motionState, shape, Vector3.Zero);
	bodyInfo.setRestitution(1.0f);
	bodyInfo.setFriction(1.0f);
	body = new btRigidBody(bodyInfo);
	body.setMotionState(motionState);
	body.setUserValue(15);
	body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
	BulletWorld.world.addRigidBody(body);
	body.setGravity(new Vector3(0, 0, 0));
    }

    public void update(float delta)
    {
	if (bounceTimer > 0)
	{
	    bounceTimer = bounceTimer - delta;
	    if (bounceTimer < 0)
		bounceTimer = 0;
	}

	body.applyCentralImpulse(impulseForce);

	Vector3 tmp = body.getCenterOfMassPosition();
	//if (impulseForce.y > 10)
	    //Gdx.app.error("Tag", "" + tmp.y + " : " + impulseForce.y + " : " + bounceTimer);
	// body.clearForces();
	if ((tmp.y > 1) || (tmp.y < -0.5))
	{
	    float amt = -(tmp.y * 2);
	    body.activate();
	    Vector3 tmp2 = body.getLinearVelocity();
	    body.setLinearVelocity(new Vector3(tmp2.x, amt, tmp2.z));
	    impulseForce.y = 0;
	}
	else if (bounceTimer == 0)
	{
	    Vector3 tmp2 = body.getLinearVelocity();
	    body.setLinearVelocity(new Vector3(tmp2.x, 0, tmp2.z));
	}

    }

    public void bounce()
    {
	if (bounceTimer == 0)
	{
	    impulseForce.y = 15;
	    bounceTimer = 2;
	    float groundY = body.getCenterOfMassPosition().y;
	    float sphereY = PongObjects.instance.sphere.body.getCenterOfMassPosition().y;
	    if (sphereY < groundY + 1.5f)
		PongObjects.instance.sphere.body.applyCentralImpulse(new Vector3(0, 5, 0));
	}

    }

}
