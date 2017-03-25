package com.mygdx.game.bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;

public class PongBulletObjects implements Disposable
{
    public static final PongBulletObjects instance = new PongBulletObjects();
    // Needed for StaticBodies, don't care if info keeps getting written over.
    // By being final and static makes for faster code so don't keep wasting time
    // making one and then throwing it away.
    private static final Vector3 temp = new Vector3();
    private static final Vector3 localInertia = new Vector3(1, 1, 1);
    
    private btCollisionShape groundShape;
    public Model groundModel;
    public ModelInstance groundInstance;
    
    protected  PongBulletObjects()
    {
    	super();
    }
    
    public void init()
    {
    	final ModelBuilder builder = new ModelBuilder();
    	float width, height, radius;
    	
    	width = 20;
    	builder.begin();
    	MeshPartBuilder mpb = builder.part("parts",GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorPacked, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
    	mpb.setColor(1f, 1f, 1f, 1f);
    	BoxShapeBuilder.build(mpb, 0, 0, 0, 2 * width, 1, 2 * width);
    	groundModel = builder.end();
    	groundShape = new btBoxShape(new Vector3(width, 1/2f, width));
    	groundInstance = new ModelInstance(groundModel);
    }
    
    private btRigidBody createRigidBody(Model model, btCollisionShape collisionShape, Vector3 position, boolean isStatic)
    {
    	if (isStatic)
    		return createStaticRigidBody(model, collisionShape, position);
    	
    	final ModelInstance instance = new ModelInstance(model);
    	final btMotionState motionState = new MyMotionState(instance);
    	motionState.setWorldTransform(instance.transform.trn(position).rotate(Vector3.Z, MathUtils.random(360)));
    	final btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(1, motionState, collisionShape, localInertia);
    	final btRigidBody body = new btRigidBody(bodyInfo);
    	//body.userData = new UserData(instance, motionState, bodyInfo, body);
    	BulletWorld.world.addRigidBody(body);
    	return body;
    }
    
    private btRigidBody createStaticRigidBody(Model model, btCollisionShape collisionShape, Vector3 position)
    {
    	final ModelInstance instance = new ModelInstance(model);
    	instance.transform.trn(position);
    	final btRigidBodyConstructionInfo bodyInfo = new btRigidBodyConstructionInfo(0, null, collisionShape, Vector3.Zero);
    	final btRigidBody body = new btRigidBody(bodyInfo);
    	body.translate(instance.transform.getTranslation(temp));
    	//body.userData = new UserData(instance, null, bodyInfo, body);
    	BulletWorld.world.addRigidBody(body);
    	return body;
    }
    
    public btRigidBody create_ground()
    {
    	return createRigidBody(groundModel, groundShape, Vector3.Zero, true);
    }

    @Override
    public void dispose()
    {
	groundModel.dispose();
	groundShape.dispose();
	
    }

}
