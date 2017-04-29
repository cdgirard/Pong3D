package com.mygdx.game.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.mygdx.game.objects.PongObjects;

public class MyContactListener extends ContactListener
{
        @Override
	public boolean onContactAdded (btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
		btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) 
        {
            //Gdx.app.error("TAG","Callback"+colObj0Wrap+" : "+colObj1Wrap);
            
              //  PongObjects.instance.ground.body.setLinearVelocity(new Vector3(0,0,0));
		//instances.get(colObj1Wrap.getCollisionObject().getUserValue()).moving = false;
		return true;
	}
        
        @Override
        public void onContactProcessed(btManifoldPoint cp, btCollisionObject colObj0, btCollisionObject colObj1)
        {
           //PongObjects.instance.ground.body.clearForces();
        }
        
        @Override
        public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1)
        {
           // PongObjects.instance.ground.body.clearForces();
        }
        
        @Override
        public void onContactEnded(btCollisionObject colObj0, btCollisionObject colObj1)
        {
            //PongObjects.instance.ground.body.clearForces();
        }

}
