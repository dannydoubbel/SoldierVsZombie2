package io.github.SoldierVsZombies;


//
//  ToDo    Clean up images resources folder
//
//
//
//   ToDo   Move entire project to the NAS
//
//
//
//
//
//
//


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    static boolean justOnce = false;
    static boolean justOnce2 = false;
    private SpriteBatch spriteBatch;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SoundManager soundManager;
    private CollisionDetector collisionDetector;
    private SkullManager skullManager;
    private BulletManager bulletManager;
    private ZombieManager zombieManager;
    private SpawnableTypeManager spawnableTypeManager;
    private ScoreBoardManager scoreBoardManager;
    private SharedVariables sharedVariables;

    private PressedKeys pressedKeys;
    private PlayerFrames playerFrames;
    private PlayerState playerState;
    private ViewParameters viewParameters;
    private Tiles tiles;
    private PortalMapManager portalMapManager;

    private static void setUpInputRelatedStuff() {
        Gdx.input.setInputProcessor(new MyInputProcessor());
    }


    @Override
    public void create() {
        String javaVersion = System.getProperty("java.version");
        System.out.println("inside main Java version: " + javaVersion);

        spriteBatch = new SpriteBatch();

        initializeManagerClasses();
        initializeSingletons();
        initializeGameComponents();
        initializePortals();

        setPlayerInitialPosition();
        addInitialEnemies();
        soundManager.playSoundEffect(SoundEffects.background);
    }

    private void initializeManagerClasses() {
        soundManager = new SoundManager();
        bulletManager = new BulletManager();
        zombieManager = new ZombieManager();
        skullManager = new SkullManager();
        spawnableTypeManager = new SpawnableTypeManager();
    }

    private void setPlayerInitialPosition() {
        playerState.setTilePosPlayer(new IntPosition(11,5));
        playerState.setPlayerCenterPos(getPixelPosFromTileCenterPos(new IntPosition(11, 5)));
    }

    private void addInitialEnemies() {
        for (int lus = 0; lus < 10; lus++) {
            addZombieAtRandomWalkablePositionAround(playerState.getPlayerTilePosition(), 10,3, 15);
        }
        skullManager.addSkull(new IntPosition(1350, 350),
            Directions.lt, 1, skullManager.SKULL_COLS_IN_FILE);
        skullManager.addSkull(new IntPosition(150, 250),
            Directions.lt, 1, skullManager.SKULL_COLS_IN_FILE);
    }

    private void addZombieAtRandomWalkablePositionAround(IntPosition startPosition, int minStepsX,int minStepsY, int maxSteps) {
        IntPosition startTilePosition = tiles.getRandomWalkablePositionAround(startPosition, minStepsX,minStepsY, maxSteps);
        IntPosition startPixelPosition = getPixelPosFromTileCenterPos(startTilePosition);
        zombieManager.addZombie(startPixelPosition, startTilePosition);
    }

    private void initializePortals() {

        portalMapManager = new PortalMapManager();
    }

    private void initializeGameComponents() {
        setupScreenRelatedStuff();
      //  setupSoundRelatedStuff();
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
        //mazeSolver =  MazeSolver.getInstance();
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
        if (justOnce2) {
            AddTransparantCrossInPNG addTransparantCrossInPNG = new AddTransparantCrossInPNG();
            justOnce2 = false;
        }

        drawBackground();
        handleGifts();
        handleBullets();
        handleZombies();
        handleSkulls();
        handleGraves(); // todo rename grave to something better
        drawPlayerFromCenterPosition();

        spriteBatch.end();
        updateWindowTitle();

        scoreBoardManager.draw();//must be called after batch.end() !
    }


    @Override
    public void dispose() {
        spriteBatch.dispose();
        soundManager.dispose();
    }

    private void updateWindowTitle() {
        String title = "";
        title += "Graphics Size " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() + "y" + " ";
        title += "XPOS = " + playerState.getXPosTilePlayer() + " YPOS = " + playerState.getYPosTilePlayer() + " ";

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
        //shapeRenderer = new ShapeRenderer();
        Gdx.graphics.setResizable(true);
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
        handleSkullFramesCycles();
        handleSkullDrawing();
        handleSkullPlayerCollision();
    }

    private void handleSkullPlayerCollision() {
        ArrayList<Skull> collidingSkulls =
            skullManager.getCollidingZombiesWithPixelPos(playerState.getPlayerCenterPos());
        if (!collidingSkulls.isEmpty()) {
            //System.out.println("Skull says : You're so dead");
            soundManager.playSoundEffect(SoundEffects.auwScream);
        }
    }


    private void handleSkullsMovement() {
        Random random = new Random();
        for (Skull skull : skullManager.getSkulls()) {
            boolean actStupid = random.nextInt(100) == 2;
            int xPos = skull.getPosition().getX();
            int yPos = skull.getPosition().getY();
            int targetXPos = playerState.getPlayerCenterPos().getX();
            int targetYPos = playerState.getPlayerCenterPos().getY();

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


            if (xPos < targetXPos && (!commitToStupidMove || random.nextBoolean())) {
                xPos += skull.getStepSize();
            } else {
                xPos -= skull.getStepSize();
            }
            if (yPos < targetYPos && !commitToStupidMove) {
                if (random.nextBoolean()) {
                    yPos += skull.getStepSize();
                }
            } else {
                yPos -= skull.getStepSize();
            }
            skull.setPosition(new IntPosition(xPos, yPos));
        }
    }

    private void handleSkullFramesCycles() {
        skullManager.handleSkullFramesCycles();
    }

    private void handleSkullDrawing() {
        for (Skull skull : skullManager.getSkulls()) {
            spriteBatch.draw(
                skullManager.getSkullFrame(skull.getFrameIndex()),
                skull.getPosition().getX() - viewParameters.getLeftOffset() - skullManager.HALF_SKULL_WIDTH,
                skull.getPosition().getY() - skullManager.HALF_SKULL_HEIGHT,
                skullManager.SKULL_WIDTH,
                skullManager.SKULL_HEIGHT);
        }
    }

    private void handleGraves() {
        handleSpawnablesCountDown();
        handleSpawnablesDrawing();
    }

    private void handleSpawnablesCountDown() {
        spawnableTypeManager.handleSpawnablesCountDown();
    }

    private void handleSpawnablesDrawing() {
        for (SpawnableSprite spawnableSprite : spawnableTypeManager.getAllSpawnables()) {
            drawSpawnableFromCenterPosition(spawnableSprite);
            /*
            if (spawnableSprite.getSpawnableType().equals(SpawnableType.PORTAL_SHINE)) {
                //System.out.println("Yes a portal");
            }

             */
        }
    }

    private void drawSpawnableFromCenterPosition(SpawnableSprite spawnableSprite) {
        if (spawnableSprite == null) return;
        spriteBatch.draw(
            spawnableTypeManager.getSpawnableFrame(spawnableSprite),
            spawnableSprite.getPosition().getX() - viewParameters.getLeftOffset() - ((Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            spawnableSprite.getPosition().getY() - ((Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            spawnableTypeManager.DEAD_WIDTH,
            spawnableTypeManager.DEAD_HEIGHT);
    }


    private void handleZombies() {
        handleZombieStartMovement();
        handleZombiesMovement();
        handleZombiesFrames();
        handleZombiesDrawing();
        handleZombiePlayerCollision();
        handleZombieLifeTimeExpired();
    }

    private void handleZombieLifeTimeExpired() {
       Iterator<Zombie> iterator = zombieManager.getZombiesLifeTimeExpired().iterator();
       while (iterator.hasNext()) {
           Zombie zombie = iterator.next();
           //todo maybe add a soundeffect
           if (spawnableTypeManager.getNumberOfGifts() < 3) {
               spawnableTypeManager.addSpawnable(new SpawnableSprite(SpawnableType.BLACK_GIFT, zombie.getPosition(), 5000));
           }
           zombieManager.remove(zombie);
           iterator.remove();
           addZombieAtRandomWalkablePositionAround(playerState.getPlayerTilePosition(),4,3,15);
       }
    }

    private void handleZombiePlayerCollision() {
        ArrayList<Zombie> collidingZombies =
            zombieManager.getCollidingZombiesWithPixelPos(playerState.getPlayerCenterPos());
        if (!collidingZombies.isEmpty()) {
            // System.out.println("Zombie says: You're so dead");
            // To Do implement player energy drain
            soundManager.playSoundEffect(SoundEffects.auwScream);
        }
    }

    private Directions getDirectionToGoTo(IntPosition startPos, IntPosition endPos) {
        if (startPos.getX() < endPos.getX()) {
            return Directions.rt;
        }
        if (startPos.getX() > endPos.getX()) {
            return Directions.lt;
        }
        if (startPos.getY() < endPos.getY()) {
            return Directions.up;
        }
        if (startPos.getY() > endPos.getY()) {
            return Directions.dn;
        }
        return Directions.no;
    }

    private void handleZombieStartMovement() {
        Random random = new Random();
        for (Zombie zombie : zombieManager.getZombies()) {
            if (random.nextInt(10) != 8 && random.nextInt(6) != 4) {
                if (!zombie.isWalking()) {
                    IntPosition tilePosZombie = getTilePositionFromPixelPosition(zombie.getPosition().clone());
                    IntPosition tilePosPlayer = playerState.getPlayerTilePosition();

                    IntPosition newTargetTilePos;
                    if (random.nextInt(10) == 6) { // do stupid
                        newTargetTilePos = getRandomlyPath(tilePosZombie);
                    } else {
                        newTargetTilePos = MazeSolver.findPath(tilePosZombie, tilePosPlayer);
                    }
                    Directions newDirection = getDirectionToGoTo(tilePosZombie, newTargetTilePos);

                    attemptZombieMove(zombie, newTargetTilePos, tilePosZombie, newDirection);
                }
            }
        }
    }

    private void attemptZombieMove(Zombie zombie, IntPosition newTargetTilePos, IntPosition tilePosZombie, Directions newDirection) {
        if (!newTargetTilePos.equals(tilePosZombie)) {
            if (!newDirection.equals(Directions.no)) {
                if (tiles.isTileWalkable(newTargetTilePos)) {
                    if (!zombieManager.isTargetTileOccupiedByOtherZombie(zombie, newTargetTilePos)) {
                        zombie.startWalking(newTargetTilePos, newDirection);
                    }

                }
            }
        }
    }

    private IntPosition getRandomlyPath(IntPosition tilePos) {
        Random random = new Random();
        IntPosition resultPosition = tilePos.clone();

         switch (random.nextInt(4)) {
            case 0:
                resultPosition.addY(1);
                break;
            case 1:
                resultPosition.addX(1);
                break;
            case 2:
                resultPosition.addX(-1);
                break;
            default:
                resultPosition.addY(-1);
        }

        if (tiles.isTileWalkable(resultPosition)) {
            return resultPosition;
        }
        return tilePos;
    }


    private void handleZombiesMovement() {
        for (Zombie zombie : zombieManager.getZombies()) {
            if (!zombie.isWalking()) {
                continue;
            }
            IntPosition centerTilePosPixels = getPixelPosFromTileCenterPos(zombie.getTargetTilePosition());
            boolean zombieOnCenterTargetTile = false;
            int zombieX = zombie.getPosition().getX();
            int zombieY = zombie.getPosition().getY();
            Orthogonal orthogonal = getOrthogonalFrom(zombie.getDirection());
            if (!orthogonal.equals(Orthogonal.NOTHING)) {
                if (orthogonal.equals(Orthogonal.HORIZONTAL)) {
                    zombieOnCenterTargetTile = Math.abs(zombieX - centerTilePosPixels.getX()) <= zombie.getStepSize() + 2;
                }
                if (orthogonal.equals(Orthogonal.VERTICAL)) {
                    zombieOnCenterTargetTile = Math.abs(zombieY - centerTilePosPixels.getY()) <= zombie.getStepSize() + 2;
                }
            }

            if (zombieOnCenterTargetTile) {
                zombie.setPosition(centerTilePosPixels);
                zombie.setWalking(false);
            }

            if (zombie.isWalking()) {
                zombie.move();
            }

        }
    }

    private void handleZombiesFrames() {
        for (Zombie zombie : zombieManager.getZombies()) {
            zombieCalculateAndSetNewFrame(zombie);
        }
    }

    private void zombieCalculateAndSetNewFrame(Zombie zombie) {
        if (zombie.isWalking()) {
            int frameIndex = zombie.getFrameIndex() - 1;
            if (frameIndex < 0) {
                frameIndex = zombie.MAX_FRAMES - 1;
            }
            zombie.setFrameIndex(frameIndex);
        }
    }


    private void handleZombiesDrawing() {
        for (Zombie zombie : zombieManager.getZombies()) {
            drawZombieFromCenterPosition(zombie);
        }
    }

    private void drawZombieFromCenterPosition(Zombie zombie) {
        spriteBatch.draw(
            zombieManager.getZombieFrame(zombie.getPreviousDirection(), zombie.getFrameIndex()),
            zombie.getPosition().getX() - viewParameters.getLeftOffset() - ((Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            zombie.getPosition().getY() - ((Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            zombieManager.ZOMBIE_WIDTH,
            zombieManager.ZOMBIE_HEIGHT);
    }

    private void handleGifts(){
        Iterator<SpawnableSprite> iterator = spawnableTypeManager.getAllSpawnables().iterator();
        while (iterator.hasNext()) {
            SpawnableSprite gift = iterator.next();
            if (gift.getSpawnableType().equals(SpawnableType.BLACK_GIFT)) {
                if (collisionDetector.isColliding(playerState.getPlayerCenterPos(), gift.getPosition())) {
                    System.out.println("Gift collision"); // TODO do something with the gift
                    soundManager.playSoundEffect(SoundEffects.howo);
                    iterator.remove();
                }
            }
        }
    }



    private void handleBullets() {
        handleBulletCreation();
        handleBulletsMovement();
        handleBulletsDrawing();
        handleBulletsOutOfPlayField();
        handleBulletsTileCollisions();
        handleBulletsEnemyCollisions();
    }

    private void handleBulletsEnemyCollisions() {
        handleBulletSkullCollisions();
        handleBulletZombieCollisions();
    }

    private void handleBulletZombieCollisions() {
        Iterator<Bullet> bulletIterator = bulletManager.getBullets().iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Zombie> zombieIterator = zombieManager.getZombies().iterator();
            while (zombieIterator.hasNext()) {
                Zombie zombie = zombieIterator.next();
                if (collisionDetector.isColliding(bullet.getPosition(), zombie.getPosition())) {

                    spawnableTypeManager.addSpawnable(new SpawnableSprite(SpawnableType.DEAD_ZOMBIE, zombie.getPosition(), 1000));
                    //soundEffectZombieIsShot.setVolume(100);
                    soundManager.stopSoundEffect(SoundEffects.zombieIsHit);
                    soundManager.playSoundEffect(SoundEffects.zombieIsHit);
                    zombieIterator.remove();
                    bulletIterator.remove();
                    addZombieAtRandomWalkablePositionAround(playerState.getPlayerTilePosition(), 5,3, 20);
                    scoreBoardManager.addKills(+1);
                    scoreBoardManager.addAmmo(+100);
                    break;
                }
                // to do implement this more
            }
        }
    }

    private void handleBulletSkullCollisions() {
        Iterator<Bullet> bulletIterator = bulletManager.getBullets().iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Skull> skullIterator = skullManager.getSkulls().iterator();
            while (skullIterator.hasNext()) {
                Skull skull = skullIterator.next();
                if (collisionDetector.isColliding(bullet.getPosition(), skull.getPosition())) {
                    spawnableTypeManager.addSpawnable(new SpawnableSprite(SpawnableType.DEAD_SKULL, skull.getPosition(), 1000));
                    skullIterator.remove();
                    bulletIterator.remove();
                    scoreBoardManager.addKills( + 1);
                    scoreBoardManager.addAmmo( + 100);
                    if (skullManager.getNumberOfSkulls() < 15)
                        skullManager.addSkull(generateRandomSkull());
                    if (skullManager.getNumberOfSkulls() < 15)
                            skullManager.addSkull(generateRandomSkull());
                    break;
                }
            }
        }
    }

    private Skull generateRandomSkull() {
        Random random = new Random();
        IntPosition randomTilePosition = new IntPosition(tiles.getRandomColNumber(), tiles.getRandomRowNumber());
        int randomSpeed = random.nextInt(3) + 1;
        IntPosition positionForNewSkull = getPixelPosFromTileCenterPos(randomTilePosition.clone());
        return new Skull(positionForNewSkull.clone(), Directions.lt, randomSpeed, skullManager.SKULL_COLS_IN_FILE);
    }

    private void handleBulletsTileCollisions() {
        Iterator<Bullet> iterator = bulletManager.getBullets().iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            IntPosition positionToTest = getTilePositionFromPixelPosition(bullet.getPosition());
            if (!tiles.isTileWalkable(positionToTest)) {
                iterator.remove();
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
                bullet.getPosition().getX() - viewParameters.getLeftOffset() - bulletManager.HALF_BULLET_WIDTH,
                bullet.getPosition().getY() - bulletManager.HALF_BULLET_HEIGHT,
                bulletManager.BULLET_WIDTH,
                bulletManager.BULLET_HEIGHT);
        }
    }

    private void handleBulletsMovement() {
        bulletManager.handleBulletMovement();
    }

    // SEE https://www.deviantart.com/chi171812/art/Surprise-Sprite-RPG-Maker-Princess-Merida-477413073
    // https://www.deviantart.com/chi171812/art/2p-Hetalia-RPG-Maker-2p-Italy-sprites-461806087
    // https://www.deviantart.com/merowynn/art/Princess-Mercury-Sprites-399524525


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

            int ammo = scoreBoardManager.getAmmo() - 1;
            ammo = Math.max(ammo, 0);
            scoreBoardManager.setAmmo(ammo);
            if (ammo > 0) {
                soundManager.stopSoundEffect(SoundEffects.singleShot);
                soundManager.playSoundEffect(SoundEffects.singleShot);
                scoreBoardManager.setAmmoFired(scoreBoardManager.getAmmoFired() + 1);
                bulletManager.addBullet(
                    startPositionBullet,
                    playerState.getPlayerPreviousDirection(),
                    7);
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
            playerState.getPlayerTargetCenterPos().setPosition(getPixelPosFromTileCenterPos(new IntPosition(targetTileX, targetTileY)));

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
            playerState.getPlayerTargetCenterPos().setPosition(getPixelPosFromTileCenterPos(new IntPosition(targetTileX, targetTileY)));
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
            playerState.getPlayerTargetCenterPos().setPosition(getPixelPosFromTileCenterPos(new IntPosition(targetTileX, targetTileY)));
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
            playerState.getPlayerTargetCenterPos().setPosition(getPixelPosFromTileCenterPos(new IntPosition(targetTileX, targetTileY)));
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
        handlePortalsSteppedOn();
        handlePortalsOpenClosedState();
    }

    private void handlePortalsOpenClosedState() {
        for (PortalMap portalMap : portalMapManager.getPortalMaps()) {
            if (portalMap.isBeyondOutOfOrderTime()) {
                tiles.setTile(portalMap.getEntryPosition(), tiles.TILE_PORTAL_OPEN);
            } else {
                tiles.setTile(portalMap.getEntryPosition(), tiles.TILE_PORTAL_CLOSE);
            }
        }
    }

    private void handlePortalsSteppedOn() {
        if (!isPlayerWalking()) {
            PortalMap portalMapSteppedOn = portalMapManager.getPortalMapSteppedOn(playerState.getPlayerTilePosition());
            if (!portalMapSteppedOn.equals(portalMapManager.illegalPortalMap)) {
                if (portalMapSteppedOn.isBeyondOutOfOrderTime()) {
                    portalMapSteppedOn.startOutOfOrder();
                    IntPosition newPlayerPos = getPixelPosFromTileCenterPos(portalMapSteppedOn.getOutComePosition());
                    playerState.setPlayerCenterPos(newPlayerPos);
                    playerState.setPlayerTargetCenterPos(newPlayerPos);
                    soundManager.playSoundEffect(SoundEffects.teleport);
                    spawnableTypeManager.addSpawnable(new SpawnableSprite(SpawnableType.PORTAL_SHINE,getPixelPosFromTileCenterPos(portalMapSteppedOn.getOutComePosition()),2500));
                    spawnableTypeManager.addSpawnable(new SpawnableSprite(SpawnableType.PORTAL_SHINE,getPixelPosFromTileCenterPos(portalMapSteppedOn.getEntryPosition()),500));
                }
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

    private IntPosition getPixelPosFromTileCenterPos(IntPosition tilePosition) {
        return new IntPosition(
            tilePosition.getX() * Tiles.TILE_WIDTH * 2 + Tiles.HALF_TILE_WIDTH,
            tilePosition.getY() * Tiles.TILE_HEIGHT * 2 + Tiles.HALF_TILE_HEIGHT);
    }

    private IntPosition getTilePositionFromPixelPosition(IntPosition pixelPosition) {
        int x = pixelPosition.getX() / (Tiles.TILE_WIDTH * Tiles.TILE_MAP_SCALE_FACTOR);
        int y = pixelPosition.getY() / (Tiles.TILE_HEIGHT * Tiles.TILE_MAP_SCALE_FACTOR);
        return new IntPosition(x, y);
    }

    private void calculatePlayerTilePosition() {
        playerState.setTilePosPlayer(
            getTilePositionFromPixelPosition(new IntPosition(getPlayerXPosCenter(), getPlayerYPosCenter())));
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
            if (soundManager.isPlayingSoundEffect(SoundEffects.background)) {
                soundManager.pauseSoundEffect(SoundEffects.background);
            } else {
                soundManager.playSoundEffect(SoundEffects.background);
            }
            sharedVariables.musicAllowed = false;
        }
    }

    private Orthogonal getOrthogonalFrom(Directions directions) {
        if (directions.equals(Directions.lt) || (directions.equals(Directions.rt))) {
            return Orthogonal.HORIZONTAL;
        }
        if (directions.equals(Directions.dn) || (directions.equals(Directions.up))) {
            return Orthogonal.VERTICAL;
        }
        return Orthogonal.NOTHING;

    }
}
