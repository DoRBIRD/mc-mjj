package de.mc.game.models;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Jonas on 01/06/2016.
 */
public class RectTile {
    private Rectangle rect;
    private TiledMapTile tile;
    private int posX;
    private int posY;

    public RectTile(Rectangle rectangle, TiledMapTile tiledMapTile, int x, int y) {
        rect = rectangle;
        tile = tiledMapTile;
        posX = x;
        posY = y;
    }

    public Rectangle getRect() {
        return rect;
    }

    public TiledMapTile getTile() {
        return tile;
    }

    public void setTile(TiledMapTile tile) {
        this.tile = tile;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}
