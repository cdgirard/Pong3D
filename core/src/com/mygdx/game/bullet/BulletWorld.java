package com.mygdx.game.bullet;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.screens.GameScreen;

public class BulletWorld implements Disposable
{
    public static final BulletWorld instance = new BulletWorld();
    
    
    protected MyContactListener contactListener;
    protected btDefaultCollisionConfiguration collisionConfiguration;
    protected btCollisionDispatcher dispatcher;
    protected btDbvtBroadphase broadphase;
    protected btSequentialImpulseConstraintSolver solver;
    // For speed and ease of access.
    public static btDiscreteDynamicsWorld world;

    private BulletWorld()
    {
    }

    @Override
    public void dispose()
    {
	world.dispose();
	collisionConfiguration.dispose();
	dispatcher.dispose();
	broadphase.dispose();
	solver.dispose();
    }

    
    public void init(GameScreen gs)
    {
	Bullet.init();
	
	collisionConfiguration = new btDefaultCollisionConfiguration();
	dispatcher = new btCollisionDispatcher(collisionConfiguration);
	contactListener = new MyContactListener(gs);
	broadphase = new btDbvtBroadphase();
	solver = new btSequentialImpulseConstraintSolver();
	world = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
	world.setGravity(new Vector3(0, -9.81f, 0f));
    }

    public void update(float delta)
    {
	world.stepSimulation(delta, 5, 1 / 60f);
    }

    public void remove(btRigidBody body)
    {
	world.removeRigidBody(body);
	//((UserData) body.userData).dispose();

    }

    public btDiscreteDynamicsWorld getWorld()
    {
	return world;
    }

}
