package io.github.some_example_name;

public class ViewParameters {
    private static ViewParameters instance;

    public final float ZOOM_MAX_VALUE = 8f;
    public final float ZOOM_MIN_VALUE = 0.5f;
    private float zoomValue = 1;
    private int leftOffset = 0; // background tiles are drawn from the left 0 + leftOffset pixels
    private int leftMargin = 100; // walking position main figure left margin in the window
    private boolean debugScreen = false;

    public boolean isDebugScreen() {
        return debugScreen;
    }

    public void setDebugScreen(boolean debugScreen) {
        this.debugScreen = debugScreen;
    }

    public float getZoomValue() {
        return zoomValue;
    }

    public void setZoomValue(float zoomValue) {
        this.zoomValue = zoomValue;
    }

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
    private int rightMargin = 100;// walking position main figure right margin in the window (should be used as width-rightMargin)

    public void setUpWindowMargins() {
       setLeftMargin(200);
       setRightMargin(200);
    }


    public static ViewParameters getInstance() {
        if (instance == null) {
            instance = new ViewParameters();
        }
        return instance;
    }
}
