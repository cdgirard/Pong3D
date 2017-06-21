package com.mygdx.game.lights;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.lights.shaders.ShadowMapShader;
import com.mygdx.game.lights.shaders.ShadowShaderProvider;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.util.SimpleTextureShader;

// TODO: Check out xoppa's blog with a simple 3D shader example and walk through it.  See if we can eventually 
//       adapt it to this Shadow light system for better shadows with spheres.

// Shadow Lights Process
//1.  Setup the Shadow Shader Provider that will do all the shadows together.
//2. Until create shader is called it will not have the shader ready until create shader is called.
//       2b. I think the design is that a shader should be created for each Renderable (maybe only called once?)
//3.   Setup the lights - The Shadow Shader needs their info to do its job, so ideally they should be attached
//                       to it.  However there could be more than one Shadow Shader for each Renderable, but should
//                       only be one set of lights...
//4. Render Process:
//       - Lights need to build their depth maps for all the objects in the world
//       - Shadow Shader needs to build the final render based on data from the lights

// ShadowShader needs access to the Lights, Lights need to render all modelInstances first
// before ShadowShader does it's render run over the same set of o

// Going to need a special ShadowRender system that takes in the list of model instances and
// then does the two step process of applying the lights and then rendering based off of the
// data from the lights.
public class ShadowSystem extends AbstractShadowSystem
{
    public static final int DEPTH_MAP_SIZE = 1024;
    
    private FrameBuffer frameBufferShadows;

    // Shadows
    private ModelBatch shadowModelBatch;
    ShaderProgram shadowsShaderProgram;
    private ShadowShaderProvider shadowProvider;

    // Scene
    private ModelBatch sceneModelBatch;
    ShaderProgram sceneShaderProgram;

    public ShadowSystem()
    {
	initSceneShader();
	initShadowShader();
    }

    /**
     * Load shader(s)
     */
    private void initSceneShader()
    {
	ShaderProgram.pedantic = false;
	sceneShaderProgram = new ShaderProgram(Assets.sceneVShader, Assets.sceneFShader);

	sceneShaderProgram.isCompiled();
	Gdx.app.error("ShadowSystem", sceneShaderProgram.getLog());

	sceneModelBatch = new ModelBatch(new DefaultShaderProvider()
	{
	    @Override
	    protected Shader createShader(final Renderable renderable)
	    {
		return new SimpleTextureShader(renderable, sceneShaderProgram);
	    }
	});
    }

    /**
     * Load shader(s)
     */
    private void initShadowShader()
    {
	ShaderProgram.pedantic = false;
	shadowsShaderProgram = new ShaderProgram(Assets.shadowVShader, Assets.shadowFShader);
	shadowsShaderProgram.isCompiled();
	Gdx.app.error("Pong3d", shadowsShaderProgram.getLog());

	shadowProvider = new ShadowShaderProvider(this, shadowsShaderProgram);
	shadowModelBatch = new ModelBatch(shadowProvider);
    }

    public void render(Camera cam, float delta)
    {
	act(delta);
	renderLights();
	renderShadows(cam);
	renderScene(cam);
    }

    /**
     * Everything that is not directly drawing but needs to be computed each
     * frame
     * 
     * @param delta
     */
    private void act(final float delta)
    {
	for (final AbstractShadowLight light : lights)
	{
	    light.act(delta);
	}
    }

    /**
     * Generate all the various shader maps needed to properly light the scene
     * with the provided lights.
     */
    private void renderLights()
    {
	for (final AbstractShadowLight light : lights)
	{
	    light.render();
	}
    }

    private void renderShadows(Camera cam)
    {
	if (frameBufferShadows == null)
	{
	    frameBufferShadows = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
	}
	frameBufferShadows.begin();

	Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 0.4f);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

	shadowModelBatch.begin(cam);
	render(shadowModelBatch);
	shadowModelBatch.end();

	frameBufferShadows.end();
    }

    /**
     * Draw the scene based on all the data from the lights.
     */
    private void renderScene(Camera cam)
    {
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
	// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	sceneShaderProgram.begin();
	final int textureNum = 4;
	frameBufferShadows.getColorBufferTexture().bind(textureNum);
	sceneShaderProgram.setUniformi("u_shadows", textureNum);
	sceneShaderProgram.setUniformf("u_screenWidth", Gdx.graphics.getWidth());
	sceneShaderProgram.setUniformf("u_screenHeight", Gdx.graphics.getHeight());
	sceneShaderProgram.end();

	sceneModelBatch.begin(cam);
	render(sceneModelBatch);
	sceneModelBatch.end();
    }
    
    /**
     * Renders all the objects being managed by the Shadow Light System.
     * @param batch
     */
    public void render(ModelBatch batch)
    {
	for (ParticleSystem system : particleSystems)
	{
	    system.begin();
	    system.draw();
	    system.end();
	    batch.render(system);
	}
	batch.render(objects);
    }
    
    public void dispose()
    {
	if (objects.size > 0)
	    objects.removeRange(0, objects.size-1);
	if (particleSystems.size > 0)
	    particleSystems.removeRange(0, particleSystems.size-1);
	shadowModelBatch.dispose();
	shadowsShaderProgram.dispose();
	sceneModelBatch.dispose();
	sceneShaderProgram.dispose();
	for (AbstractShadowLight light : lights)
	    light.dispose();
    }
}
