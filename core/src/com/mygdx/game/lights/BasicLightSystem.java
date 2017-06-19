package com.mygdx.game.lights;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
/**
 * Created for testing rendering objects without all the Shadow Shaders that might
 * break some of the rendering capabilities in libGDX.
 * @author cdgira
 *
 */
public class BasicLightSystem extends AbstractShadowSystem
{
  //  Environment environment;
    ModelBatch modelBatch;
    
    public BasicLightSystem()
    {
	//environment = new Environment();
	//environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
	//environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	modelBatch = new ModelBatch();
    }

    @Override
    public void render(Camera cam, float delta)
    {
	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	
	modelBatch.begin(cam);
	modelBatch.render(objects);
	modelBatch.end();
	
    }

    @Override
    public void render(ModelBatch batch)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub
	
    }

}
