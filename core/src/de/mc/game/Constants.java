package de.mc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Constants {
    public static final float w = Gdx.graphics.getWidth();
    public static final float h = Gdx.graphics.getHeight();

    public static final float WIDTH = 1080;
    public static final float HEIGHT = WIDTH  * (h / w);

    public static final float TILE_WIDTH = 160;
    public static final float TILE_HEIGHT = 160;
    public static final float TILE_X_AMOUNT = 20;
    public static final float TILE_Y_AMOUNT = 50;
    public static final float MAP_WIDTH = TILE_X_AMOUNT * TILE_WIDTH;
    public static final float MAP_HEIGHT = TILE_Y_AMOUNT * TILE_HEIGHT;

    public static final Skin SKIN = new Skin(Gdx.files.internal("skins/uiskin.json"));
}
