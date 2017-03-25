package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyContactListener;
import com.mygdx.game.bullet.PongBulletObjects;

public class Pong3d extends ApplicationAdapter
{
    PerspectiveCamera cam;
    Environment environment;
    ModelBatch modelBatch;
    
    btRigidBody groundBody;

    private btDefaultCollisionConfiguration collisionConfiguration;
    private btCollisionDispatcher dispatcher;
    private btDbvtBroadphase broadphase;
    private btSequentialImpulseConstraintSolver solver;
    private btDiscreteDynamicsWorld world;

    @Override
    public void create()
    {
	modelBatch = new ModelBatch();
	environment = new Environment();
	environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

	cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	cam.position.set(0, 10, -20);
	cam.lookAt(0, 0, 0);
	cam.update();

	// Initializing Bullet Physics
	Bullet.init();

	// setting up the world
	collisionConfiguration = new btDefaultCollisionConfiguration();
	dispatcher = new btCollisionDispatcher(collisionConfiguration);
	broadphase = new btDbvtBroadphase();
	solver = new btSequentialImpulseConstraintSolver();
	world = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
	world.setGravity(new Vector3(0, -9.81f, 1f));
	
	MyContactListener contactListener = new MyContactListener();
	BulletWorld.instance.init();
	PongBulletObjects.instance.init();
	groundBody = PongBulletObjects.instance.create_ground();
    }

    @Override
    public void render()
    {
	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

	float delta = Gdx.graphics.getDeltaTime();
	BulletWorld.instance.update(delta);

	
	// draw info
	modelBatch.begin(cam);
	
	modelBatch.render(PongBulletObjects.instance.groundInstance, environment);
	modelBatch.end();

    }

    @Override
    public void dispose()
    {
        world.dispose();
        
    }
}
