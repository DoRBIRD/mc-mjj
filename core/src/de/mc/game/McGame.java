package de.mc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.mc.game.utils.AndroidOnlyInterface;
import de.mc.game.utils.Constants;
import de.mc.game.utils.SaveUserNameListener;
import de.mc.game.views.GameScreen;
import de.mc.game.views.MainMenuScreen;
import de.mc.game.views.TutorialScreen;

public class McGame extends Game implements InputProcessor {

    public static AndroidOnlyInterface AOI;
    public SpriteBatch batch;
    public Screen
            mainMenuScreen,
            gameScreen,
            tutorialScreen;
    public InputMultiplexer inputMultiplexer;

    public McGame(AndroidOnlyInterface androidOnlyInterface) {
        AOI = androidOnlyInterface;
    }

    public void create() {
        batch = new SpriteBatch();

        inputMultiplexer = new InputMultiplexer(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setCatchBackKey(true);

        de.mc.game.utils.Assets.load();

        Preferences preferences = Gdx.app.getPreferences(Constants.DEFAULT_PREFS);
        if(preferences.getString(Constants.PREFS_USERNAME).equals(Constants.LANGUAGE_STRINGS.get("yourname"))) {
            SaveUserNameListener listener = new SaveUserNameListener();
            Gdx.input.getTextInput(listener, "Dein Spielername:", "", Constants.LANGUAGE_STRINGS.get("yourname"));
        }

        mainMenuScreen = new MainMenuScreen();
        gameScreen = new GameScreen();
        tutorialScreen = new TutorialScreen();
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
