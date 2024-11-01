package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.abs;

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
    public final int TILE_GATE = 398;

    private int[][] backgroundTileMap = new int[TILE_MAP_COLS][TILE_MAP_ROWS];
    private Sprite[] sourceBackgroundTiles;
    public final Set<Integer> WALKABLE_TILES = new HashSet<>(Arrays.asList(TILE_NOTHING, TILE_GATE));


    private final int[] tileMap = {70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 70, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 0, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 0, 0, 70, 0, 70, 0, 0, 70, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 70, 70, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 0, 70, 0, 0, 0, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 70, 70, 70, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 70, 70, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 70, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 70, 70, 70, 70, 70, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 70, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 0, 0, 0, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 70, 70, 70, 0, 70, 70, 70, 0, 0, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70};

    Tiles(){
        loadTiles();
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
        if (XPosTile >= TILE_MAP_COLS || YPosTile >= TILE_MAP_ROWS) return false;
        return WALKABLE_TILES.contains(getBackgroundTileMap()[XPosTile][YPosTile]);
    }
    public boolean isTileWalkable(IntPosition tilePos) {
        if (tilePos.getX() < 0 || tilePos.getY() < 0) return false;
        if ( tilePos.getX() >= TILE_MAP_COLS || tilePos.getY() >= TILE_MAP_ROWS) return false;
        return WALKABLE_TILES.contains(getBackgroundTileMap()[tilePos.getX()][tilePos.getY()]);
    }

    public static IntPosition getRandomWalkablePositionAround(IntPosition start, int minSteps,int maxSteps) {
        Random random = new Random();
        int xPlayer = start.getX();
        int yPlayer = start.getY();

        IntPosition newPosition;
        int maxAttempts = 100;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            // Randomize positive/negative offset around player
            int randomX = abs(xPlayer + (random.nextBoolean() ? 1 : -1) * (minSteps + random.nextInt(maxSteps - minSteps)));
            int randomY = abs(yPlayer + (random.nextBoolean() ? 1 : -1) * (minSteps + random.nextInt(maxSteps - minSteps)));
                newPosition = new IntPosition(randomX, randomY);
                //System.out.println("Testing position : " + newPosition);
                if (instance.isTileWalkable(randomX, randomY)) {
                    return newPosition;
                }

        }
        System.out.println("No walkable spot found within attempts");
        return start;
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
    public static Tiles getInstance() {
        if (instance == null) {
            instance = new Tiles();
        }
        return instance;
    }
}
