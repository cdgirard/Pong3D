package com.mygdx.game.lights;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.screens.GameScreen;

public class MyDirectionalShadowLight extends AbstractShadowLight
{

    public Vector3 direction;
    public FrameBuffer frameBuffer;
    public Texture depthMap;

    /**
     * direction is the direction the camera is looking, not the position the camera is
     * looking at.
     * @param position
     * @param direction
     */
    public MyDirectionalShadowLight(final Vector3 position, final Vector3 direction, float i)
    {
	intensity = i;
	this.position = position;
	this.direction = direction;
	init();
    }

    @Override
    public void applyToShader(final ShaderProgram sceneShaderProgram)
    {
	sceneShaderProgram.begin();
	
	final int textureNum = 3;
	depthMap.bind(textureNum);
	sceneShaderProgram.setUniformi("u_depthMapDir", textureNum);
	sceneShaderProgram.setUniformMatrix("u_lightTrans", camera.combined);
	sceneShaderProgram.setUniformf("u_cameraFar", camera.far);
	sceneShaderProgram.setUniformf("u_type", 1);
	sceneShaderProgram.setUniformf("u_lightPosition", position);
	sceneShaderProgram.setUniformf("u_lightIntensity", intensity);

    }

    @Override
    public void init()
    {
	super.init();

	camera = new PerspectiveCamera(120f, ShadowSystem.DEPTH_MAP_SIZE, ShadowSystem.DEPTH_MAP_SIZE);
	camera.near = 1f;
	camera.far = 70;
	camera.position.set(position);
	camera.direction.set(direction);
	camera.update();
    }

    @Override
    public void render()
    {
	if (!needsUpdate)
	{
	    return;
	}
	needsUpdate = false;
	if (frameBuffer == null)
	{
	    frameBuffer = new FrameBuffer(Format.RGBA8888, ShadowSystem.DEPTH_MAP_SIZE, ShadowSystem.DEPTH_MAP_SIZE, true);
	}
	frameBuffer.begin();
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

	shaderProgram.begin();
	shaderProgram.setUniformf("u_cameraFar", camera.far);
	shaderProgram.setUniformf("u_lightPosition", position);
	
	shaderProgram.end();

	modelBatch.begin(camera);
	system.render(modelBatch);
	modelBatch.end();

	frameBuffer.end();
	depthMap = frameBuffer.getColorBufferTexture();
    }

}
















