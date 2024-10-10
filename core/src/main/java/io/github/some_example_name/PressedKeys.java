package io.github.some_example_name;

public class PressedKeys {
    private static PressedKeys instance;
    boolean goLeft = false;
    boolean goRight = false;
    boolean goUp = false;
    boolean goDown = false;
    boolean zoomIn = false;
    boolean zoomOut = false;


    public static PressedKeys getInstance() {
        if (instance == null) {
            instance = new PressedKeys();
        }
        return instance;
    }
}
