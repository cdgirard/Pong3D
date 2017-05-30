package com.mygdx.game.util;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.FlushablePool;

public class PongParticlePool extends FlushablePool<Renderable>
{
    @Override
    protected Renderable newObject()
    {
	return new Renderable();
    }

    @Override
    public Renderable obtain()
    {
	Renderable renderable = super.obtain();
	renderable.environment = null;
	renderable.material = null;
	renderable.meshPart.set("", null, 0, 0, 0);
	renderable.shader = null;
	renderable.userData = null;
	return renderable;
    }
}
