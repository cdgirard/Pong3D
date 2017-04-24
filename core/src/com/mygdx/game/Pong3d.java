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
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
import com.mygdx.game.util.DirectionalShadowLight;
import com.mygdx.game.util.MovingPointShadowLight;
import com.mygdx.game.util.PointShadowLight;
import com.mygdx.game.util.ShadowMapShader;
import com.mygdx.game.util.ShadowShaderProvider;
import com.mygdx.game.util.ShadowSystem;
import com.mygdx.game.util.SimpleTextureShader;

public class Pong3d extends ApplicationAdapter
{
    PerspectiveCamera cam;
    Environment environment;
    ModelBatch modelBatch;
  
    // For Shadow Lights
    ModelBatch shaderModelBatch;
    
    // For Shadow Environment
    ShadowSystem shadowSystem;
    
    public static final int DEPTH_MAP_SIZE = 1024;
    
    // For Bullet
    DebugDrawer debugDrawer;

    @Override
    public void create()
    {
	Assets.instance.init();
	modelBatch = new ModelBatch();
	
	environment = new Environment();
	environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
	environment.add( new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1.0f, -1.0f, 0.0f));

	cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	cam.near = 1f;
	cam.far = 200;
	cam.position.set(-5, 5, 0);
	cam.lookAt(0, 0, 0);
	cam.update();
	
	shadowSystem = new ShadowSystem();
	
	// Shadow Lights Process
//	1.  Setup the Shadow Shader Provider that will do all the shadows together.
//	2. Until create shader is called it will not have the shader ready until create shader is called.
//	       2b. I think the design is that a shader should be created for each Renderable (maybe only called once?)
//	3.   Setup the lights - The Shadow Shader needs their info to do its job, so ideally they should be attached
//	                       to it.  However there could be more than one Shadow Shader for each Renderable, but should
//	                       only be one set of lights...
//	4. Render Process:
//	       - Lights need to build their depth maps for all the objects in the world
//	       - Shadow Shader needs to build the final render based on data from the lights
	
	// ShadowShader needs access to the Lights, Lights need to render all modelInstances first
	// before ShadowShader does it's render run over the same set of o
	
	// Going to need a special ShadowRender system that takes in the list of model instances and
	// then does the two step process of applying the lights and then rendering based off of the
	// data from the lights.
	
	
//	// Initializing Bullet Physics
//	Bullet.init();
//
//	// setting up the world
//	collisionConfiguration = new btDefaultCollisionConfiguration();
//	dispatcher = new btCollisionDispatcher(collisionConfiguration);
//	broadphase = new btDbvtBroadphase();
//	solver = new btSequentialImpulseConstraintSolver();
//	world = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
//	world.setGravity(new Vector3(0, -9.81f, 1f));
	
	
	//MyContactListener contactListener = new MyContactListener();
	BulletWorld.instance.init();
	//debugDrawer = new DebugDrawer();
	//BulletWorld.world.setDebugDrawer(debugDrawer);
	//debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
	PongObjects.instance.init();
	
	
	shadowSystem.addLight(new PointShadowLight(new Vector3(0f, 13.8f, 32f)));
	shadowSystem.addLight(new PointShadowLight(new Vector3(-25.5f, 12.0f, -26f)));
	shadowSystem.addLight(new DirectionalShadowLight(new Vector3(33, 10, 3), new Vector3(-10, 0, 0)));
	shadowSystem.addLight(new MovingPointShadowLight(new Vector3(0f, 30.0f, 0f)));
    }


    


    /**
     * Two shaders, one creates the depth map, the other uses the depth map to create the shadows.
     * Both need to be setup correctly (see Git Hub example to fill in the missing pieces.
     */
    @Override
    public void render()
    {
//	currentLight.render(PongObjects.instance);
//	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//	Gdx.gl.glClearColor(0, 0, 0, 1);
//	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

	// draw info
	//modelBatch.begin(cam);
	//PongObjects.instance.render(modelBatch,environment);
        //modelBatch.end();
	float delta = Gdx.graphics.getDeltaTime();
	shadowSystem.render(cam, delta);
        
        //debugDrawer.begin(cam);
        //BulletWorld.world.debugDrawWorld();
        //debugDrawer.end();
	
//	currentLight.applyToShader(shaderProgram);
//	shaderModelBatch.begin(cam);
//	PongObjects.instance.render(shaderModelBatch);
//	shaderModelBatch.end();
	
	
	BulletWorld.instance.update(delta);
    }

    @Override
    public void dispose()
    {
        BulletWorld.instance.dispose();
        
    }
}
