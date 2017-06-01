package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.utils.Disposable;


public class Assets implements Disposable, AssetErrorListener
{
    public static final Assets instance = new Assets();
    
    public static final String TAG = Assets.class.getName();
    
    // Sounds
    public static final String explosion_snd = "sounds/explosion_snd.wav";
    public static final String splash_snd = "sounds/splash_snd.wav";
    
    // 3D Models
    public static final String sphere = "models/sphere.g3dj";
    public static final String wood = "models/rockWall3.g3dj";
    public static final String wood2 = "models/rockWall2.g3dj";
    public static final String marble = "models/bronzeCopper_platform.g3dj";
    public static final String target = "models/target2.g3dj";
    public static final String water = "models/water.g3dj";
    
    // 3D Effects
    public static final String fire = "particles/flame2.pfx";
    public static final String splash = "particles/splash.pfx";
    public static final String explosion = "particles/explosion2.pfx";
    
    public static FileHandle sceneVShader = Gdx.files.internal("shaders/scene_v.glsl");
    public static FileHandle sceneFShader = Gdx.files.internal("shaders/scene_f.glsl");
    
    public static FileHandle shadowVShader = Gdx.files.internal("shaders/shadows_v.glsl");
    public static FileHandle shadowFShader = Gdx.files.internal("shaders/shadows_f.glsl");

    public static FileHandle depthMapVShader = Gdx.files.internal("shaders/depthmap_v.glsl");
    public static FileHandle depthMapFShader = Gdx.files.internal("shaders/depthmap_f.glsl");

    public static AssetManager assetManager = new AssetManager();
    
    // ParticleSystem manages our particles - Not sure belongs here.
    // We need it both to load the particles and to use the particles in the game...is an 
    // odd class.
    public ParticleSystem particleSystem = new ParticleSystem();

    private Assets()
    {

    }

    /**
     * Ideally we should load our particles here, but they need the camera at loading
     * time, which we won't have access to later.  May create a method that can be called
     * later by classes that want to load/work with particles.
     */
    public void init()
    {
	assetManager.setErrorListener(this);
	
	// Load 3D Models
	assetManager.load(marble, Model.class);
	assetManager.finishLoading();
	
	assetManager.load(sphere, Model.class);
	assetManager.finishLoading();
	
	assetManager.load(wood, Model.class);
	assetManager.finishLoading();
	
	assetManager.load(wood2, Model.class);
	assetManager.finishLoading();
	
	assetManager.load(target, Model.class);
	assetManager.finishLoading();
	
	assetManager.load(water, Model.class);
	assetManager.finishLoading();
	
	// Load Sound Effects
	assetManager.load(explosion_snd, Sound.class);
	assetManager.finishLoading();
	
	assetManager.load(splash_snd, Sound.class);
	assetManager.finishLoading();
    }
    
    /**
     * 3D Particle Effects need the camera to load properly, so need method
     * called after the camera is setup to load them.  If I decide to attach
     * particles to different cameras, then I will need different load methods.
     * @param cam
     */
    public void loadParticleEffects(Camera cam)
    {
	
	// Setup the ParticleBatch for the two different types of particles we plan to load - Not using one now, so leaving out.
	//BillboardParticleBatch billboardSpriteBatch = new BillboardParticleBatch();
	//billboardSpriteBatch.setCamera(cam);
	//particleSystem.add(billboardSpriteBatch);
	
	 PointSpriteParticleBatch pointSpriteBatch = new PointSpriteParticleBatch();
	
	pointSpriteBatch.setCamera(cam);
	particleSystem.add(pointSpriteBatch);

	// How is it connected to this.
	ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
	ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
	assetManager.setLoader(ParticleEffect.class, loader);
	// Loading the fire trail effect
	assetManager.load(Assets.fire, ParticleEffect.class, loadParam);
	// Loading the explosion effect
	assetManager.load(Assets.explosion, ParticleEffect.class, loadParam);
	// Load the splash particle
	assetManager.load(Assets.splash, ParticleEffect.class, loadParam);
	assetManager.finishLoading();
	
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable)
    {
	Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);

    }

    @Override
    public void dispose()
    {
	assetManager.dispose();
    }

}
