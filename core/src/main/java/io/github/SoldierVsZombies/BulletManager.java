package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class BulletManager {

    private final int WAIT_CYCLES = 25;

    private int waitCylesToAddBullets=0;
    private Sprite[] bulletFrames;

    private final ArrayList<Bullet> bullets = new ArrayList<>();

    BulletManager() {
        loadBulletFrames();
    }

    public void addBullet(IntPosition position,Directions direction,int stepSize) {
        waitCylesToAddBullets--;
        waitCylesToAddBullets = Math.max(waitCylesToAddBullets, 0);
        if (waitCylesToAddBullets==0) {
            Bullet bulletToAdd = new Bullet(position.clone(), direction, stepSize);
            bullets.add(bulletToAdd);
            waitCylesToAddBullets = WAIT_CYCLES;
        }
    }

    public Sprite getBulletFrame(int index) {
        return bulletFrames[index];
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void removeBullet(Bullet bulletToRemove) {
        bullets.remove(bulletToRemove);
    }

    void loadBulletFrames() {

        final int TILE_SOURCE_COLS = 4;

        bulletFrames = new Sprite[TILE_SOURCE_COLS];
        Texture fullFile = new Texture(Gdx.files.internal("images/bullets64x64DnUpLtRt.png"));
        for (int col = 0; col < TILE_SOURCE_COLS; col++) {
            // the image is here, transfer writeable image to image
            bulletFrames[col] = new Sprite(fullFile,
                col*64, 0, 64, 64);
        }
    }
}

