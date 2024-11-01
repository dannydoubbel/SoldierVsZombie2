package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class GraveManager {

    public final int GRAVE_WIDTH = 64;
    public final int GRAVE_HEIGHT = 96;
    private final ArrayList<Grave> graves = new ArrayList<>();
    private Sprite graveFrame;


    GraveManager() {
        loadGrave();
    }

    public Sprite getGraveFrame() {
        return graveFrame;
    }

    public void addGrave(IntPosition pixelPosition, int countDown) {
        Grave graveToAdd = new Grave(countDown);
        graveToAdd.setPosition(pixelPosition);
        graves.add(graveToAdd);
    }

    public ArrayList<Grave> getGraves() {
        return graves;
    }

    void loadGrave() {
        graveFrame = new Sprite();
        Texture fullFile = new Texture(Gdx.files.internal("images/grave64x96.png"));
        graveFrame = new Sprite(fullFile, 0, 0, GRAVE_WIDTH, GRAVE_HEIGHT);
    }
}
