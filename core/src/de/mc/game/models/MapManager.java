package de.mc.game.models;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import de.mc.game.Assets;
import de.mc.game.Constants;
import de.mc.game.TextureMapObjectRenderer;

public class MapManager {
    private String lastconnection;
    private TiledMap tiledMap;
    private Array<Rectangle> mapWaterHitBoxes;
    private Array<Rectangle> mapSnowHitBoxes;
    private TiledMapRenderer tiledMapRenderer;

    private TextureMapObjectRenderer objectRenderer;
    private Array<Array<MapBlock>> blocks;

    private static final String folder = "maps/block";
    private static final String[] mappaths =
            {"A_A_1", "A_B_1", "B_A_1", "A_C_1", "C_A_1", "A_D_1",
            "B_A_1", "B_B_1", "B_A_1", "B_C_1", "C_A_1", "B_D_1",
            "C_A_1", "C_B_1", "C_A_1", "C_C_1", "C_A_1", "C_D_1",
            "D_A_1", "D_B_1", "D_A_1", "D_C_1", "C_A_1", "D_D_1"};
    private static final String sub = ".tmx";
    private static final String ICE_BERGLAYER = "Eisberg";
    private static final String FLOOR_LAYER = "Floor";
    private static final String PICKUPS_LAYER = "PickUps";
    private static final String OBSTACLES_LAYER = "Obstacles";

    public MapManager() {
        blocks = new Array<Array<MapBlock>>();
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        initMapBlocks();
        resetMap();
    }

    public void resetMap() {
        tiledMap = null;
        tiledMap = blocks.get(0).get(0).getMap();
        lastconnection = "A";
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.MAP_SCALING);
        objectRenderer = new TextureMapObjectRenderer(tiledMap, Constants.MAP_SCALING);
        createAllHitBoxArrays();
    }

    public void addNextBlock() {
        tiledMap = addBlockToMap(tiledMap, getNextBlock());
        createAllHitBoxArrays();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.MAP_SCALING);
        objectRenderer = new TextureMapObjectRenderer(tiledMap, Constants.MAP_SCALING);
    }

    private TiledMap addBlockToMap(TiledMap tm1, TiledMap tm2) {
        TiledMap newMap = new TiledMap();
        MapLayers layers = newMap.getLayers();
        layers.add(addTileCellLayers(FLOOR_LAYER,tm1, tm2));
        layers.add(addTileCellLayers(PICKUPS_LAYER,tm1, tm2));
        layers.add(addTileCellLayers(OBSTACLES_LAYER,tm1, tm2));
        //layers.add(addObjectLayers(ICE_BERGLAYER, tm1, tm2));
        return newMap;
    }

    private MapLayer addTileCellLayers(String layerName,TiledMap tm1, TiledMap tm2) {
        TiledMapTileLayer toAddMapLayer = (TiledMapTileLayer) tm2.getLayers().get(layerName);
        TiledMapTileLayer oldMapLayer = (TiledMapTileLayer) tm1.getLayers().get(layerName);
        int oldHeight = oldMapLayer.getHeight();
        int newHeight = oldHeight + toAddMapLayer.getHeight();
        int tileWidth = (int) ((TiledMapTileLayer) tm1.getLayers().get(layerName)).getTileWidth();
        int tileHeight = (int) ((TiledMapTileLayer) tm1.getLayers().get(layerName)).getTileHeight();
        TiledMapTileLayer newMapLayer = new TiledMapTileLayer(oldMapLayer.getWidth(), newHeight, tileWidth, tileHeight);
        newMapLayer.setName(layerName);
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
        return newMapLayer;
    }

    private MapLayer addObjectLayers(String objectLayerName, TiledMap tm1, TiledMap tm2) {
        int oldHeight = ((TiledMapTileLayer) tm1.getLayers().get(0)).getHeight();
        int newHeight = oldHeight + ((TiledMapTileLayer) tm2.getLayers().get(0)).getHeight();
        MapLayer newObjectLayer = new TiledMapTileLayer((int) Constants.MAP_WIDTH, newHeight, (int) Constants.TILE_WIDTH, (int) Constants.TILE_HEIGHT);
        newObjectLayer.setName(objectLayerName);

        MapLayer oldObjectLayer = tm1.getLayers().get(objectLayerName);
        MapLayer toAddObjectLayer = tm2.getLayers().get(objectLayerName);
        if (oldObjectLayer != null && toAddObjectLayer != null) {
            MapObjects oldMapObjects = oldObjectLayer.getObjects();
            MapObjects newMapObjects = toAddObjectLayer.getObjects();
            for (MapObject mapObject : oldMapObjects) {
                newObjectLayer.getObjects().add(mapObject);
            }
            float offsetY = (oldHeight) * Constants.TILE_HEIGHT;
            for (MapObject mapObject : newMapObjects) {
                if (mapObject instanceof TextureMapObject) {
                    TextureMapObject textureObject = (TextureMapObject) mapObject;
                    float oldY = textureObject.getY();
                    float newY = oldY + offsetY;
                    textureObject.setY(newY);
                }
                newObjectLayer.getObjects().add(mapObject);
            }
        }
        return newObjectLayer;
    }

    private void updateScaling(TextureMapObject textureObject) {
        float newY = textureObject.getY() * Constants.MAP_SCALING;
        textureObject.setY(newY);
        float newX = textureObject.getX() * Constants.MAP_SCALING;
        textureObject.setX(newX);
        float newWidth =Constants.MAP_SCALING;
        textureObject.setScaleX(newWidth);
        float newHeigth =  Constants.MAP_SCALING;
        textureObject.setScaleY(newHeigth);
    }

    public static void loadMaps() {
        for (String mbp : mappaths) {
            Assets.assetManager.load(folder + mbp + sub, TiledMap.class);
        }
    }

    private void initMapBlocks() {
        for (String mbp : mappaths) {
            String bot = mbp.substring(0, 1);
            String top = mbp.substring(2, 3);
            TiledMap map = Assets.assetManager.get(folder + mbp + sub, TiledMap.class);
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

    private void createAllHitBoxArrays() {
        mapWaterHitBoxes = crateHitboxArray(FLOOR_LAYER,"0,0,0,0","");
        //mapSnowHitBoxes = crateHitboxArray(FLOOR_LAYER,"-","2");
    }

    private Array<Rectangle> crateHitboxArray(String layerName,String contains, String equals){
        Array<Rectangle> hitBoxes = new Array<Rectangle>();
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
        for (int x = 0; x < mapLayer.getWidth(); x++) {
            for (int y = 0; y < mapLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
                TiledMapTile tile = cell.getTile();
                Object tmp = tile.getProperties().get("terrain");
                if (tmp != null && (tmp.toString().equals(equals) || tmp.toString().contains(contains)))
                    hitBoxes.add(new Rectangle(x * Constants.TILE_WIDTH, y * Constants.TILE_WIDTH, Constants.TILE_WIDTH, Constants.TILE_HEIGHT));
            }
        }
        return hitBoxes;
    }


    public void checkForCollisionWithIcebergs(Rectangle playerHitbox) {
        //something like this
        MapObjects mapObjects = tiledMap.getLayers().get(ICE_BERGLAYER).getObjects();
        for (RectangleMapObject rectangleObject : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, playerHitbox))
                System.out.println("Crack! You hit an iceblock");
        }
    }

    private TiledMap getNextBlock() {
        Array<MapBlock> tmp = getMapWithBotConnection(lastconnection);
        Random randomGenerator = new Random();
        MapBlock block = tmp.get(tmp.size - randomGenerator.nextInt(tmp.size) - 1);
        lastconnection = block.getConnectionTop();
        return block.getMap();
    }

    public Array<Rectangle> getWaterHitBoxes() {
        return mapWaterHitBoxes;
    }

    public Array<Rectangle> getSnowHitBoxes() {
        return mapSnowHitBoxes;
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
