package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyMotionState;

/**
 * This class needs some redesign on how we are managing the objects in the
 * game.
 * 
 * @author cdgira
 *
 */
public class PongObjects implements Disposable
{
    public static final PongObjects instance = new PongObjects();

    public static final Vector3 localInertia = new Vector3(1, 1, 1);

    private float timePassed = 0.0f;
    
    public GameObject wall, wall2, wall3, target, water;
    public PlatformGameObject ground;
    public SphereGameObject sphere;

    // Needs a better home.

    ParticleEffect splashEffect;
    ParticleEffect explosionEffect;
    boolean explosion = false;
    boolean splash = false;

    protected PongObjects()
    {
	super();
    }

    public void init(Camera cam)
    {
	ground = new PlatformGameObject();
	sphere = new SphereGameObject(cam);
	create_wall();
	create_wall2();
	create_wall3();
	target = new TargetGameObject();
	create_water();

	// Setup Splash for Testing


	ParticleEffect originalEffect = Assets.assetManager.get(Assets.splash);
	// we cannot use the originalEffect, we must make a copy each time we
	// create new particle effect
	splashEffect = originalEffect.copy();

	
	// Prep the Explosion effect for when we need to trigger it.  Will just keep one
	// and move it around as we need it.  Will work so long as we don't need to trigger more than one
	// explosion at a time.
	originalEffect = Assets.assetManager.get(Assets.explosion);
	explosionEffect = originalEffect.copy();
    }

    public void create_wall3()
    {
	Model model = Assets.assetManager.get(Assets.wood2, Model.class);

	wall3 = new GameObject();
	Vector3 position = new Vector3(30, 0, 0);
	wall3.instance = new ModelInstance(model);
	// Bullet does not deal well with scaled objects for some reason.
	// wall.instance.transform.scale(1f,1.5f,1f);
	wall3.motionState = new MyMotionState(wall3.instance);
	wall3.motionState.setWorldTransform(wall3.instance.transform.trn(position));
	int width = 10;
	wall3.shape = new btBoxShape(new Vector3(0.025f, width - 1.8f, width));
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
	Vector3 position = new Vector3(20, 0, -10);
	wall2.instance = new ModelInstance(model);
	// Bullet does not deal well with scaled objects for some reason.
	// wall.instance.transform.scale(1f,1.5f,1f);
	wall2.motionState = new MyMotionState(wall2.instance);
	wall2.motionState.setWorldTransform(wall2.instance.transform.trn(position));
	int width = 10;
	wall2.shape = new btBoxShape(new Vector3(width, width - 1.8f, 0.025f));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, wall2.motionState, wall2.shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	wall2.body = new btRigidBody(bodyInfo);
	wall2.body.setCollisionFlags(wall2.body.getCollisionFlags());// |
								     // btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK
								     // );

	BulletWorld.world.addRigidBody(wall2.body);
    }

    public void create_wall()
    {
	Model model = Assets.assetManager.get(Assets.wood, Model.class);

	wall = new GameObject();
	Vector3 position = new Vector3(0, 0, -10);
	wall.instance = new ModelInstance(model);
	// wall.instance.transform.scale(1f,1.5f,1f);
	wall.motionState = new MyMotionState(wall.instance);
	wall.motionState.setWorldTransform(wall.instance.transform.trn(position));
	int width = 10;
	wall.shape = new btBoxShape(new Vector3(width, width - 1.8f, 0.025f));
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, wall.motionState, wall.shape, Vector3.Zero);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	wall.body = new btRigidBody(bodyInfo);
	wall.body.setCollisionFlags(wall.body.getCollisionFlags());// |
								   // btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK
								   // );

	BulletWorld.world.addRigidBody(wall.body);
    }

    public void create_water()
    {
	Model model = Assets.assetManager.get(Assets.water, Model.class);
	water = new GameObject();
	Vector3 position = new Vector3(0, -5, 0);
	water.instance = new ModelInstance(model);
	water.instance.transform.trn(position);
    }
    
    /**
     * Sphere collided with a target so want to create an explosion effect from
     * that collision.
     */
    public void startExplosion(Matrix4 location)
    {
	explosion = true;
	explosionEffect.setTransform(location);
	explosionEffect.init();
	
	Assets.instance.particleSystem.add(explosionEffect);
	explosionEffect.start();
        // Hopefully this will work to then immediately tell the explosion to stop and only do one cycle.
	RegularEmitter reg = (RegularEmitter) explosionEffect.getControllers().first().emitter;
	reg.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);
    }
    
    /**
     * Sphere went into the water, trigger a splash at the entry point.
     * @param locaton
     */
    public void startSplash(Matrix4 location)
    {
	splash = true;
	splashEffect.setTransform(location);
	splashEffect.init();
	
	Assets.instance.particleSystem.add(splashEffect);
	splashEffect.start();
        // Hopefully this will work to then immediately tell the explosion to stop and only do one cycle.
	RegularEmitter reg = (RegularEmitter) splashEffect.getControllers().first().emitter;
	reg.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);
    }

    private void particleStuffCleanUp()
    {

	// // Stopping a Partcle Effect
	// Emitter emitter = pfx.getControllers().first().emitter;
	// if (emitter instanceof RegularEmitter)
	// {
	// 
	// }
	//
	// // Cleanup of a dead particle
	// if (currentEffects != null)
	// {
	// Assets.instance.particleSystem.remove(currentEffects);
	// currentEffects.dispose();
	// }
    }

    public void update(float delta)
    {
	if (explosion)
	{
	    RegularEmitter reg = (RegularEmitter) explosionEffect.getControllers().first().emitter;
	    if (reg.isComplete())
	    {
		Assets.instance.particleSystem.remove(explosionEffect);
	        explosion = false;
	    }
	}
	if (splash)
	{
	    RegularEmitter reg = (RegularEmitter) splashEffect.getControllers().first().emitter;
	    if (reg.isComplete())
	    {
		Assets.instance.particleSystem.remove(splashEffect);
		splash = false;
	    }
	}
	timePassed += delta;
	if (timePassed > 1/60f)
	{
	        // Needs to be called so all particles update for next time step.
		// Should only be called once per game loop update.
		// 3D particle system has no connection to the game time, seems we have to 
		// handle this on our own.
		Assets.instance.particleSystem.update(); 
		timePassed -= 1/60f;
	}
	ground.update(delta);
	sphere.update(delta);
	wall.update(delta);
	wall2.update(delta);
	wall3.update(delta);
	if (target.visible)
	    target.update(delta);
    }

    public void render(ModelBatch batch)
    {
	ground.render(batch);
	sphere.render(batch);
	wall.render(batch);
	wall2.render(batch);
	wall3.render(batch);
	water.render(batch);
	if (target.visible)
	    target.render(batch);
	renderParticleEffects(batch);
    }

    public void render(ModelBatch batch, Environment env)
    {
	ground.render(batch, env);
	sphere.render(batch, env);
	wall.render(batch, env);
	wall2.render(batch, env);
	wall3.render(batch, env);
	if (target.visible)
	    target.render(batch, env);
    }

    /**
     * Rendering the particles....probably needs to be housed in an object in
     * PongObjects.
     */
    private void renderParticleEffects(ModelBatch batch)
    {
	
						
	Assets.instance.particleSystem.begin();
	Assets.instance.particleSystem.draw();
	Assets.instance.particleSystem.end();
	batch.render(Assets.instance.particleSystem);
	
    }

    @Override
    public void dispose()
    {
	ground.dispose();
    }

}
