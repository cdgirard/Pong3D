package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyMotionState;

public class PongObjects implements Disposable
{
    public static final PongObjects instance = new PongObjects();

    private static final Vector3 localInertia = new Vector3(1, 1, 1);
    
    public GameObject sphere, wall, wall2, wall3, target;
    public PlatformGameObject ground;
    
    protected  PongObjects()
    {
    	super();
    }
    
    public void init()
    {
    	create_platform();	
    	create_sphere();
    	create_wall();
    	create_wall2();
    	create_wall3();
    	create_target();
    }
    
    public void create_platform()
    {	
	Model model = Assets.assetManager.get(Assets.marble, Model.class);
    	ground = new PlatformGameObject();
    	ground.impulseForce = new Vector3(0,0,0);
    	ground.instance = new ModelInstance(model);
    	ground.motionState = new MyMotionState(ground.instance);
    	
    	float width = 5;
    	ground.shape = new btBoxShape(new Vector3(width, 1/4f, width));
    	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(100f, ground.motionState, ground.shape, Vector3.Zero);
    	bodyInfo.setRestitution(1.0f);
    	bodyInfo.setFriction(1.0f);
    	ground.body = new btRigidBody(bodyInfo);
    	ground.body.setMotionState(ground.motionState);	
    	ground.body.setUserValue(15);
    	ground.body.setCollisionFlags(ground.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
    	BulletWorld.world.addRigidBody(ground.body);
    	ground.body.setGravity(new Vector3(0,0,0));
    }
    
    public void create_sphere()
    {	
	Model model = Assets.assetManager.get(Assets.sphere, Model.class);
	float radius = 2.0f;
	
	sphere = new GameObject();
	Vector3 position = new Vector3(0,7,0);
	sphere.instance = new ModelInstance(model);
	sphere.motionState = new MyMotionState(sphere.instance);
	sphere.motionState.setWorldTransform(sphere.instance.transform.trn(position));
	sphere.shape = new btSphereShape(radius / 2f);
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0.1f, sphere.motionState, sphere.shape, localInertia);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	sphere.body = new btRigidBody(bodyInfo);
        //sphere.body.setCollisionFlags(sphere.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
	BulletWorld.world.addRigidBody(sphere.body);
    }
    
    public void create_target()
    {	
	Model model = Assets.assetManager.get(Assets.target, Model.class);
	
	target = new GameObject();
	Vector3 position = new Vector3(10,7,0);
	target.instance = new ModelInstance(model);
	target.motionState = new MyMotionState(target.instance);
	target.motionState.setWorldTransform(target.instance.transform.trn(position));
	int width = 10;
	target.shape = new btBoxShape(new Vector3(0.25f, 0.25f, 0.25f));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, target.motionState, target.shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	target.body = new btRigidBody(bodyInfo);
	target.body.setCollisionFlags(target.body.getCollisionFlags());
	
	BulletWorld.world.addRigidBody(target.body);
    }
    
    public void create_wall3()
    {	
	Model model = Assets.assetManager.get(Assets.wood2, Model.class);
	
	wall3 = new GameObject();
	Vector3 position = new Vector3(30,0,0);
	wall3.instance = new ModelInstance(model);
	// Bullet does not deal well with scaled objects for some reason.
	//wall.instance.transform.scale(1f,1.5f,1f);
	wall3.motionState = new MyMotionState(wall3.instance);
	wall3.motionState.setWorldTransform(wall3.instance.transform.trn(position));
	int width = 10;
	wall3.shape = new btBoxShape(new Vector3(0.025f, width-1.8f, width));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, wall3.motionState, wall3.shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	wall3.body = new btRigidBody(bodyInfo);
	wall3.body.setCollisionFlags(wall3.body.getCollisionFlags());
	
	BulletWorld.world.addRigidBody(wall3.body);
    }
    
    public void create_wall2()
    {	
	Model model = Assets.assetManager.get(Assets.wood, Model.class);
	
	wall2 = new GameObject();
	Vector3 position = new Vector3(20,0,-10);
	wall2.instance = new ModelInstance(model);
	// Bullet does not deal well with scaled objects for some reason.
	//wall.instance.transform.scale(1f,1.5f,1f);
	wall2.motionState = new MyMotionState(wall2.instance);
	wall2.motionState.setWorldTransform(wall2.instance.transform.trn(position));
	int width = 10;
	wall2.shape = new btBoxShape(new Vector3(width, width-1.8f, 0.025f));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, wall2.motionState, wall2.shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	wall2.body = new btRigidBody(bodyInfo);
	wall2.body.setCollisionFlags(wall2.body.getCollisionFlags());// | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK );
	
	BulletWorld.world.addRigidBody(wall2.body);
    }
    
    public void create_wall()
    {	
	Model model = Assets.assetManager.get(Assets.wood, Model.class);
	
	wall = new GameObject();
	Vector3 position = new Vector3(0,0,-10);
	wall.instance = new ModelInstance(model);
	//wall.instance.transform.scale(1f,1.5f,1f);
	wall.motionState = new MyMotionState(wall.instance);
	wall.motionState.setWorldTransform(wall.instance.transform.trn(position));
	int width = 10;
	wall.shape = new btBoxShape(new Vector3(width, width-1.8f, 0.025f));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, wall.motionState, wall.shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	wall.body = new btRigidBody(bodyInfo);
	wall.body.setCollisionFlags(wall.body.getCollisionFlags());// | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK );
	
	BulletWorld.world.addRigidBody(wall.body);
    }
    
    public void update(float delta)
    {
	ground.update(delta);
	sphere.update(delta);
	wall.update(delta);
	wall2.update(delta);
	wall3.update(delta);
	target.update(delta);
    }
    
    public void render(ModelBatch batch)
    {
	ground.render(batch);
	sphere.render(batch);
	wall.render(batch);
	wall2.render(batch);
	wall3.render(batch);
	target.render(batch);
    }
    
    public void render(ModelBatch batch, Environment env)
    {
	ground.render(batch, env);
	sphere.render(batch, env);
	wall.render(batch,env);
	wall2.render(batch,env);
	wall3.render(batch,env);
	target.render(batch,env);
    }

    @Override
    public void dispose()
    {
	ground.dispose();	
    }

}
