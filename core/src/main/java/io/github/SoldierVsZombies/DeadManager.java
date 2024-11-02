package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class DeadManager {

    public final int DEAD_WIDTH = 64;
    public final int DEAD_HEIGHT = 96;
    private final ArrayList<Dead> allTheDead = new ArrayList<>();
    private Sprite deadFrameZombie;
    private Sprite[] deadFrameSkull;



    DeadManager() {
        loadDeadFrames();
    }

    public Sprite getDeadFrame(Dead dead) {
        if (dead.getDeadType().equals(DeadType.DEAD_ZOMBIE)) return deadFrameZombie;
        if (dead.isBeyondHalfLifeTime()) return deadFrameSkull[1];
        return deadFrameSkull[0];
    }

    public void addDead(Dead deadToAdd) {
        allTheDead.add(deadToAdd);
    }

    public ArrayList<Dead> getAllTheDead() {
        return allTheDead;
    }

    void loadDeadFrames() {
        loadZombieDeadFrames();
        loadSkullDeadFrames();
    }
    private void loadSkullDeadFrames() {
        deadFrameSkull = new Sprite[2];
        Texture deadSkullFile1 = new Texture(Gdx.files.internal("images/skull100x100-ex1.png"));
        Texture deadSkullFile2 = new Texture(Gdx.files.internal("images/skull100x100-ex2.png"));
        deadFrameSkull[0] = new Sprite(deadSkullFile1,0,0,DEAD_WIDTH,DEAD_HEIGHT);
        deadFrameSkull[1] = new Sprite(deadSkullFile2,0,0,DEAD_WIDTH,DEAD_HEIGHT);
    }

    private void loadZombieDeadFrames() {
        deadFrameZombie = new Sprite();
        Texture deadZombieFile = new Texture(Gdx.files.internal("images/grave64x96.png"));
        deadFrameZombie = new Sprite(deadZombieFile, 0, 0, DEAD_WIDTH, DEAD_HEIGHT);
    }
}
