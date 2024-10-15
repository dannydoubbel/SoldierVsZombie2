package io.github.SoldierVsZombies;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    static boolean justOnce = false;
    private SpriteBatch batch;
    //private Sprite[] sourceBackgroundTiles;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private Music backgroundMusic;
    //private Sound soundEffect;

    private CollisionDetector collisionDetector;
    private SkullManager skullManager;
    private BulletManager bulletManager;
    private ZombieManager zombieManager;
    private SharedVariables sharedVariables;
    private PressedKeys pressedKeys;
    private PlayerFrames playerFrames; // To Do implement this
    private PlayerState playerState;
    private ViewParameters viewParameters;
    private Tiles tiles;
    private Portals portals;

    private static void setUpInputRelatedStuff() {
        Gdx.input.setInputProcessor(new MyInputProcessor());
    }


    // todo move this to the ScoreBoard class
    private BitmapFont fontSmall;
    private BitmapFont fontBig;
    private Stage stage;

    private int lives = 3;
    private int ammo = 50;
    private float time = 0; // Time in seconds
    private int kills = 10;

    private Label livesLabel;
    private Label livesNumber;
    private Label ammoLabel;
    private Label ammoNumber;
    private Label timeLabel;
    private Label killsLabel;




    @Override
    public void create() {


        bulletManager = new BulletManager();
        zombieManager = new ZombieManager();
        skullManager = new SkullManager();
        zombieManager.addZombie(new IntPosition(200, 200));
        skullManager.addSkull(new IntPosition(350, 350), Directions.lt,1 ,skullManager.SKULL_COLS_IN_FILE);
        initializeSingletons();
        initializeGameComponents();
        initializePortals();

        batch = new SpriteBatch();

        playerState.setPlayerCenterPos(getCenterPositionOfTile(new IntPosition(11, 5)));


        // todo move that to the ScoreBoard Class
        fontSmall = new BitmapFont();
        fontSmall.getData().setScale(0.4f);
        fontBig = new BitmapFont();
        fontBig.getData().setScale(0.6f);

        stage = new Stage(new ScreenViewport(), batch);

        /*
        try {
            Gdx.app.log("FileHandle", "File exists: " + Gdx.files.internal("skins/uiskin.json").exists());
            FileHandle fileHandle = Gdx.files.internal("skins/uiskin.json");
            Skin skin = new Skin(fileHandle); // Use a skin file for UI styles
        } catch (Exception exception) {
            System.out.println("Exception : " + exception.getMessage()+ " "+exception.getLocalizedMessage() + exception.getStackTrace().toString());
        }
    */















        // Table to arrange labels
        Table table = new Table();
        table.top().left();
        table.setFillParent(false); // The table will take up the whole stage
        table.setPosition(0, Gdx.graphics.getHeight() - table.getHeight());


        // Create your labels
        livesLabel = new Label("Lives: " + lives, new Label.LabelStyle(fontSmall, Color.WHITE));
        livesNumber = new Label("100", new Label.LabelStyle(fontBig, Color.PURPLE));

        ammoLabel = new Label("Ammo: " + ammo, new Label.LabelStyle(fontSmall, Color.WHITE));
        ammoNumber = new Label("30", new Label.LabelStyle(fontBig, Color.PURPLE));



// Add labels to the table for lives
        table.add(livesLabel).pad(10).expandX().left(); // Align livesLabel to left
        //table.add().expandX().fillX(); // This creates a blank cell in between
        table.add(livesNumber).pad(10).expandX().right(); // Align livesNumber to right
        table.row(); // Move to the next row

// Add labels to the table for ammo
        table.add(ammoLabel).pad(10).expandX().left(); // Align ammoLabel to left
        //table.add().expandX().fillX(); // This creates a blank cell in between
        table.add(ammoNumber).pad(10).expandX().right(); // Align ammoNumber to right
        table.row(); // Move to the next row


        stage.addActor(table);

    }

    public void splitUpAndReArrangeHelper() {
        // Load the image file as a texture
        FileHandle fileHandle = Gdx.files.internal("images/zombie.png"); // Your PNG file path here
        Texture texture = new Texture(fileHandle);

        // Get the texture data
        TextureData textureData = texture.getTextureData();

        // Make sure the texture data is prepared
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }


        // Get the texture data and create a Pixmap
        Pixmap pixmap = texture.getTextureData().consumePixmap();


        // Make sure the texture data is prepared
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        // Dimensions of each part
        int width = 128;
        int height = 48; // 192 / 4

        // Create four pixmaps from the original image
        Pixmap part1 = new Pixmap(width, height, pixmap.getFormat());
        Pixmap part2 = new Pixmap(width, height, pixmap.getFormat());
        Pixmap part3 = new Pixmap(width, height, pixmap.getFormat());
        Pixmap part4 = new Pixmap(width, height, pixmap.getFormat());

        // Copy each part from the original pixmap
        part1.drawPixmap(pixmap, 0, 0, 0, 0, width, height);        // Top (part 1)
        part2.drawPixmap(pixmap, 0, 0, 0, height, width, height);   // Second (part 2)
        part3.drawPixmap(pixmap, 0, 0, 0, 2 * height, width, height); // Third (part 3)
        part4.drawPixmap(pixmap, 0, 0, 0, 3 * height, width, height); // Bottom (part 4)

        // Create a new pixmap for the rearranged image
        Pixmap resultPixmap = new Pixmap(width, 192, pixmap.getFormat());

        // Draw the parts in the desired order (1, 4, 2, 3)
        resultPixmap.drawPixmap(part1, 0, 0);                     // Part 1 on top
        resultPixmap.drawPixmap(part4, 0, height);                // Part 4 under Part 1
        resultPixmap.drawPixmap(part2, 0, 2 * height);            // Part 2 under Part 4
        resultPixmap.drawPixmap(part3, 0, 3 * height);            // Part 3 on the bottom

        // Save the new image to a file
        FileHandle outputFile = Gdx.files.local("images/zombie128x192DnUpLtRt.png"); // Output file path
        PixmapIO.writePNG(outputFile, resultPixmap);
        System.out.println("Saved file");
// Print the local storage path for verification
        System.out.println("Saved file at: " + Gdx.files.getLocalStoragePath() + "images/zombie128x192DnUpLtRt.png");
        // Dispose resources
        pixmap.dispose();
        part1.dispose();
        part2.dispose();
        part3.dispose();
        part4.dispose();
        resultPixmap.dispose();
        texture.dispose();


    }

    private void initializePortals() {
        portals = new Portals();
    }

    IntPosition getCenterPositionOfTile(IntPosition tilePosition) {
        return new IntPosition(
            tilePosition.getX() * Tiles.TILE_WIDTH * 2 + Tiles.HALF_TILE_WIDTH,
            tilePosition.getY() * Tiles.TILE_HEIGHT * 2 + Tiles.HALF_TILE_HEIGHT);
    }

    private void initializeGameComponents() {
        setupScreenRelatedStuff();
        setupSoundRelatedStuff();
        setUpInputRelatedStuff();
    }

    private void initializeSingletons() {
        tiles = Tiles.getInstance();
        viewParameters = ViewParameters.getInstance();
        playerState = PlayerState.getInstance();
        pressedKeys = PressedKeys.getInstance();
        sharedVariables = SharedVariables.getInstance();
        playerFrames = PlayerFrames.getInstance();
        collisionDetector = CollisionDetector.getInstance();
    }

    @Override
    public void resize(int width, int height) {
        int viewportHeight = Tiles.TILE_HEIGHT * Tiles.TILE_MAP_ROWS * Tiles.TILE_MAP_SCALE_FACTOR; // Example: Fixed viewport height (in pixels)
        int viewportWidth = Tiles.TILE_WIDTH * Tiles.TILE_MAP_COLS * Tiles.TILE_MAP_SCALE_FACTOR;
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

        if (justOnce) {
            splitUpAndReArrangeHelper();
            justOnce = false;
        }


        drawBackground();
        handleBullets();
        handleZombies();
        handleSkulls();
        if (!viewParameters.isDebugScreen()) drawPlayerFromCenterPosition();


        batch.end();
        if (viewParameters.isDebugScreen()) drawPlayerBorder();
        updateWindowTitle();

        time += Gdx.graphics.getDeltaTime();
        livesLabel.setText("Lives:");//lives
        ammoLabel.setText("Ammo:");//ammo
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void updateWindowTitle() {
        String title = "";
        title += "Graphics Size " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() + "y" + " ";
        title += "XPOS = " + playerState.getXPosTilePlayer() + " YPOS = " + playerState.getYPosTilePlayer() + " ";
        title += "Debug (INS) : " + viewParameters.isDebugScreen() + " ";

        int tileValueUnderMainCharacter = tiles.getBackgroundTileMap()[playerState.getXPosTilePlayer()][playerState.getYPosTilePlayer()];
        title += "Value under = " + tileValueUnderMainCharacter + " ";

        Gdx.graphics.setTitle(title);
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

    private void drawBackground() {
        int rightStopDrawing = (Gdx.graphics.getWidth() + viewParameters.getLeftOffset()) / Tiles.TILE_WIDTH + 200;

        for (int row = 0; row < Tiles.TILE_MAP_ROWS; row++) {
            for (int col = 0; col < Tiles.TILE_MAP_COLS && col < rightStopDrawing; col++) {
                int tileNr = tiles.getBackgroundTileMap()[col][row];
                tileNr = tileNr > 0 ? tileNr - 1 : tileNr;
                batch.draw(tiles.getSourceBackgroundTiles()[tileNr],
                    col * Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR - viewParameters.getLeftOffset(),
                    row * Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR,
                    Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR,
                    Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR);
            }
        }
    }

    private void drawPlayerFromCenterPosition() {
        int direction = calculatePlayerFrameIndex();
        batch.draw(playerFrames.getPlayerFramesSprites()[direction][playerState.getPlayerFrameIndex()],
            playerState.getPlayerCenterPos().getX() - viewParameters.getLeftOffset() - ((Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            playerState.getPlayerCenterPos().getY() - ((Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            PlayerFrames.PLAYER_WIDTH, PlayerFrames.PLAYER_HEIGHT);
    }

    private void handleSkulls() {
        handleSkullsMovement();
        handleSkullFrames();
        handleSkullDrawing();
        handleSkullCollision();
    }

    private void handleSkullCollision() {
        Iterator<Skull> iterator = skullManager.getSkulls().iterator();
        while (iterator.hasNext()) {
            Skull skull = iterator.next();
            if (collisionDetector.isColliding(playerState.getPlayerCenterPos(), skull.getPosition())) {
                //System.out.println("You're so dead");
            } else {
                //System.out.println("I will kill you");
            }
        }
    }

    private void handleSkullsMovement() {
        Random random = new Random();
        for (Skull skull : skullManager.getSkulls()) {
            boolean actStupid = random.nextInt(100) == 2;
            int xPos = skull.getPosition().getX();
            int yPos = skull.getPosition().getY();
            int targetxPos = playerState.getPlayerCenterPos().getX();
            int targetyPos = playerState.getPlayerCenterPos().getY();

            int stupidMoveCounter = skull.getStupidMoveCounter();

            if (actStupid && stupidMoveCounter == 0) {
                stupidMoveCounter = 20 + random.nextInt(20);
                skull.setStupidMoveCounter(stupidMoveCounter);
            }

            if (!actStupid && stupidMoveCounter > 0) {
                stupidMoveCounter--;
                skull.setStupidMoveCounter(stupidMoveCounter);
            }
            boolean commitToStupidMove =  stupidMoveCounter > 0;


            if (xPos < targetxPos && (!commitToStupidMove || random.nextBoolean() )) {
                xPos += skull.getStepSize();
            } else {
                xPos -= skull.getStepSize();
            }
            if (yPos < targetyPos && !commitToStupidMove ) {
                if (random.nextBoolean() ) {
                    yPos += skull.getStepSize();
                }
            } else {

                yPos -= skull.getStepSize();
            }
            skull.setPosition(new IntPosition(xPos, yPos));
        }
    }

    private void handleSkullFrames() {
        for (Skull skull : skullManager.getSkulls()) {
            int waitCycli = skull.getWaitCycli() - 1;
            if (waitCycli <= 0) {

                skull.changeFrameIndexByDirection();



                waitCycli = skull.RESET_TIME_VALUE;
            }
            skull.setWaitCycli(waitCycli);
        }
    }

    private void handleSkullDrawing() {
        for (Skull skull : skullManager.getSkulls()) {
            batch.draw(
                skullManager.getSkullFrame(skull.getFrameIndex()),
                skull.getPosition().getX() - viewParameters.getLeftOffset() - skullManager.SKULL_WIDTH / 2,
                skull.getPosition().getY() - skullManager.SKULL_HEIGHT / 2,
                skullManager.SKULL_WIDTH,
                skullManager.SKULL_HEIGHT);
        }
    }

    private void handleZombies() {
        handleZombiesMovement();
        handleZombiesDrawing();
    }

    private void handleZombiesMovement() {
        for (Zombie zombie : zombieManager.getZombies()) {
            zombie.setTargetTilePosition(playerState.getPlayerTilePosition());
            zombie.move();
        }
    }

    private void handleZombiesDrawing() {
        for (Zombie zombie : zombieManager.getZombies()) {

            batch.draw(
                zombieManager.getZombieFrame(Directions.dn, 1),
                zombie.getPosition().getX() - viewParameters.getLeftOffset() - zombieManager.ZOMBIE_WIDTH / 2,
                zombie.getPosition().getY() - zombieManager.ZOMBIE_HEIGHT / 2,
                zombieManager.ZOMBIE_WIDTH,
                zombieManager.ZOMBIE_HEIGHT);
        }
    }

    private void handleBullets() {
        handleBulletCreation();
        handleBulletsMovement();
        handleBulletsDrawing();
        handleBulletsOutOfPlayField();
        handleBulletsTileCollisions(); // to do implement this more
        handleBulletsEnemyCollisions();
    }

    private void handleBulletsEnemyCollisions() {
        Collection<Skull> newSkulls = new ArrayList<>(); // Store new skulls to be added later
        Random random = new Random();
        int randomCol;// =random.nextInt(351); // 351 is exclusive, so this generates numbers between 0 and 350 inclusive
        int randomRow;
        int randomSpeed;
        Iterator<Bullet> bulletIterator = bulletManager.getBullets().iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Skull> skullIterator = skullManager.getSkulls().iterator();
            while (skullIterator.hasNext()) {
                Skull skull = skullIterator.next();
                if (collisionDetector.isColliding(bullet.getPosition(), skull.getPosition())) {
                    System.out.println("My bullet got you");
                    skullIterator.remove();
                    bulletIterator.remove();
                    randomCol = random.nextInt(Tiles.TILE_MAP_COLS + 1);
                    randomRow = random.nextInt(Tiles.TILE_MAP_ROWS + 1);
                    randomSpeed = random.nextInt(3)+1;
                    IntPosition positionForNewSkull = getCenterPositionOfTile(new IntPosition(randomCol, randomRow));
                    newSkulls.add(new Skull(positionForNewSkull.clone(), Directions.lt,randomSpeed, skullManager.SKULL_COLS_IN_FILE));
                    randomCol = random.nextInt(Tiles.TILE_MAP_COLS + 1);
                    randomRow = random.nextInt(Tiles.TILE_MAP_ROWS + 1);
                    randomSpeed = random.nextInt(3)+1;
                    positionForNewSkull = getCenterPositionOfTile(new IntPosition(randomCol, randomRow));
                    newSkulls.add(new Skull(positionForNewSkull.clone(), Directions.rt ,randomSpeed, skullManager.SKULL_COLS_IN_FILE));
                    break;
                }
                // to do implement this more
            }
        }
        skullManager.addSkulls(newSkulls);
    }

    private void handleBulletsTileCollisions() {
        Iterator<Bullet> iterator = bulletManager.getBullets().iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            IntPosition positionToTest = calculateTilePositionFromPixels(bullet.getPosition());
            if (!tiles.isTileWalkable(positionToTest)) {
                iterator.remove();
                continue;
            }
            // to do implement this more
        }
    }

    private void handleBulletsOutOfPlayField() {
        Iterator<Bullet> iterator = bulletManager.getBullets().iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            IntPosition positionToTest = bullet.getPosition();
            if (positionToTest.getX() > viewParameters.getLeftOffset() + Gdx.graphics.getWidth()) {
                // ToDo checkout if the check to the right boundery is correct, maybe it's to limited ...
                iterator.remove();
                continue;
            }
            if (positionToTest.getX() < viewParameters.getLeftOffset() - bulletManager.BULLET_WIDTH) {
                iterator.remove();
                continue;
            }

            if (positionToTest.getY() < -bulletManager.BULLET_HEIGHT) {
                iterator.remove();
                continue;
            }
            if (positionToTest.getY() > Gdx.graphics.getHeight()) {
                iterator.remove();
            }
        }
    }

    private void handleBulletsDrawing() {
        for (Bullet bullet : bulletManager.getBullets()) {
            batch.draw(
                bulletManager.getBulletFrame(bullet.getDirection().getValue() - 1),
                bullet.getPosition().getX() - viewParameters.getLeftOffset() - bulletManager.BULLET_WIDTH / 2,
                bullet.getPosition().getY() - bulletManager.BULLET_HEIGHT / 2,
                bulletManager.BULLET_WIDTH,
                bulletManager.BULLET_HEIGHT);
        }
    }

    private void handleBulletsMovement() {
        for (Bullet bullet : bulletManager.getBullets()) {
            bullet.move();
        }
    }

    private void handleBulletCreation() {
        if (pressedKeys.fire) {
            IntPosition startPositionBullet = playerState.getPlayerCenterPos().clone();
            switch (playerState.getPlayerPreviousDirection()) {
                case rt:
                    startPositionBullet.addX(Tiles.TILE_WIDTH);
                    startPositionBullet.addY(5);
                    break;
                case lt:
                    startPositionBullet.addY(5);
                    break;
                case up:
                    startPositionBullet.addX(PlayerFrames.PLAYER_WIDTH / 3);
                    break;
                case dn:
                    break;
            }

            bulletManager.addBullet(
                startPositionBullet,
                playerState.getPlayerPreviousDirection(),
                7);
        }
    }

    private int calculatePlayerFrameIndex() {
        int directionValue = playerState.getPlayerCurrentDirection().getValue();
        if (directionValue > 0) {
            playerState.setPlayerPreviousDirection(playerState.getPlayerCurrentDirection());
        } else {
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
        shapeRenderer.rect(playerState.getPlayerCenterPos().getX() - viewParameters.getLeftOffset(), playerState.getPlayerCenterPos().getY(), Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR, Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR); // Draw the rectangle around the sprite

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
        if (pressedKeys.goRight && isValidStartCondition && tiles.isTileWalkable(targetTileX, targetTileY)) {
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
        if (pressedKeys.goLeft && isValidStartCondition && tiles.isTileWalkable(targetTileX, targetTileY)) {
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
        int targetTileY = playerState.getYPosTilePlayer() + 1;
        boolean isValidStartCondition = !isPlayerWalking();
        if (pressedKeys.goUp && isValidStartCondition && tiles.isTileWalkable(targetTileX, targetTileY)) {
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
        if (pressedKeys.goDown && isValidStartCondition && tiles.isTileWalkable(targetTileX, targetTileY)) {
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

    private void setPlayerFrame(int playerIndexFrame) {
        playerIndexFrame = playerIndexFrame % 7; // Ensures indexSoldier wraps around to 0 if it exceeds 6
        playerState.setPlayerFrameIndex(playerIndexFrame);
    }

    private int getPlayerXPosCenter() {
        return playerState.getPlayerCenterPos().getX() + (Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR / 2);
    }

    private int getPlayerYPosCenter() {
        return playerState.getPlayerCenterPos().getY() + (Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR / 2);
    }


    private IntPosition calculateTilePositionFromPixels(IntPosition pixelPosition) {
        int x = pixelPosition.getX() / (Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR);
        int y = pixelPosition.getY() / (Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR);
        return new IntPosition(x, y);
    }

    private void calculatePlayerTilePosition() {

        playerState.setTilePosPlayer(
            calculateTilePositionFromPixels(new IntPosition(getPlayerXPosCenter(), getPlayerYPosCenter())));

        ensurePlayerPositivePosition();
    }

    private void ensurePlayerPositivePosition() {
        playerState.setYPosTilePlayer(Math.max(0, playerState.getYPosTilePlayer()));
        playerState.setXPosTilePlayer(Math.max(0, playerState.getXPosTilePlayer()));
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
}
