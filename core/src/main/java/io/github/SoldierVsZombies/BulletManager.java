package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class BulletManager {

    public final int BULLET_WIDTH = 64;
    public final int BULLET_HEIGHT = 64;

    public final int HALF_BULLET_WIDTH = 64 / 2;
    public final int HALF_BULLET_HEIGHT = 64 / 2;

    private final ArrayList<Bullet> bullets = new ArrayList<>();

    private Sprite[] bulletFrames;
    private long lastTimeStampBulletFired;

    BulletManager() {
        loadBulletFrames();
        lastTimeStampBulletFired = System.currentTimeMillis();
    }


    public void addBullet(IntPosition position, Directions direction, int stepSize) {
        lastTimeStampBulletFired = System.currentTimeMillis();
        Bullet bulletToAdd = new Bullet(position.clone(), direction, stepSize);
        bullets.add(bulletToAdd);
    }

    public Sprite getBulletFrame(int index) {
        return bulletFrames[index];
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void handleBulletMovement() {
        for (Bullet bullet : getBullets()) {
            bullet.move();
        }
    }


    void loadBulletFrames() {
        final int TILE_SOURCE_COLS = 4;
        bulletFrames = new Sprite[TILE_SOURCE_COLS];
        Texture fullFile = new Texture(Gdx.files.internal("images/bullets64x64DnUpLtRt.png"));
        for (int col = 0; col < TILE_SOURCE_COLS; col++) {
            bulletFrames[col] = new Sprite(fullFile,
                col * BULLET_WIDTH, 0, BULLET_WIDTH, BULLET_HEIGHT);
        }
    }

    public long getElapsedTimeSinceLastBulletFired() {
        return System.currentTimeMillis() - lastTimeStampBulletFired;
    }
}

