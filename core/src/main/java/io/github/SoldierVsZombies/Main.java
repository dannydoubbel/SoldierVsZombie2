package io.github.SoldierVsZombies;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
    private SpriteBatch spriteBatch;
    //private Sprite[] sourceBackgroundTiles;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private Music backgroundMusic;
    private Music soundEffect;

    private CollisionDetector collisionDetector;
    private SkullManager skullManager;
    private BulletManager bulletManager;
    private ZombieManager zombieManager;
    private ScoreBoardManager scoreBoardManager;
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

    @Override
    public void create() {

        spriteBatch = new SpriteBatch();

        bulletManager = new BulletManager();
        zombieManager = new ZombieManager();
        skullManager = new SkullManager();
        initializeSingletons();
        initializeGameComponents();
        initializePortals();

        addInitialEnemies();

        setPlayerInitialPosition();
    }

    private void setPlayerInitialPosition() {
        playerState.setPlayerCenterPos(getCenterPositionOfTile(new IntPosition(11, 5)));
    }

    private void addInitialEnemies() {
        zombieManager.addZombie(new IntPosition(200, 200));
        skullManager.addSkull(new IntPosition(350, 350),
            Directions.lt, 1, skullManager.SKULL_COLS_IN_FILE);
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
        scoreBoardManager = ScoreBoardManager.getInstance(spriteBatch);
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
        spriteBatch.setProjectionMatrix(camera.combined);

        handlePlayerMovement();
        handlePortals();
        handleZoomKeyPress();
        handleMusicKeyPress();

        spriteBatch.begin();

        if (justOnce) {
            SplitUpAndRearangePNGs splitUpAndRearangePNGs = new SplitUpAndRearangePNGs();
            splitUpAndRearangePNGs.splitUpAndReArrangeHelper();
            justOnce = false;
        }

        drawBackground();
        handleBullets();
        handleZombies();
        handleSkulls();
        if (!viewParameters.isDebugScreen()) drawPlayerFromCenterPosition();

        spriteBatch.end();
        if (viewParameters.isDebugScreen()) drawPlayerBorder();
        updateWindowTitle();

        scoreBoardManager.draw();//must be called after batch.end() !
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundMusic.dispose();
        soundEffect.dispose();
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
        soundEffect = Gdx.audio.newMusic(Gdx.files.internal("sound/gun-single.mp3"));
        backgroundMusic.play();
        backgroundMusic.setLooping(true); // Loop the background music
    }

    private void drawBackground() {
        int rightStopDrawing = (Gdx.graphics.getWidth() + viewParameters.getLeftOffset()) / Tiles.TILE_WIDTH + 200;

        for (int row = 0; row < Tiles.TILE_MAP_ROWS; row++) {
            for (int col = 0; col < Tiles.TILE_MAP_COLS && col < rightStopDrawing; col++) {
                int tileNr = tiles.getBackgroundTileMap()[col][row];
                tileNr = tileNr > 0 ? tileNr - 1 : tileNr;
                spriteBatch.draw(tiles.getSourceBackgroundTiles()[tileNr],
                    col * Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR - viewParameters.getLeftOffset(),
                    row * Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR,
                    Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR,
                    Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR);
            }
        }
    }

    private void drawPlayerFromCenterPosition() {
        int direction = calculatePlayerFrameIndex();
        spriteBatch.draw(playerFrames.getPlayerFramesSprites()[direction][playerState.getPlayerFrameIndex()],
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
            boolean commitToStupidMove = stupidMoveCounter > 0;


            if (xPos < targetxPos && (!commitToStupidMove || random.nextBoolean())) {
                xPos += skull.getStepSize();
            } else {
                xPos -= skull.getStepSize();
            }
            if (yPos < targetyPos && !commitToStupidMove) {
                if (random.nextBoolean()) {
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
            spriteBatch.draw(
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
            Directions directionTowardsPlayer = getZombieDirectionTowardsPlayer(zombie);
            if (zombie.isWalking()) {
                zombie.move();
                zombie.setDirection(directionTowardsPlayer);
            }

            System.out.print("Xpos " + zombie.getPosition().getX() + " tile x " + calculateTilePositionFromPixels(zombie.getPosition()).getX());
            System.out.println(" Ypos " + zombie.getPosition().getY() + " tile y " + calculateTilePositionFromPixels(zombie.getPosition()).getY());
            if (directionTowardsPlayer != Directions.no) {
                zombieCalculateAndSetNewFrame(zombie);
            }
        }
    }

    private  void zombieCalculateAndSetNewFrame(Zombie zombie) {
        int frameIndex = zombie.getFrameIndex() - 1;
        if (frameIndex < 0) {
            frameIndex = zombie.MAX_FRAMES - 1;
        }
        zombie.setFrameIndex(frameIndex);
    }

    private Directions getZombieDirectionTowardsPlayer(Zombie zombie) {
        IntPosition zombieTilePos = calculateTilePositionFromPixels(zombie.getPosition());
        IntPosition playerTilePos = calculateTilePositionFromPixels(new IntPosition(getPlayerXPosCenter(), getPlayerYPosCenter()));

        if (!zombie.isWalking()) {
            return zombieStartWalking(zombie, zombieTilePos, playerTilePos);
        }

        if (zombieTilePos.equals(zombie.getTargetTilePosition())) {
            zombie.setWalking(false);
            zombie.setDirection(Directions.no);
            System.out.println("Equals zombie and player");
            //zombie.setPosition(getCenterPositionOfTile(zombieTilePos).clone());
        }
        return zombie.isWalking() ? zombie.getDirection() : Directions.no; // todo change as needed
    }

    private Directions zombieStartWalking(Zombie zombie, IntPosition zombieTilePos, IntPosition playerTilePos) {
        Directions newDirection = Directions.no;
        boolean isZombieXPosEqualToPlayerXPos = zombieTilePos.getX() == playerTilePos.getX();
        boolean isZombieYPosEqualToPlayerYPos = zombieTilePos.getY() == playerTilePos.getY();
        if (isZombieXPosEqualToPlayerXPos && isZombieYPosEqualToPlayerYPos) {
            return newDirection;
        }
        zombie.setWalking(true);
        if (isZombieXPosEqualToPlayerXPos) {
            newDirection = (zombieTilePos.getY() < playerTilePos.getY()) ? Directions.up : Directions.dn;
        }
        if (isZombieYPosEqualToPlayerYPos) {
            newDirection = (zombieTilePos.getX() < playerTilePos.getX()) ? Directions.rt : Directions.lt;
        }
        zombieSetNewTargetTilePosition(zombie, zombieTilePos, newDirection);
        zombie.setDirection(newDirection);
        return newDirection;
    }

    private void zombieSetNewTargetTilePosition(Zombie zombie, IntPosition zombieTilePos, Directions newDirection) {
        IntPosition targetPosition = new IntPosition(zombieTilePos);
        switch (newDirection) {
            case lt: // HERE
                targetPosition.addX(-1);
                break;
            case rt:
                targetPosition.addX(1);
                break;
            case dn:
                targetPosition.addY(-1);
                break;
            case up:
                targetPosition.addY(1);
                break;
        }
        zombie.setTargetTilePosition(targetPosition);
    }


    private void handleZombiesDrawing() {
        for (Zombie zombie : zombieManager.getZombies()) {
            drawZombieFromCenterPosition(zombie);
        }
    }

    private void drawZombieFromCenterPosition(Zombie zombie) {
        spriteBatch.draw(
            zombieManager.getZombieFrame(Directions.dn, zombie.getFrameIndex()),
            zombie.getPosition().getX() - viewParameters.getLeftOffset() - ((Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            zombie.getPosition().getY() - ((Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            zombieManager.ZOMBIE_WIDTH,
            zombieManager.ZOMBIE_HEIGHT);
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
                    //ystem.out.println("My bullet got you");
                    skullIterator.remove();
                    bulletIterator.remove();
                    randomCol = random.nextInt(Tiles.TILE_MAP_COLS + 1);
                    randomRow = random.nextInt(Tiles.TILE_MAP_ROWS + 1);
                    randomSpeed = random.nextInt(3) + 1;
                    IntPosition positionForNewSkull = getCenterPositionOfTile(new IntPosition(randomCol, randomRow));
                    newSkulls.add(new Skull(positionForNewSkull.clone(), Directions.lt, randomSpeed, skullManager.SKULL_COLS_IN_FILE));
                    randomCol = random.nextInt(Tiles.TILE_MAP_COLS + 1);
                    randomRow = random.nextInt(Tiles.TILE_MAP_ROWS + 1);
                    randomSpeed = random.nextInt(3) + 1;
                    positionForNewSkull = getCenterPositionOfTile(new IntPosition(randomCol, randomRow));
                    newSkulls.add(new Skull(positionForNewSkull.clone(), Directions.rt, randomSpeed, skullManager.SKULL_COLS_IN_FILE));
                    scoreBoardManager.setKills(scoreBoardManager.getKills() + 1);
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
            spriteBatch.draw(
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

            int ammo = scoreBoardManager.getAmmoLeft() - 1;
            ammo = Math.max(ammo, 0);
            scoreBoardManager.setAmmoLeft(ammo);
            if (ammo > 0) {
                if (!soundEffect.isPlaying()) {
                    soundEffect.play();
                }
                scoreBoardManager.setAmmoFired(scoreBoardManager.getAmmoFired() + 1);
                bulletManager.addBullet(
                    startPositionBullet,
                    playerState.getPlayerPreviousDirection(),
                    7);
            } else {
                // todo implement this
            }
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
