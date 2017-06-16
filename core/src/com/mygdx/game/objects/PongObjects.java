package com.mygdx.game.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.PongGlobals;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.AudioManager;
import com.mygdx.game.util.LevelLoader;

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
    
    public Array<GameObject> objects = new Array<GameObject>();
    
    public GameObject water;
    public WallGameObject wall, wall2, wall3;
    public PlatformGameObject ground;
    public SphereGameObject sphere;
    public TargetGameObject target, target2;

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
	// Build the Game Objects
	//ground = new PlatformGameObject();
	//sphere = new SphereGameObject(new Vector3(0, 7, 0),cam);
	//create_wall();
	//create_wall2();
	//create_wall3();
	target = new TargetGameObject(new Vector3(7,5,5),GameObject.SCORE_TARGET);
	target2 = new TargetGameObject(new Vector3(50,5,50),GameObject.SOLID_TARGET);
	create_water();
	
	// Put them all in an array to make adding new objects to the game 
	// more easy.
	//objects.add(ground);
	//objects.add(sphere);
	//objects.add(wall);
	//objects.add(wall2);
	//objects.add(wall3);
	objects.add(target);
	objects.add(target2);
	objects.add(water);
	
	LevelLoader.loadLevel("levels/Empty_Level.png", cam);
	

	// Setup Splash
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
	wall3 = new WallGameObject(new Vector3(20, 0, -10), WallGameObject.NORTH_SOUTH);
    }

    public void create_wall2()
    {
	wall2 = new WallGameObject(new Vector3(20, 0, -10), WallGameObject.EAST_WEST);
    }

    public void create_wall()
    {
        wall = new WallGameObject(new Vector3(0, 0, -10), WallGameObject.EAST_WEST);
    }

    public void create_water()
    {
	Model model = Assets.assetManager.get(Assets.water, Model.class);
	water = new GameObject();
	Vector3 position = new Vector3(50, -5, 50);
	water.instance = new ModelInstance(model);
	water.instance.transform.trn(position);
    }
    
    /**
     * Sphere collided with a target so want to create an explosion effect from
     * that collision.
     */
    public void startExplosion(Matrix4 location)
    {
	Sound exp_snd = Assets.assetManager.get(Assets.explosion_snd, Sound.class);
	AudioManager.instance.play(exp_snd);
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
	Sound splash_snd = Assets.assetManager.get(Assets.splash_snd, Sound.class);
	AudioManager.instance.play(splash_snd);
	
	splash = true;
	splashEffect.setTransform(location);
	splashEffect.init();
	
	Assets.instance.particleSystem.add(splashEffect);
	splashEffect.start();
        // Hopefully this will work to then immediately tell the explosion to stop and only do one cycle.
	RegularEmitter reg = (RegularEmitter) splashEffect.getControllers().first().emitter;
	reg.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);
    }

    public void update(float delta)
    {
	// These should probably go with the objects that triggered them.
	if (explosion)
	{
	    RegularEmitter reg = (RegularEmitter) explosionEffect.getControllers().first().emitter;
	    if (reg.isComplete())
	    {
		Assets.instance.particleSystem.remove(explosionEffect);
		PongGlobals.scorePoints();
	        explosion = false;
	    }
	}
	if (splash)
	{
	    RegularEmitter reg = (RegularEmitter) splashEffect.getControllers().first().emitter;
	    if (reg.isComplete())
	    {
		Assets.instance.particleSystem.remove(splashEffect);
		PongGlobals.lifeLost();
		splash = false;
	    }
	}
	timePassed += delta;
	while (timePassed > 1/60f)
	{
	        // Needs to be called so all particles update for next time step.
		// Should only be called once per game loop update.
		// 3D particle system has no connection to the game time, seems we have to 
		// handle this on our own.
		Assets.instance.particleSystem.update(); 
		timePassed -= 1/60f;
	}
	for (GameObject obj: objects)
	{
	    obj.update(delta);
	}
    }

    @Override
    public void dispose()
    {
	for (GameObject obj: objects)
	    obj.dispose();
	objects.removeRange(0, objects.size-1);
	Assets.instance.particleSystem.removeAll();
	splashEffect.dispose();
	explosionEffect.dispose();
	
    }

}
