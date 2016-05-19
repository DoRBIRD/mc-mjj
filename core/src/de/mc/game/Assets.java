package de.mc.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.mc.game.models.MapManager;

public class Assets {

    public static AssetManager assetManager;

    public static Texture
            playerStraightTexture,
            playerLeftTexture,
            playerRightTexture;

    public static void load() {
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        assetManager.load("images/player.gif", Texture.class);
        assetManager.load("images/player-left.gif", Texture.class);
        assetManager.load("images/player-right.gif", Texture.class);

        MapManager.loadMaps();

        assetManager.finishLoading();

        init();
    }

    private static void init() {
        playerStraightTexture = assetManager.get("images/player.gif");
        playerLeftTexture = assetManager.get("images/player-left.gif");
        playerRightTexture = assetManager.get("images/player-right.gif");
    }
}
