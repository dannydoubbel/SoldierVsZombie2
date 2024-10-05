package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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

    private int yPosPixelSoldier;
    private int xPosTileSoldier;
    private int yPosTileSoldier;
    private boolean isWalkingLeft = false;
    private boolean isWalkingRight = false;
    private boolean isWalkingUp = false;
    private boolean isWalkingDown = false;
    private int[][] tileMapIds = new int[TILE_MAP_COLS][TILE_MAP_ROWS];
    private SpriteBatch batch;
    private Sprite[][] solderTextureRegion;
    private Sprite[] sourceTilesTextures;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private Music backgroundMusic;
    //private Sound soundEffect;
    private SharedVariables sharedVariables;

    private static void setUpInputRelatedStuff() {
        Gdx.input.setInputProcessor(new MyInputProcessor());
    }

    @Override
    public void create() {
        sharedVariables = SharedVariables.getInstance();
        sharedVariables.setCurrentSolderDirection(Directions.dn);

        sharedVariables.setMain_xpos(Math.round(Gdx.graphics.getWidth() / 2f));
        sharedVariables.setLeftMargin(200);
        sharedVariables.setRightMargin(200);

        loadSpritesAndTiles();
        setupScreenRelatedStuff();
        setupSoundRelatedStuff();
        setUpInputRelatedStuff();
        setupTileMap();

        batch = new SpriteBatch();

        yPosPixelSoldier = (TILE_MAP_ROWS * TILE_HEIGHT) / TILE_MAP_SCALE_FACTOR;
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

        doMainCharacterMovement();

        handleZoomKeyPress();


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
        title += "leftOffset " + sharedVariables.getLeftOffset() + " ";
        title += "XPOS = " + xPosTileSoldier + " YPOS = " + yPosTileSoldier + " ";
        title += "XPOS PIX = " + sharedVariables.getMain_xpos() + " ";
        title += "Debug (INS) : " + sharedVariables.isDebugScreen() + " ";

        int tileValueUnderMainCharacter = tileMapIds[xPosTileSoldier][yPosTileSoldier];
        title += "Value under = " + tileValueUnderMainCharacter + " ";
        title += "Zoom =  " + sharedVariables.getzoomValue() + " ";
        if (isWalkingRight) title += "RT ";
        if (isWalkingLeft) title += "LT ";
        if (isWalkingUp) title += "UP ";
        if (isWalkingDown) title += "Dn ";


        Gdx.graphics.setTitle(title); // Set the window title
    }

    void setupScreenRelatedStuff() {
        camera = new OrthographicCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        viewport = new ScreenViewport(camera); // Use ScreenViewport to adapt to window size without scaling
        viewport.apply();
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
            (Gdx.graphics.getWidth() + sharedVariables.getLeftOffset()) / TILE_WIDTH + 200;

        for (int row = 0; row < TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TILE_MAP_COLS && col < rightStopDrawing; col++) {
                int tileNr = tileMapIds[col][row];
                tileNr = tileNr > 0 ? tileNr - 1 : tileNr;
                batch.draw(sourceTilesTextures[tileNr],
                    col * TILE_WIDTH * TILE_MAP_SCALE_FACTOR - sharedVariables.getLeftOffset(),
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
            sharedVariables.getMain_xpos() - sharedVariables.getLeftOffset(),
            getMainCharacterYPos(),
            WIDTH_SOLDIER, HEIGHT_SOLDIER);
    }

    private void drawMainCharacterBorder() {
        // Draw a white rectangle on top of the sprite to represent its bounds
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Draw the rectangle as lines
        shapeRenderer.setColor(1, 1, 1, 1); // Set color to white (RGBA)
        shapeRenderer.rect(sharedVariables.getMain_xpos() - sharedVariables.getLeftOffset(), getMainCharacterYPos(), WIDTH_SOLDIER, HEIGHT_SOLDIER); // Draw the rectangle around the sprite
        shapeRenderer.setColor(0, 0.2f, 0.5f, 1); // Set color to dark blue (RGBA)
        shapeRenderer.circle(getMainCharacterXPosCenter() - sharedVariables.getLeftOffset(),getMainCharacterYPosCenter() , 6);
        shapeRenderer.end();

    }

    private int getMainCharacterXPosCenter() {
        return sharedVariables.getMain_xpos()+(WIDTH_SOLDIER/2);
    }
    private int getMainCharacterYPosCenter() {
        return  getMainCharacterYPos() + (HEIGHT_SOLDIER/2);
    }

    private int getMainCharacterYPos() {
        return yPosPixelSoldier;
    }


    private void doMainCharacterMovement() {
        int indexSoldier = sharedVariables.getTextureIndexSoldier();
        handleContradictingDirections();
        handleGoDown();
        handleGoUp();
        handleGoLeft();
        handleGoRight();
        if (isWalkingLeft || isWalkingRight || isWalkingDown || isWalkingUp) indexSoldier++;
        setMainCharacterAnimationFrame(indexSoldier);
        calculateTilePositionsMainCharacter();
    }

    private void handleContradictingDirections() {
        isWalkingDown = isWalkingDown && !isWalkingUp;
        isWalkingLeft = isWalkingLeft && !isWalkingRight;
    }

    private void handleGoRight() {
        if (sharedVariables.goRight && xPosTileSoldier - 1 < TILE_MAP_COLS
            && sharedVariables.WALKABLE_TILES.contains(tileMapIds[xPosTileSoldier + 1][yPosTileSoldier])) {
            isWalkingRight = true;
            sharedVariables.setCurrentSolderDirection(Directions.rt);
            sharedVariables.setMain_xpos(sharedVariables.getMain_xpos() + sharedVariables.STEP_SIZE);
        } else if (isWalkingRight && sharedVariables.getMain_xpos() % TILE_WIDTH != 0) {
            sharedVariables.setMain_xpos(sharedVariables.getMain_xpos() + 1);
        } else {
            isWalkingRight = false;
        }
        while (sharedVariables.getMain_xpos() - sharedVariables.getLeftOffset() > (Gdx.graphics.getWidth()- sharedVariables.getRightMargin())) {
            sharedVariables.setLeftOffset(sharedVariables.getLeftOffset() + 1);
        }
    }


    private void handleGoLeft() {
        // Ensure we don't go out of bounds by checking if xPosTileSoldier - 1 >= 0
        if (sharedVariables.goLeft && xPosTileSoldier - 1 >= 0
            && sharedVariables.WALKABLE_TILES.contains(tileMapIds[xPosTileSoldier][yPosTileSoldier])) {
            isWalkingLeft = true;
            sharedVariables.setCurrentSolderDirection(Directions.lt);
            sharedVariables.setMain_xpos(sharedVariables.getMain_xpos() - sharedVariables.STEP_SIZE);

        } else if (isWalkingLeft && ((sharedVariables.getMain_xpos() - TILE_WIDTH / 2) % TILE_WIDTH != 0)) {
            sharedVariables.setMain_xpos(sharedVariables.getMain_xpos() + 1);
        } else {
            isWalkingLeft = false;
        }
        while (sharedVariables.getMain_xpos() - sharedVariables.getLeftOffset() < sharedVariables.getLeftMargin()) {
            sharedVariables.setLeftOffset(sharedVariables.getLeftOffset()-1);
        }
        if (sharedVariables.getLeftOffset() < 0) sharedVariables.setLeftOffset(0);
    }


    private void handleGoUp() {

        if (sharedVariables.goUp && yPosTileSoldier + 1 < tileMapIds[0].length
            && sharedVariables.WALKABLE_TILES.contains(tileMapIds[xPosTileSoldier][yPosTileSoldier + 1])) {

            isWalkingUp = true;
            sharedVariables.setCurrentSolderDirection(Directions.up);
            yPosPixelSoldier += sharedVariables.STEP_SIZE;

        } else if (isWalkingUp && yPosPixelSoldier % TILE_HEIGHT != 0) {
            yPosPixelSoldier++;
        } else {
            isWalkingUp = false;
        }
    }

    private void handleGoDown() {

        if (sharedVariables.goDown && yPosTileSoldier - 1 >= 0
            && sharedVariables.WALKABLE_TILES.contains(tileMapIds[xPosTileSoldier][yPosTileSoldier - 1])) {

            isWalkingDown = true;
            sharedVariables.setCurrentSolderDirection(Directions.dn);
            yPosPixelSoldier -= sharedVariables.STEP_SIZE;

        } else if (isWalkingDown && (yPosPixelSoldier - TILE_HEIGHT / 2) % TILE_HEIGHT != 0) {
            yPosPixelSoldier--;
        } else {
            isWalkingDown = false;
        }
    }

    private void setMainCharacterAnimationFrame(int indexSoldier) {
        indexSoldier = indexSoldier % 7; // Ensures indexSoldier wraps around to 0 if it exceeds 6
        sharedVariables.setTextureIndexSoldier(indexSoldier);
    }


    private void calculateTilePositionsMainCharacter() {
        xPosTileSoldier = (sharedVariables.getMain_xpos() / TILE_WIDTH) /2 ;
        yPosTileSoldier = ((yPosPixelSoldier + TILE_HEIGHT / 2) / TILE_HEIGHT);
        yPosTileSoldier = (yPosTileSoldier != 0) ? yPosTileSoldier / 2 : yPosTileSoldier;
        if (yPosTileSoldier < 0) {
            yPosTileSoldier = 0;
        }
    }


    private void handleZoomKeyPress() {
        float newZoomValue = sharedVariables.getzoomValue();
        if (sharedVariables.isZoomIn()) {
            newZoomValue *= 2;
            sharedVariables.setZoomIn(false);
        }
        if (sharedVariables.isZoomOut()) {
            newZoomValue = newZoomValue > sharedVariables.ZOOM_MIN_VALUE ? newZoomValue / 2 : sharedVariables.ZOOM_MIN_VALUE;
            sharedVariables.setZoomOut(false);
        }
        newZoomValue = MathUtils.clamp(newZoomValue, sharedVariables.ZOOM_MIN_VALUE, sharedVariables.ZOOM_MAX_VALUE);
        sharedVariables.setzoomValue(newZoomValue);
    }
}
