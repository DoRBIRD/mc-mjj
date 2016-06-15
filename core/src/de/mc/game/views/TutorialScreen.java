package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.text.DecimalFormat;

import de.mc.game.models.MapManager;
import de.mc.game.models.Player;
import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;
import de.mc.game.utils.CustomTextButton;
import de.mc.game.utils.TextureMapObjectRenderer;

public class TutorialScreen extends CustomScreenAdapter {

    private final String
            inputTypeAccelerometer = "ACCELEROMETER",
            inputTypeTouch = "TOUCH";
    private final CustomTextButton pauseButton;
    public TutorialOverlay tutorialOverlay;
    private float traveledDistance = 0.0f;
    private int collectedCoins = 0;
    private GameOverOverlay gameOverOverlay;
    private PauseOverlay pauseOverlay;
    private Player player;
    private State state;
    private MapManager mapManager;
    private TiledMapRenderer tiledMapRenderer;
    private Label labelScore;
    private Table swipeTable;
    private float
            cameraOffsetY = Constants.HEIGHT * 1 / 3,
            accelerometerYDefault;
    private float lastShownPopup;
    private float[] popupPosList = {100f, 800f, 2000f, 3500f, 5000f, 6500f};
    private String[] popupContentList =
            {"Neige das Handy zum Steuern des Spielers",
                    "Neige Handy vorwärts zum Beschleunigen",
                    "Münzen erhöhen deinen Score",
                    "Ringe helfen dir über Wasser zu kommen",
                    "Weiche den Eisblöcken aus!",
                    "Schilde schützen dich vor Eisblöcken"};

    public TutorialScreen() {
        super();

        final TutorialScreen tutorialScreen = this;

        pauseButton = new CustomTextButton("\uF04C", Assets.iconButtonStyle);
        pauseButton.setWidth(pauseButton.getWidth() * 2.5f);
        pauseButton.setHeight(pauseButton.getHeight() * 2f);
        pauseButton.setPosition(Constants.WIDTH - pauseButton.getWidth() - 70, Constants.HEIGHT - pauseButton.getHeight() - 30);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause();
                pauseOverlay = new PauseOverlay(null, tutorialScreen);
                pauseButton.remove();
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.BLACK);
        labelScore = new Label(traveledDistance + " " + Constants.LANGUAGE_STRINGS.get("meter"), labelStyle);
        labelScore.setHeight(pauseButton.getHeight());
        labelScore.setPosition(70, Constants.HEIGHT - labelScore.getHeight() - 30);

        final Image imageSwipe = new Image(Assets.swipeUpTexture);

        final Label labelSwipe = new Label(Constants.LANGUAGE_STRINGS.get("swipe_to_start"), labelStyle);
        labelSwipe.setPosition(Constants.WIDTH / 2 - labelSwipe.getWidth() / 2, Constants.HEIGHT / 2 - labelSwipe.getHeight() / 2);
        labelSwipe.setFontScale(0.8f);

        swipeTable = new Table();
        swipeTable.setFillParent(true);
        swipeTable.add(imageSwipe).padBottom(50);
        swipeTable.row();
        swipeTable.add(labelSwipe);

        player = new Player(stage);
        player.setPosition(Constants.MAP_WIDTH / 2 - player.getWidth() / 2, 400);

        stage.addActor(labelScore);

        camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
        camera.update();

        mapManager = new MapManager(true);
        System.out.println("Tutmapmanager created");
        tiledMapRenderer = mapManager.getTiledMapRenderer();
        setReady();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(119f / 255f, 202f / 255f, 228f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer = mapManager.getTiledMapRenderer();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        TextureMapObjectRenderer objectRenderer = mapManager.getObjectRenderer();
        objectRenderer.setView(camera);
        objectRenderer.render();

        drawGameObjects();

        if (state == State.GAME_RUNNING) {
            checkInputs();
            //TEMP
            float accelY = 0;
            if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                accelY = Gdx.input.getAccelerometerY() - accelerometerYDefault;
                if (accelY > 1) {
                    accelY = 1;
                } else if (accelY < -1) {
                    accelY *= 2;
                } else {
                    accelY = 0;
                }
            }
            float yVelocity = 500 * delta - accelY;
            player.moveBy(0, yVelocity);
            if (player.getY() > mapManager.getMapHeigth() - Constants.MAP_HEIGHT) {
                //mapManager.addNextBlock();
            }
            if (player.getY() > mapManager.getMapHeigth()) {
                gameOver();
            }

            updateScore();

            checkCollision();
            player.updatePowerUpsTimer(delta);
        }
        checkForTutorialPopUp(player.getY());
        updateCameraPosition();
        super.render(delta);
    }

    private void checkForTutorialPopUp(float y) {
        for (int i = 0; i < popupPosList.length; i++) {
            if (lastShownPopup < popupPosList[i] && y >= popupPosList[i]) {
                showPopup(popupContentList[i]);
                lastShownPopup = y - 1;
            }
        }
    }

    private void showPopup(String content) {
        tutorialOverlay = new TutorialOverlay(this, content);
        //pause();
    }


    private void checkCollision() {
        for (Rectangle hb : mapManager.getWaterHitBoxes()) {
            if (!player.hasRing() && Intersector.overlaps(hb, player.getHitBox())) {
                gameOver();
                return;
            }
        }
        if (mapManager.checkCollisionIcebergs(player.getHitBox()) && !player.hasShield()) {
            gameOver();
            return;
        }
        if (mapManager.checkCollisionCoins(player.getHitBox())) {
            collectedCoins++;
            System.out.println("Coin");
        }
        if (mapManager.checkCollisionShields(player.getHitBox())) {
            player.pickupShield();
            System.out.println("Shield");
        }
        if (mapManager.checkCollisionRings(player.getHitBox())) {
            player.pickupRing();
            System.out.println("Rings");
        }
    }


    //test
    private void checkInputs() {
        // touch or accelerator input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            updatePlayerPosition(touchPos.x, inputTypeTouch);
        } else if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            float accelX = -Gdx.input.getAccelerometerX() * 3;
            if (accelX > 1.5f || accelX < -1.5f) {
                updatePlayerPosition(accelX, inputTypeAccelerometer);

            } else {
                player.updateImage(Player.Direction.STRAIGHT);
            }
        } else {
            player.updateImage(Player.Direction.STRAIGHT);
        }

    }

    private void drawGameObjects() {
        if (state != State.GAME_RUNNING) {
            player.updateImage(Player.Direction.STRAIGHT);
        }
        mcGame.batch.begin();
        player.draw(mcGame.batch, state);
        mcGame.batch.end();
    }

    private void gameOver() {
        pauseButton.remove();
        player.setPosition(Constants.MAP_WIDTH / 2 - player.getWidth() / 2, 400);
        player.resetPickups();
        mapManager.resetMap();
        gameOverOverlay = new GameOverOverlay(null, this, traveledDistance, collectedCoins);
        resetScore();
        lastShownPopup = 0f;
        state = State.GAME_OVER;
    }

    private void resetScore() {
        traveledDistance = 0.0f;
        collectedCoins = 0;
        labelScore.setText(traveledDistance + " " + Constants.LANGUAGE_STRINGS.get("meter"));
    }

    public void setReady() {
        state = State.GAME_READY;
        stage.addActor(swipeTable);
        stage.addActor(pauseButton);
        if (gameOverOverlay != null)
            gameOverOverlay.dispose();
        if (pauseOverlay != null)
            pauseOverlay.dispose();
        if (tutorialOverlay != null)
            tutorialOverlay.dispose();
    }

    private void startGame() {
        state = State.GAME_RUNNING;
        accelerometerYDefault = Gdx.input.getAccelerometerY();
        swipeTable.remove();
    }

    private void updateCameraPosition() {
        camera.position.x = player.getX();
        camera.position.y = player.getY() + cameraOffsetY;
        camera.update();
    }

    private void updatePlayerPosition(float newX, String inputType) {
        float oldX = player.getX();
        float velocity = 6;
        float marginoferror = 3f;

        if (inputType.equals(inputTypeAccelerometer)) {
            newX = oldX + newX;
        } else if (inputType.equals(inputTypeTouch)) {
            if (newX > oldX + marginoferror) {
                newX = oldX + velocity;
            } else if (newX < oldX - marginoferror) {
                newX = oldX - velocity;
            }
        }


        if (newX > oldX) {
            player.updateImage(Player.Direction.RIGHT);
        } else if (newX < oldX) {
            player.updateImage(Player.Direction.LEFT);
        } else {
            player.updateImage(Player.Direction.STRAIGHT);
        }
        // stay player in screen
        if (newX < 0) newX = 0;
        if (newX > Constants.MAP_WIDTH - player.getWidth())
            newX = Constants.MAP_HEIGHT - player.getWidth();
        player.setX(newX);
    }

    private void updateScore() {
        if (state == State.GAME_RUNNING) {
            DecimalFormat df = new DecimalFormat("#.#");
            traveledDistance = player.getY() / 160;
            String distance = df.format(traveledDistance);
            labelScore.setText(distance + " " + Constants.LANGUAGE_STRINGS.get("meter") + " Coins: " + collectedCoins);
        }
    }

    @Override
    public void show() {
        super.show();

        setReady();

        mcGame.inputMultiplexer.addProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if (state == State.GAME_READY && velocityY <= -1500) {
                    startGame();
                    return true;
                }
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }
        }));
    }

    //Changes from game to main menu and sets the game state to GAME_OVER
    public void changeToMainMenu() {
        mcGame.setScreen(mcGame.mainMenuScreen);
        gameOver();
    }

    @Override
    public void hide() {
        state = State.GAME_PAUSED;
    }

    @Override
    public void pause() {
        state = State.GAME_PAUSED;
    }

    @Override
    public void resume() {
        setReady();
    }

    @Override
    public void dispose() {
        state = null;
    }

}
