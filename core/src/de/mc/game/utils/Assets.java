package de.mc.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import de.mc.game.models.MapManager;

public class Assets {

    public static AssetManager assetManager;

    public static Skin defaultSkin;

    public static Texture
            playerStraightTexture,
            playerLeftTexture,
            playerRightTexture,
            ringStraightTexture,
            ringLeftTexture,
            ringRightTexture,
            shieldStraightTexture,
            shieldLeftTexture,
            shieldRightTexture,
            menuTexture,
            menuButtonTexture,
            menuTitleTexture,
            menuTitleButtonTexture,
            menuLargeTexture,
            menuButtonLargeTexture,
            menuTitleLargeTexture,
            menuTitleButtonLargeTexture,
            swipeUpTexture,
            backgroundLabelGreenTexture;

    public static TextButton.TextButtonStyle
            defaultTextButtonStyle,
            iconButtonStyle,
            blueButtonBackgroundStyle,
            menuCloseButtonStyle;

    public static TextField.TextFieldStyle defaultTextFieldStyle;

    public static Slider.SliderStyle defaultSliderStyle;

    public static BitmapFont
            FONT_AWESOME,
            TONDU_BETA;

    public static void load() {
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        assetManager.load("images/player.gif", Texture.class);
        assetManager.load("images/player-left.gif", Texture.class);
        assetManager.load("images/player-right.gif", Texture.class);
        assetManager.load("images/Ring-top.gif", Texture.class);
        assetManager.load("images/Ring-left.gif", Texture.class);
        assetManager.load("images/Ring-right.gif", Texture.class);

        assetManager.load("menus/menu.png", Texture.class);
        assetManager.load("menus/menu_button.png", Texture.class);
        assetManager.load("menus/menu_title.png", Texture.class);
        assetManager.load("menus/menu_title_button.png", Texture.class);
        assetManager.load("menus/menu_large.png", Texture.class);
        assetManager.load("menus/menu_button_large.png", Texture.class);
        assetManager.load("menus/menu_title_large.png", Texture.class);
        assetManager.load("menus/menu_title_button_large.png", Texture.class);
        assetManager.load("images/swipe_up.png", Texture.class);
        assetManager.load("images/background_label_green.png", Texture.class);

        MapManager.loadMaps();

        assetManager.setLoader(BitmapFont.class, new FreetypeFontLoader(new InternalFileHandleResolver()));

        assetManager.finishLoading();

        if(assetManager.update()) {
            init();
        }

        loadFonts();

        loadStyles();
    }

    private static void init() {
        playerStraightTexture = assetManager.get("images/player.gif");
        playerLeftTexture = assetManager.get("images/player-left.gif");
        playerRightTexture = assetManager.get("images/player-right.gif");
        ringStraightTexture = assetManager.get("images/Ring-top.gif");
        ringLeftTexture = assetManager.get("images/Ring-left.gif");
        ringRightTexture = assetManager.get("images/Ring-right.gif");

        shieldStraightTexture = assetManager.get("images/player.gif");
        shieldLeftTexture = assetManager.get("images/player-left.gif");
        shieldRightTexture = assetManager.get("images/player-right.gif");

        menuTexture = assetManager.get("menus/menu.png");
        menuButtonTexture = assetManager.get("menus/menu_button.png");
        menuTitleTexture = assetManager.get("menus/menu_title.png");
        menuTitleButtonTexture = assetManager.get("menus/menu_title_button.png");
        menuLargeTexture = assetManager.get("menus/menu_large.png");
        menuButtonLargeTexture = assetManager.get("menus/menu_button_large.png");
        menuTitleLargeTexture = assetManager.get("menus/menu_title_large.png");
        menuTitleButtonLargeTexture = assetManager.get("menus/menu_title_button_large.png");
        swipeUpTexture = assetManager.get("images/swipe_up.png");
        backgroundLabelGreenTexture = assetManager.get("images/background_label_green.png");
    }

    private static void loadFonts() {


        FreeTypeFontGenerator generatorDefaultFont = new FreeTypeFontGenerator(Gdx.files.internal("fonts/TonduBeta.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        TONDU_BETA = generatorDefaultFont.generateFont(parameter);

        FreeTypeFontGenerator generatorFontAwesome = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FontAwesome.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterFontAwesome = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterFontAwesome.size = 80;
        parameterFontAwesome.characters = "\uF04C";
        FONT_AWESOME = generatorFontAwesome.generateFont(parameterFontAwesome);
    }

    private static void loadStyles() {
        Skin defaultTextButtonSkin = new Skin(new TextureAtlas("buttons/default-button.pack"));
        defaultTextButtonStyle = new TextButton.TextButtonStyle();
        defaultTextButtonStyle.up = defaultTextButtonSkin.getDrawable("button");
        defaultTextButtonStyle.over = defaultTextButtonSkin.getDrawable("button_pressed");
        defaultTextButtonStyle.down = defaultTextButtonSkin.getDrawable("button_pressed");
        defaultTextButtonStyle.font = TONDU_BETA;
        defaultTextButtonStyle.fontColor = Color.WHITE;

        Skin iconButtonSkin = new Skin(new TextureAtlas("buttons/icon-button.pack"));
        iconButtonStyle = new TextButton.TextButtonStyle();
        iconButtonStyle.up = iconButtonSkin.getDrawable("icon_button");
        iconButtonStyle.over = iconButtonSkin.getDrawable("icon_button_pressed");
        iconButtonStyle.down = iconButtonSkin.getDrawable("icon_button_pressed");
        iconButtonStyle.font = FONT_AWESOME;
        iconButtonStyle.fontColor = Color.WHITE;

        Skin blueButtonBackground = new Skin(new TextureAtlas("buttons/blue-button-background.pack"));
        blueButtonBackgroundStyle = new TextButton.TextButtonStyle();
        blueButtonBackgroundStyle.up = blueButtonBackground.getDrawable("blue_button_background");
        blueButtonBackgroundStyle.over = blueButtonBackground.getDrawable("blue_button_background_pressed");
        blueButtonBackgroundStyle.down = blueButtonBackground.getDrawable("blue_button_background_pressed");
        blueButtonBackgroundStyle.font = TONDU_BETA;
        blueButtonBackgroundStyle.fontColor = Color.WHITE;

        Skin menuCloseButton = new Skin(new TextureAtlas("buttons/menu-close-button.pack"));
        menuCloseButtonStyle = new TextButton.TextButtonStyle();
        menuCloseButtonStyle.up = menuCloseButton.getDrawable("menu_close_button");
        menuCloseButtonStyle.over = menuCloseButton.getDrawable("menu_close_button_pressed");
        menuCloseButtonStyle.down = menuCloseButton.getDrawable("menu_close_button_pressed");

        NinePatch backgroundLabelGreen = new NinePatch(backgroundLabelGreenTexture, 0, 0, 0, 0);
        defaultSkin = new Skin();
        defaultSkin.add("backgroundLabelGreen", backgroundLabelGreen);

        Skin textField = new Skin(new TextureAtlas("textfield/default-text-field.pack"));
        defaultTextFieldStyle = new TextField.TextFieldStyle();
        defaultTextFieldStyle.background = textField.getDrawable("background_text_field");
        defaultTextFieldStyle.cursor = textField.getDrawable("cursor");
        defaultTextFieldStyle.selection = textField.getDrawable("text_field_selection");
        defaultTextFieldStyle.font = Assets.TONDU_BETA;
        defaultTextFieldStyle.fontColor = Color.WHITE;

        Skin slider = new Skin(new TextureAtlas("slider/default-slider.pack"));
        defaultSliderStyle = new Slider.SliderStyle();
        defaultSliderStyle.background = slider.getDrawable("slider_background");
        defaultSliderStyle.knob = slider.getDrawable("slider_knob");
    }
}
