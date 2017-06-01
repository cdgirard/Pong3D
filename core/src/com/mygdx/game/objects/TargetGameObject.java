package com.mygdx.game.objects;

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

public class TargetGameObject extends GameObject
{
    
    public TargetGameObject()
    {
	Model model = Assets.assetManager.get(Assets.target, Model.class);

	objId = GameObject.TARGET;
	Vector3 position = new Vector3(7, 5, 5);
	instance = new ModelInstance(model);
	motionState = new MyMotionState(instance);
	motionState.setWorldTransform(instance.transform.trn(position));
	int width = 10;
	shape = new btBoxShape(new Vector3(1.0f, 1.0f, 1.0f));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, motionState, shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	body = new btRigidBody(bodyInfo);
	// btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT
	// btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE
	// btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK
	body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	body.userData = this;

	BulletWorld.world.addRigidBody(body);
    }

}
