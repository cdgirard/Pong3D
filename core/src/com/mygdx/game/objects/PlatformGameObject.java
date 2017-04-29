package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class PlatformGameObject extends GameObject
{
    public float bounceTimer = 0;
    public Vector3 impulseForce;

    public void update(float delta)
    {
	if (bounceTimer > 0)
	{
	    bounceTimer = bounceTimer - delta;
	    if (bounceTimer < 0)
		bounceTimer = 0;
	}

	body.applyCentralImpulse(impulseForce);

	Vector3 tmp = body.getCenterOfMassPosition();
	if (impulseForce.y > 10)
	    Gdx.app.error("Tag", "" + tmp.y + " : " + impulseForce.y + " : " + bounceTimer);
	// body.clearForces();
	if ((tmp.y > 1) || (tmp.y < -0.5))
	{
	    float amt = -(tmp.y * 2);
	    body.activate();
	    Vector3 tmp2 = body.getLinearVelocity();
	    body.setLinearVelocity(new Vector3(tmp2.x, amt, tmp2.z));
	    impulseForce.y = 0;
	}
	else if (bounceTimer == 0)
	{
	    Vector3 tmp2 = body.getLinearVelocity();
	    body.setLinearVelocity(new Vector3(tmp2.x, 0, tmp2.z));
	}

    }

    public void bounce()
    {
	if (bounceTimer == 0)
	{
	    impulseForce.y = 15;
	    bounceTimer = 2;
	    float groundY = body.getCenterOfMassPosition().y;
	    float sphereY = PongObjects.instance.sphere.body.getCenterOfMassPosition().y;
	    if (sphereY < groundY + 1.5f)
		PongObjects.instance.sphere.body.applyCentralImpulse(new Vector3(0, 5, 0));
	}

    }

}
