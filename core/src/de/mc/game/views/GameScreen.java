package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.input.GestureDetector;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import de.mc.game.Constants;
import de.mc.game.McGame;
import de.mc.game.models.GameWorld;
import de.mc.game.models.Player;

public class GameScreen extends CustomScreenAdapter {

    static final int GAME_READY, GAME_RUNNING, GAME_PAUSED, GAME_OVER;

    static {
        GAME_READY = 0;
        GAME_RUNNING = 1;
        GAME_PAUSED = 2;
        GAME_OVER = 3;
    }

    private final String inputTypeAccelerometer = "ACCELEROMETER";
    private final String inputTypeTouch = "TOUCH";
    private GameWorld gameWorld;
    private Sound collisionSound;
    private Player player;
    private int score;
    private int gameState;
    private float timerScore;
    private TiledMap tiledMap;
    private TiledMap tiledMap2;
    private TiledMapRenderer tiledMapRenderer;
    private Label labelScore;

    private Array<Rectangle> mapHitBoxes;

    public GameScreen(final McGame g) {
        super(g);

        // load the sound effects
        mcGame.assetManager.load("sounds/plop.ogg", Sound.class);

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

        player = new Player(g);

        stage.addActor(btnMenu);
        stage.addActor(labelScore);
        stage.addActor(player);

        //WIP MAP
        camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
        //camera.setToOrtho(false,w,h);
        camera.update();
        tiledMap = new TmxMapLoader().load("maps/Map-v1.tmx");
        tiledMap2 = new TmxMapLoader().load("maps/Map-v1.tmx");
        tiledMap = addBlockToMap(tiledMap, tiledMap2);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        gameState = GAME_READY;
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

            //TEMP
            float yVelocity = 6;


            if(gameState == GAME_RUNNING) {
                checkInputs();

                player.moveBy(0, yVelocity);
                if (player.getY() > Constants.MAP_HEIGHT) player.setY(0);

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
            if (Intersector.overlaps(hb, player.getHitBox())) player.setY(0);
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
        mcGame.batch.begin();
        player.draw(mcGame.batch, 0);
        mcGame.batch.end();
    }

    private void gameOver() {

       // gameState == GAME_OVER;
    }

    private void updateCameraPosition() {
        float xOffset = 0f;
        float yOffset = Constants.HEIGHT * 1 / 3;

        camera.position.x = player.getX() + xOffset;
        camera.position.y = player.getY() + yOffset;
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
        if (newX > Constants.MAP_WIDTH - 64) newX = Constants.MAP_HEIGHT - 64;
        player.setX(newX);
    }

    private void updateScore() {
        timerScore += Gdx.graphics.getDeltaTime();
        if (gameState == GAME_RUNNING && timerScore >= 0.1) {
            // 1/10 second just passed
            timerScore -= 0.1; // reset our timer
            score++;
            labelScore.setText(mcGame.languageStrings.get("score") + ": " + score);
        }
    }

    @Override
    public void show() {
        super.show();

        gameState = GAME_PAUSED;

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
                Gdx.app.log("log", "drin");
                if(gameState == GAME_PAUSED && velocityY <= -1500) {
                    gameState = GAME_RUNNING;
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

        collisionSound.dispose();
        gameState = GAME_OVER;
    }
}
