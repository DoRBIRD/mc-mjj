package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
		player.x = Constants.WIDTH / 2 - 64 / 2;
		player.y = 20;
		player.width = 44;
		player.height = 56;

		timerScore = 0;

		gameState = GAME_READY;
	}

	@Override
	public void render (float delta) {
		super.render(delta);

		// wait for assetManager loading all sources
		if(mcGame.assetManager.update()) {
			// we are done loading, do some action!

			checkInputs();

			moveGameObjects();

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

	private void moveGameObjects() {
		// move gameWorld
		/*
		while(iter.hasNext()) {
			GameWorld b = iter.next();
			b.setX(b.getX() - (200 * Gdx.graphics.getDeltaTime()));
			if(b.getY() + 14 < 0) iter.remove();
			// check for overlapping/collision
			if(b.getBounding().overlaps(player)) {
				collisionSound = mcGame.assetManager.get("sounds/plop.ogg", Sound.class);
				collisionSound.play();
				score = 0;
				iter.remove();
			}
		}
		*/
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
		if(newX > Constants.WIDTH - 64) newX = Constants.WIDTH - 64;
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
