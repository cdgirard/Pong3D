package com.mygdx.game.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.AudioManager;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.screens.GameScreen;

public class MyContactListener extends ContactListener
{
    private GameScreen game;
    
    public MyContactListener(GameScreen gs)
    {
	game = gs;
    }
    
    @Override
    public boolean onContactAdded(btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0, btCollisionObjectWrapper colObj1Wrap, int partId1, int index1)
    {
	// Gdx.app.error("TAG","Callback"+colObj0Wrap+" : "+colObj1Wrap);

	// PongObjects.instance.ground.body.setLinearVelocity(new
	// Vector3(0,0,0));
	// instances.get(colObj1Wrap.getCollisionObject().getUserValue()).moving
	// = false;
	return true;
    }

    @Override
    public void onContactProcessed(btManifoldPoint cp, btCollisionObject colObj0, btCollisionObject colObj1)
    {
	// PongObjects.instance.ground.body.clearForces();
    }

    @Override
    public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1)
    {
	// PongObjects.instance.ground.body.clearForces();
	if ((colObj0.userData != null) && (colObj1.userData != null))
	{
	    //Gdx.app.error("TAG", "Callback" + colObj0.userData + " : " + colObj1.userData);
	    if (((GameObject) colObj0.userData).objId == GameObject.SPHERE)
		processSphereCollision((GameObject) colObj1.userData);
	    else if (((GameObject) colObj1.userData).objId == GameObject.SPHERE)
		processSphereCollision((GameObject) colObj0.userData);
	    else if (((GameObject) colObj0.userData).objId == GameObject.PLATFORM)
		processPlatformCollision((GameObject) colObj1.userData);
	    else if (((GameObject) colObj1.userData).objId == GameObject.PLATFORM)
		processPlatformCollision((GameObject) colObj0.userData);
	}
    }
    
    /**
     * Sphere collided with another game object.
     * @param obj
     */
    public void processSphereCollision(GameObject obj)
    {
	if (obj.objId == GameObject.SCORE_TARGET)
	{
	    BulletWorld.world.removeRigidBody(obj.body);
		obj.visible = false;
		game.shadowSystem.removeRenderObject(obj.instance);
		PongObjects.instance.startExplosion(obj.instance.transform);
	}
	else if (obj.objId == GameObject.OBSTACLE_TARGET)
	{
	    Sound exp_snd = Assets.assetManager.get(Assets.anvil_snd, Sound.class);
	    AudioManager.instance.play(exp_snd);
	}
	else if (obj.objId == GameObject.WALL)
	{
	    Sound exp_snd = Assets.assetManager.get(Assets.tick_snd, Sound.class);
	    AudioManager.instance.play(exp_snd);
	}
	if (obj.objId == GameObject.PLATFORM)
	{
	    Sound exp_snd = Assets.assetManager.get(Assets.muted_snd, Sound.class);
	    AudioManager.instance.play(exp_snd);
	}
    }
    
    /**
     * Platform collided with a game object that is not the sphere (should just
     * be colliding with the wall).
     * @param obj
     */
    public void processPlatformCollision(GameObject obj)
    {
	if (obj.objId == GameObject.WALL)
	{
	    Sound exp_snd = Assets.assetManager.get(Assets.pan_snd, Sound.class);
	    AudioManager.instance.play(exp_snd);
	}
    }

    @Override
    public void onContactEnded(btCollisionObject colObj0, btCollisionObject colObj1)
    {
	// PongObjects.instance.ground.body.clearForces();
    }

}
