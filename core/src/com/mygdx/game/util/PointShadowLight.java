package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Pong3d;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.objects.PongObjects;

public class PointShadowLight extends AbstractShadowLight
{
    public FrameBufferCubeMap frameBuffer;
    public Cubemap depthMap;

    public PointShadowLight(final Vector3 position, float i)
    {
	this.position = position;
	this.intensity = i;
	init();
    }

    @Override
    public void applyToShader(final ShaderProgram sceneShaderProgram)
    {
	final int textureNum = 2;
	depthMap.bind(textureNum);
	sceneShaderProgram.setUniformf("u_type", 2);
	sceneShaderProgram.setUniformi("u_depthMapCube", textureNum);
	sceneShaderProgram.setUniformf("u_cameraFar", camera.far);
	sceneShaderProgram.setUniformf("u_lightPosition", position);
	sceneShaderProgram.setUniformf("u_lightIntensity", intensity);
    }

    @Override
    public void init()
    {
	super.init();

	camera = new PerspectiveCamera(90f, Pong3d.DEPTH_MAP_SIZE, Pong3d.DEPTH_MAP_SIZE);
	camera.near = 4f;
	camera.far = 70;
	camera.position.set(position);
	camera.update();
    }

    @Override
    public void render()
    {
	if (!needsUpdate)
	    return;
	needsUpdate = false;
	if (frameBuffer == null)
	{
	    frameBuffer = new FrameBufferCubeMap(Format.RGBA8888, Pong3d.DEPTH_MAP_SIZE, true);
	}
	
	shaderProgram.begin();
	shaderProgram.setUniformf("u_cameraFar", camera.far);
	shaderProgram.setUniformf("u_lightPosition", position);
	
	shaderProgram.end();

	for (int s = 0; s <= 5; s++)
	{
	    final Cubemap.CubemapSide side = Cubemap.CubemapSide.values()[s];
	    // If you comment out the frameBuffer commands you can see what is gathered for the depthMap.
	    frameBuffer.begin(side, camera);
	    Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

	    modelBatch.begin(camera);
	    PongObjects.instance.render(modelBatch);
	    modelBatch.end();
	}

	frameBuffer.end();
	depthMap = frameBuffer.getColorBufferTexture();
    }
}