package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyContactListener;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.util.AbstractShadowLight;
import com.mygdx.game.util.DirectionalShadowSystemLight;
import com.mygdx.game.util.MovingPointShadowLight;
import com.mygdx.game.util.PointShadowLight;
import com.mygdx.game.util.ShadowMapShader;
import com.mygdx.game.util.ShadowShaderProvider;
import com.mygdx.game.util.ShadowSystem;
import com.mygdx.game.util.SimpleTextureShader;

public class Pong3d extends ApplicationAdapter
{
    private static final String TAG = "Pong3d";
    
    PongController controller;
    PerspectiveCamera cam;
    
    // For Shadow Environment
    ShadowSystem shadowSystem;
    
    public static final int DEPTH_MAP_SIZE = 1024;
    
    // For Bullet
    DebugDrawer debugDrawer;
    private static final boolean BULLET_DEBUG = true;

    @Override
    public void create()
    {
	Assets.instance.init();

	cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	cam.near = 1f;
	cam.far = 200;
	cam.position.set(0, 10, 20);
	cam.lookAt(0, 0, 0);
	cam.update();
	
	shadowSystem = new ShadowSystem();
	
	BulletWorld.instance.init();
	if (BULLET_DEBUG)
	{
	    debugDrawer = new DebugDrawer();
	    BulletWorld.world.setDebugDrawer(debugDrawer);
	    debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
	}
	PongObjects.instance.init();
	
	shadowSystem.addLight(new PointShadowLight(new Vector3(0f, 13.8f, 32f),0.3f));
 	shadowSystem.addLight(new PointShadowLight(new Vector3(45f, 0.0f, 0f),0.3f));
	shadowSystem.addLight(new DirectionalShadowSystemLight(new Vector3(33, 0, 0), new Vector3(-1, 0, 0), 0.3f));
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
	    BulletWorld.instance.world.debugDrawWorld();
	    debugDrawer.end();
	}
	
	BulletWorld.instance.update(delta);
    }

    public void update(float delta)
    {
	//PongObjects.instance.ground.body.applyCentralImpulse(new Vector3(5,0,0));
	PongObjects.instance.update(delta);

    }
    
    @Override
    public void dispose()
    {
        BulletWorld.instance.dispose();
        
    }
}
