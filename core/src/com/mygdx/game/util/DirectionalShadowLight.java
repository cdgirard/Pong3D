package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Pong3d;
import com.mygdx.game.objects.PongObjects;

public class DirectionalShadowLight extends AbstractShadowLight
{

    public Vector3 direction;
    public FrameBuffer frameBuffer;
    public Texture depthMap;

    public DirectionalShadowLight(final Vector3 position, final Vector3 direction)
    {
	this.position = position;
	this.direction = direction;
	init();
    }

    @Override
    public void applyToShader(final ShaderProgram sceneShaderProgram)
    {
	sceneShaderProgram.begin();
//	final int textureNum = 2;
//	depthMap.bind(textureNum);
//	sceneShaderProgram.setUniformi("u_depthMap", textureNum);
//	sceneShaderProgram.setUniformMatrix("u_lightTrans", camera.combined);
//	sceneShaderProgram.setUniformf("u_cameraFar", camera.far);
//	sceneShaderProgram.setUniformf("u_lightPosition", camera.position);
//	sceneShaderProgram.end();
	
	final int textureNum = 3;
	depthMap.bind(textureNum);
	sceneShaderProgram.setUniformi("u_depthMapDir", textureNum);
	sceneShaderProgram.setUniformMatrix("u_lightTrans", camera.combined);
	sceneShaderProgram.setUniformf("u_cameraFar", camera.far);
	sceneShaderProgram.setUniformf("u_type", 1);
	sceneShaderProgram.setUniformf("u_lightPosition", position);

    }

    @Override
    public void init()
    {
	super.init();

	camera = new PerspectiveCamera(120f, Pong3d.DEPTH_MAP_SIZE, Pong3d.DEPTH_MAP_SIZE);
	camera.near = 1f;
	camera.far = 70;
	camera.position.set(position);
	camera.direction.set(direction);
	camera.update();
    }

    @Override
    public void render()
    {

	if (frameBuffer == null)
	{
	    frameBuffer = new FrameBuffer(Format.RGBA8888, Pong3d.DEPTH_MAP_SIZE, Pong3d.DEPTH_MAP_SIZE, true);
	}
	frameBuffer.begin();
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

	shaderProgram.begin();
	shaderProgram.setUniformf("u_cameraFar", camera.far);
	shaderProgram.setUniformf("u_lightPosition", camera.position);
	shaderProgram.end();

	modelBatch.begin(camera);
	PongObjects.instance.render(modelBatch);
	modelBatch.end();

	frameBuffer.end();
	depthMap = frameBuffer.getColorBufferTexture();
    }

}
