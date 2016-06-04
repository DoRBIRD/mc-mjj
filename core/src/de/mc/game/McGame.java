package de.mc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.mc.game.utils.AndroidOnlyInterface;
import de.mc.game.views.GameScreen;
import de.mc.game.views.MainMenuScreen;

public class McGame extends Game implements InputProcessor {

    public SpriteBatch batch;
    public Screen
            mainMenuScreen,
            gameScreen;
    public InputMultiplexer inputMultiplexer;

    public static AndroidOnlyInterface AOI;

    public McGame(AndroidOnlyInterface androidOnlyInterface){
        AOI = androidOnlyInterface;
    }

    public void create() {
        batch = new SpriteBatch();

        inputMultiplexer = new InputMultiplexer(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setCatchBackKey(true);

        de.mc.game.utils.Assets.load();

        mainMenuScreen = new MainMenuScreen();
        gameScreen = new GameScreen();
        setScreen(mainMenuScreen);
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK) {
            if (screen instanceof GameScreen) {
                setScreen(mainMenuScreen);
            } else {
                Gdx.app.exit();
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
