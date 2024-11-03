package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Tiles {
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int HALF_TILE_WIDTH = TILE_WIDTH / 2;
    public static final int HALF_TILE_HEIGHT = TILE_HEIGHT / 2;
    public static final int TILE_MAP_COLS = 100;
    public static final int TILE_MAP_ROWS = 20;
    public static final int TILE_MAP_SCALE_FACTOR = 2;
    private static Tiles instance;

    public final int TILE_NOTHING = 0;
    public final int TILE_PORTAL_OPEN = 398;

    public final int TILE_PORTAL_CLOSE = 397; // TODO implement this
    public final Set<Integer> WALKABLE_TILES = new HashSet<>(Arrays.asList(TILE_NOTHING, TILE_PORTAL_OPEN));
    private final int[] tileMap = {397, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70,
        70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 70,
        70, 0, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 70, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 0, 70, 70, 0, 70, 70, 70, 70, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 70, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 70, 0, 70, 0, 70, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 70, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70,
        70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 0, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 0, 70, 0, 70, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 0, 70, 70, 0, 70,
        70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 70, 70, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 70, 70, 0, 0, 70, 0, 70, 0, 0, 0, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 0, 0, 0, 70, 70, 70, 70, 0, 0, 0, 70, 0, 70, 0, 70, 70, 70, 70, 70, 0, 70, 0, 70, 0, 0, 70, 0, 0, 70,
        70, 0, 0, 0, 70, 0, 70, 0, 0, 70, 70, 0, 70, 70, 0, 0, 0, 0, 70, 0, 0, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 70, 70, 0, 70, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 70, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 0, 70, 70,
        70, 70, 70, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 0, 70, 0, 0, 0, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 70, 70, 0, 70, 70, 0, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 70, 70, 0, 70, 70, 70, 70, 70, 70, 70, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 70, 70,
        70, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 70, 70, 70, 70, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 70, 70, 70, 0, 0, 70, 0, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 0, 70, 70, 70, 70, 0, 70, 70,
        70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 70, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 70, 70, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 0, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 70, 0, 70, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70,
        70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 70, 70, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 70,
        70, 0, 0, 0, 0, 70, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 70, 70,
        70, 70, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 70, 70, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 0, 70, 70, 0, 0, 70, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 70, 0, 70, 0, 0, 70, 70,
        70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 70, 70, 0, 0, 70, 0, 0, 0, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 0, 70, 70, 0, 0, 70,
        70, 0, 70, 0, 70, 70, 70, 70, 70, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 70, 70, 0, 70, 70, 0, 0, 70, 70, 0, 70, 70, 0, 70, 0, 0, 70, 0, 0, 70, 0, 0, 0, 0, 70, 70, 70, 70, 70, 0, 0, 0, 70, 70, 70, 70, 0, 70, 70, 70, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 0, 70, 0, 0, 0, 70, 0, 70,
        70, 0, 0, 0, 0, 70, 0, 0, 0, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 70, 70, 70, 0, 70, 70, 70, 70, 0, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 70, 0, 70, 0, 70, 70, 0, 0, 0, 70, 0, 70, 70, 70, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70,
        70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70,
        70, 0, 70, 0, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 70, 0, 70,
        70, 0, 0, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 70,
        70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70};
    private int[][] backgroundTileMap = new int[TILE_MAP_COLS][TILE_MAP_ROWS];
    private Sprite[] sourceBackgroundTiles;


    Tiles() {
        loadTiles();
    }


    public static int getRandomInt(int minimum, int maximum) {
        Random random = new Random();
        int absoluteValue = random.nextInt(minimum,maximum+1);
        return  random.nextBoolean() ? absoluteValue : -absoluteValue;
    }

    public  IntPosition getRandomWalkablePositionAround(IntPosition start, int minStepsX,int minStepsY, int maxSteps) {
        //System.out.println("Start pos random " + start);
            IntPosition newPosition;
            int maxAttempts = 500;

            for (int attempt = 0; attempt < maxAttempts; attempt++) {
                int x = getRandomInt(minStepsX,maxSteps);
                int y = getRandomInt(minStepsY,maxSteps);
                int newX = start.getX() + x;
                int newY = start.getY() + y;

            newPosition = new IntPosition(newX,newY);
            if (isTileWalkable(newPosition)) {
                return newPosition;
            }

        }
        System.out.println("No walkable spot found within attempts");
        return start;
    }

    public static Tiles getInstance() {
        if (instance == null) {
            instance = new Tiles();
        }
        return instance;
    }

    Sprite[] loadSourceBackgroundTiles() {
        final int TILE_SOURCE_COLS = 23;
        final int TILE_SOURCE_ROWS = 21;

        Sprite[] slicedTiles = new Sprite[TILE_SOURCE_COLS * TILE_SOURCE_COLS];
        Texture fullFile = new Texture(Gdx.files.internal("images/5z1KX.png"));
        for (int row = 0; row < TILE_SOURCE_ROWS; row++) {
            for (int col = 0; col < TILE_SOURCE_COLS; col++) {
                // the image is here, transfer writeable image to image
                slicedTiles[col + row * TILE_SOURCE_COLS] = new Sprite(fullFile, col * (TILE_WIDTH), row * (TILE_HEIGHT), TILE_WIDTH, TILE_HEIGHT);
            }
        }
        return slicedTiles;
    }



    public boolean isTileWalkable(int XPosTile, int YPosTile) {
        if (XPosTile < 0 || YPosTile < 0) return false;
        if (XPosTile >= TILE_MAP_COLS || YPosTile >= TILE_MAP_ROWS)
            return false;
        return WALKABLE_TILES.contains(getBackgroundTileMap()[XPosTile][YPosTile]);
    }

    public boolean isTileWalkable(IntPosition tilePos) {
        if (tilePos.getX() < 0 || tilePos.getY() < 0) return false;
        if (tilePos.getX() >= TILE_MAP_COLS || tilePos.getY() >= TILE_MAP_ROWS)
            return false;
        return isTileWalkable(tilePos.getX(),tilePos.getY());
    }

    public int[][] getBackgroundTileMap() {

        return backgroundTileMap;
    }

    public Sprite[] getSourceBackgroundTiles() {

        return sourceBackgroundTiles;
    }

    public int[] getTileMap() {

        return tileMap;
    }

    private void loadTiles() {
        sourceBackgroundTiles = loadSourceBackgroundTiles();
        backgroundTileMap = setupBackgroundTileMap();
    }

    public int[][] setupBackgroundTileMap() {
        int[] test = getTileMap();
        int[][] result = new int[TILE_MAP_COLS][TILE_MAP_ROWS];
        for (int row = 0; row < TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TILE_MAP_COLS; col++) {
                result[col][row] = test[col + row * TILE_MAP_COLS];
            }
        }
        return result;
    }

    public void setTile(IntPosition tilePos, int tileSetNumber) {
        backgroundTileMap[tilePos.getX()][tilePos.getY()] = tileSetNumber;
    }
}
