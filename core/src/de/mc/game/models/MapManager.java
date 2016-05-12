package de.mc.game.models;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import de.mc.game.Constants;
import de.mc.game.McGame;
import de.mc.game.TextureMapObjectRenderer;

/**
 * Created by Jonas on 28/04/2016.
 */
public class MapManager {
    private String lastconnection;
    private TiledMap tiledMap;
    private Array<Rectangle> mapHitBoxes;
    private TiledMapRenderer tiledMapRenderer;


    private TextureMapObjectRenderer objectRenderer;
    private Array<Array<MapBlock>> blocks;
    private McGame mcGame;

    private String folder = "maps/block";
    private String mappaths[] = {"A_A_1", "A_B_1", "B_A_1", "A_C_1", "C_A_1", "A_D_1",
            "B_A_1", "B_B_1", "B_A_1", "B_C_1", "C_A_1", "B_D_1",
            "C_A_1", "C_B_1", "C_A_1", "C_C_1", "C_A_1", "C_D_1",
            "D_A_1", "D_B_1", "D_A_1", "D_C_1", "C_A_1", "D_D_1"};
    private String sub = ".tmx";

    public MapManager(McGame g) {
        mcGame = g;
        blocks = new Array<Array<MapBlock>>();
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        loadMaps();
        mcGame.assetManager.finishLoading();
        initMapBlocks();
        resetMap();
    }

    public void resetMap() {
        tiledMap = null;
        tiledMap = blocks.get(0).get(0).getMap();
        lastconnection = "A";
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        objectRenderer = new TextureMapObjectRenderer(tiledMap);
        createHitBoxArray();
    }

    public void addNextBlock() {
        tiledMap = addBlockToMap(tiledMap, getNextBlock());
        createHitBoxArray();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        objectRenderer = new TextureMapObjectRenderer(tiledMap);
    }

    private TiledMap addBlockToMap(TiledMap tm1, TiledMap tm2) {
        TiledMap newMap = new TiledMap();
        MapLayers layers = newMap.getLayers();
        TiledMapTileLayer toAddMapLayer = (TiledMapTileLayer) tm2.getLayers().get(0);
        TiledMapTileLayer oldMapLayer = (TiledMapTileLayer) tm1.getLayers().get(0);
        int oldHeight = oldMapLayer.getHeight();
        int newHeight = oldHeight + toAddMapLayer.getHeight();
        int tileWidth = (int) ((TiledMapTileLayer) tm1.getLayers().get(0)).getTileWidth();
        int tileHeight = (int) ((TiledMapTileLayer) tm1.getLayers().get(0)).getTileHeight();
        TiledMapTileLayer newMapLayer = new TiledMapTileLayer(oldMapLayer.getWidth(), newHeight, tileWidth, tileHeight);

        for (int x = 0; x < toAddMapLayer.getWidth(); x++) {
            for (int y = 0; y < newHeight; y++) {
                TiledMapTileLayer.Cell cell;
                if (y < oldHeight) {
                    cell = oldMapLayer.getCell(x, y);
                } else {
                    cell = toAddMapLayer.getCell(x, y - oldHeight);
                }
                newMapLayer.setCell(x, y, cell);
            }
        }
        layers.add(newMapLayer);

        /*this should hopefully add obejcts to running map
        doesnt work yet since not all blocks have a object layer*/
        String objectLayerName = "Eisberg";
        MapLayer newObjectLayer = new TiledMapTileLayer(oldMapLayer.getWidth(), newHeight, tileWidth, tileHeight);
        newObjectLayer.setName(objectLayerName);

        MapLayer oldObjectLayer = tm1.getLayers().get(objectLayerName);
        MapLayer toAddObjectLayer = tm2.getLayers().get(objectLayerName);
        if (oldObjectLayer != null && toAddObjectLayer != null) {
            MapObjects oldMapObjects = oldObjectLayer.getObjects();
            MapObjects newMapObjects = toAddObjectLayer.getObjects();
            for (int i = 0; i < newMapObjects.getCount(); i++) {
                MapObject temp = newMapObjects.get(i);
                newObjectLayer.getObjects().add(temp);
            }
            for (int i = 0; i < oldMapObjects.getCount(); i++) {
                MapObject temp1 = oldMapObjects.get(i);
                MapObject temp2 = new MapObject();
                float oldY = temp1.getProperties().get("y", float.class);
                temp2.getProperties().putAll(temp1.getProperties());
                temp2.getProperties().put("y", Constants.MAP_HEIGHT + oldY);
                newObjectLayer.getObjects().add(temp2);
            }
        }

        layers.add(newObjectLayer);
        return newMap;
    }

    public void loadMaps() {
        for (String mbp : mappaths) {
            mcGame.assetManager.load(folder + mbp + sub, TiledMap.class);
        }
    }

    private void initMapBlocks() {
        for (String mbp : mappaths) {
            String bot = mbp.substring(0, 1);
            String top = mbp.substring(2, 3);
            TiledMap map = mcGame.assetManager.get(folder + mbp + sub, TiledMap.class);
            //TiledMap map = new TmxMapLoader().load(folder + mbp + sub);
            int index;
            switch (bot.charAt(0)) {
                case 'A':
                    index = 0;
                    break;
                case 'B':
                    index = 1;
                    break;
                case 'C':
                    index = 2;
                    break;
                case 'D':
                    index = 3;
                    break;
                default:
                    index = 0;
            }
            blocks.get(index).add(new MapBlock(map, bot, top));
            System.out.println("New mapblock name:" + mbp + " bot connection: " + bot + " top connection: " + top);
        }
    }

    private Array<MapBlock> getMapWithBotConnection(String connection) {
        int index;
        switch (connection.charAt(0)) {
            case 'A':
                index = 0;
                break;
            case 'B':
                index = 1;
                break;
            case 'C':
                index = 2;
                break;
            case 'D':
                index = 3;
                break;
            default:
                index = 0;
        }
        return blocks.get(index);
    }

    private void createHitBoxArray() {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        mapHitBoxes = new Array<Rectangle>();
        //iterrate through map
        for (int x = 0; x < mapLayer.getWidth(); x++) {
            for (int y = 0; y < mapLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
                TiledMapTile tile = cell.getTile();
                Object tmp = tile.getProperties().get("terrain");
                //Wenn tile kein eis enthält -> add to hitbox array
                if (tmp != null && tmp.toString().equals("0,0,0,0")) {
                    mapHitBoxes.add(new Rectangle(x * mapLayer.getTileWidth(), y * mapLayer.getTileHeight(), mapLayer.getTileWidth(), mapLayer.getTileHeight()));
                }
            }
        }
    }


    private TiledMap getNextBlock() {
        Array<MapBlock> tmp = getMapWithBotConnection(lastconnection);
        Random randomGenerator = new Random();
        MapBlock block = tmp.get(tmp.size - randomGenerator.nextInt(tmp.size) - 1);
        //MapBlock block = tmp.get(0);
        lastconnection = block.getConnectionTop();
        return block.getMap();
    }


    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array<Rectangle> getHitBoxes() {
        return mapHitBoxes;
    }

    public TiledMapRenderer getTiledMapRenderer() {
        return tiledMapRenderer;
    }

    public TextureMapObjectRenderer getObjectRenderer() {
        return objectRenderer;
    }

    public int getMapHeigth() {
        return (int) (((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight() * ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getTileHeight());
    }
}
