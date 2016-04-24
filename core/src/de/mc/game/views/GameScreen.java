package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import de.mc.game.Constants;
import de.mc.game.McGame;
import de.mc.game.models.Player;

public class GameScreen extends CustomScreenAdapter {

    public enum State {
        GAME_READY, GAME_RUNNING, GAME_PAUSED, GAME_OVER, WAIT_FOR_USER_INPUT
    }

    private final String inputTypeAccelerometer = "ACCELEROMETER";
    private final String inputTypeTouch = "TOUCH";
    private GameOverOverlay gameOverOverlay;
    private Player player;
    private int score;
    private int lastScore;
    private State state;
    private float timerScore;
    private TiledMap tiledMap;
    private TiledMap tiledMap2;
    private TiledMapRenderer tiledMapRenderer;
    private Label labelScore, labelSwipe;

    private Array<Rectangle> mapHitBoxes;

    private float cameraOffsetY = Constants.HEIGHT * 1 / 3;

    public GameScreen(final McGame g) {
        super(g);

        final TextButton btnMenu = new TextButton("Menü", mcGame.defaultTextButtonStyle);
        btnMenu.setWidth(btnMenu.getWidth() + 30);
        btnMenu.setHeight(btnMenu.getHeight() + 20);
        btnMenu.setPosition(Constants.WIDTH - btnMenu.getWidth() - 20, Constants.HEIGHT - btnMenu.getHeight() - 20);
        btnMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mcGame.setScreen(mcGame.mainMenuScreen);
                if(gameOverOverlay != null) {
                    setReady();
                    gameOverOverlay.dispose();
                }
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(mcGame.droidSansMedium, Color.BLACK);
        labelScore = new Label(mcGame.languageStrings.get("score") + ": " + score, labelStyle);
        labelScore.setPosition(20, Constants.HEIGHT - btnMenu.getHeight() - 20);

        labelSwipe = new Label("Swipe up to start", labelStyle);
        labelSwipe.setPosition(Constants.WIDTH / 2 - labelSwipe.getWidth() / 2, Constants.HEIGHT / 2 - labelSwipe.getHeight() / 2);

        player = new Player(g);
        player.setPosition(Constants.MAP_WIDTH / 2 - player.getWidth() / 2, cameraOffsetY);

        stage.addActor(btnMenu);
        stage.addActor(labelScore);
        stage.addActor(player);
        stage.addActor(labelSwipe);

        //WIP MAP
        camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
        camera.update();
        tiledMap = new TmxMapLoader().load("maps/Map-v1.tmx");
        tiledMap2 = new TmxMapLoader().load("maps/Map-v1.tmx");
        tiledMap = addBlockToMap(tiledMap, tiledMap2);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        setReady();
        createHitBoxArray();
    }

    public TiledMap addBlockToMap(TiledMap tm1, TiledMap tm2) {
        TiledMap newMap = new TiledMap();
        MapLayers layers = newMap.getLayers();
        //TODO check if same width
        int oldHeight = ((TiledMapTileLayer) tm1.getLayers().get(0)).getHeight();
        int newHeight = oldHeight + ((TiledMapTileLayer) tm2.getLayers().get(0)).getHeight();
        int tileWidth = (int) ((TiledMapTileLayer) tm1.getLayers().get(0)).getTileWidth();
        int tileHeight = (int) ((TiledMapTileLayer) tm1.getLayers().get(0)).getTileWidth();
        TiledMapTileLayer toAddMapLayer = (TiledMapTileLayer) tiledMap2.getLayers().get(0);
        TiledMapTileLayer oldMapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        TiledMapTileLayer newMaplayer = new TiledMapTileLayer(((TiledMapTileLayer) tm1.getLayers().get(0)).getWidth(), newHeight, tileWidth, tileHeight);

        for (int x = 0; x < toAddMapLayer.getWidth(); x++) {
            for (int y = 0; y < newHeight; y++) {
                TiledMapTileLayer.Cell cell;
                if (y < oldHeight) {
                    cell = oldMapLayer.getCell(x, y);
                } else {
                    cell = toAddMapLayer.getCell(x, y - oldHeight);
                }
                newMaplayer.setCell(x, y, cell);
            }
        }
        layers.add(newMaplayer);
        return newMap;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // wait for assetManager loading all sources
        if (mcGame.assetManager.update()) {
            // we are done loading, do some action!

            drawGameObjects();

            if(state == State.GAME_OVER) {
                gameOverOverlay = new GameOverOverlay(mcGame, this, lastScore);

                state = State.WAIT_FOR_USER_INPUT;
            }

            if(state == State.GAME_RUNNING) {
                checkInputs();

                //TEMP
                float yVelocity = 6;
                player.moveBy(0, yVelocity);
                if (player.getY() > Constants.MAP_HEIGHT * 2) player.setY(cameraOffsetY);

                updateScore();

                checkCollision();
            }

            updateCameraPosition();
        } else {
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

    private void createHitBoxArray() {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        mapHitBoxes = new Array<Rectangle>();
        //iterrate through map
        for (int x = 0; x < mapLayer.getWidth(); x++) {
            for (int y = 0; y < mapLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
                TiledMapTile tile = cell.getTile();
                Object tmp = tile.getProperties().get("terrain");
                //Wenn tile kein eis enthält -> add to hitbox array
                if (tmp != null && !tmp.toString().contains("0")) {
                    mapHitBoxes.add(new Rectangle(x * mapLayer.getTileWidth(), y * mapLayer.getTileHeight(), mapLayer.getTileWidth(), mapLayer.getTileHeight()));
                }
            }
        }
    }

    private void checkCollision() {
        for (Rectangle hb : mapHitBoxes) {
            if (Intersector.overlaps(hb, player.getHitBox())) gameOver();
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
        if(state != State.GAME_RUNNING) {
            player.updateImage(Player.Direction.STRAIGHT);
        }
        mcGame.batch.begin();
        player.draw(mcGame.batch, 0);
        mcGame.batch.end();
    }

    private void gameOver() {
        player.setPosition(Constants.MAP_WIDTH / 2 - player.getWidth() / 2, cameraOffsetY);
        resetScore();
        state = State.GAME_OVER;
    }

    private void resetScore() {
        lastScore = score;
        score = 0;
        labelScore.setText(mcGame.languageStrings.get("score") + ": " + score);
    }

    public void setReady() {
        state = State.GAME_READY;
        stage.addActor(labelSwipe);
    }

    private void startGame() {
        state = State.GAME_RUNNING;
        labelSwipe.remove();
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
        if (newX > Constants.MAP_WIDTH - player.getWidth()) newX = Constants.MAP_HEIGHT - player.getWidth();
        player.setX(newX);
    }

    private void updateScore() {
        timerScore += Gdx.graphics.getDeltaTime();
        if (state == State.GAME_RUNNING && timerScore >= 0.1) {
            // 1/10 second just passed
            timerScore -= 0.1; // reset our timer
            score++;
            labelScore.setText(mcGame.languageStrings.get("score") + ": " + score);
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
                if((state == State.GAME_PAUSED || state == State.GAME_OVER || state == State.GAME_READY) && velocityY <= -1500) {
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

    @Override
    public void hide() {
        super.hide();

        state = State.GAME_PAUSED;
    }

    @Override
    public void pause() {
        super.pause();

        state = State.GAME_PAUSED;
    }

    @Override
    public void resume() {
        super.resume();

        setReady();
    }

    @Override
    public void dispose() {
        super.dispose();

        state = null;
    }
}
