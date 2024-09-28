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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {


    private static final int MAX_SOLDIER_PER_DIRECTION = 11;
    private static final int MAX_SOLDIER_DIRECTIONS = 4;
    private static final int WIDTH_SOLDIER = 1650 / MAX_SOLDIER_PER_DIRECTION;
    private static final int HEIGHT_SOLDIER = 468 / MAX_SOLDIER_DIRECTIONS;

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int TILE_MAP_COLS = 100;
    private static final int TILE_MAP_ROWS = 20;

    private final int WORLD_WIDTH = 800; // your desired virtual width in pixels
    private final int WORLD_HEIGHT = 600; // your desired virtual height in pixels


    private final int[][] tileMapId = new int[TILE_MAP_COLS][TILE_MAP_ROWS];


    private SpriteBatch batch;

    private Sprite[][] solderTextureRegion;
    private Sprite[] sourceTilesTextures;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Music backgroundMusic;
    private Sound soundEffect;


    @Override
    public void create() {
        SharedVariables.getInstance().setCurrentSolderDirection(Directions.dn);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("sound/gun-single.mp3"));

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera); // Use ScreenViewport to adapt to window size without scaling
        viewport.apply();
        camera.update();

        solderTextureRegion = loadMainCharacter();
        sourceTilesTextures = loadSourceTilesTextures();
        Gdx.input.setInputProcessor(new MyInputProcessor());
        batch = new SpriteBatch();


        backgroundMusic.play();
        backgroundMusic.setLooping(true); // Loop the background music

        loadSourceTilesTextures();
        loadTileMap();
        updateWindowTitle(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport on window resize to handle expanded world area
        viewport.update(width, height);
        camera.setToOrtho(false, width, height); // Adjust the camera to the new window size
        camera.update();
        updateWindowTitle(width, height);

    }

    @Override
    public void render() {
        SharedVariables sharedVariables = SharedVariables.getInstance();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();


        for (int col = 0; col < TILE_MAP_COLS; col++) {
            for (int row = 0; row < TILE_MAP_ROWS; row++) {
                int tileNr = tileMapId[col][row];
                tileNr = tileNr > 0 ? tileNr - 1 : tileNr;
                batch.draw(sourceTilesTextures[tileNr], col * 32, row * 32);//,16,16);
                //if (tileNr!=0) System.out.println("Tile nr "+tileNr);
            }
        }


        batch.draw(
            solderTextureRegion[sharedVariables.getCurrentSolderDirection().getValue()]
                [sharedVariables.getTextureIndexSoldier()],
            100, 100,
            WIDTH_SOLDIER, HEIGHT_SOLDIER);


        batch.end();
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
                spriteRegions[row][col] =
                    new Sprite(
                        spriteSheet,
                        col * WIDTH_SOLDIER,
                        row * HEIGHT_SOLDIER,
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

    private void loadTileMap() {
        SharedVariables sharedVariables = SharedVariables.getInstance();
        int[] test = sharedVariables.getTileMap();
        for (int row = 0; row < TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TILE_MAP_COLS; col++) {
                tileMapId[col][row] = test[col + row * TILE_MAP_COLS];
            }
        }
    }

    private void updateWindowTitle(int width, int height) {
        String title = "Window Size: " + width + "x" + height;
        Gdx.graphics.setTitle(title); // Set the window title
    }

}


