package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Random;

public class ShortLifeTimeSpriteTypeManager {

    public final int DEAD_WIDTH = 64;
    public final int DEAD_HEIGHT = 96;
    public final int GIFT_WIDTH = 100;
    public final int GIFT_HEIGHT = 100;
    public final int PORTAL_SHINE_WIDTH = 100;
    public final int PORTAL_SHINE_HEIGHT = 100;

    private final ArrayList<ShortLifeTimeSprite> allShortLifeTimeSprites = new ArrayList<>();
    private Sprite deadFrameZombie;
    private Sprite[] deadFrameSkull;
    private Sprite blackGift;
    private Sprite[] portalShine;
    private Sprite wow;
    private Sprite[] woodFire;


    ShortLifeTimeSpriteTypeManager() {
        String javaVersion = System.getProperty("java.version");
        System.out.println("inside ShortLifeTimeSpriteManager Java version: " + javaVersion);
        loadSpawnAbleFrames();
    }

    public Sprite getShortLifeTimeFrame(ShortLifeTimeSprite shortLifetimeSprite) {
        Random random = new Random();
        if (shortLifetimeSprite.getShortLifeTimeSpriteType().equals(ShortLifeTimeSpriteType.DEAD_ZOMBIE))
            return deadFrameZombie;
        if (shortLifetimeSprite.getShortLifeTimeSpriteType().equals(ShortLifeTimeSpriteType.WOW)) return wow;
        if (shortLifetimeSprite.getShortLifeTimeSpriteType().equals(ShortLifeTimeSpriteType.DEAD_SKULL))
            return shortLifetimeSprite.isBeyondHalfLifeTime() ? deadFrameSkull[1] : deadFrameSkull[0];
        if (shortLifetimeSprite.getShortLifeTimeSpriteType().equals(ShortLifeTimeSpriteType.PORTAL_SHINE))
            return shortLifetimeSprite.isLifeTimeInOddPartAlternatingPercentRange(10) ? portalShine[0] : portalShine[1];
        // so deadType is BLACK_GIFT
        if (shortLifetimeSprite.getShortLifeTimeSpriteType().equals(ShortLifeTimeSpriteType.WOOD_FIRE)) {
            return woodFire[random.nextInt(woodFire.length)];
        }

        return blackGift;
    }

    public void addShortLifeTimeSprite(ShortLifeTimeSprite shortLifeTimeSpriteToAdd) {
        allShortLifeTimeSprites.add(shortLifeTimeSpriteToAdd);
    }

    public ArrayList<ShortLifeTimeSprite> getAllShotyLifeTimeSprites() {
        return allShortLifeTimeSprites;
    }


    public void handleShortLifeTimeSpritesCountDown() {
        getAllShotyLifeTimeSprites().removeIf(ShortLifeTimeSprite::isBeyondLifeTime);
    }

    public long getNumberOfGifts() {
        return getAllShotyLifeTimeSprites().stream()
            .filter(shortLifeTimeSprite -> shortLifeTimeSprite.getShortLifeTimeSpriteType().equals(ShortLifeTimeSpriteType.BLACK_GIFT))
            .count();
    }


    void loadSpawnAbleFrames() {
        loadZombieDeadFrames();
        loadSkullDeadFrames();
        loadBlackGiftFrames();
        loadPortalShineFrames();
        loadWowFrames();
        loadWoodFireFrames();
    }

    private void loadWowFrames() {
        wow = new Sprite();
        Texture wowFile = new Texture(Gdx.files.internal("images/wow100x100.png"));
        wow = new Sprite(wowFile, 0, 0, GIFT_WIDTH, GIFT_HEIGHT);
    }

    private void loadBlackGiftFrames() {
        blackGift = new Sprite();
        Texture blackGiftFile = new Texture(Gdx.files.internal("images/blackGiftBox100x100.png"));
        blackGift = new Sprite(blackGiftFile, 0, 0, GIFT_WIDTH, GIFT_HEIGHT);
    }

    private void loadPortalShineFrames() {
        portalShine = new Sprite[2];
        Texture portalShineFile1 = new Texture(Gdx.files.internal("images/portalshine_1_100x100.png"));
        Texture portalShineFile2 = new Texture(Gdx.files.internal("images/portalshine_2_100x100.png"));
        portalShine[0] = new Sprite(portalShineFile1, 0, 0, PORTAL_SHINE_WIDTH, PORTAL_SHINE_HEIGHT);
        portalShine[1] = new Sprite(portalShineFile2, 0, 0, PORTAL_SHINE_WIDTH, PORTAL_SHINE_HEIGHT);
    }

    private void loadSkullDeadFrames() {
        deadFrameSkull = new Sprite[2];
        Texture deadSkullFile1 = new Texture(Gdx.files.internal("images/skull100x100-ex1.png"));
        Texture deadSkullFile2 = new Texture(Gdx.files.internal("images/skull100x100-ex2.png"));
        deadFrameSkull[0] = new Sprite(deadSkullFile1, 0, 0, DEAD_WIDTH, DEAD_HEIGHT);
        deadFrameSkull[1] = new Sprite(deadSkullFile2, 0, 0, DEAD_WIDTH, DEAD_HEIGHT);
    }

    private void loadZombieDeadFrames() {
        deadFrameZombie = new Sprite();
        Texture deadZombieFile = new Texture(Gdx.files.internal("images/grave64x96.png"));
        deadFrameZombie = new Sprite(deadZombieFile, 0, 0, DEAD_WIDTH, DEAD_HEIGHT);
    }

    private void loadWoodFireFrames() {
        woodFire = new Sprite[12];
        Texture woodFireFile = new Texture(Gdx.files.internal("images/woodFire_12_100x100.png"));
        for (int lus = 0; lus < 12; lus++) {
            woodFire[lus] = new Sprite(woodFireFile, lus * 100, 0, GIFT_WIDTH, GIFT_HEIGHT);
        }
    }


    public boolean isOnPositionATypeOf(IntPosition pixelPosToTest, ShortLifeTimeSpriteType typeToTest) {
        for (ShortLifeTimeSprite shortLifeTimeSprite : getAllShotyLifeTimeSprites()) {
            if (shortLifeTimeSprite.getShortLifeTimeSpriteType().equals(typeToTest) &&
                shortLifeTimeSprite.getPosition().equals(pixelPosToTest)) return true;
        }
        return false;
    }
}
