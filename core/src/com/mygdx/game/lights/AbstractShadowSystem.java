package com.mygdx.game.lights;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.utils.Array;

public abstract class AbstractShadowSystem
{
    
    public Array<AbstractShadowLight> lights = new Array<AbstractShadowLight>();
    public Array<RenderableProvider> objects = new Array<RenderableProvider>();
    public Array<ParticleSystem> particleSystems = new Array<ParticleSystem>();
    
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
	light.setSystem(this);
	lights.add(light);
    }

    /**
     * Adds and object to be rendered by the lighting system.
     * 
     * @param obj
     */
    public void addRenderObject(RenderableProvider obj)
    {
	objects.add(obj);
    }

    /**
     * Remove and object to be rendered by the lighting system.
     * 
     * @param obj
     */
    public void removeRenderObject(RenderableProvider obj)
    {
	objects.removeValue(obj, false);
    }
    
    /**
     * Adds and object to be rendered by the lighting system.
     * 
     * @param obj
     */
    public void addParticleSystem(ParticleSystem obj)
    {
	particleSystems.add(obj);
    }

    /**
     * Remove and object to be rendered by the lighting system.
     * 
     * @param obj
     */
    public void removeParticleSystem(ParticleSystem obj)
    {
	particleSystems.removeValue(obj, false);
    }

    public abstract void render(Camera cam, float delta);
    
    /**
     * Renders all the objects being managed by the Shadow Light System.
     * @param batch
     */
    public abstract void render(ModelBatch batch);
    
    /**
     * Clean up all the memory being used by the light system
     */
    public abstract void dispose();
    

}
