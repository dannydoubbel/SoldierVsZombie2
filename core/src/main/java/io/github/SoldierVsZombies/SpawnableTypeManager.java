package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Iterator;

public class SpawnableTypeManager {

    public final int DEAD_WIDTH = 64;
    public final int DEAD_HEIGHT = 96;
    public final int GIFT_WIDTH = 100;
    public final int GIFT_HEIGHT = 100;
    public final int PORTAL_SHINE_WIDTH = 100;
    public final int PORTAL_SHINE_HEIGHT = 100;

    private final ArrayList<SpawnableSprite> allSpawnableSprites = new ArrayList<>();
    private Sprite deadFrameZombie;
    private Sprite[] deadFrameSkull;
    private Sprite blackGift;
    private Sprite portalShine[];



    SpawnableTypeManager() {
        String javaVersion = System.getProperty("java.version");
        System.out.println("inside spanwableTypeManager Java version: " + javaVersion);
        loadDeadFrames();
    }

    public Sprite getSpawnableFrame(SpawnableSprite spawnableSprite) {
        if (spawnableSprite.getSpawnableType().equals(SpawnableType.DEAD_ZOMBIE)) return deadFrameZombie;
        if (spawnableSprite.getSpawnableType().equals(SpawnableType.DEAD_SKULL)) {
            if (spawnableSprite.isBeyondHalfLifeTime()) return deadFrameSkull[1];
            return deadFrameSkull[0];
        }
        if (spawnableSprite.getSpawnableType().equals(SpawnableType.PORTAL_SHINE)) {
            if (
                (spawnableSprite.isLifeTimeBetween(10,20)) ||
                    (spawnableSprite.isLifeTimeBetween(30,40) ||
                        (spawnableSprite.isLifeTimeBetween(50,60) ||
                            (spawnableSprite.isLifeTimeBetween(70,90))))) return portalShine[0];
            return portalShine[1];
        }
        // so deadType is BLACK_GIFT
        return  blackGift;
    }

    public void addSpawnable(SpawnableSprite spawnableSpriteToAdd) {
        allSpawnableSprites.add(spawnableSpriteToAdd);
    }

    public ArrayList<SpawnableSprite> getAllSpawnables() {
        return allSpawnableSprites;
    }


    public void handleSpawnablesCountDown() {
        Iterator<SpawnableSprite> deadsIterator = getAllSpawnables().iterator();
        while (deadsIterator.hasNext()) {
            SpawnableSprite spawnableSprite = deadsIterator.next();
            if (spawnableSprite.isBeyondLifeTime()) {
                deadsIterator.remove();
            }
        }
    }

    public long getNumberOfGifts() {
        long blackGiftCount = getAllSpawnables().stream()
            .filter(spawnableSprite -> spawnableSprite.getSpawnableType().equals(SpawnableType.BLACK_GIFT))
            .count();
        return blackGiftCount;
    }


    void loadDeadFrames() {
        loadZombieDeadFrames();
        loadSkullDeadFrames();
        loadBlackGiftFrames();
        loadPortalShineFrames();
    }

    private void loadBlackGiftFrames() {
        blackGift = new Sprite();
        Texture blackGiftFile = new Texture(Gdx.files.internal("images/blackGiftBox100x100.png"));
        blackGift = new Sprite(blackGiftFile, 0, 0,GIFT_WIDTH,GIFT_HEIGHT);
    }

    private void loadPortalShineFrames() {
        portalShine = new Sprite[2];
        Texture portalShineFile1 = new Texture(Gdx.files.internal("images/portalshine_1_100x100.png"));
        Texture portalShineFile2 = new Texture(Gdx.files.internal("images/portalshine_2_100x100.png"));
        portalShine[0] = new Sprite(portalShineFile1, 0, 0,PORTAL_SHINE_WIDTH,PORTAL_SHINE_HEIGHT);
        portalShine[1] = new Sprite(portalShineFile2, 0, 0,PORTAL_SHINE_WIDTH,PORTAL_SHINE_HEIGHT);
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
