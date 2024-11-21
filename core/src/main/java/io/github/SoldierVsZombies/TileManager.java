package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TileManager {
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int HALF_TILE_WIDTH = TILE_WIDTH / 2;
    public static final int HALF_TILE_HEIGHT = TILE_HEIGHT / 2;
    public static final int TILE_MAP_COLS = 100;
    public static final int TILE_MAP_ROWS = 50;
    public static final int TILE_MAP_SCALE_FACTOR = 2;
    private static TileManager instance;

    public static final int TILE_NOTHING = 0;
    public static final int TILE_PORTAL_OPEN = 398;
    public static final int TILE_PORTAL_CLOSE = 397;
    public static final int TILE_WATER = 237;
    public static final int TILE_LAVA = 7;

    public final Set<Integer> HURTING_TILES_PLAYER = new HashSet<>(Arrays.asList(TILE_WATER, TILE_LAVA)); // TODO ADD WATER AND LAVA
    public final Set<Integer> WALKABLE_TILES_PLAYER = new HashSet<>(Arrays.asList(TILE_NOTHING, TILE_PORTAL_OPEN));

    public final Set<Integer> WALKABLE_TILES_ENEMY = new HashSet<>(Arrays.asList(TILE_NOTHING/*, TILE_PORTAL_OPEN*/));

    private int[][] backgroundTileMap;
    private Sprite[] sourceBackgroundTiles;


    TileManager() {
        WALKABLE_TILES_PLAYER.addAll(HURTING_TILES_PLAYER);
        loadTiles();
    }

    public boolean isPlayerHurtingTile(IntPosition tilePosition) {
        return HURTING_TILES_PLAYER.contains(backgroundTileMap[tilePosition.getX()][tilePosition.getY()]);
    }

    public static int getRandomInt(int minimum, int maximum) {
        Random random = new Random();
        int absoluteValue = random.nextInt(minimum, maximum + 1);
        return random.nextBoolean() ? absoluteValue : -absoluteValue;
    }

    public IntPosition getRandomWalkablePositionAround(IntPosition start, int minStepsX, int minStepsY, int maxSteps, Set<Integer> walkableTiles) {
        IntPosition newPosition;
        int maxAttempts = 500;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = getRandomInt(minStepsX, maxSteps);
            int y = getRandomInt(minStepsY, maxSteps);
            int newX = start.getX() + x;
            int newY = start.getY() + y;

            newPosition = new IntPosition(newX, newY);
            if (isTileWalkable(newPosition, walkableTiles)) {
                return newPosition;
            }

        }
        return start;
    }

    public IntPosition getRandomWalkablePosition(Set<Integer> walkableTiles) {
        IntPosition newPosition;
        int watchdog = 500;
        for (int attempt = 0; attempt < watchdog; attempt++) {
            int x = getRandomInt(0, TILE_MAP_COLS - 1);
            int y = getRandomInt(0, TILE_MAP_ROWS - 1);
            newPosition = new IntPosition(x, y);
            if (isTileWalkable(newPosition, walkableTiles)) {
                return newPosition;
            }
        }
        System.out.println("Watchdog getRandomWalkablePosition ");
        return new IntPosition(Legal.NOT_LEGAL);
    }

    public int getRandomColNumber() {
        Random random = new Random();
        return random.nextInt(TileManager.TILE_MAP_COLS + 1);
    }

    public int getRandomRowNumber() {
        Random random = new Random();
        return random.nextInt(TileManager.TILE_MAP_ROWS + 1);
    }


    public static TileManager getInstance() {
        if (instance == null) {
            instance = new TileManager();
        }
        return instance;
    }

    public int[][] loadBackgroundTileMap() {
        TileMapJSONReader tileMapJSONReader = new TileMapJSONReader();
        return tileMapJSONReader.getTileMapArray("tileMap/versie2saveas.json");
    }

    Sprite[] loadBackgroundTextures() {

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

    public boolean isTileNr(IntPosition intPosition, int tileNr){
        return getTileNr(intPosition) == tileNr;
    }

    public int getTileNr(IntPosition intPosition){
        return backgroundTileMap[intPosition.getX()][intPosition.getY()];
    }


    public boolean isTileWalkable(int XPosTile, int YPosTile, Set<Integer> walkableTiles) {
        if (new IntPosition(XPosTile, YPosTile).equals(new IntPosition(Legal.NOT_LEGAL))) return false;
        if (YPosTile < 0) {
            System.out.println("OLA, dat was minder dan 0 voor Y");
            return false;
        }
        if (XPosTile >= TILE_MAP_COLS || YPosTile >= TILE_MAP_ROWS)
            return false;

        return walkableTiles.contains(backgroundTileMap[XPosTile][YPosTile]); // todo range check uitvoeren
    }

    public boolean isTileWalkable(IntPosition tilePos, Set<Integer> walkableTiles) {
        if (tilePos.isLegalIntPosition().equals(Legal.NOT_LEGAL)) return false;
        if (tilePos.getX() >= TILE_MAP_COLS || tilePos.getY() >= TILE_MAP_ROWS)
            return false;
        return isTileWalkable(tilePos.getX(), tilePos.getY(), walkableTiles);
    }

    public Sprite[] getSourceBackgroundTiles() {

        return sourceBackgroundTiles;
    }

    public int getBackGroundTileNumber(int col, int row) {
        return backgroundTileMap[col][row];
    }


    private void loadTiles() {
        sourceBackgroundTiles = loadBackgroundTextures();
        backgroundTileMap = loadBackgroundTileMap();
    }

    public void setTile(IntPosition tilePos, int tileSetNumber) {
        if (tilePos.isLegalIntPosition().equals(Legal.NOT_LEGAL)) return;
        backgroundTileMap[tilePos.getX()][tilePos.getY()] = tileSetNumber;
    }

}
