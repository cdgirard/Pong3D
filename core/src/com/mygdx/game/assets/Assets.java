package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener
{
    public static final Assets instance = new Assets();
    
    public static final String TAG = Assets.class.getName();
    
    // 3D Models
    public static final String sphere = "models/sphere2.g3dj";
    public static final String wood = "models/wood.g3dj";
    public static final String wood2 = "models/wood2.g3dj";
    public static final String marble = "models/marble.g3dj";
    public static final String target = "models/target.g3dj";
    public static final String water = "models/water.g3dj";
    
    // 3D Effects
    public static final String fire = "particles/flame.pfx";
    public static final String splash = "particles/splash.pfx";
    
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
