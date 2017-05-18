package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier.PolarAcceleration;
import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Array;
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

    public GameObject wall, wall2, wall3, target, water;
    public PlatformGameObject ground;
    public SphereGameObject sphere;

    // Needs a better home.

    ParticleEffect splashEffect;

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
	PointSpriteParticleBatch pointSpriteBatch = new PointSpriteParticleBatch();
	pointSpriteBatch.setCamera(cam);
	Assets.instance.particleSystem.add(pointSpriteBatch);

	ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(Assets.instance.particleSystem.getBatches());
	ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
	Assets.assetManager.setLoader(ParticleEffect.class, loader);
	Assets.assetManager.load(Assets.splash, ParticleEffect.class, loadParam);
	Assets.assetManager.finishLoading();

	ParticleEffect originalEffect = Assets.assetManager.get(Assets.splash);
	// we cannot use the originalEffect, we must make a copy each time we
	// create new particle effect
	splashEffect = originalEffect.copy();
	splashEffect.init();
	splashEffect.start(); // optional: particle will begin playing
			      // immediately

	Assets.instance.particleSystem.add(splashEffect);
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

    private void particleStuffCleanUp()
    {

	// // Stopping a Partcle Effect
	// Emitter emitter = pfx.getControllers().first().emitter;
	// if (emitter instanceof RegularEmitter)
	// {
	// RegularEmitter reg = (RegularEmitter) emitter;
	// reg.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);
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
	Assets.instance.particleSystem.update(); // technically not
						 // necessary for rendering
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
