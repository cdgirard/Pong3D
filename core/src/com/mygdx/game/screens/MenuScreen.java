package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Pong3D;
import com.mygdx.game.PongGlobals;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.AudioManager;
import com.mygdx.game.util.GamePreferences;
import com.mygdx.game.util.HighScoreEntry;

public class MenuScreen extends AbstractGameScreen
{
    private static final String TAG = MenuScreen.class.getName();

    private Stage stage;
    // private Skin skinGame;
    private Skin skinLibgdx;

    // Menu
    private Image imgBackground;
    private Image imgTitle;
    private Button btnMenuPlay;
    private Button btnMenuOptions;
    private TextButton btnExit;

    // Options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private CheckBox chkShowFpsCounter;

    // debug
    private boolean debugEnabled = false;

    public MenuScreen()
    {
	GamePreferences prefs = GamePreferences.instance;
	prefs.load();
    }

    /**
     * Put all the UI componenets together.
     */
    private void rebuildStage()
    {
	skinLibgdx = Assets.instance.skinLibgdx;
	
	PongGlobals.sortHighScoreList();

	Table layerBackground = buildBackgroundLayer();
	Table layerObjects = buildTitleLayer();
	Table layerControls = buildControlsLayer();
	Table highScores = buildHighScoreLayer();
	Table layerOptionsWindow = buildOptionsWindowLayer();

	stage.clear();
	Stack stack = new Stack();
	stage.addActor(stack);
	stack.setSize(Assets.VIEWPORT_GUI_WIDTH, Assets.VIEWPORT_GUI_HEIGHT);
	stack.add(layerBackground);
	stack.add(layerObjects);
	stack.add(highScores);
	stack.add(layerControls);
	stack.add(buildExitLayer());
	stage.addActor(layerOptionsWindow);
    }

    /**
     * Build the background for the MenuScrean.
     * @return
     */
    private Table buildBackgroundLayer()
    {
	Table layer = new Table();
	imgBackground = new Image(Assets.assetManager.get(Assets.BACKGROUND_IMG, Texture.class));
	layer.add(imgBackground);
	return layer;
    }

    /**
     * Build and place the title to be displayed.
     * @return
     */
    private Table buildTitleLayer()
    {
	Table layer = new Table();
	imgTitle = new Image(Assets.assetManager.get(Assets.TITLE, Texture.class));
	layer.addActor(imgTitle);
	imgTitle.setPosition(375, 600);
	return layer;
    }
    
    /**
     * Build and place the exit button so we can quit out in full screen mode.
     * @return
     */
    private Table buildExitLayer()
    {
	Table layer = new Table();
	btnExit = new TextButton("Exit", skinLibgdx);
	layer.addActor(btnExit);
	btnExit.addListener(new ChangeListener()
	{
	    @Override
	    public void changed(ChangeEvent event, Actor actor)
	    {
		Gdx.app.exit();
	    }
	});
	btnExit.setPosition(Assets.VIEWPORT_GUI_WIDTH - btnExit.getWidth() - 5, Assets.VIEWPORT_GUI_HEIGHT - btnExit.getHeight() - 5);
	return layer;
    }

    private Table buildControlsLayer()
    {
	Table layer = new Table();
	layer.right().bottom();
	Image playBtnUp = new Image(Assets.assetManager.get(Assets.PLAY_BTN_UP_IMG, Texture.class));
	Image playBtnDwn = new Image(Assets.assetManager.get(Assets.PLAY_BTN_DWN_IMG, Texture.class));
	btnMenuPlay = new Button(playBtnUp.getDrawable(), playBtnDwn.getDrawable());
	layer.add(btnMenuPlay);
	btnMenuPlay.addListener(new ChangeListener()
	{
	    @Override
	    public void changed(ChangeEvent event, Actor actor)
	    {
		onPlayClicked();
	    }
	});
	Image optionBtnUp = new Image(Assets.assetManager.get(Assets.OPTION_BTN_UP_IMG, Texture.class));
	Image optionBtnDwn = new Image(Assets.assetManager.get(Assets.OPTION_BTN_DWN_IMG, Texture.class));
	btnMenuOptions = new Button(optionBtnUp.getDrawable(), optionBtnDwn.getDrawable());
	layer.add(btnMenuOptions);
	btnMenuOptions.addListener(new ChangeListener()
	{
	    @Override
	    public void changed(ChangeEvent event, Actor actor)
	    {
		onOptionsClicked();
	    }
	});
	if (debugEnabled)
	    layer.debug();
	return layer;
    }

    /**
     * Need to build a highscore list.
     * 
     * @return
     */
    private Table buildHighScoreLayer()
    {
	Table tbl = new Table();
	tbl.add(new Label("HIGH SCORE LIST", skinLibgdx));
	tbl.row();
	tbl.pad(10, 10, 0, 10);
	int counter = 1;
	for (HighScoreEntry e : PongGlobals.highScores)
	{
	    Label playerLbl = new Label(""+counter+". "+e.getPlayer(), skinLibgdx);
	    Label scoreLbl = new Label(" "+e.getScore(), skinLibgdx);
	    tbl.add(playerLbl);
	    tbl.add(scoreLbl);
	    tbl.row();
	    counter++;
	}
	return tbl;
    }

    private Table buildOptionsWindowLayer()
    {
	winOptions = new Window("Options", skinLibgdx);
	winOptions.add(buildOptWinAudioSettings()).row();
	winOptions.add(buildOptWinDebug()).row();
	winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);
	// Making the whole window transparent.
	winOptions.setColor(1, 1, 1, 0.8f);
	winOptions.setVisible(false);
	if (debugEnabled)
	    winOptions.debug();
	winOptions.pack();
	// Not doing anything
	winOptions.setPosition(Assets.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
	winOptions.setMovable(false);
	return winOptions;
    }

    private Table buildOptWinAudioSettings()
    {
	Table tbl = new Table();
	tbl.pad(10, 10, 0, 10);
	tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
	tbl.row();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	chkSound = new CheckBox("", skinLibgdx);
	tbl.add(chkSound);
	tbl.add(new Label("Sound", skinLibgdx));
	sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
	tbl.add(sldSound);
	tbl.row();
	chkMusic = new CheckBox("", skinLibgdx);
	tbl.add(chkMusic);
	tbl.add(new Label("Music", skinLibgdx));
	sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
	tbl.add(sldMusic);
	tbl.row();
	return tbl;
    }

    private Table buildOptWinDebug()
    {
	Table tbl = new Table();
	tbl.pad(10, 10, 0, 10);
	tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
	tbl.row();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	chkShowFpsCounter = new CheckBox("", skinLibgdx);
	tbl.add(new Label("Show FPS Counter", skinLibgdx));
	tbl.add(chkShowFpsCounter);
	tbl.row();
	return tbl;
    }

    private Table buildOptWinButtons()
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
		onSaveClicked();
	    }
	});
	btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
	tbl.add(btnWinOptCancel);
	btnWinOptCancel.addListener(new ChangeListener()
	{
	    @Override
	    public void changed(ChangeEvent event, Actor actor)
	    {
		onCancelClicked();
	    }
	});
	return tbl;
    }

    private void loadSettings()
    {
	GamePreferences prefs = GamePreferences.instance;
	prefs.load();
	chkSound.setChecked(prefs.sound);
	sldSound.setValue(prefs.volSound);
	chkMusic.setChecked(prefs.music);
	sldMusic.setValue(prefs.volMusic);
	chkShowFpsCounter.setChecked(prefs.showFpsCounter);
    }

    private void saveSettings()
    {
	GamePreferences prefs = GamePreferences.instance;
	prefs.sound = chkSound.isChecked();
	prefs.volSound = sldSound.getValue();
	prefs.music = chkMusic.isChecked();
	prefs.volMusic = sldMusic.getValue();
	prefs.showFpsCounter = chkShowFpsCounter.isChecked();
	prefs.save();
    }

    private void onCancelClicked()
    {
	btnMenuPlay.setVisible(true);
	btnMenuOptions.setVisible(true);
	winOptions.setVisible(false);
	AudioManager.instance.onSettingsUpdated(); // Not sure why we need it
						   // here....
    }

    private void onPlayClicked()
    {
	PongGlobals.startGame();
	Pong3D.instance.setScreen(new GameScreen());
    }

    /**
     * How to update the UI when the option button is pressed.
     */
    private void onOptionsClicked()
    {
	loadSettings();
	btnMenuPlay.setVisible(false);
	btnMenuOptions.setVisible(false);
	winOptions.setVisible(true);
    }

    /**
     * What to do when you click save at the Options window.
     */
    private void onSaveClicked()
    {
	saveSettings();
	onCancelClicked();
	AudioManager.instance.onSettingsUpdated();
    }

    /**
     * Draws the information to the screen.
     */
    @Override
    public void render(float deltaTime)
    {
	Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	stage.act(deltaTime);
	stage.draw();
	// stage.setDebugAll(true);

    }

    @Override
    public void show()
    {
	stage = new Stage(new StretchViewport(Assets.VIEWPORT_GUI_WIDTH, Assets.VIEWPORT_GUI_HEIGHT));
	Gdx.input.setInputProcessor(stage);
	rebuildStage();
    }

    @Override
    public void resize(int width, int height)
    {
	stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause()
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void hide()
    {
	stage.dispose();
	// skinGame.dispose();
    }

}
