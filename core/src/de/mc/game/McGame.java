package de.mc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class McGame extends Game implements InputProcessor {

    public SpriteBatch batch;
    public GlyphLayout glyphLayout;
    public BitmapFont droidSansSmall;
    public BitmapFont droidSansMedium;
    public BitmapFont droidSansLarge;
    public FreeTypeFontGenerator generator;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public Screen currentScreen;
    public Screen mainMenuScreen;
    public Screen gameScreen;
    public AssetManager assetManager;
    public I18NBundle languageStrings;
    public int width;
    public int height;

    public void create() {
        batch = new SpriteBatch();

        width = 480;
        height = 800;

        // init fonts
        glyphLayout = new GlyphLayout();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/DroidSans.ttf"));

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        droidSansSmall = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        droidSansMedium = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        droidSansLarge = generator.generateFont(parameter);

        Locale locale = new Locale("de", "en");
        languageStrings = I18NBundle.createBundle(Gdx.files.internal("strings/strings"), locale);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

        assetManager = new AssetManager();

        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        switchScreen(mainMenuScreen);
    }

    public void switchScreen(Screen s) {
        currentScreen = s;
        setScreen(currentScreen);
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.BACK){
            System.out.println(currentScreen);
            if(currentScreen == gameScreen ) {
                switchScreen(mainMenuScreen);
            }
            else {
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

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        droidSansSmall.dispose();
        droidSansMedium.dispose();
        droidSansLarge.dispose();
        generator.dispose();
        assetManager.dispose();
    }
}
