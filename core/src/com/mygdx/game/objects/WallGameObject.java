package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyMotionState;
import com.mygdx.game.exceptions.InvalidFacingException;

// TODO: Need a way to see through a wall when platform in front of it.
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
	    model = Assets.assetManager.get(Assets.wall_b, Model.class);
	else
	    throw new InvalidFacingException();

	// Trying to make the wall opaque so can see the platform through it.
	//Material mat = model.getMaterial("Material");
	// mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 0.3f));
	//mat.set(FloatAttribute.createAlphaTest(0.5f));

	// loads the track -- including all the transparent, billboard trees in the scene
	//Model model = GameServices.getModelLoader().loadModel(Gdx.files.getFileHandle("data/3d/scene.g3db", FileType.Internal));

	//Material mat = model.getMaterial("TreeMaterial");
	//mat.set(FloatAttribute.createAlphaTest(0.5f));

	objId = GameObject.WALL;
	instance = new ModelInstance(model);
	Material mat = instance.getMaterial("Material");
	mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 1.0f));
	//mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 0.3f));

	motionState = new MyMotionState(instance);
	motionState.setWorldTransform(instance.transform.trn(position));
	if (facing == EAST_WEST)
	    shape = new btBoxShape(new Vector3(WIDTH, WIDTH, 1.0f));
	else
	    shape = new btBoxShape(new Vector3(1.0f, WIDTH, WIDTH));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, motionState, shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	body = new btRigidBody(bodyInfo);
	body.setCollisionFlags(body.getCollisionFlags());
	body.userData = this;
	BulletWorld.world.addRigidBody(body);
    }

    @Override
    public void update(float delta)
    {
	Vector3 wallPosition = body.getCenterOfMassPosition().cpy();
	Vector3 platformPosition = PongObjects.instance.ground.body.getCenterOfMassPosition().cpy();
	boolean solid = true;
	float[] vertices = { platformPosition.x - 13, platformPosition.z, 
                platformPosition.x, platformPosition.z, 
                platformPosition.x, platformPosition.z + 13, 
                platformPosition.x - 13, platformPosition.z + 13 };

        Polygon p = new Polygon(vertices);

	if (facing == WallGameObject.EAST_WEST)
	{
	    if ((p.contains(wallPosition.x - 10, wallPosition.z)) || (p.contains(wallPosition.x, wallPosition.z)) || (p.contains(wallPosition.x + 10, wallPosition.z)))
	    {
		solid = false;

	    }
	}
	if (facing == WallGameObject.NORTH_SOUTH)
	{
	    
	    if ((p.contains(wallPosition.x, wallPosition.z-10)) || (p.contains(wallPosition.x, wallPosition.z)) || (p.contains(wallPosition.x, wallPosition.z+10)))
	    {
		solid = false;

	    }
	}
	Material mat = instance.getMaterial("Material");
	BlendingAttribute att = (BlendingAttribute) mat.get(BlendingAttribute.Type);

	if (solid)
	{
	    if (att.opacity < 0.9f)
		att.opacity = 1.0f;
	}
	else
	{
	    if (att.opacity > 0.9f)
		att.opacity = 0.3f;

	}

    }

}
