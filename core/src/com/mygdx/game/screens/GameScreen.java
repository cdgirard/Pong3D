package com.mygdx.game.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.mygdx.game.PongController;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.util.DirectionalShadowSystemLight;
import com.mygdx.game.util.GamePreferences;
import com.mygdx.game.util.MovingPointShadowLight;
import com.mygdx.game.util.PointShadowLight;
import com.mygdx.game.util.ShadowSystem;

/**
 * Is the screen displayed when playing the game.
 * 
 * TODO: Is in need of some refactoring, probably to pull apart the UI from the
 * Game graphics and maybe some other stuff.
 * 
 * @author cdgira
 *
 */
public class GameScreen extends AbstractGameScreen
{
    private static final String TAG = "GameScreen";

    // TODO: Needs a better home.
    public static final int LIVES_START = 3;

    PongController controller;
    PerspectiveCamera cam;

    // For Shadow Environment
    ShadowSystem shadowSystem;

    public static final int DEPTH_MAP_SIZE = 1024;

    // For Bullet
    DebugDrawer debugDrawer;
    private static final boolean BULLET_DEBUG = false;

    // For UI
    private OrthographicCamera cameraUI;
    private SpriteBatch batch;
    int score = 0;
    int lives = 3;

    public GameScreen(Game g)
    {
	super(g);
	create();
    }

    /**
     * May be able to move this code into show(), but not sure yet.
     */
    public void create()
    {

	// UI
	cameraUI = new OrthographicCamera(Assets.VIEWPORT_GUI_WIDTH, Assets.VIEWPORT_GUI_HEIGHT);
	cameraUI.setToOrtho(true);
	cameraUI.update();
	batch = new SpriteBatch();

	// Game
	cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	cam.near = 1f;
	cam.far = 200;
	cam.position.set(-15, 15, 15);
	cam.lookAt(0, 0, 0);
	cam.update();

	Assets.instance.loadParticleEffects(cam);

	shadowSystem = new ShadowSystem();

	BulletWorld.instance.init();
	if (BULLET_DEBUG)
	{
	    debugDrawer = new DebugDrawer();
	    BulletWorld.world.setDebugDrawer(debugDrawer);
	    debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
	}
	PongObjects.instance.init(cam);

	shadowSystem.addLight(new PointShadowLight(new Vector3(0f, 13.8f, 32f), 0.3f));
	// shadowSystem.addLight(new PointShadowLight(new Vector3(45f, 0.0f,
	// 0f),0.3f));
	// shadowSystem.addLight(new DirectionalShadowSystemLight(new
	// Vector3(33, 0, 0), new Vector3(-1, 0, 0), 0.3f));
	shadowSystem.addLight(new MovingPointShadowLight(new Vector3(0f, 30.0f, 0f), 0.1f));

	controller = new PongController();
    }

    /**
     * Two shaders, one creates the depth map, the other uses the depth map to
     * create the shadows. Both need to be setup correctly (see Git Hub example
     * to fill in the missing pieces.
     */
    @Override
    public void render(float delta)
    {
	update(delta);
	shadowSystem.render(cam, delta);

	if (BULLET_DEBUG)
	{
	    debugDrawer.begin(cam);
	    BulletWorld.world.debugDrawWorld();
	    debugDrawer.end();
	}

	renderGui();

	BulletWorld.instance.update(delta);
    }

    private void renderGui()
    {
	batch.setProjectionMatrix(cameraUI.combined);
	batch.begin();
	renderGuiScore(batch);
	renderGuiExtraLive(batch);
	if (GamePreferences.instance.showFpsCounter)
	    renderGuiFpsCounter(batch);
	batch.end();
    }

    private void renderGuiFpsCounter(SpriteBatch batch)
    {
	float x = cameraUI.viewportWidth - 55;
	float y = cameraUI.viewportHeight - 15;
	int fps = Gdx.graphics.getFramesPerSecond();
	BitmapFont fpsFont = Assets.instance.defaultNormal;
	if (fps >= 45)
	    fpsFont.setColor(0, 1, 0, 1);
	else if (fps >= 30)
	    fpsFont.setColor(1, 1, 0, 1);
	else
	    fpsFont.setColor(1, 0, 0, 1);
	fpsFont.draw(batch, "FPS: " + fps, x, y);
	fpsFont.setColor(1, 1, 1, 1);
    }

    private void renderGuiScore(SpriteBatch batch)
    {
	float x = -15;
	float y = -15;
	Assets.instance.defaultNormal.draw(batch, "" + score, x + 75, y + 37);
    }

    private void renderGuiExtraLive(SpriteBatch batch)
    {
	float x = cameraUI.viewportWidth - 50 - LIVES_START * 50;
	float y = 15;
	for (int i = 0; i < LIVES_START; i++)
	{
	    if (lives <= i)
		batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
	    batch.draw(Assets.assetManager.get(Assets.BALL, Texture.class), x + i * 50, y, 0, 0, 101, 73, 0.35f, 0.35f, 0, 0, 0, 101, 73, false, false);
	    batch.setColor(1, 1, 1, 1);
	}
    }

    @Override
    public void resize(int width, int height)
    {
	cameraUI.viewportHeight = Assets.VIEWPORT_GUI_HEIGHT;
	cameraUI.viewportWidth = (Assets.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
	cameraUI.position.set(cameraUI.viewportWidth / 2, cameraUI.viewportHeight / 2, 0);
	cameraUI.update();
    }

    @Override
    public void show()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void pause()
    {

    }

    public void update(float delta)
    {
	// PongObjects.instance.ground.body.applyCentralImpulse(new
	// Vector3(5,0,0));
	PongObjects.instance.update(delta);
	Vector3 groundPos = PongObjects.instance.ground.body.getCenterOfMassPosition();
	// Vector3 groundPos =
	// PongObjects.instance.sphere.body.getCenterOfMassPosition();
	cam.position.set(groundPos.x - 15, groundPos.y + 15, groundPos.z + 15);
	cam.lookAt(groundPos);
	cam.update();
    }

    @Override
    public void dispose()
    {
	BulletWorld.instance.dispose();
	batch.dispose();

    }
}
