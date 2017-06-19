package com.mygdx.game.lights;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.lights.shaders.DepthMapShader;

public abstract class AbstractShadowLight
{
    // Which display system does this light belong to.
    protected AbstractShadowSystem system;
    protected boolean needsUpdate = true;
    public static ShaderProgram shaderProgram;
    public static ModelBatch modelBatch;
    protected float intensity = 1.0f;
    /**
     * The light camera
     */
    public Camera camera;

    /**
     * Position of the light
     */
    public Vector3 position = new Vector3();

    public AbstractShadowLight()
    {
    }

    /**
     * Add the uniforms to the scene shader
     * 
     * @param sceneShaderProgram
     */
    public abstract void applyToShader(ShaderProgram sceneShaderProgram);

    /**
     * Called on creation, initialize the object All lights will use the same
     * shader program to create their depth map - so we only need to setup the
     * shader once. If I am wrong then this will need to change.
     */
    public void init()
    {
	if (shaderProgram == null)
	{
	    shaderProgram = new ShaderProgram(Assets.depthMapVShader, Assets.depthMapFShader);

	    modelBatch = new ModelBatch(new DefaultShaderProvider()
	    {
		@Override
		protected Shader createShader(final Renderable renderable)
		{
		    return new DepthMapShader(renderable, shaderProgram);
		}
	    });
	}
    }
    
    /**
     * Set the system this light is attached.
     * @param sys
     */
    public void setSystem(AbstractShadowSystem sys)
    {
	system = sys;
    }

    /**
     * Create the depth map for this light
     * 
     * @param modelInstance
     */
    public abstract void render();

    /**
     * Execute an action on the light if needed
     * 
     * @param delta
     *            time delta
     */
    public void act(final float delta)
    {
	needsUpdate = true;
    }
    
    public void dispose()
    {
	if (shaderProgram != null)
	{
	    shaderProgram.dispose();
	    shaderProgram = null;
	}
	if (modelBatch != null)
	{
	    modelBatch.dispose();
	    modelBatch = null;
	}
    }

}
