package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.objects.PongObjects;

public class ShadowSystem
{
    public Array<AbstractShadowLight> lights = new Array<AbstractShadowLight>();
    private FrameBuffer frameBufferShadows;

    // Shadows
    private ModelBatch shadowModelBatch;
    private ShadowMapShader shadowEnv;
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

    public Array<AbstractShadowLight> getLights()
    {
	return lights;
    }

    /**
     * Adds a light to be managed by the shadow light system.
     * 
     * @param light
     */
    public void addLight(AbstractShadowLight light)
    {
	lights.add(light);
    }

    /**
     * Load shader(s)
     */
    public void initSceneShader()
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
    public void act(final float delta)
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
	PongObjects.instance.render(shadowModelBatch);
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
	//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	sceneShaderProgram.begin();
	final int textureNum = 4;
	frameBufferShadows.getColorBufferTexture().bind(textureNum);
	sceneShaderProgram.setUniformi("u_shadows", textureNum);
	sceneShaderProgram.setUniformf("u_screenWidth", Gdx.graphics.getWidth());
	sceneShaderProgram.setUniformf("u_screenHeight", Gdx.graphics.getHeight());
	sceneShaderProgram.end();

	sceneModelBatch.begin(cam);
	PongObjects.instance.render(sceneModelBatch);
	sceneModelBatch.end();

    }
}
