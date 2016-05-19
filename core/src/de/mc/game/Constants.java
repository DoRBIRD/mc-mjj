package de.mc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public final class Constants {
    public static final float w = Gdx.graphics.getWidth();
    public static final float h = Gdx.graphics.getHeight();

    public static final float SCALING = 1.3f;
    public static final float MAP_SCALING = 1f;
    public static final float WIDTH = 1080 * SCALING;
    public static final float HEIGHT = WIDTH * (h / w);

    public static final float TILE_WIDTH = 160 * MAP_SCALING;
    public static final float TILE_HEIGHT = 160 * MAP_SCALING;
    public static final float TILE_X_AMOUNT = 20;
    public static final float TILE_Y_AMOUNT = 50;
    public static final float MAP_WIDTH = TILE_X_AMOUNT * TILE_WIDTH;
    public static final float MAP_HEIGHT = TILE_Y_AMOUNT * TILE_HEIGHT;

    public static final Skin SKIN = new Skin(Gdx.files.internal("skins/uiskin.json"));

    public static final I18NBundle LANGUAGE_STRINGS = I18NBundle.createBundle(Gdx.files.internal("strings/strings"), new Locale("de", "en"));

    public static BitmapFont
            TONDU_BETA_SMALL,
            TONDU_BETA_MEDIUM,
            TONDU_BETA_LARGE;

    public Constants() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/TonduBeta.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        TONDU_BETA_SMALL = generator.generateFont(parameter);

        parameter.size = 100;
        TONDU_BETA_MEDIUM = generator.generateFont(parameter);

        parameter.size = 150;
        TONDU_BETA_LARGE = generator.generateFont(parameter);
    }
}
