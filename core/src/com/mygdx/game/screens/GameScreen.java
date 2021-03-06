package com.mygdx.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Pong3D;
import com.mygdx.game.PongController;
import com.mygdx.game.PongGlobals;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.AudioManager;
import com.mygdx.game.bullet.BulletWorld;
import com.mygdx.game.lights.AbstractShadowSystem;
import com.mygdx.game.lights.BasicLightSystem;
import com.mygdx.game.lights.MovingPointShadowLight;
import com.mygdx.game.lights.MyDirectionalShadowLight;
import com.mygdx.game.lights.PointShadowLight;
import com.mygdx.game.lights.ShadowSystem;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.PongObjects;
import com.mygdx.game.util.GamePreferences;
import com.mygdx.game.util.HighScoreEntry;
import com.mygdx.game.util.HighScoreListFileManager;

/**
 * TODO: Shadows do not show through opaque objects.  Basically the render code is smart enough to grab the object behind the
 *       opaque object so we can "see it".  However, it is not smart enough to know there should be a shadow on it.  Would require
 *       two render calls to get it to work right I think.
 *       
 * Is the screen displayed when playing the game.
 * 
 * @author cdgira
 *
 */
public class GameScreen extends AbstractGameScreen
{
    private static final String TAG = "GameScreen";

    PongController controller;

    // For Shadow Environment
    public AbstractShadowSystem shadowSystem;
    PerspectiveCamera cam;
    private MyDirectionalShadowLight dShadow;
    private PointShadowLight pShadow;

    // For Bullet
    DebugDrawer debugDrawer;
    private static final boolean BULLET_DEBUG = false;

    // For UI
    private OrthographicCamera cameraUI;
    private SpriteBatch batch;
    private Skin skinLibgdx;
    private Stage stage;
    Label scoreLbl;
    boolean highScoreWindowActive = false;
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextField tfPlayer;
    
    public GameScreen()
    {
	create();
    }

    /**
     * May be able to move this code into show(), but not sure yet.
     */
    public void create()
    {
	PongGlobals.startLevel();
	
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
	shadowSystem.addParticleSystem(Assets.instance.particleSystem);

	BulletWorld.instance.init(this);
	if (BULLET_DEBUG)
	{
	    debugDrawer = new DebugDrawer();
	    BulletWorld.world.setDebugDrawer(debugDrawer);
	    debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
	}

	PongObjects.instance.init(cam);
	for (GameObject obj : PongObjects.instance.objects)
	{
	    shadowSystem.addRenderObject(obj.instance);
	}
	
	Vector3 groundPos = PongObjects.instance.ground.body.getCenterOfMassPosition();

	Vector3 lightPos = new Vector3(groundPos.x, 30f, groundPos.z);
	Vector3 lightDir = new Vector3(0,-1,0.1f);
	dShadow = new MyDirectionalShadowLight(lightPos, lightDir, 0.3f);
	pShadow = new PointShadowLight(lightPos, 0.4f);
	//pShadow.updateTarget(groundPos);
	//shadowSystem.addLight(pShadow);
	 shadowSystem.addLight(pShadow); //new PointShadowLight(new Vector3(50f, 30.0f, 50f),0.3f));
	// shadowSystem.addLight(pShadow);
	shadowSystem.addLight(new MovingPointShadowLight(new Vector3(50f, 30.0f, 50f), 0.1f));

	controller = new PongController(this);

	// TODO: Might want in a different method
	stage = new Stage(new StretchViewport(Assets.VIEWPORT_GUI_WIDTH, Assets.VIEWPORT_GUI_HEIGHT));
	rebuildStage();
	highScoreWindowActive = false;
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

	BulletWorld.instance.update(delta);
	
	renderGui(delta);

	
    }

    private void renderGui(float deltaTime)
    {
	batch.setProjectionMatrix(cameraUI.combined);
	batch.begin();
	renderGuiExtraLive(batch);
	if (GamePreferences.instance.showFpsCounter)
	    renderGuiFpsCounter(batch);    
	batch.end();
	
	// Placing inside of batch start end caused problems with the batch rendering.
	stage.act(deltaTime);
	stage.draw();
	
	if (PongGlobals.numScoreBlocks == 0)
	{
	    PongGlobals.changeLevel();
	    dispose();
	    Pong3D.instance.setScreen(new GameScreen());
	}
	else if ((!highScoreWindowActive) && (PongGlobals.lives == 0))
	{
	    if (PongGlobals.highScores.size == PongGlobals.MAX_SCORES)
	    {
		if (PongGlobals.score > PongGlobals.highScores.get(PongGlobals.MAX_SCORES - 1).score)
		{
		    winOptions.setVisible(true);
		    Gdx.input.setInputProcessor(stage);
		}
		else
		{
		    dispose();
		    AudioManager.instance.stopMusic();
		    Pong3D.instance.setScreen(new MenuScreen());    
		}
	    }
	    else
	    {
		winOptions.setVisible(true);
		Gdx.input.setInputProcessor(stage);
	    }
	}
    }

    /**
     * Displays a counter that shows how many frames are occurring per minute.
     * @param batch
     */
    private void renderGuiFpsCounter(SpriteBatch batch)
    {
	float x = cameraUI.viewportWidth - 75;
	float y = cameraUI.viewportHeight - 55;
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

    private void renderGuiExtraLive(SpriteBatch batch)
    {
	float x = cameraUI.viewportWidth - 25 - PongGlobals.LIVES_START * 50;
	float y = 15;
	for (int i = 0; i < PongGlobals.LIVES_START; i++)
	{
	  //  Assets.instance.defaultNormal.draw(batch, "" + PongGlobals.score, x + 75, y + 37);
	    if (PongGlobals.lives <= i)
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
	PongObjects.instance.update(delta);
	Vector3 groundPos = PongObjects.instance.ground.body.getCenterOfMassPosition();
	cam.position.set(groundPos.x - 15, groundPos.y + 15, groundPos.z + 15);
	cam.lookAt(groundPos);
	cam.update();
	
	// Have the Shadow Light track the Platform from above.
	pShadow.updatePosition(new Vector3(groundPos.x,pShadow.camera.position.y,groundPos.z));
	
	scoreLbl.setText("Score: " +PongGlobals.score);
    }

    private void rebuildStage()
    {
	skinLibgdx = Assets.instance.skinLibgdx;

	Table layerOptionsWindow = buildHighScoreWindowLayer();

	stage.clear();
	Stack stack = new Stack();
	stage.addActor(stack);
	stack.setSize(Assets.VIEWPORT_GUI_WIDTH, Assets.VIEWPORT_GUI_HEIGHT);
	stack.add(buildScoreTable());
	stage.addActor(layerOptionsWindow);
    }
    
    private Table buildScoreTable()
    {
	Table tbl = new Table();
	scoreLbl = new Label("Score: "+0, skinLibgdx, "default-font", Color.ORANGE);
	tbl.addActor(scoreLbl);
	scoreLbl.setPosition(15, Assets.VIEWPORT_GUI_HEIGHT - 25);
	return tbl;
    }

    /**
     * Creates the pop-up end of game window to enter the name of the player
     * that got a new highscore.
     */
    private Table buildHighScoreWindowLayer()
    {
	winOptions = new Window("Options", skinLibgdx);
	winOptions.add(buildEnterNameTbl()).row();
	winOptions.add(buildWinButtons()).pad(10, 0, 10, 0);
	// Making the whole window transparent.
	winOptions.setColor(1, 1, 1, 0.8f);
	winOptions.setVisible(false);
	// if (debugEnabled)
	// winOptions.debug();
	winOptions.pack();
	// Not doing anything
	winOptions.setPosition(Assets.VIEWPORT_GUI_WIDTH / 2 - winOptions.getWidth() / 2, Assets.VIEWPORT_GUI_HEIGHT / 2 - winOptions.getHeight() / 2);
	winOptions.setMovable(false);
	return winOptions;
    }

    private Table buildEnterNameTbl()
    {
	Table tbl = new Table();
	tbl.pad(10, 10, 0, 10);
	tbl.add(new Label("Name:", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
	tbl.row();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	tfPlayer = new TextField("", skinLibgdx);
	tbl.add(tfPlayer);
	return tbl;
    }

    private Table buildWinButtons()
    {
	Table tbl = new Table();
	Label lbl = null;
	lbl = new Label("", skinLibgdx);
	lbl.setColor(0.75f, 0.75f, 0.75f, 1);
	lbl.setStyle(new LabelStyle(lbl.getStyle()));
	lbl.getStyle().background = skinLibgdx.newDrawable("white");
	tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
	tbl.row();
	lbl = new Label("", skinLibgdx);
	lbl.setColor(0.5f, 0.5f, 0.5f, 1);
	lbl.setStyle(new LabelStyle(lbl.getStyle()));
	lbl.getStyle().background = skinLibgdx.newDrawable("white");
	tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
	tbl.row();
	btnWinOptSave = new TextButton("Save", skinLibgdx);
	tbl.add(btnWinOptSave).padRight(30);
	btnWinOptSave.addListener(new ChangeListener()
	{
	    @Override
	    public void changed(ChangeEvent event, Actor actor)
	    {
		HighScoreEntry entry = new HighScoreEntry(tfPlayer.getText(), PongGlobals.score);
		PongGlobals.highScores.add(entry);
		PongGlobals.sortHighScoreList();
		HighScoreListFileManager.saveHighScores(PongGlobals.highScores);
		dispose();
		Pong3D.instance.setScreen(new MenuScreen());
		
	    }
	});
	return tbl;
    }

    /**
     * Cleans up all the memory being used by the GameScreen before switching it out 
     * with another screen.
     */
    @Override
    public void dispose()
    {
	BulletWorld.instance.dispose();
	batch.dispose();
	shadowSystem.dispose();
	PongObjects.instance.dispose();
	stage.dispose();
    }
}
