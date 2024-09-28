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

;


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

    private final int WORLD_WIDTH = 800; // your desired virtual width in pixels
    private final int WORLD_HEIGHT = 600; // your desired virtual height in pixels
    private final int WORLD_MIN_HEIGHT = 400; // Minimum window height
    private final int WORLD_MAX_HEIGHT = 640; // Maximum window height


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
    }

    @Override
    public void resize(int width, int height) {

        //height = MathUtils.clamp(height, WORLD_MIN_HEIGHT, WORLD_MAX_HEIGHT);
        // Force the window height to match the viewport height
        int viewportHeight = TILE_HEIGHT * TILE_MAP_ROWS * TILE_MAP_SCALE_FACTOR; // Example: Fixed viewport height (in pixels)
        int viewportWidth = TILE_WIDTH * TILE_MAP_COLS * TILE_MAP_SCALE_FACTOR;

        // Adjust the window width to match the new aspect ratio
        float aspectRatio = (float) width / height;
        int windowWidth = (int) (viewportHeight * aspectRatio);

        // Set the windowed mode to enforce the fixed height
        Gdx.graphics.setWindowedMode(windowWidth, viewportHeight);

        // Update the viewport to match the new size
        viewport.update(viewportWidth, viewportHeight, false);

        // Update the camera to handle the new size
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();


    }


    @Override
    public void render() {
        updateWindowTitle();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.zoom = sharedVariables.getzoomValue();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawBackground();
        drawMainCharacter();

        batch.end();
        drawMainCharacterBorder();
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
                        (WIDTH_SOLDIER_IN_FILE - WIDTH_SOLDIER_WITH_RIGHT_MARGE) / 2,
                        (HEIGHT_SOLDIER_IN_FILE - HEIGHT_SOLDIER),
                        WIDTH_SOLDIER,
                        HEIGHT_SOLDIER);
            }
        }
        return spriteRegions;
    }

    Sprite[] loadSourceTilesTextures() {
        int tileSourceCols = 23;
        int tileSourceRows = 21;

        Sprite[] slicedTiles = new Sprite[tileSourceCols * tileSourceRows];
        Texture fullFile = new Texture(Gdx.files.internal("images/5z1KX.png"));
        for (int row = 0; row < tileSourceRows; row++) {
            for (int col = 0; col < tileSourceCols; col++) {
                // the image is here, transfer writeable image to image
                slicedTiles[col + row * tileSourceCols] =
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
        String title =
            "Graphics Size " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() + "y" + " " +
            "Offset " + sharedVariables.getTileMapLeftOffset();

        Gdx.graphics.setTitle(title); // Set the window title
    }

    void setupScreenRelatedStuff() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera); // Use ScreenViewport to adapt to window size without scaling
        viewport.apply();
        camera.update();
        shapeRenderer = new ShapeRenderer();
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
            (Gdx.graphics.getWidth() + sharedVariables.getTileMapLeftOffset())/TILE_WIDTH+1;

        for (int row = 0; row < TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TILE_MAP_COLS && col < rightStopDrawing ; col++) {
                int tileNr = tileMapIds[col][row];
                tileNr = tileNr > 0 ? tileNr - 1 : tileNr;
                batch.draw(sourceTilesTextures[tileNr],
                    col * TILE_WIDTH * TILE_MAP_SCALE_FACTOR + sharedVariables.getTileMapLeftOffset(), row * TILE_HEIGHT * TILE_MAP_SCALE_FACTOR,
                    TILE_WIDTH * TILE_MAP_SCALE_FACTOR, TILE_HEIGHT * TILE_MAP_SCALE_FACTOR);
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
        return (TILE_MAP_ROWS * TILE_HEIGHT) / 2;
    }

    private int getMainCharacterXPosCenter() {
        return getMainCharacterXPos() + (WIDTH_SOLDIER / 2);
    }

    private int getMainCharacterYPosCenter() {
        return getMainCharacterYPos() + (HEIGHT_SOLDIER / 2);
    }

}


