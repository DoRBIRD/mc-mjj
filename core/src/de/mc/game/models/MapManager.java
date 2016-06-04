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

import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;
import de.mc.game.utils.TextureMapObjectRenderer;

/**
 * this manages the map and adds new blocks when nearing current end of the map
 */
public class MapManager {
    private static final String folder = "maps/block";
    private static final String[] mappaths =
            {"A_A_1", "A_B_1", "B_A_1", "A_C_1", "C_A_1", "A_D_1",
                    "B_A_1", "B_B_1", "B_A_1", "B_C_1", "C_A_1", "B_D_1",
                    "C_A_1", "C_B_1", "C_A_1", "C_C_1", "C_A_1", "C_D_1",
                    "D_A_1", "D_B_1", "D_A_1", "D_C_1", "C_A_1", "D_D_1"};
    private static final String sub = ".tmx";
    private static final String ICE_BERG_LAYER = "Eisberg";
    private static final String FLOOR_LAYER = "Floor";
    private static final String PICKUPS_LAYER = "PickUps";
    private static final String OBSTACLES_LAYER = "Obstacles";
    private static final String WATER_TERRAIN = "0";
    private static final String ICE_TERRAIN = "1";
    private static final String SNOW_TERRAIN = "2";
    private static final String SHIELD_TERRAIN = "3";
    private static final String RING_TERRAIN = "4";
    private static final String ICEBERG_TERRAIN = "5";
    private static final String COIN_TERRAIN = "6";
    private String lastconnection;
    private TiledMap tiledMap;
    private Array<Rectangle> mapWaterHitBoxes;
    private Array<RectTile> icebergTiles;
    private Array<RectTile> coinTiles;
    private Array<RectTile> shieldTiles;
    private Array<RectTile> ringTiles;
    private TiledMapRenderer tiledMapRenderer;
    private TextureMapObjectRenderer objectRenderer;
    private Array<Array<MapBlock>> blocks;

    /**
     * This the MapManages constructor.
     * This will create the blocks array wich is used to store sorted map blocks
     */
    public MapManager() {
        blocks = new Array<Array<MapBlock>>();
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        blocks.add(new Array<MapBlock>());
        initMapBlocks();
        resetMap();
    }

    /**
     * This loads all maps that are defined in mappaths into the games Asset manager
     */
    public static void loadMaps() {
        for (String mbp : mappaths) {
            Assets.assetManager.load(folder + mbp + sub, TiledMap.class);
        }
    }

    /**
     * This initialises the map blocks. It sorts the loaded maps into the right blocks array
     */
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

    /**
     * This returns Array with all possible blocks that start with the last used connection
     *
     * @param connection This is the last connection
     * @return Array<MapBlock>  This returns Array with all possible blocks
     */
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

    /**
     * This resets the map and makes sure it always starts with the same startblock also resets the renderer and the hitboxes
     */
    public void resetMap() {
        tiledMap = null;
        tiledMap = blocks.get(0).get(0).getMap();
        lastconnection = "A";
        updateHitboxesAndRenderer();
    }

    /**
     * This invokes the adding of the next map block and makes sure the hitboxes and the map renderer gets updated
     */
    public void addNextBlock() {
        tiledMap = addBlockToMap(tiledMap, getNextBlock());
        updateHitboxesAndRenderer();
    }

    /**
     * This gets the next mapblock by choosing one of four with fitting connection
     *
     * @return TiledMap This returns next map block
     */
    private TiledMap getNextBlock() {
        Array<MapBlock> tmp = getMapWithBotConnection(lastconnection);
        Random randomGenerator = new Random();
        MapBlock block = tmp.get(tmp.size - randomGenerator.nextInt(tmp.size) - 1);
        lastconnection = block.getConnectionTop();
        return block.getMap();
    }

    /**
     * This makes sure the hitboxes and the map renderer gets updated
     */
    private void updateHitboxesAndRenderer() {
        createAllHitBoxArrays();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.MAP_SCALING);
        objectRenderer = new TextureMapObjectRenderer(tiledMap, Constants.MAP_SCALING);
    }

    /**
     * Returns new Tiledmap from two input maps
     * wich layers are combine is defined by calling addTileCellLayers with each layer name and the two origin maps
     *
     * @param tm1 This is input map one
     * @param tm2 This is input map two
     * @return MapLayer This returns new combined layer
     */
    private TiledMap addBlockToMap(TiledMap tm1, TiledMap tm2) {
        TiledMap newMap = new TiledMap();
        MapLayers layers = newMap.getLayers();
        layers.add(addTileCellLayers(FLOOR_LAYER, tm1, tm2));
        layers.add(addTileCellLayers(PICKUPS_LAYER, tm1, tm2));
        layers.add(addTileCellLayers(OBSTACLES_LAYER, tm1, tm2));
        //layers.add(addObjectLayers(ICE_BERG_LAYER, tm1, tm2));
        return newMap;
    }

    private void createAllHitBoxArrays() {
        mapWaterHitBoxes = crateHitboxArray(FLOOR_LAYER, "-", "0,0,0,0");
        icebergTiles = getMapTilesOfType(OBSTACLES_LAYER, ICEBERG_TERRAIN, "-");
        coinTiles = getMapTilesOfType(PICKUPS_LAYER, COIN_TERRAIN, "-");
        shieldTiles = getMapTilesOfType(PICKUPS_LAYER, SHIELD_TERRAIN, "-");
        ringTiles = getMapTilesOfType(PICKUPS_LAYER, RING_TERRAIN, "-");
    }

    /**
     * Returns new Maplayer with the combine cells of the input maps
     *
     * @param layerName This defines wich layer to combine
     * @param tm1       This is input map one
     * @param tm2       This is input map two
     * @return MapLayer This returns new combined layer
     */
    private MapLayer addTileCellLayers(String layerName, TiledMap tm1, TiledMap tm2) {
        //Gets the wanted layer from the tiled map and some info of height and width
        TiledMapTileLayer toAddMapLayer = (TiledMapTileLayer) tm2.getLayers().get(layerName);
        TiledMapTileLayer oldMapLayer = (TiledMapTileLayer) tm1.getLayers().get(layerName);
        int oldHeight = oldMapLayer.getHeight();
        int newHeight = oldHeight + toAddMapLayer.getHeight();
        int tileWidth = (int) ((TiledMapTileLayer) tm1.getLayers().get(layerName)).getTileWidth();
        int tileHeight = (int) ((TiledMapTileLayer) tm1.getLayers().get(layerName)).getTileHeight();
        //creates new mapLayer and make sure it has the same name as the input layers
        TiledMapTileLayer newMapLayer = new TiledMapTileLayer(oldMapLayer.getWidth(), newHeight, tileWidth, tileHeight);
        newMapLayer.setName(layerName);
        //itterates through new map layer and adds cells from both tilemaps
        for (int x = 0; x < toAddMapLayer.getWidth(); x++) {
            for (int y = 0; y < newHeight; y++) {
                TiledMapTileLayer.Cell cell;
                if (y < oldHeight)
                    cell = oldMapLayer.getCell(x, y);
                else
                    cell = toAddMapLayer.getCell(x, y - oldHeight);
                newMapLayer.setCell(x, y, cell);
            }
        }
        return newMapLayer;
    }

    /**
     * Returns new MapObjectLayer with the combine mapobjects of the input maps
     * currently not in use since we use the normal tile laysers at the moment
     *
     * @param objectLayerName This defines wich objectlayer to combine
     * @param tm1             This is input map one
     * @param tm2             This is input map two
     * @return MapLayer       This returns new combined layer
     */
    private MapLayer addObjectLayers(String objectLayerName, TiledMap tm1, TiledMap tm2) {
        //Gets the wanted layer from the tiled map and some info of height and width
        int oldHeight = ((TiledMapTileLayer) tm1.getLayers().get(0)).getHeight();
        int newHeight = oldHeight + ((TiledMapTileLayer) tm2.getLayers().get(0)).getHeight();
        //creates new mapLayer and make sure it has the same name as the input layers
        MapLayer newObjectLayer = new TiledMapTileLayer((int) Constants.MAP_WIDTH, newHeight, (int) Constants.TILE_WIDTH, (int) Constants.TILE_HEIGHT);
        newObjectLayer.setName(objectLayerName);

        MapLayer oldObjectLayer = tm1.getLayers().get(objectLayerName);
        MapLayer toAddObjectLayer = tm2.getLayers().get(objectLayerName);
        if (oldObjectLayer != null && toAddObjectLayer != null) {
            //gets all mapobjects
            MapObjects oldMapObjects = oldObjectLayer.getObjects();
            MapObjects newMapObjects = toAddObjectLayer.getObjects();
            //adds old objects without offset
            for (MapObject mapObject : oldMapObjects) {
                newObjectLayer.getObjects().add(mapObject);
            }
            //adds new objects with offset
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


    /**
     * Returns new Array with rectangles of the cells with Terrain described in "contains" and "equals".
     * looks in the main tiled map for defined layer name then checks each sell for terrain if it is equals or contains the given parameters
     *
     * @param layerName This defines wich object layer to combine
     * @param contains  This is used to check if the terrain contains gien string
     * @param equals    This is used to check if the terrain equals gien string
     * @return Array<Rectangle> This returns Hitboxes of defined cells
     */
    private Array<Rectangle> crateHitboxArray(String layerName, String contains, String equals) {
        Array<Rectangle> hitBoxes = new Array<Rectangle>();
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
        //iterates through map
        for (int x = 0; x < mapLayer.getWidth(); x++) {
            for (int y = 0; y < mapLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
                if (cell != null) { //cells wit (0,0,0,0) seem to return null pointer
                    TiledMapTile tile = cell.getTile();
                    Object tmp = tile.getProperties().get("terrain");
                    //checsks if cell has wanted terrain
                    if (tmp != null && (tmp.toString().equals(equals) || tmp.toString().contains(contains)))
                        hitBoxes.add(new Rectangle(x * Constants.TILE_WIDTH, y * Constants.TILE_WIDTH, Constants.TILE_WIDTH, Constants.TILE_HEIGHT));
                }
            }
        }
        return hitBoxes;
    }


    /**
     * This returns Array with maps wich map rect to the respective tile of the cells with Terrain described in "contains" and "equals".
     * looks in the main tiled map for defined layer name then checks each sell for terrain if it is equals or contains the given parameters
     *
     * @param layerName This defines wich objectlayer to combine
     * @param contains  This is input map one
     * @param equals    This is input map two
     * @return Array<RectTile>  This returns Array with maps wich map rect to the respective tile
     */
    private Array<RectTile> getMapTilesOfType(String layerName, String contains, String equals) {
        Array<RectTile> rectTiles = new Array<RectTile>();
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
        for (int x = 0; x < mapLayer.getWidth(); x++) {
            for (int y = 0; y < mapLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
                if (cell != null) {
                    TiledMapTile tile = cell.getTile();
                    if (tile != null) {
                        Object terrain = tile.getProperties().get("terrain");
                        if (terrain != null && (terrain.toString().equals(equals) || terrain.toString().contains(contains))) {
                            RectTile rectTile = new RectTile(new Rectangle(x * Constants.TILE_WIDTH, y * Constants.TILE_WIDTH, Constants.TILE_WIDTH, Constants.TILE_HEIGHT),
                                    tile,
                                    x,
                                    y);
                            rectTiles.add(rectTile);
                        }
                    }
                }
            }
        }
        return rectTiles;
    }


    /**
     * Checks for overlap with coin tiles
     *
     * @param playerHitbox This is the players hitboxes, used to check for overlaps with coins hitboxes
     * @return boolean      This returns true if there was a overlap with a coin
     */
    public boolean checkCollisionIcebergs(Rectangle playerHitbox) {
        return checkCollisionWith(playerHitbox, icebergTiles, OBSTACLES_LAYER);
    }
    /**
     * Checks for overlap with coin tiles
     *
     * @param playerHitbox This is the players hitboxes, used to check for overlaps with coins hitboxes
     * @return boolean      This returns true if there was a overlap with a coin
     */
    public boolean checkCollisionCoins(Rectangle playerHitbox) {
        return checkCollisionWith(playerHitbox, coinTiles, PICKUPS_LAYER);
    }

    /**
     * Checks for overlap with ring tiles
     *
     * @param playerHitbox This is the players hitboxes, used to check for overlaps with coins hitboxes
     * @return boolean      This returns true if there was a overlap with a coin
     */
    public boolean checkCollisionShields(Rectangle playerHitbox) {
        return checkCollisionWith(playerHitbox, shieldTiles, PICKUPS_LAYER);
    }

    /**
     * Checks for overlap with ring tiles
     *
     * @param playerHitbox This is the players hitboxes, used to check for overlaps with coins hitboxes
     * @return boolean      This returns true if there was a overlap with a coin
     */
    public boolean checkCollisionRings(Rectangle playerHitbox) {
        return checkCollisionWith(playerHitbox, ringTiles, PICKUPS_LAYER);
    }

    /**
     * Checks for overlap with tiles then clears the tiles
     *
     * @param playerHitbox This is the players hitboxes, used to check for overlaps with coins hitboxes
     * @param tiles         This are the tiles to test overlap with
     * @param layername     This is the layer where the tiles are from
     * @return boolean      This returns true if there was a overlap with a coin
     */
    private boolean checkCollisionWith(Rectangle playerHitbox, Array<RectTile> tiles, String layername) {
        for (RectTile rectTile : tiles) {
            Rectangle rect = rectTile.getRect();
            if (rect.overlaps(playerHitbox)) {
                TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layername);
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                mapLayer.setCell(rectTile.getPosX(), rectTile.getPosY(), cell);
                tiles.removeIndex(tiles.indexOf(rectTile, true));
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for overlap with icebergs obejcts
     * current doesn't work map objects don't seem to be rectange objects somehow
     *
     * @param playerHitbox This is the players hitboxes, used to check for overlaps with coins hitboxes
     */
    public void checkForCollisionWithIcebergs(Rectangle playerHitbox) {
        MapObjects mapObjects = tiledMap.getLayers().get(ICE_BERG_LAYER).getObjects();
        for (RectangleMapObject rectangleObject : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, playerHitbox))
                System.out.println("Crack! You hit an iceblock");
        }
    }

    public Array<Rectangle> getWaterHitBoxes() {
        return mapWaterHitBoxes;
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