package io.github.SoldierVsZombies;

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

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int HALF_TILE_WIDTH = TILE_WIDTH / 2;
    private static final int HALF_TILE_HEIGHT = TILE_HEIGHT / 2;
    private static final int TILE_MAP_COLS = 100;
    private static final int TILE_MAP_ROWS = 20;
    private static final int TILE_MAP_SCALE_FACTOR = 2;
    private int[][] backgroundTileMap = new int[TILE_MAP_COLS][TILE_MAP_ROWS];
    private SpriteBatch batch;
    private Sprite[] sourceBackgroundTiles;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private Music backgroundMusic;
    //private Sound soundEffect;
    private SharedVariables sharedVariables;
    private PressedKeys pressedKeys;
    private PlayerFrames playerFrames; // To Do implement this
    private PlayerState playerState;
    private ViewParameters viewParameters;

    private Portals portals;

    @Override
    public void create() {
        initializeSingletons();
        initializeGameComponents();
        initializePortals();

        loadTiles();

        batch = new SpriteBatch();

        playerState.getPlayerCenterPos().setPosition(getCenterPositionOfTile(new IntPosition(11, 5)));
    }

    private void initializePortals() {
        portals = new Portals();
    }

    IntPosition getCenterPositionOfTile(IntPosition tilePosition) {
        return new IntPosition(tilePosition.getX() * TILE_WIDTH * 2 + HALF_TILE_WIDTH, tilePosition.getY() * TILE_HEIGHT * 2 + HALF_TILE_HEIGHT);
    }

    private void initializeGameComponents() {
        setupScreenRelatedStuff();
        setupSoundRelatedStuff();
        setUpInputRelatedStuff();
        setupBackgroundTileMap();
        viewParameters.setUpWindowMargins();
    }

    private void initializeSingletons() {
        viewParameters = ViewParameters.getInstance();
        playerState = PlayerState.getInstance();
        pressedKeys = PressedKeys.getInstance();
        sharedVariables = SharedVariables.getInstance();
        playerFrames = PlayerFrames.getInstance(); // To Do implement this class
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
        camera.zoom = viewParameters.getZoomValue();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        handlePlayerMovement();
        handlePortals();
        handleZoomKeyPress();
        handleMusicKeyPress();

        batch.begin();
        drawBackground();
        if (!viewParameters.isDebugScreen()) drawPlayerFromCenterPosition();

        batch.end();
        if (viewParameters.isDebugScreen()) drawPlayerBorder();
        updateWindowTitle();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    Sprite[] loadSourceBackgroundTiles() {
        final int TILE_SOURCE_COLS = 23;
        final int TILE_SOURCE_ROWS = 21;

        Sprite[] slicedTiles = new Sprite[TILE_SOURCE_COLS * TILE_SOURCE_COLS];
        Texture fullFile = new Texture(Gdx.files.internal("images/5z1KX.png"));
        for (int row = 0; row < TILE_SOURCE_ROWS; row++) {
            for (int col = 0; col < TILE_SOURCE_COLS; col++) {
                // the image is here, transfer writeable image to image
                slicedTiles[col + row * TILE_SOURCE_COLS] = new Sprite(fullFile, col * (TILE_WIDTH), row * (TILE_HEIGHT), TILE_WIDTH, TILE_HEIGHT);
            }
        }
        return slicedTiles;
    }


    private int[][] setupBackgroundTileMap() {
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
        //title += "leftOffset " + sharedVariables.getLeftOffset() + " ";
        title += "XPOS = " + playerState.getXPosTilePlayer() + " YPOS = " + playerState.getYPosTilePlayer()+ " ";
        title += "Debug (INS) : " + viewParameters.isDebugScreen() + " ";

        int tileValueUnderMainCharacter = backgroundTileMap[playerState.getXPosTilePlayer()][playerState.getYPosTilePlayer()];
        title += "Value under = " + tileValueUnderMainCharacter + " ";
        //title += "Zoom =  " + viewParameters.getZoomValue() + " ";

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

    private void loadTiles() {
        sourceBackgroundTiles = loadSourceBackgroundTiles();
        backgroundTileMap = setupBackgroundTileMap();
    }

    private void drawBackground() {
        int rightStopDrawing = (Gdx.graphics.getWidth() + viewParameters.getLeftOffset()) / TILE_WIDTH + 200;

        for (int row = 0; row < TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TILE_MAP_COLS && col < rightStopDrawing; col++) {
                int tileNr = backgroundTileMap[col][row];
                tileNr = tileNr > 0 ? tileNr - 1 : tileNr;
                batch.draw(sourceBackgroundTiles[tileNr],
                    col * TILE_WIDTH * TILE_MAP_SCALE_FACTOR - viewParameters.getLeftOffset(),
                    row * TILE_HEIGHT * TILE_MAP_SCALE_FACTOR,
                    TILE_WIDTH * TILE_MAP_SCALE_FACTOR,
                    TILE_HEIGHT * TILE_MAP_SCALE_FACTOR);
            }
        }
    }


    private void drawPlayerFromCenterPosition() {
        int direction = calculatePlayerFrameIndex();
        batch.draw(playerFrames.getPlayerFramesSprites()[direction][playerState.getPlayerFrameIndex()],
            playerState.getPlayerCenterPos().getX() - viewParameters.getLeftOffset() - ((TILE_WIDTH * TILE_MAP_SCALE_FACTOR) / 2) + (15),
            playerState.getPlayerCenterPos().getY() - ((TILE_HEIGHT * TILE_MAP_SCALE_FACTOR) / 2) + (15),
            PlayerFrames.PLAYER_WIDTH, PlayerFrames.PLAYER_HEIGHT);
    }

    private int calculatePlayerFrameIndex() {
        int directionValue = playerState.getPlayerCurrentDirection().getValue();
        if (directionValue > 0) {
            //sharedVariables.setPlayerPreviousDirection(sharedVariables.getPlayerCurrentDirection());
            playerState.setPlayerPreviousDirection(playerState.getPlayerCurrentDirection());
        } else {
            //directionValue = sharedVariables.getPlayerPreviousDirection().getValue();
            directionValue = playerState.getPlayerPreviousDirection().getValue();
        }
        // Decrement the directionValue if it's greater than 0
        return Math.max(directionValue - 1, 0);
    }

    private void drawPlayerBorder() {
        // Draw a white rectangle on top of the sprite to represent its bounds
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Draw the rectangle as lines
        shapeRenderer.setColor(1, 1, 1, 1); // Set color to white (RGBA)
        shapeRenderer.rect(
            playerState.getPlayerCenterPos().getX() - viewParameters.getLeftOffset(),
            playerState.getPlayerCenterPos().getY(),
            PlayerFrames.PLAYER_WIDTH,
            PlayerFrames.PLAYER_HEIGHT); // Draw the rectangle around the sprite
        shapeRenderer.setColor(0, 0.2f, 0.5f, 1); // Set color to dark blue (RGBA)
        shapeRenderer.rect(playerState.getPlayerCenterPos().getX() - viewParameters.getLeftOffset(), playerState.getPlayerCenterPos().getY(), TILE_WIDTH * TILE_MAP_SCALE_FACTOR, TILE_HEIGHT * TILE_MAP_SCALE_FACTOR); // Draw the rectangle around the sprite

        shapeRenderer.circle(playerState.getPlayerTargetCenterPos().getX() - viewParameters.getLeftOffset(), playerState.getPlayerCenterPos().getY(), 6);
        shapeRenderer.end();
    }


    private void handlePlayerMovement() {
        handleGoDown();
        handleGoUp();
        handleGoLeft();
        handleGoRight();
        handlePlayerFrameIndex();
        handleAdjustLeftOffsetToFitMargins();

        calculatePlayerTilePosition();
    }

    private void handlePlayerFrameIndex() {
        if (isPlayerWalking()) {
            playerState.addPlayerFrameIndex(1);
        }
        setPlayerFrame(playerState.getPlayerFrameIndex());
    }

    private void handleGoRight() {
        int targetTileX = playerState.getXPosTilePlayer() + 1;
        int targetTileY = playerState.getYPosTilePlayer();
        boolean isValidStartCondition = !isPlayerWalking();
        if (pressedKeys.goRight && isValidStartCondition && isTileWalkable(targetTileX, targetTileY)) {
            playerState.setPlayerCurrentDirection(Directions.rt);
            playerState.getPlayerTargetCenterPos().setPosition(getCenterPositionOfTile(new IntPosition(targetTileX, targetTileY)));

        } else if (playerState.getPlayerCurrentDirection().equals(Directions.rt)) {
            playerState.getPlayerCenterPos().addX(playerState.STEP_SIZE);
            if (playerState.getPlayerCenterPos().getX() >= playerState.getPlayerTargetCenterPos().getX()) {
                playerState.getPlayerCenterPos().setX(playerState.getPlayerTargetCenterPos().getX());
                playerState.setPlayerCurrentDirection(Directions.no);
            }
        }
    }


    private void handleGoLeft() {
        int targetTileX = playerState.getXPosTilePlayer() - 1;
        int targetTileY = playerState.getYPosTilePlayer();
        boolean isValidStartCondition = !isPlayerWalking();
        if (pressedKeys.goLeft && isValidStartCondition && isTileWalkable(targetTileX, targetTileY)) {
            playerState.setPlayerCurrentDirection(Directions.lt);
            playerState.getPlayerTargetCenterPos().setPosition(getCenterPositionOfTile(new IntPosition(targetTileX, targetTileY)));
        } else if (playerState.getPlayerCurrentDirection().equals(Directions.lt)) {
            playerState.getPlayerCenterPos().addX(-playerState.STEP_SIZE);
            if (playerState.getPlayerCenterPos().getX() <= playerState.getPlayerTargetCenterPos().getX()) {
                playerState.getPlayerCenterPos().setX(playerState.getPlayerTargetCenterPos().getX());
                playerState.setPlayerCurrentDirection(Directions.no);
            }
        }
    }


    private void handleGoUp() {
        int targetTileX = playerState.getXPosTilePlayer();
        int targetTileY = playerState.getYPosTilePlayer()+1;
        boolean isValidStartCondition = !isPlayerWalking();
        if (pressedKeys.goUp && isValidStartCondition && isTileWalkable(targetTileX, targetTileY)) {
            playerState.setPlayerCurrentDirection(Directions.up);
            playerState.getPlayerTargetCenterPos().setPosition(getCenterPositionOfTile(new IntPosition(targetTileX, targetTileY)));
        } else if (playerState.getPlayerCurrentDirection().equals(Directions.up)) {
            playerState.getPlayerCenterPos().addY(playerState.STEP_SIZE);
            if (playerState.getPlayerCenterPos().getY() >= playerState.getPlayerTargetCenterPos().getY()) {
                playerState.getPlayerCenterPos().setY(playerState.getPlayerTargetCenterPos().getY());
                playerState.setPlayerCurrentDirection(Directions.no);
            }
        }
    }

    private void handleGoDown() {
        int targetTileX = playerState.getXPosTilePlayer();
        int targetTileY = playerState.getYPosTilePlayer() - 1;
        boolean isValidStartCondition = !isPlayerWalking();
        if (pressedKeys.goDown && isValidStartCondition && isTileWalkable(targetTileX, targetTileY)) {
            playerState.setPlayerCurrentDirection(Directions.dn);
            playerState.getPlayerTargetCenterPos().setPosition(getCenterPositionOfTile(new IntPosition(targetTileX, targetTileY)));
        } else if (playerState.getPlayerCurrentDirection().equals(Directions.dn)) {
            playerState.getPlayerCenterPos().addY(-playerState.STEP_SIZE);
            if (playerState.getPlayerCenterPos().getY() <= playerState.getPlayerTargetCenterPos().getY()) {
                playerState.getPlayerCenterPos().setY(playerState.getPlayerTargetCenterPos().getY());
                playerState.setPlayerCurrentDirection(Directions.no);
            }
        }
    }

    private void handleAdjustLeftOffsetToFitMargins() {
        int rightLimitInWindow = Gdx.graphics.getWidth() - viewParameters.getRightMargin();
        int leftLimitInWindow = viewParameters.getLeftMargin();
        int playerXPosInWindow = playerState.getPlayerCenterPos().getX() - viewParameters.getLeftOffset();
        int adjustment = 0;

        if (playerXPosInWindow > rightLimitInWindow) {
            // Compute adjustment for the right margin
            adjustment = playerXPosInWindow - rightLimitInWindow;
        } else if (playerXPosInWindow < leftLimitInWindow) {
            // Compute adjustment for the left margin
            adjustment = playerXPosInWindow - leftLimitInWindow;
        }
        viewParameters.setLeftOffset(Math.max(0, viewParameters.getLeftOffset() + adjustment));
    }

    private void handlePortals() {
        if (!isPlayerWalking()) {
            IntPosition portalExitPosition = portals.findPortalExitForPortalEntry(playerState.getPlayerTilePosition());
            if (!portalExitPosition.equals(portals.illegalPosition)) {
                IntPosition newPlayerPos = getCenterPositionOfTile(portalExitPosition);
                playerState.setPlayerCenterPos(newPlayerPos);
                playerState.setPlayerTargetCenterPos(newPlayerPos);
            }
        }
    }

    private boolean isPlayerWalking() {
        return !(playerState.getPlayerCurrentDirection().getValue() == 0);
    }

    private boolean isTileWalkable(int x, int y) {
        return sharedVariables.WALKABLE_TILES.contains(backgroundTileMap[x][y]);
    }

    private void setPlayerFrame(int playerIndexFrame) {
        playerIndexFrame = playerIndexFrame % 7; // Ensures indexSoldier wraps around to 0 if it exceeds 6
        playerState.setPlayerFrameIndex(playerIndexFrame);
    }


    private int getPlayerXPosCenter() {
        return playerState.getPlayerCenterPos().getX() + (TILE_WIDTH * TILE_MAP_SCALE_FACTOR / 2);
    }

    private int getPlayerYPosCenter() {
        return playerState.getPlayerCenterPos().getY() + (TILE_HEIGHT * TILE_MAP_SCALE_FACTOR / 2);
    }

    private void calculatePlayerTilePosition() {
        playerState.setXPosTilePlayer( getPlayerXPosCenter() / (TILE_WIDTH * TILE_MAP_SCALE_FACTOR));
        playerState.setYPosTilePlayer( getPlayerYPosCenter() / (TILE_HEIGHT * TILE_MAP_SCALE_FACTOR));


        if (playerState.getYPosTilePlayer() < 0) {
            playerState.setYPosTilePlayer(0);
        }
        if (playerState.getXPosTilePlayer() < 0) {
            playerState.setXPosTilePlayer(0);
        }
    }


    private void handleZoomKeyPress() {
        float newZoomValue = viewParameters.getZoomValue();
        if (pressedKeys.zoomIn) {
            newZoomValue *= 2;
            pressedKeys.zoomIn = false;
        }
        if (pressedKeys.zoomOut) {
            newZoomValue = newZoomValue > viewParameters.ZOOM_MIN_VALUE ? newZoomValue / 2 : viewParameters.ZOOM_MIN_VALUE;
            pressedKeys.zoomOut = false;
        }
        newZoomValue = MathUtils.clamp(newZoomValue, viewParameters.ZOOM_MIN_VALUE, viewParameters.ZOOM_MAX_VALUE);
        viewParameters.setZoomValue(newZoomValue);
    }

    private void handleMusicKeyPress() {
        if (sharedVariables.musicAllowed) {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.pause();
            } else {
                backgroundMusic.play();
            }
            sharedVariables.musicAllowed = false;
        }
    }

    private static void setUpInputRelatedStuff() {
        Gdx.input.setInputProcessor(new MyInputProcessor());
    }
}
