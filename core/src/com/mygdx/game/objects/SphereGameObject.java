package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier.PolarAcceleration;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.bullet.MyMotionState;

/**
 * TODO Remember to clamp the force applied on the sphere so it doesn't shoot off into space.
 * @author cdgira
 *
 */
public class SphereGameObject extends GameObject
{
    ParticleEffect flameEffect;
    boolean alive = true;

    /**
     * Creates the sphere with the fire trail particle affect attached.
     * 
     * @param cam
     */
    public SphereGameObject(Vector3 position, Camera cam)
    {
	
	Model model = Assets.assetManager.get(Assets.sphere, Model.class);
	float radius = 2.0f;
	
	objId = GameObject.SPHERE;

	instance = new ModelInstance(model);
	motionState = new MyMotionState(instance);
	motionState.setWorldTransform(instance.transform.trn(position));
	shape = new btSphereShape(radius / 2f);
	btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0.1f, motionState, shape, PongObjects.localInertia);
	bodyInfo.setRestitution(1.01f);
	bodyInfo.setFriction(1.0f);
	body = new btRigidBody(bodyInfo);
	body.userData = this;
	BulletWorld.world.addRigidBody(body);
	
	// Is a particle effect to make a flame trail from the ball.
	// Particle effects do their own special shading that is separate from
	// any other setup.  This is bad if you want the effects to cast shadows
	// and their system isn't setup to support it.
	ParticleEffect originalEffect = Assets.assetManager.get(Assets.fire);
	// we cannot use the originalEffect, we must make a copy each time we
	// create new particle effect
	flameEffect = originalEffect.copy();
	
	flameEffect.init();
	// optional: particle will begin playing immediately
	flameEffect.start();

	// If want to see the particle need to add it to a particle system.
	Assets.instance.particleSystem.add(flameEffect);
    }
    
    /**
     * Rotates the particle effect so that it emits the opposite direction that the sphere is moving.
     */
    public void rotate_particle()
    {
	for (int i = 0; i < flameEffect.getControllers().size; i++)
	{
	    Array<Influencer> data = flameEffect.getControllers().get(i).influencers;
	    
	    boolean flag = false;
	    for (int x = 0; x < data.size; x++)
	    {
		if (data.get(x) instanceof DynamicsInfluencer)
		{
		   // Gdx.app.error("INFO", "FOUND DI");
		    flag = true;
		    DynamicsInfluencer di = (DynamicsInfluencer) data.get(x);
		    DynamicsModifier dm;

		    for (int j = 0; j < di.velocities.size; j++)
		    {
			dm = (DynamicsModifier) di.velocities.get(j);

			if (dm instanceof PolarAcceleration)
			{
			    Vector3 angVel = body.getLinearVelocity();
			    float len = angVel.len();
			    if (len > 0)
			    {
				angVel.x = angVel.x / len;
			        angVel.y = angVel.y / len;
			        angVel.z = angVel.z / len;
			    } 

			    // This is the angle along the y axis to spit out particles from.
			    float angPhi = (float)(Math.acos(-angVel.y)*180/Math.PI);
			    // This is the where on the xz circle to spit out particles from
			    float  angTheta = (float)(Math.acos(-angVel.x)*180/Math.PI);
			    if (angVel.z > 0)
				angTheta = angTheta + 180;
			    
			    ((PolarAcceleration) dm).phiValue.setHigh(angPhi,angPhi-10);
			    ((PolarAcceleration) dm).thetaValue.setHigh(angTheta,angTheta-10);

			}
			else
			{
			   // Gdx.app.error("INFO", "NO polar acc for: ");
			}
		    }
		}
		if (!flag)
		    ;//Gdx.app.error("INFO", "no DI");
	    }
	}
    }
    
    /**
     * Will place the Sphere back over the center of the platform.
     */
    public void reset()
    {
	body.setLinearVelocity(new Vector3(0,0,0));
	// The Vector3 returned by the method calls is a shared memory space that will overwrite the
	// memory on each call so need to create copies. Not sure if I need to dispose of the copies as
	// well.
	Vector3 groundPos = PongObjects.instance.ground.body.getCenterOfMassPosition().cpy();
	Vector3 spherePos = body.getCenterOfMassPosition().cpy();
	Vector3 transPos = new Vector3(groundPos.x - spherePos.x, groundPos.y - spherePos.y + 12, groundPos.z - spherePos.z);
	Matrix4 mat = body.getCenterOfMassTransform();
	mat.trn(transPos);
	body.setCenterOfMassTransform(mat);
	alive = true;
    }
    
    @Override
    public void update (float delta)
    {
	// Try and cap this at around 15.
	Vector3 speed = body.getLinearVelocity();
	if (speed.y > 15)
	    body.setLinearVelocity(new Vector3(speed.x,15,speed.z));
	
	Matrix4 targetMatrix = new Matrix4();

	if (flameEffect != null)
	{
	    rotate_particle();

	    targetMatrix = instance.transform;

	    // Making sure the particle effect stays with the sphere.
	    flameEffect.setTransform(targetMatrix);
	}
	
	float sphereY = body.getCenterOfMassPosition().y;
	
	if ((alive) && (sphereY < -5.0f))  // Setup this up to use the spalsh effect instead.
	{
	        PongObjects.instance.startSplash(instance.transform);
	        alive = false;
	}
    }

}
