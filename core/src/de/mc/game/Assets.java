package de.mc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import de.mc.game.models.MapManager;

public class Assets {

    public static AssetManager assetManager;

    public static Texture
            playerStraightTexture,
            playerLeftTexture,
            playerRightTexture;

    public static TextButton.TextButtonStyle
            defaultTextButtonStyle,
            iconButtonStyle;

    public static BitmapFont
            FONT_AWESOME,
            TONDU_BETA_SMALL,
            TONDU_BETA_MEDIUM,
            TONDU_BETA_LARGE;

    public static void load() {
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        assetManager.load("images/player.gif", Texture.class);
        assetManager.load("images/player-left.gif", Texture.class);
        assetManager.load("images/player-right.gif", Texture.class);

        MapManager.loadMaps();

        assetManager.setLoader(BitmapFont.class, new FreetypeFontLoader(new InternalFileHandleResolver()));

        assetManager.finishLoading();

        if(assetManager.update()) {
            init();
        }

        loadFonts();

        loadDefaultStyles();
    }

    private static void init() {
        playerStraightTexture = assetManager.get("images/player.gif");
        playerLeftTexture = assetManager.get("images/player-left.gif");
        playerRightTexture = assetManager.get("images/player-right.gif");
    }

    private static void loadFonts() {


        FreeTypeFontGenerator generatorDefaultFont = new FreeTypeFontGenerator(Gdx.files.internal("fonts/TonduBeta.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 50;
        TONDU_BETA_SMALL = generatorDefaultFont.generateFont(parameter);

        parameter.size = 100;
        TONDU_BETA_MEDIUM = generatorDefaultFont.generateFont(parameter);

        parameter.size = 150;
        TONDU_BETA_LARGE = generatorDefaultFont.generateFont(parameter);


        FreeTypeFontGenerator generatorFontAwesome = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FontAwesome.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterFontAwesome = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterFontAwesome.size = 80;
        parameterFontAwesome.characters = "\uF04C";
        FONT_AWESOME = generatorFontAwesome.generateFont(parameterFontAwesome);
    }

    private static void loadDefaultStyles() {
        Skin defaultTextButtonSkin = new Skin(new TextureAtlas("buttons/default-button.pack"));
        defaultTextButtonStyle = new TextButton.TextButtonStyle();
        defaultTextButtonStyle.up = defaultTextButtonSkin.getDrawable("button");
        defaultTextButtonStyle.over = defaultTextButtonSkin.getDrawable("button_pressed");
        defaultTextButtonStyle.down = defaultTextButtonSkin.getDrawable("button_pressed");
        defaultTextButtonStyle.font = TONDU_BETA_MEDIUM;
        defaultTextButtonStyle.fontColor = Color.WHITE;

        Skin iconButtonSkin = new Skin(new TextureAtlas("buttons/icon-button.pack"));
        iconButtonStyle = new TextButton.TextButtonStyle();
        iconButtonStyle.up = iconButtonSkin.getDrawable("icon_button");
        iconButtonStyle.over = iconButtonSkin.getDrawable("icon_button_pressed");
        iconButtonStyle.down = iconButtonSkin.getDrawable("icon_button_pressed");
        iconButtonStyle.font = FONT_AWESOME;
        iconButtonStyle.fontColor = Color.WHITE;
    }
}
