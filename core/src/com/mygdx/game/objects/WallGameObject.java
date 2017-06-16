package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyMotionState;
import com.mygdx.game.exceptions.InvalidFacingException;

public class WallGameObject extends GameObject
{
    // TODO: Is there a way to query the 3D Model to get this info?
    public static final int WIDTH = 10;
    
    public static final int NORTH_SOUTH = 1;
    public static final int EAST_WEST = 2;
    
    private int facing = 0;
    
    public WallGameObject(Vector3 position, int type)
    {
	facing = type;
	Model model = null;
	if (facing == EAST_WEST)
	     model = Assets.assetManager.get(Assets.wall_a, Model.class);
	else if (facing == NORTH_SOUTH)
	    model = Assets.assetManager.get(Assets.wall_a, Model.class);
	else 
	    throw new InvalidFacingException();

	objId = GameObject.WALL;
	instance = new ModelInstance(model);
	motionState = new MyMotionState(instance);
	motionState.setWorldTransform(instance.transform.trn(position));
	if (facing == EAST_WEST)
	    shape = new btBoxShape(new Vector3(WIDTH, WIDTH, 1.0f));
	else
	    shape =  new btBoxShape(new Vector3(1.0f, WIDTH, WIDTH));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, motionState, shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	body = new btRigidBody(bodyInfo);
	body.setCollisionFlags(body.getCollisionFlags());
	body.userData = this;
	BulletWorld.world.addRigidBody(body);
    }

}
