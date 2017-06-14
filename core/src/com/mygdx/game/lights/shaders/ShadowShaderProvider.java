package com.mygdx.game.util;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShadowShaderProvider extends DefaultShaderProvider
{
    private ShadowMapShader shadowMapShader = null;
    private ShaderProgram shaderProgram;
    private ShadowSystem system;

    public ShadowShaderProvider(ShadowSystem sys, ShaderProgram shaderProg)
    {
	shaderProgram = shaderProg;
	system = sys;
    }

    /**
     * This is only called when the call to getShader by ModelBatch does not
     * have a shader for the Renderable.  The call to getShader is only done 
     * during the call to render (so shadowMapShader will not exist until 
     * after the first attempt to draw the scene.
     */
    @Override
    protected Shader createShader(final Renderable renderable)
    {
	if (shadowMapShader == null)
	    shadowMapShader = new ShadowMapShader(renderable, shaderProgram, system);

	return shadowMapShader;
    }

}
