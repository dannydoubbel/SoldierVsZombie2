package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Collection;

public class SkullManager {


    public final int SKULL_ROWS_IN_FILE = 1;
    public final int SKULL_COLS_IN_FILE = 8;
    public final int SKULL_HEIGHT = 100/ SKULL_ROWS_IN_FILE;
    public final int SKULL_WIDTH = 800 / SKULL_COLS_IN_FILE;
    private final ArrayList<Skull> skulls = new ArrayList<>();
    private Sprite[] skullFrames;

    SkullManager() {
        loadSkullFrames();
    }

    public void addSkull(IntPosition position,int stepSize) {
        Skull skullToAdd = new Skull(position,stepSize);
        skulls.add(skullToAdd);
    }
    public void addSkulls(Collection<Skull> skullsToAdd) {
        skulls.addAll(skullsToAdd);
    }

    public Sprite getSkullFrame(int index) {
        return skullFrames[index];
    }

    public ArrayList<Skull> getSkulls() {
        return skulls;
    }

    void loadSkullFrames() {
        skullFrames = new Sprite[SKULL_COLS_IN_FILE];
        Texture fullFile = new Texture(Gdx.files.internal("images/skullSheet800x100.png"));

        for (int col = 0; col < SKULL_COLS_IN_FILE; col++) {
            // the image is here, transfer writeable image to image
            skullFrames[col] = new Sprite(fullFile,
                col * SKULL_WIDTH,
                0,
                SKULL_WIDTH,
                SKULL_HEIGHT);
        }
    }
}


