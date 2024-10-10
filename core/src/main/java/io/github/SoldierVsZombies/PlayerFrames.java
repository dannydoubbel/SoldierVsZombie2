package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PlayerFrames {
    private static final int MAX_PLAYER_FRAMES_PER_DIRECTION = 11;
    private static final int MAX_PLAYER_DIRECTIONS = 4;
    private static final int WIDTH_PLAYER_IN_FILE = 1650 / MAX_PLAYER_FRAMES_PER_DIRECTION;
    private static final int WIDTH_PLAYER_WITH_RIGHT_MARGE = WIDTH_PLAYER_IN_FILE - 70;
    private static final int HEIGHT_PLAYER_IN_FILE = 468 / MAX_PLAYER_DIRECTIONS;
    public static final int PLAYER_HEIGHT = HEIGHT_PLAYER_IN_FILE - 35;
    public static final int PLAYER_WIDTH = WIDTH_PLAYER_WITH_RIGHT_MARGE - 10;
    private static PlayerFrames instance;
    private Sprite[][] playerFramesSprites;
    private static final int TILE_MAP_SCALE_FACTOR = 2;

    PlayerFrames() {
        loadPlayerFrames();
    }

    public static PlayerFrames getInstance() {
        if (instance == null) {
            instance = new PlayerFrames();
        }
        return instance;
    }

    public Sprite[][] getPlayerFramesSprites() {
        return playerFramesSprites;
    }

    void loadPlayerFrames() {
        Texture spriteSheet = new Texture(Gdx.files.internal("images/top down soldier.png"));
        playerFramesSprites = new Sprite[MAX_PLAYER_DIRECTIONS][MAX_PLAYER_FRAMES_PER_DIRECTION];


        for (int row = 0; row < MAX_PLAYER_DIRECTIONS; row++) {
            for (int col = 0; col < MAX_PLAYER_FRAMES_PER_DIRECTION; col++) {
                Sprite spriteAsInSpriteSheet =
                    new Sprite(spriteSheet, col * WIDTH_PLAYER_IN_FILE, row * HEIGHT_PLAYER_IN_FILE, WIDTH_PLAYER_IN_FILE, HEIGHT_PLAYER_IN_FILE);

                playerFramesSprites[row][col] =
                    new Sprite(spriteAsInSpriteSheet, (WIDTH_PLAYER_IN_FILE - WIDTH_PLAYER_WITH_RIGHT_MARGE) / TILE_MAP_SCALE_FACTOR, (HEIGHT_PLAYER_IN_FILE - PLAYER_HEIGHT), PLAYER_WIDTH, PLAYER_HEIGHT);
            }
        }

    }
}
