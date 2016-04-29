package de.mc.game.models;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Jonas on 28/04/2016.
 */
public class MapManager {
    private Array<MapBlock> mapBlocks = new Array<MapBlock>();
    private String lastconnection = "A";
    private TiledMap tiledMap;
    private Array<Rectangle> mapHitBoxes;
    private TiledMapRenderer tiledMapRenderer;

    public MapManager() {
        initMapBlocks();
        tiledMap = getNextBlock();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        createHitBoxArray();
    }

    public void addNextBlock() {
        tiledMap = addBlockToMap(tiledMap, getNextBlock());
        createHitBoxArray();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    private TiledMap addBlockToMap(TiledMap tm1, TiledMap tm2) {
        TiledMap newMap = new TiledMap();
        MapLayers layers = newMap.getLayers();
        int oldHeight = ((TiledMapTileLayer) tm1.getLayers().get(0)).getHeight();
        int newHeight = oldHeight + ((TiledMapTileLayer) tm2.getLayers().get(0)).getHeight();
        int tileWidth = (int) ((TiledMapTileLayer) tm1.getLayers().get(0)).getTileWidth();
        int tileHeight = (int) ((TiledMapTileLayer) tm1.getLayers().get(0)).getTileWidth();
        TiledMapTileLayer toAddMapLayer = (TiledMapTileLayer) tm2.getLayers().get(0);
        TiledMapTileLayer oldMapLayer = (TiledMapTileLayer) tm1.getLayers().get(0);
        TiledMapTileLayer newMapLayer = new TiledMapTileLayer(((TiledMapTileLayer) tm1.getLayers().get(0)).getWidth(), newHeight, tileWidth, tileHeight);

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
        return newMap;
    }

    public int getMapHeigth() {
        return (int) (((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight() * ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getTileHeight());
    }

    private void initMapBlocks() {
        String folder = "maps/";
        String mappaths[] = {"blockA_A_1", "blockA_B_1", "blockB_A_1", "blockA_C_1", "blockC_A_1", "blockA_D_1",
                "blockB_A_1", "blockB_B_1", "blockB_A_1", "blockB_C_1", "blockC_A_1", "blockB_D_1",
                "blockC_A_1", "blockC_B_1", "blockC_A_1", "blockC_C_1", "blockC_A_1", "blockC_D_1",
                "blockD_A_1", "blockD_B_1", "blockD_A_1", "blockD_C_1", "blockC_A_1", "blockD_D_1"};
        String ending = ".tmx";
        for (String mbp : mappaths) {
            String bot = mbp.substring(5, 6);
            String top = mbp.substring(7, 8);
            TiledMap map = new TmxMapLoader().load(folder + mbp + ending);
            mapBlocks.add(new MapBlock(map, bot, top));
            System.out.println("New mapblock name:" + mbp + " bot connection: " + bot + " top connection: " + top);
        }
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
                if (tmp != null && !tmp.toString().contains("0")) {
                    mapHitBoxes.add(new Rectangle(x * mapLayer.getTileWidth(), y * mapLayer.getTileHeight(), mapLayer.getTileWidth(), mapLayer.getTileHeight()));
                }
            }
        }
    }


    private Array<MapBlock> getMapWithBotConnection(String connection) {
        Array<MapBlock> result = new Array<MapBlock>();
        for (MapBlock block : mapBlocks) {
            if (block.getConnectionBottom().equals(connection)) {
                result.add(block);
            }
        }
        System.out.println("Found" + result.size + " with bot connection: " + connection);
        return result;
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
}
