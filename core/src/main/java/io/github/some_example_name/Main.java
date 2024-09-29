package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private static final int MAX_SOLDIER_PER_DIRECTION = 11;
    private static final int MAX_SOLDIER_DIRECTIONS = 4;
    private static final int WIDTH_SOLDIER_IN_FILE = 1650 / MAX_SOLDIER_PER_DIRECTION;
    private static final int WIDTH_SOLDIER_WITH_RIGHT_MARGE = WIDTH_SOLDIER_IN_FILE - 70;
    private static final int WIDTH_SOLDIER = WIDTH_SOLDIER_WITH_RIGHT_MARGE - 10;
    private static final int HEIGHT_SOLDIER_IN_FILE = 468 / MAX_SOLDIER_DIRECTIONS;
    private static final int HEIGHT_SOLDIER = HEIGHT_SOLDIER_IN_FILE - 35;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int TILE_MAP_COLS = 100;
    private static final int TILE_MAP_ROWS = 20;
    private static final int TILE_MAP_SCALE_FACTOR = 2;
    private int xposPixelSoldier;
    private int yposPixelSoldier;
    private int xposTileSoldier;
    private int yposTileSoldier;
    private int[][] tileMapIds = new int[TILE_MAP_COLS][TILE_MAP_ROWS];
    private SpriteBatch batch;
    private Sprite[][] solderTextureRegion;
    private Sprite[] sourceTilesTextures;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private Music backgroundMusic;
    private Sound soundEffect;
    private SharedVariables sharedVariables;

    private static void setUpInputRelatedStuff() {
        Gdx.input.setInputProcessor(new MyInputProcessor());
    }

    @Override
    public void create() {
        sharedVariables = SharedVariables.getInstance();
        sharedVariables.setCurrentSolderDirection(Directions.dn);

        loadSpritesAndTiles();
        setupScreenRelatedStuff();
        setupSoundRelatedStuff();
        setUpInputRelatedStuff();
        setupTileMap();

        batch = new SpriteBatch();

        yposPixelSoldier = (TILE_MAP_ROWS * TILE_HEIGHT) / TILE_MAP_SCALE_FACTOR;
    }

    @Override
    public void resize(int width, int height) {
        int viewportHeight = TILE_HEIGHT * TILE_MAP_ROWS * TILE_MAP_SCALE_FACTOR; // Example: Fixed viewport height (in pixels)
        int viewportWidth = TILE_WIDTH * TILE_MAP_COLS * TILE_MAP_SCALE_FACTOR;
        float aspectRatio = (float) width / height;
        int windowWidth = (int) (viewportHeight * aspectRatio);
        Gdx.graphics.setWindowedMode(windowWidth, viewportHeight);
        viewport.update(viewportWidth, viewportHeight, false);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Adjust if needed
        camera.update();
    }


    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.zoom = sharedVariables.getzoomValue();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        mainCharacterDoMovement();

        batch.begin();
        drawBackground();
        drawMainCharacter();

        batch.end();
        if (sharedVariables.isDebugScreen()) drawMainCharacterBorder();
        updateWindowTitle();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    Sprite[][] loadMainCharacter() {
        Texture spriteSheet = new Texture(Gdx.files.internal("images/top down soldier.png"));
        Sprite[][] spriteRegions =
            new Sprite[MAX_SOLDIER_DIRECTIONS][MAX_SOLDIER_PER_DIRECTION];


        for (int row = 0; row < MAX_SOLDIER_DIRECTIONS; row++) {
            for (int col = 0; col < MAX_SOLDIER_PER_DIRECTION; col++) {
                Sprite spriteAsInSpriteSheet =
                    new Sprite(
                        spriteSheet,
                        col * WIDTH_SOLDIER_IN_FILE,
                        row * HEIGHT_SOLDIER_IN_FILE,
                        WIDTH_SOLDIER_IN_FILE,
                        HEIGHT_SOLDIER_IN_FILE);

                spriteRegions[row][col] =
                    new Sprite(
                        spriteAsInSpriteSheet,
                        (WIDTH_SOLDIER_IN_FILE - WIDTH_SOLDIER_WITH_RIGHT_MARGE) / TILE_MAP_SCALE_FACTOR,
                        (HEIGHT_SOLDIER_IN_FILE - HEIGHT_SOLDIER),
                        WIDTH_SOLDIER,
                        HEIGHT_SOLDIER);
            }
        }
        return spriteRegions;
    }

    Sprite[] loadSourceTilesTextures() {
        final int TILE_SOURCE_COLS = 23;
        final int TILE_SOURCE_ROWS = 21;

        Sprite[] slicedTiles = new Sprite[TILE_SOURCE_COLS * TILE_SOURCE_COLS];
        Texture fullFile = new Texture(Gdx.files.internal("images/5z1KX.png"));
        for (int row = 0; row < TILE_SOURCE_ROWS; row++) {
            for (int col = 0; col < TILE_SOURCE_COLS; col++) {
                // the image is here, transfer writeable image to image
                slicedTiles[col + row * TILE_SOURCE_COLS] =
                    new Sprite(
                        fullFile,
                        col * (TILE_WIDTH),
                        row * (TILE_HEIGHT),
                        TILE_WIDTH,
                        TILE_HEIGHT);
            }
        }
        return slicedTiles;
    }

    private int[][] setupTileMap() {
        int[] test = sharedVariables.getTileMap();
        int[][] result = new int[TILE_MAP_COLS][TILE_MAP_ROWS];
        for (int row = 0; row < TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TILE_MAP_COLS; col++) {
                result[col][row] = test[col + row * TILE_MAP_COLS];
            }
        }
        return result;
    }

    private void updateWindowTitle() {
        String title = "";
        title += "Graphics Size " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() + "y" + " ";
        title += "Offset " + sharedVariables.getTileMapLeftOffset() + " ";
        title += "XPOS = " + xposTileSoldier + " YPOS = " + yposTileSoldier + " ";
        title += "Debug (INS) : " + sharedVariables.isDebugScreen();

        Gdx.graphics.setTitle(title); // Set the window title
    }

    void setupScreenRelatedStuff() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera); // Use ScreenViewport to adapt to window size without scaling
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        viewport.apply();
        camera.update();
        shapeRenderer = new ShapeRenderer();
        Gdx.graphics.setResizable(true);
    }

    void setupSoundRelatedStuff() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("sound/gun-single.mp3"));
        backgroundMusic.play();
        backgroundMusic.setLooping(true); // Loop the background music
    }

    private void loadSpritesAndTiles() {
        solderTextureRegion = loadMainCharacter();
        sourceTilesTextures = loadSourceTilesTextures();
        tileMapIds = setupTileMap();
    }

    private void drawBackground() {
        int rightStopDrawing =
            (Gdx.graphics.getWidth() + sharedVariables.getTileMapLeftOffset()) / TILE_WIDTH + 200;

        for (int row = 0; row < TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TILE_MAP_COLS && col < rightStopDrawing; col++) {
                int tileNr = tileMapIds[col][row];
                tileNr = tileNr > 0 ? tileNr - 1 : tileNr;
                batch.draw(sourceTilesTextures[tileNr],
                    col * TILE_WIDTH * TILE_MAP_SCALE_FACTOR + sharedVariables.getTileMapLeftOffset(),
                    row * TILE_HEIGHT * TILE_MAP_SCALE_FACTOR,
                    TILE_WIDTH * TILE_MAP_SCALE_FACTOR,
                    TILE_HEIGHT * TILE_MAP_SCALE_FACTOR);
            }
        }
    }

    private void drawMainCharacter() {
        batch.draw(
            solderTextureRegion[sharedVariables.getCurrentSolderDirection().getValue()]
                [sharedVariables.getTextureIndexSoldier()],
            getMainCharacterXPos(),
            getMainCharacterYPos(),
            WIDTH_SOLDIER, HEIGHT_SOLDIER);
    }

    private void drawMainCharacterBorder() {
        // Draw a white rectangle on top of the sprite to represent its bounds
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Draw the rectangle as lines
        shapeRenderer.setColor(1, 1, 1, 1); // Set color to white (RGBA)
        shapeRenderer.rect(getMainCharacterXPos(), getMainCharacterYPos(), WIDTH_SOLDIER, HEIGHT_SOLDIER); // Draw the rectangle around the sprite
        shapeRenderer.setColor(0, 0.2f, 0.5f, 1); // Set color to dark blue (RGBA)
        shapeRenderer.circle(getMainCharacterXPosCenter(), getMainCharacterYPosCenter(), 6);
        shapeRenderer.end();
    }

    private int getMainCharacterXPos() {
        return (TILE_MAP_COLS * TILE_WIDTH) / 2;
    }

    private int getMainCharacterYPos() {
        return yposPixelSoldier;
    }

    private int getMainCharacterXPosCenter() {
        return getMainCharacterXPos() + (WIDTH_SOLDIER / 2);
    }

    private int getMainCharacterYPosCenter() {
        return getMainCharacterYPos() + (HEIGHT_SOLDIER / 2);
    }

    private void mainCharacterDoMovement() {

        int indexSoldier = sharedVariables.getTextureIndexSoldier();
        int previousIndexSoldier = indexSoldier;
        int leftOffset = sharedVariables.getTileMapLeftOffset();

        if (sharedVariables.goDown) {
            sharedVariables.setCurrentSolderDirection(Directions.dn);
            yposPixelSoldier -= sharedVariables.LEFT_OFFSET_STEP_SIZE;
            indexSoldier++;
        }
        if (sharedVariables.goUp) {
            sharedVariables.setCurrentSolderDirection(Directions.up);
            yposPixelSoldier += sharedVariables.LEFT_OFFSET_STEP_SIZE;
            indexSoldier++;
        }
        if (sharedVariables.goLeft) {
            sharedVariables.setCurrentSolderDirection(Directions.lt);
            leftOffset += sharedVariables.LEFT_OFFSET_STEP_SIZE;
            indexSoldier++;
        }
        if (sharedVariables.goRight) {
            sharedVariables.setCurrentSolderDirection(Directions.rt);
            indexSoldier++;
            leftOffset -= sharedVariables.LEFT_OFFSET_STEP_SIZE;
        }
        setMainCharacterAnimationFrame(indexSoldier, previousIndexSoldier);
        fitMainCharacterPositionWithinBoundaries(leftOffset);
        calculateTilePositionsMainCharacter();
    }

    private void setMainCharacterAnimationFrame(int indexSoldier, int previousIndexSoldier) {
        if (previousIndexSoldier != indexSoldier) {
            indexSoldier = indexSoldier % 7; // Ensures indexSoldier wraps around to 0 if it exceeds 6
            sharedVariables.setTextureIndexSoldier(indexSoldier);
        }
    }

    private void fitMainCharacterPositionWithinBoundaries(int leftOffset) {
        leftOffset = Math.min(leftOffset, getMainCharacterXPos());

        yposPixelSoldier = Math.max(yposPixelSoldier, 0);
        yposPixelSoldier = Math.min(yposPixelSoldier, TILE_HEIGHT * (TILE_MAP_ROWS - 1) * TILE_MAP_SCALE_FACTOR);

        int maxLeftOffset = -((TILE_MAP_COLS * TILE_WIDTH) + (TILE_MAP_COLS * TILE_WIDTH) / TILE_MAP_SCALE_FACTOR) + TILE_WIDTH;
        if (leftOffset < maxLeftOffset) {
            leftOffset = maxLeftOffset;
        }
        sharedVariables.setTileMapLeftOffset(leftOffset);
    }

    private void calculateTilePositionsMainCharacter() {
        xposPixelSoldier = getMainCharacterXPosCenter();
        yposPixelSoldier = getMainCharacterYPos();
        xposTileSoldier = ((xposPixelSoldier - sharedVariables.getTileMapLeftOffset()) / TILE_WIDTH);
        yposTileSoldier = (yposPixelSoldier / TILE_HEIGHT);
        xposTileSoldier = (xposTileSoldier != 0) ? xposTileSoldier / 2 : xposTileSoldier;
        yposTileSoldier = (yposTileSoldier != 0) ? yposTileSoldier / 2 : yposTileSoldier;
    }
}
