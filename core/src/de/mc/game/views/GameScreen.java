package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.mc.game.Constants;
import de.mc.game.McGame;
import de.mc.game.models.GameWorld;

public class GameScreen extends CustomScreenAdapter {

	static final int GAME_READY, GAME_RUNNING, GAME_PAUSED, GAME_OVER;
	static {
		GAME_READY = 0;
		GAME_RUNNING = 1;
		GAME_PAUSED = 2;
		GAME_OVER = 3;
	}

	private GameWorld gameWorld;
	private Texture playerImage;
	private Sound collisionSound;
	private Rectangle player;
	private int score;
	private int gameState;
	private float timerScore;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;

    private Label labelScore;

	private final String inputTypeAccelerometer = "ACCELEROMETER";
	private final String inputTypeTouch = "TOUCH";

	public GameScreen(final McGame g) {
		super(g);

		// load the images
		//mcGame.assetManager.load("images/gameWorld.jpg", Texture.class);
		/*
		mcGame.assetManager.load("images/player_normal.png", Texture.class);
		mcGame.assetManager.load("images/player_left.png", Texture.class);
		mcGame.assetManager.load("images/player_right.png", Texture.class);*/
		mcGame.assetManager.load("images/player_normal_b.png", Texture.class);
		mcGame.assetManager.load("images/player_left_b.png", Texture.class);
		mcGame.assetManager.load("images/player_right_b.png", Texture.class);

		// load the sound effects
		mcGame.assetManager.load("sounds/plop.ogg", Sound.class);

		player = new Rectangle();
		player.x = Constants.MAP_WIDTH/ 2 - 64 / 2;
		player.y = 20;
		player.width = 44;
		player.height = 56;


		//WIP MAP
		camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
		//camera.setToOrtho(false,w,h);
		camera.update();
		tiledMap = new TmxMapLoader().load("maps/Map-v1.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,2f/3f);

		gameState = GAME_READY;
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();


		// wait for assetManager loading all sources
		if(mcGame.assetManager.update()) {
			// we are done loading, do some action!

			checkInputs();

			updateCameraPosition();
			//TEMP
			float yVelocity = 2;
			player.y+=yVelocity;
			drawGameObjects();

			updateScore();
		}
		else {
			// display loading information
			float progress = mcGame.assetManager.getProgress();
			System.out.println(progress);

			/* draw loading spinner or something equal
			mcGame.batch.begin();
			mcGame.glyphLayout.setText(mcGame.droidSansMedium, mcGame.languageStrings.get("loading") + " " + score * 100);
			mcGame.droidSansLarge.draw(mcGame.batch, mcGame.glyphLayout, mcGame.width / 2 - mcGame.glyphLayout.width / 2, mcGame.height / 2 - mcGame.glyphLayout.height / 2);
			mcGame.batch.end();
			*/
		}
		super.render(delta);
	}
		//test
	private void checkInputs() {
		// touch or accelerator input
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			updatePlayerPosition(touchPos.x, inputTypeTouch);
		}
		else if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
			float accelX = -Gdx.input.getAccelerometerX() * 3;
			if(accelX > 1.5f || accelX < -1.5f) {
				updatePlayerPosition(accelX, inputTypeAccelerometer);
			}
			else {
				playerImage = mcGame.assetManager.get("images/player_normal_b.png", Texture.class);
				//playerImage = mcGame.assetManager.get("images/player_normal.png", Texture.class);
			}
		}
		else {
			playerImage = mcGame.assetManager.get("images/player_normal_b.png", Texture.class);
			//playerImage = mcGame.assetManager.get("images/player_normal.png", Texture.class);
		}
	}

	private void drawGameObjects() {
		mcGame.batch.begin();

		mcGame.batch.draw(playerImage, player.x, player.y);

		mcGame.batch.end();
	}

	private void updateCameraPosition() {
		float xOffset=0f;
		float yOffset=0f;

		camera.position.x = player.x + xOffset;
		camera.position.y = player.y + yOffset;
	}

	private void updatePlayerPosition(float newX, String inputType) {
		float oldX = player.x;
		float velocity = 6;
		float marginoferror = 3f;



		if(inputType.equals(inputTypeAccelerometer)) {
			newX = oldX + newX;
		}else if (inputType.equals(inputTypeTouch)){
			if(newX > oldX + marginoferror){
				newX = oldX + velocity;
			}else if(newX < oldX - marginoferror){
				newX = oldX - velocity;
			}
		}


		if (newX > oldX) {
			playerImage = mcGame.assetManager.get("images/player_right_b.png", Texture.class);
			//playerImage = mcGame.assetManager.get("images/player_right.png", Texture.class);
		}
		else if (newX < oldX){
			playerImage = mcGame.assetManager.get("images/player_left_b.png", Texture.class);
			//playerImage = mcGame.assetManager.get("images/player_left.png", Texture.class);
		}
		else {
			playerImage = mcGame.assetManager.get("images/player_normal_b.png", Texture.class);
			//playerImage = mcGame.assetManager.get("images/player_normal.png", Texture.class);
		}
		// stay player in screen
		if(newX < 0) newX = 0;
		if(newX > Constants.MAP_WIDTH - 64) newX = Constants.MAP_HEIGHT - 64;
		player.x = newX;
	}

	private void updateScore() {
		timerScore += Gdx.graphics.getDeltaTime();
		if(gameState == GAME_RUNNING && timerScore >= 0.1){
			// 1/10 second just passed
			timerScore -= 0.1; // reset our timer
			score++;
            labelScore.setText(mcGame.languageStrings.get("score") + ": " + score);
		}
	}

	@Override
	public void show() {
		super.show();

		gameState = GAME_RUNNING;

		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		Skin skin = new Skin();
		TextureAtlas buttonAtlas = new TextureAtlas("buttons/default-button.pack");
		skin.addRegions(buttonAtlas);
		textButtonStyle.up = skin.getDrawable("button");
		textButtonStyle.over = skin.getDrawable("button_pressed");
		textButtonStyle.down = skin.getDrawable("button_pressed");
		textButtonStyle.font = mcGame.droidSansMedium;
		textButtonStyle.fontColor = Color.BLUE;
		final TextButton btnMenu = new TextButton("Menü", textButtonStyle);
		btnMenu.setWidth(btnMenu.getWidth() + 30);
		btnMenu.setHeight(btnMenu.getHeight() + 20);
		btnMenu.setPosition(Constants.WIDTH - btnMenu.getWidth() - 20, Constants.HEIGHT - btnMenu.getHeight() - 20);
		btnMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				mcGame.setScreen(mcGame.mainMenuScreen);
			}
		});

        Label.LabelStyle labelStyle = new Label.LabelStyle(mcGame.droidSansMedium, Color.WHITE);
		labelScore = new Label(mcGame.languageStrings.get("score") + ": " + score, labelStyle);
        labelScore.setPosition(20, Constants.HEIGHT - btnMenu.getHeight() - 20);

		stage.addActor(btnMenu);
        stage.addActor(labelScore);
	}

	@Override
	public void hide() {
		super.hide();

		gameState = GAME_PAUSED;
	}

	@Override
	public void pause() {
		super.pause();

		gameState = GAME_PAUSED;
	}

	@Override
	public void resume() {
		super.resume();

		gameState = GAME_RUNNING;
	}

	@Override
	public void dispose() {
		super.dispose();

		playerImage.dispose();
		collisionSound.dispose();
		gameState = GAME_OVER;
	}
}
