package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class ZombieManager {
    public final int ZOMBIE_WIDTH = 256 / 4;
    public final int ZOMBIE_HEIGHT = 384 / 4;
    private final int ZOMBIE_ROWS_IN_FILE = 4;
    private final int ZOMBIE_COLS_IN_FILE = 4;
    private final ArrayList<Zombie> zombies = new ArrayList<>();
    private Sprite[][] zombieFrames;

    ZombieManager() {
        loadZombieFrames();
    }

    public void addZombie(IntPosition pixelPosition,IntPosition tilePosition) {
        Zombie zombieToAdd = new Zombie(pixelPosition,ZOMBIE_COLS_IN_FILE);
        zombieToAdd.setTargetTilePosition(tilePosition);
        zombies.add(zombieToAdd);
        //System.out.println("Addzombie targettile " + tilePosition);
    }

    public Sprite getZombieFrame(Directions direction, int index) {
        return zombieFrames[direction.getValue() - 1][index];
    }

    public ArrayList<Zombie> getZombies() {
        return zombies;
    }

    void loadZombieFrames() {
        zombieFrames = new Sprite[ZOMBIE_ROWS_IN_FILE][ZOMBIE_COLS_IN_FILE];
        Texture fullFile = new Texture(Gdx.files.internal("images/zombie256x384DnUpLtRt.png"));
        for (int row = 0; row < ZOMBIE_ROWS_IN_FILE; row++) {
            for (int col = 0; col < ZOMBIE_COLS_IN_FILE; col++) {
                // the image is here, transfer writeable image to image
                zombieFrames[row][col] = new Sprite(fullFile,
                    col * ZOMBIE_WIDTH,
                    row * ZOMBIE_HEIGHT,
                    ZOMBIE_WIDTH,
                    ZOMBIE_HEIGHT);
            }
        }
    }
}
