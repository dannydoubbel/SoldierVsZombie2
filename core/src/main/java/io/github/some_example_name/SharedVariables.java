package io.github.some_example_name;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SharedVariables {
    private static SharedVariables instance;
    public final float ZOOM_MAX_VALUE = 8f;
    public final float ZOOM_MIN_VALUE = 0.5f;

    public final int STEP_SIZE = 5;

    public final int TILE_NOTHING = 0;
    public final int TILE_GATE = 398;
    public final Set<Integer> WALKABLE_TILES = new HashSet<>(Arrays.asList(TILE_NOTHING,TILE_GATE));
    boolean goLeft = false;
    boolean goRight = false;
    boolean goUp = false;
    boolean goDown = false;
    boolean zoomIn = false;
    boolean zoomOut = false;

    private Directions currentSolderDirection = Directions.dn;
    private boolean debugScreen = false;

    private float zoomValue = 1;

    private int leftOffset = 0; // background tiles are drawn from the left 0 + leftOffset pixels
    private int leftMargin = 100; // walking position main figure left margin in the window
    private int rightMargin = 100;// walking position main figure right margin in the window (should be used as width-rightMargin)
    private int main_xpos = 1000;// xpos in pixels starting from leftOffset



    private int textureIndexSoldier = 0;

    public int getLeftOffset() {
        return leftOffset;
    }

    public void setLeftOffset(int leftOffset) {
        this.leftOffset = leftOffset;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

    public int getMain_xpos() {
        return main_xpos;
    }

    public void setMain_xpos(int main_xpos) {
        this.main_xpos = main_xpos;
    }

    public void setDebugScreen(boolean debugScreen) {
        this.debugScreen = debugScreen;
    }

    private int tileMap[] = {
        70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
        70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 70, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 0, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 0, 0, 70, 0, 70, 0, 0, 70, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 70, 70, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 0, 70, 0, 0, 0, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 70, 70, 70, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 70, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 70, 70, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 0, 0, 0, 70, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 70, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 70, 70, 70, 0, 70, 70, 0, 70, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 70, 70, 0, 70, 70, 70, 0, 70, 0, 70, 70, 70, 0, 70, 70, 70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 0, 70, 70, 70, 70, 70, 70, 0, 0, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 70, 70, 70, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 0, 0, 0, 70, 0, 0, 0, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 70, 70, 70, 0, 70, 70, 70, 0, 0, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 70, 0, 0, 0, 70, 0, 0, 0, 70, 0, 0, 0, 70, 70, 0, 0, 0, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 70, 0, 70, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 70, 0, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 70, 0, 0, 70, 70, 0, 70, 0, 70, 0, 70, 0, 70, 70, 70, 70, 70, 70, 0, 70, 0, 70, 0, 70, 70, 70, 0, 70, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70,
        70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 398, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70
    };


    private SharedVariables() {
    }
    public static SharedVariables getInstance() {
        if (instance == null) {
            instance = new SharedVariables();
        }
        return instance;
    }


    public int getTextureIndexSoldier() {
        return textureIndexSoldier;
    }

    public void setTextureIndexSoldier(int textureIndexSoldier) {
        this.textureIndexSoldier = textureIndexSoldier;
    }

    Directions getCurrentSolderDirection() {
        return currentSolderDirection;
    }

    void setCurrentSolderDirection(Directions newDirection) {
        currentSolderDirection = newDirection;
    }

    float getzoomValue() {
        return zoomValue;
    }

    void setzoomValue(float newZoomValue) {
        zoomValue = newZoomValue;
    }


    public int[] getTileMap() {
        return tileMap;
    }

    public boolean isZoomIn() {
        return zoomIn;
    }

    public void setZoomIn(boolean zoomIn) {
        this.zoomIn = zoomIn;
    }

    public boolean isZoomOut() {
        return zoomOut;
    }

    public void setZoomOut(boolean zoomOut) {
        this.zoomOut = zoomOut;
    }



    public boolean isDebugScreen() {
        return debugScreen;
    }
}
