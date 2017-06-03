package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.util.DirectionalShadowSystemLight;
import com.mygdx.game.util.MovingPointShadowLight;
import com.mygdx.game.util.PointShadowLight;
import com.mygdx.game.util.ShadowSystem;

public class GameScreen extends ApplicationAdapter
{
    private static final String TAG = "Pong3d";
    
    PongController controller;
    PerspectiveCamera cam;
    
    // For Shadow Environment
    ShadowSystem shadowSystem;
    
    public static final int DEPTH_MAP_SIZE = 1024;
    
    // For Bullet
    DebugDrawer debugDrawer;
    private static final boolean BULLET_DEBUG = false;
    
    @Override
    public void create()
    {


	cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	cam.near = 1f;
	cam.far = 200;
	cam.position.set(-15, 15, 15);
	cam.lookAt(0, 0, 0);
	cam.update();
	
	Assets.instance.loadParticleEffects(cam);
	
	shadowSystem = new ShadowSystem();
	
	BulletWorld.instance.init();
	if (BULLET_DEBUG)
	{
	    debugDrawer = new DebugDrawer();
	    BulletWorld.world.setDebugDrawer(debugDrawer);
	    debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
	}
	PongObjects.instance.init(cam);
	
	shadowSystem.addLight(new PointShadowLight(new Vector3(0f, 13.8f, 32f),0.3f));
 	//shadowSystem.addLight(new PointShadowLight(new Vector3(45f, 0.0f, 0f),0.3f));
	//shadowSystem.addLight(new DirectionalShadowSystemLight(new Vector3(33, 0, 0), new Vector3(-1, 0, 0), 0.3f));
	shadowSystem.addLight(new MovingPointShadowLight(new Vector3(0f, 30.0f, 0f),0.1f));
	
	controller = new PongController();
    }
    


    /**
     * Two shaders, one creates the depth map, the other uses the depth map to create the shadows.
     * Both need to be setup correctly (see Git Hub example to fill in the missing pieces.
     */
    @Override
    public void render()
    {
	float delta = Gdx.graphics.getDeltaTime();
	update(delta);
	shadowSystem.render(cam, delta);
	
	if (BULLET_DEBUG)
	{
	    debugDrawer.begin(cam);
	    BulletWorld.world.debugDrawWorld();
	    debugDrawer.end();
	}
	
	BulletWorld.instance.update(delta);
    }

    public void update(float delta)
    {
	//PongObjects.instance.ground.body.applyCentralImpulse(new Vector3(5,0,0));
	PongObjects.instance.update(delta);
	Vector3 groundPos = PongObjects.instance.ground.body.getCenterOfMassPosition();
	//Vector3 groundPos = PongObjects.instance.sphere.body.getCenterOfMassPosition();
	cam.position.set(groundPos.x-15,groundPos.y+15,groundPos.z+15);
	cam.lookAt(groundPos);
	cam.update();
    }
    
    @Override
    public void dispose()
    {
        BulletWorld.instance.dispose();
        
    }
}
