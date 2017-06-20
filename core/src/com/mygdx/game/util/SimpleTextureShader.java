package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Inputs;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Setters;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Simple shader for an object with a texture
 */
public class SimpleTextureShader extends BaseShader
{
    public Renderable renderable;

    public final static Uniform opacity = new Uniform("u_opacity", BlendingAttribute.Type);

    public final int u_opacity;

    @Override
    public void end()
    {
	super.end();
    }

    public SimpleTextureShader(final Renderable renderable, final ShaderProgram shaderProgramModelBorder)
    {
	this.renderable = renderable;
	this.program = shaderProgramModelBorder;
	register(Inputs.worldTrans, Setters.worldTrans);
	register(Inputs.projViewTrans, Setters.projViewTrans);
	register(Inputs.normalMatrix, Setters.normalMatrix);
	register(Inputs.diffuseTexture, Setters.diffuseTexture);
	u_opacity = register(Inputs.opacity);

    }

    @Override
    public void begin(final Camera camera, final RenderContext context)
    {
	super.begin(camera, context);
	context.setDepthTest(GL20.GL_LEQUAL);
	context.setCullFace(GL20.GL_BACK);

    }

    @Override
    public void render(final Renderable renderable)
    {
	super.render(renderable);
    }

    @Override
    public void render(Renderable renderable, Attributes combinedAttributes)
    {
	if (!combinedAttributes.has(BlendingAttribute.Type))
	    context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	bindMaterial(combinedAttributes);
	super.render(renderable, combinedAttributes);
    }

    /**
     * This is where I can set specific properties for each object, so could improve
     * the functionality of the Shadow System from here.  
     * 
     * TODO Working on improving the capabilities for the shader from here.
     * @param attributes
     */
    protected void bindMaterial(final Attributes attributes)
    {
	for (final Attribute attr : attributes)
	{
	    final long t = attr.type;
	    if (BlendingAttribute.is(t))
	    {
		//  Not sure why first two lines are not passing then info to the shaders.
		context.setBlending(true, ((BlendingAttribute) attr).sourceFunction, ((BlendingAttribute) attr).destFunction);
		set(u_opacity, ((BlendingAttribute) attr).opacity);
		// Presently this gets the opacity value to the shader properly.
		this.program.setUniformf("u_opacity", ((BlendingAttribute) attr).opacity);
	    }
	}
    }

    @Override
    public void init()
    {
	final ShaderProgram program = this.program;
	this.program = null;
	init(program, renderable);
	renderable = null;
    }

    @Override
    public int compareTo(final Shader other)
    {
	return 0;
    }

    @Override
    public boolean canRender(final Renderable instance)
    {
	return true;
    }

}
