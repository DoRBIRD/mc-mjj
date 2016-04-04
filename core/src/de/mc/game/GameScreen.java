package de.mc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class GameScreen implements Screen {

	final McGame mcGame;

	private Texture barrierImage;
	private Texture playerImage;
	private Sound collisionSound;
	private OrthographicCamera camera;
	private Rectangle player;
	private Array<Rectangle> barriers;
	private int score;
	private boolean gameStarted;
	private float timerBarrier;
	private float timerScore;

	private final String inputTypeAccelerometer = "ACCELEROMETER";
	private final String inputTypeTouch = "TOUCH";

	public GameScreen(final McGame g) {
		mcGame = g;

		// load the images
		mcGame.assetManager.load("images/barrier.jpg", Texture.class);
		/*
		mcGame.assetManager.load("images/player_normal.png", Texture.class);
		mcGame.assetManager.load("images/player_left.png", Texture.class);
		mcGame.assetManager.load("images/player_right.png", Texture.class);*/
		mcGame.assetManager.load("images/player_normal_b.png", Texture.class);
		mcGame.assetManager.load("images/player_left_b.png", Texture.class);
		mcGame.assetManager.load("images/player_right_b.png", Texture.class);

		// load the sound effects
		mcGame.assetManager.load("sounds/plop.ogg", Sound.class);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, mcGame.width, mcGame.height);

		player = new Rectangle();
		player.x = mcGame.width / 2 - 64 / 2;
		player.y = 20;
		player.width = 44;
		player.height = 56;

		timerBarrier = 0;
		timerScore = 0;

		barriers = new Array<Rectangle>();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		mcGame.batch.setProjectionMatrix(camera.combined);

		// wait for assetManager loading all sources
		if(mcGame.assetManager.update()) {
			// we are done loading, do some action!

			checkInputs();

			spawnBarrier();

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

		// keyboard input
		//if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.x -= 200 * Gdx.graphics.getDeltaTime();
		//if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.x += 200 * Gdx.graphics.getDeltaTime();
	}

	private void drawGameObjects() {
		mcGame.batch.begin();

		mcGame.glyphLayout.setText(mcGame.droidSansMedium, mcGame.languageStrings.get("score") + ": " + score);
		mcGame.droidSansMedium.draw(mcGame.batch, mcGame.glyphLayout, 20, mcGame.height - 20);

		mcGame.batch.draw(playerImage, player.x, player.y);

		barrierImage = mcGame.assetManager.get("images/barrier.jpg", Texture.class);
		for(Rectangle barrier: barriers) {
			mcGame.batch.draw(barrierImage, barrier.x, barrier.y);
		}

		mcGame.batch.end();
	}

	private void moveGameObjects() {
		// move barrier
		Iterator<Rectangle> iter = barriers.iterator();
		while(iter.hasNext()) {
			Rectangle barrier = iter.next();
			barrier.y -= 200 * Gdx.graphics.getDeltaTime();
			if(barrier.y + 14 < 0) iter.remove();
			// check for overlapping/collision
			if(barrier.overlaps(player)) {
				collisionSound = mcGame.assetManager.get("sounds/plop.ogg", Sound.class);
				collisionSound.play();
				score = 0;
				iter.remove();
			}
		}
	}

	private void spawnBarrier() {
		timerBarrier += Gdx.graphics.getDeltaTime();
		if(gameStarted && timerBarrier >= 1) {
			timerBarrier -= 1;
			Rectangle barrier = new Rectangle();
			barrier.x = MathUtils.random(0, mcGame.width - 64);
			barrier.y = mcGame.height;
			barrier.width = 64;
			barrier.height = 14;
			barriers.add(barrier);
		}
	}

	private void updatePlayerPosition(float newX, String inputType) {
		float oldX = player.x;
		float velocity = 6;
		float marginoferror = 3f;

		if(inputType.equals(inputTypeAccelerometer)) {
			newX = oldX + newX;
		}else{
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
		if(newX > mcGame.width - 64) newX = mcGame.width - 64;
		player.x = newX;
	}

	private void updateScore() {
		timerScore += Gdx.graphics.getDeltaTime();
		if(gameStarted && timerScore >= 0.1){
			// 1/10 second just passed
			timerScore -= 0.1; // reset our timer
			score++;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		gameStarted = true;
	}

	@Override
	public void hide() {
		gameStarted = false;
	}

	@Override
	public void pause() {
		gameStarted = false;
	}

	@Override
	public void resume() {
		gameStarted = true;
	}

	@Override
	public void dispose() {
		barrierImage.dispose();
		playerImage.dispose();
		collisionSound.dispose();
	}
}
