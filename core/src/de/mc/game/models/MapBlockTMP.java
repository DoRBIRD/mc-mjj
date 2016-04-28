package de.mc.game.models;

import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by Jonas on 28/04/2016.
 */
public class MapBlockTMP {
    private TiledMap map;
    private String connectionBottom;
    private String connectionTop;

    public MapBlockTMP(TiledMap block, String bot, String top) {
        map = block;
        connectionBottom = bot;
        connectionTop = top;
    }

    public TiledMap getMap() {
        return map;
    }

    public String getConnectionBottom() {
        return connectionBottom;
    }

    public String getConnectionTop() {
        return connectionTop;
    }
}
