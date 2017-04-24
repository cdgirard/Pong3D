package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyMotionState;

public class PongObjects implements Disposable
{
    public static final PongObjects instance = new PongObjects();
    // Needed for StaticBodies, don't care if info keeps getting written over.
    // By being final and static makes for faster code so don't keep wasting time
    // making one and then throwing it away.
    private static final Vector3 temp = new Vector3();
    private static final Vector3 localInertia = new Vector3(1, 1, 1);
    
    GameObject ground, sphere;
    
    protected  PongObjects()
    {
    	super();
    }
    
    public void init()
    {
    	create_ground();	
    	create_sphere();
    }
    
    public void create_ground()
    {	
	Model model = Assets.assetManager.get("models/marble.g3dj", Model.class);
    	ground = new GameObject();
    	ground.instance = new ModelInstance(model);
    	Vector3 position = new Vector3(0,0.25f,0);
    	//(ground.instance.transform.trn(position)).set(ground.instance.transform);
    	float width = 5;
    	ground.shape = new btBoxShape(new Vector3(width, 1/4f, width));
    	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, null, ground.shape, Vector3.Zero);
    	ground.body = new btRigidBody(bodyInfo);
    	ground.body.translate(position);
    	BulletWorld.world.addRigidBody(ground.body);
    }
    
    public void create_sphere()
    {	
	Model model = Assets.assetManager.get("models/sphere.g3dj", Model.class);
	float radius = 2.0f;
	Vector3 position = new Vector3(0,7,0);
	sphere = new GameObject();
    	
	sphere.instance = new ModelInstance(model);
	sphere.motionState = new MyMotionState(sphere.instance);
	sphere.motionState.setWorldTransform(sphere.instance.transform.trn(position));
	sphere.shape = new btSphereShape(radius / 2f);
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(1, sphere.motionState, sphere.shape, localInertia);
	sphere.body = new btRigidBody(bodyInfo);
	BulletWorld.world.addRigidBody(sphere.body);
    }
    
    public void render(ModelBatch batch)
    {
	ground.render(batch);
	sphere.render(batch);
    }
    
    public void render(ModelBatch batch, Environment env)
    {
	ground.render(batch, env);
	sphere.render(batch, env);
    }

    @Override
    public void dispose()
    {
	ground.dispose();	
    }

}
