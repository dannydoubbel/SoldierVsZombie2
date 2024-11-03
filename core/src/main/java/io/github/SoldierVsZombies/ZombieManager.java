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

    private final CollisionDetector collisionDetector=CollisionDetector.getInstance();


    ZombieManager() {
        loadZombieFrames();
    }

    public void addZombie(IntPosition pixelPosition,IntPosition tilePosition) {
        Zombie zombieToAdd = new Zombie(pixelPosition,ZOMBIE_COLS_IN_FILE,10000);
        zombieToAdd.setTargetTilePosition(tilePosition);
        zombies.add(zombieToAdd);
    }

    public boolean isTargetTileOccupiedByOtherZombie(Zombie currentZombie, IntPosition targetTile) {
        for (Zombie zombie : getZombies()) {
            if (!zombie.equals(currentZombie) && targetTile.equals(zombie.getTargetTilePosition())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Zombie> getCollidingZombiesWithPixelPos(IntPosition pixelPosToCollideWith) {
        ArrayList<Zombie> collidingZombies = new ArrayList<>();
        for (Zombie zombie : getZombies()) {
            if (isZombieCollidingWithPixelPos(zombie,pixelPosToCollideWith)) {
                collidingZombies.add(zombie);
            }
        }
        return collidingZombies;
    }

    private boolean isZombieCollidingWithPixelPos(Zombie zombie, IntPosition pixelPosToCollideWith) {
        return collisionDetector.isColliding(pixelPosToCollideWith, zombie.getPosition());
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

    public ArrayList<Zombie> getZombiesLifeTimeExpired() {
        ArrayList<Zombie> zombiesToDieOfOldAge = new ArrayList<>();
        for (Zombie zombie : zombies) {
            if (zombie.isLifeTimeExpired()) {
                zombiesToDieOfOldAge.add(zombie);
            }
        }
        return zombiesToDieOfOldAge;
    }

    public void remove(Zombie zombie) {
        zombies.remove(zombie);
    }
}
