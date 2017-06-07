package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;


public class Assets implements Disposable, AssetErrorListener
{
    public static final Assets instance = new Assets();
    
    public static final String TAG = Assets.class.getName();
    
    // Sounds
    public static final String explosion_snd = "sounds/explosion_snd.wav";
    public static final String splash_snd = "sounds/splash_snd.wav";
    public static final String anvil_snd = "sounds/anvil_snd.wav";
    public static final String golf_snd = "sounds/golfBall_snd.wav";
    public static final String bone_snd = "sounds/boneBreaking_snd.wav";
    public static final String pan_snd = "sounds/fryingPan_snd.wav";
    public static final String tick_snd = "sounds/tick_snd.mp3";
    public static final String muted_snd = "sounds/mutedMetal_snd.wav";
    
    // 3D Models
    public static final String sphere = "models/concreteBare_sphere.g3dj";
    public static final String wood = "models/rockWall_wall_a.g3dj";
    public static final String wood2 = "models/rockWall_wall_b.g3dj";
    public static final String marble = "models/bronzeCopper_platform.g3dj";
    public static final String target = "models/woodPlanks_target.g3dj";
    public static final String target2 = "models/metalPlatesDucts_target.g3dj";
    public static final String water = "models/paperCrumpled_water.g3dj";
    
    // 3D Effects
    public static final String fire = "particles/flame2.pfx";
    public static final String splash = "particles/splash.pfx";
    public static final String explosion = "particles/explosion2.pfx";
    
    // Shaders
    public static FileHandle sceneVShader = Gdx.files.internal("shaders/scene_v.glsl");
    public static FileHandle sceneFShader = Gdx.files.internal("shaders/scene_f.glsl");
    
    public static FileHandle shadowVShader = Gdx.files.internal("shaders/shadows_v.glsl");
    public static FileHandle shadowFShader = Gdx.files.internal("shaders/shadows_f.glsl");

    public static FileHandle depthMapVShader = Gdx.files.internal("shaders/depthmap_v.glsl");
    public static FileHandle depthMapFShader = Gdx.files.internal("shaders/depthmap_f.glsl");
    
    // UI
    public static final String TEXTURE_ATLAS_LIBGDX_UI = "ui/uiskin.atlas";
    public static final String SKIN_LIBGDX_UI = "ui/uiskin.json";
    public static final String BACKGROUND_IMG = "ui/Pong3D.jpg";
    public static final String PLAY_BTN_UP_IMG = "ui/PlayBtn_Up.jpg";
    public static final String PLAY_BTN_DWN_IMG = "ui/PlayBtn_Dwn.jpg";
    public static final String OPTION_BTN_UP_IMG = "ui/OptionBtn_Up.jpg";
    public static final String OPTION_BTN_DWN_IMG = "ui/OptionBtn_Dwn.jpg";
    public static final String BALL = "ui/ball2.png";
    public static final String TITLE = "ui/Title.png";
    public static final float VIEWPORT_GUI_WIDTH = 1024.0f;
    public static final float VIEWPORT_GUI_HEIGHT = 768.0f;
    public  BitmapFont defaultSmall;
    public  BitmapFont defaultNormal;
    public BitmapFont defaultBig;
    public Skin skinLibgdx;

    
    // GamePreferences
    // TODO: Some of these constants should be moved to another class.
    public static final String PREFERENCES = "game.prefs";



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
	
	assetManager.load(target2, Model.class);
	assetManager.finishLoading();
	
	assetManager.load(water, Model.class);
	assetManager.finishLoading();
	
	// Load Sound Effects
	assetManager.load(explosion_snd, Sound.class);
	assetManager.finishLoading();
	
	assetManager.load(splash_snd, Sound.class);
	assetManager.finishLoading();
	
	assetManager.load(anvil_snd, Sound.class);
	assetManager.finishLoading();
	
	assetManager.load(golf_snd, Sound.class);
	assetManager.finishLoading();
	
	assetManager.load(bone_snd, Sound.class);
	assetManager.finishLoading();
	
	assetManager.load(pan_snd, Sound.class);
	assetManager.finishLoading();
	
	assetManager.load(tick_snd, Sound.class);
	assetManager.finishLoading();
	
	assetManager.load(muted_snd, Sound.class);
	assetManager.finishLoading();
	
	// Load UI Images
	assetManager.load(BACKGROUND_IMG, Texture.class);
	assetManager.finishLoading();
	
	assetManager.load(PLAY_BTN_UP_IMG, Texture.class);
	assetManager.finishLoading();
	
	assetManager.load(PLAY_BTN_DWN_IMG, Texture.class);
	assetManager.finishLoading();
	
	assetManager.load(OPTION_BTN_UP_IMG, Texture.class);
	assetManager.finishLoading();
	
	assetManager.load(OPTION_BTN_DWN_IMG, Texture.class);
	assetManager.finishLoading();
	
	assetManager.load(BALL, Texture.class);
	assetManager.finishLoading();
	
	assetManager.load(TITLE, Texture.class);
	assetManager.finishLoading();
	
	loadFonts();
    }
    
    private void loadFonts()
    {
	// Images to be used in the UI
		//skinGame = new Skin(Gdx.files.internal(Assets.SKIN_GAME_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		// Special images for creating the core of the UI.
		skinLibgdx = new Skin(Gdx.files.internal(Assets.SKIN_LIBGDX_UI), new TextureAtlas(Assets.TEXTURE_ATLAS_LIBGDX_UI));
		
    	defaultSmall = new BitmapFont(Gdx.files.internal("ui/default.fnt"),true);
    	defaultNormal = new BitmapFont(Gdx.files.internal("ui/default.fnt"),true);
    	defaultBig = new BitmapFont(Gdx.files.internal("ui/default.fnt"),true);
    	
    	defaultSmall.getData().setScale(0.75f);
    	defaultNormal.getData().setScale(1.0f);
    	defaultBig.getData().setScale(2.0f);
    	
    	defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    	defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    	defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
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
	skinLibgdx.dispose();
    }

}
