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
//	if (!renderable.material.has(BlendingAttribute.Type))
//	{
//	    context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//	}
//	else
//	{
//	    context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//	}
	super.render(renderable);
    }

    @Override
    public void render(Renderable renderable, Attributes combinedAttributes)
    {
	if (!combinedAttributes.has(BlendingAttribute.Type))
	    context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	bindMaterial(combinedAttributes);
	//if (lighting)
	//    bindLights(renderable, combinedAttributes);
	super.render(renderable, combinedAttributes);
    }

    protected void bindMaterial(final Attributes attributes)
    {
	//		int cullFace = config.defaultCullFace == -1 ? defaultCullFace : config.defaultCullFace;
	//		int depthFunc = config.defaultDepthFunc == -1 ? defaultDepthFunc : config.defaultDepthFunc;
	//		float depthRangeNear = 0f;
	//		float depthRangeFar = 1f;
	//		boolean depthMask = true;

	for (final Attribute attr : attributes)
	{
	    final long t = attr.type;
	    if (BlendingAttribute.is(t))
	    {
		context.setBlending(true, ((BlendingAttribute) attr).sourceFunction, ((BlendingAttribute) attr).destFunction);
		set(u_opacity, ((BlendingAttribute) attr).opacity);
		this.program.setUniformf("u_opacity", ((BlendingAttribute) attr).opacity);
	    }
	    //			else if ((t & IntAttribute.CullFace) == IntAttribute.CullFace)
	    //				cullFace = ((IntAttribute)attr).value;
	    //			else if ((t & FloatAttribute.AlphaTest) == FloatAttribute.AlphaTest)
	    //				set(u_alphaTest, ((FloatAttribute)attr).value);
	    //			else if ((t & DepthTestAttribute.Type) == DepthTestAttribute.Type) {
	    //				DepthTestAttribute dta = (DepthTestAttribute)attr;
	    //				depthFunc = dta.depthFunc;
	    //				depthRangeNear = dta.depthRangeNear;
	    //				depthRangeFar = dta.depthRangeFar;
	    //				depthMask = dta.depthMask;
	    //			} else if (!config.ignoreUnimplemented) throw new GdxRuntimeException("Unknown material attribute: " + attr.toString());
	}

	//		context.setCullFace(cullFace);
	//		context.setDepthTest(depthFunc, depthRangeNear, depthRangeFar);
	//		context.setDepthMask(depthMask);
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
