package io.github.SoldierVsZombies;


//  // https://sanderfrenken.github.io/Universal-LPC-Spritesheet-Character-Generator/#?body=Body_color_light&head=Human_female_light&sex=female&ears=Elven_ears_light&eyes=Eyes_blue&dress=Sash_dress_leather&clothes=TShirt_VNeck_blue&vest=Corset_blue&shoes_plate=Boots_Metal_Plating_steel&shoes=Boots_black&hair=Ponytail_ginger&earring_left=Simple_Earring_Left_gold

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
    private SpriteBatch spriteBatch;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SoundManager soundManager;
    private CollisionDetector collisionDetector;
    private SkullManager skullManager;
    private BulletManager bulletManager;
    private ZombieManager zombieManager;
    private ShortLifeTimeSpriteTypeManager shortLifeTimeSpriteTypeManager;
    private ScoreBoardManager scoreBoardManager;
    private SharedVariables sharedVariables;

    private PressedKeys pressedKeys;
    private PlayerState playerState;
    private ViewParameters viewParameters;
    private TileManager tileManager;
    private PortalMapManager portalMapManager;

    private IndicatorBar energyIndicator;
    private IndicatorBar livesIndicator;
    private IndicatorBar amunitionIndicator;
    private IndicatorBar killsIndicator;
    private IndicatorBar timerIndicator;


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
        energyIndicator = new IndicatorBar("Energy",10,10,250,15);
        livesIndicator = new IndicatorBar("Lives",10,10+15+5,250,15);
        amunitionIndicator = new IndicatorBar("Amunition",10,10+20+20,250,15);
        killsIndicator = new IndicatorBar("Kills",10,10+20+20+20,250,15);
        timerIndicator = new IndicatorBar("Time left",10,10+20+20+20+20,250,15);

        soundManager.playSoundEffect(SoundEffects.background, 0.5f);
    }

    private void initializeManagerClasses() {
        soundManager = new SoundManager();
        bulletManager = new BulletManager();
        zombieManager = new ZombieManager();
        skullManager = new SkullManager();
        shortLifeTimeSpriteTypeManager = new ShortLifeTimeSpriteTypeManager();
    }

    private void setPlayerInitialPosition() {
        playerState.setTilePosPlayer(new IntPosition(11, 5));
        playerState.setPlayerCenterPos(getPixelPosFromTileCenterPos(new IntPosition(11, 5)));
    }

    private void addInitialEnemies() {
        addInitialZombies();
        addInitialSkulls();

        shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(new ShortLifeTimeSprite(ShortLifeTimeSpriteType.WOOD_FIRE,new IntPosition(200,200),10000));
    }

    private void addInitialSkulls() {
        skullManager.addSkull(new IntPosition(1350, 350),
            Directions.lt, 1, skullManager.SKULL_COLS_IN_FILE);
        skullManager.addSkull(new IntPosition(150, 250),
            Directions.lt, 1, skullManager.SKULL_COLS_IN_FILE);
    }

    private void addInitialZombies() {
        for (int lus = 0; lus < 10; lus++) {
            addZombieAtRandomWalkablePositionAround(playerState.getPlayerTilePosition(), 10, 3, 15);
        }
    }

    private void addZombieAtRandomWalkablePositionAround(IntPosition startPosition, int minStepsX, int minStepsY, int maxSteps) {
        IntPosition startTilePosition = tileManager.getRandomWalkablePositionAround(startPosition, minStepsX, minStepsY, maxSteps);
        IntPosition startPixelPosition = getPixelPosFromTileCenterPos(startTilePosition);
        zombieManager.addZombie(startPixelPosition, startTilePosition);
    }

    private void initializePortals() {

        portalMapManager = new PortalMapManager();
        handleFlamingTorchCreation();
    }

    private void handleFlamingTorchCreation() {
        for (PortalMap portalMap : portalMapManager.getPortalMaps()) {
            int x1 = portalMap.getEntryPosition().getX()-1;
            int y1 = portalMap.getEntryPosition().getY();
            IntPosition leftTorchPosition = getPixelPosFromTileCenterPos(new IntPosition(x1, y1));
            leftTorchPosition.addX(15);
            int x2 = portalMap.getEntryPosition().getX()+1;
            int y2 = portalMap.getEntryPosition().getY();
            IntPosition rightTorchPosition = getPixelPosFromTileCenterPos(new IntPosition(x2, y2));
            rightTorchPosition.addX(15);
            ShortLifeTimeSprite leftTorchToAdd = new ShortLifeTimeSprite(ShortLifeTimeSpriteType.FLAMING_TORCH, leftTorchPosition,-1);
            ShortLifeTimeSprite rightTorchToAdd = new ShortLifeTimeSprite(ShortLifeTimeSpriteType.FLAMING_TORCH, rightTorchPosition,-1);
            shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(leftTorchToAdd);
            shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(rightTorchToAdd);
        }
    }

    private void initializeGameComponents() {
        setupScreenRelatedStuff();
        //  setupSoundRelatedStuff();
        setUpInputRelatedStuff();
    }

    private void initializeSingletons() {
        tileManager = TileManager.getInstance();
        viewParameters = ViewParameters.getInstance();
        playerState = PlayerState.getInstance();
        pressedKeys = PressedKeys.getInstance();
        sharedVariables = SharedVariables.getInstance();
        collisionDetector = CollisionDetector.getInstance();
        scoreBoardManager = ScoreBoardManager.getInstance(spriteBatch);
    }

    @Override
    public void resize(int width, int height) {
        int viewportHeight = TileManager.TILE_HEIGHT * TileManager.TILE_MAP_ROWS * TileManager.TILE_MAP_SCALE_FACTOR; // Example: Fixed viewport height (in pixels)
        int viewportWidth = TileManager.TILE_WIDTH * TileManager.TILE_MAP_COLS * TileManager.TILE_MAP_SCALE_FACTOR;
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
        handleGifts();
        handleFireWoodCollision();
        handleBullets();
        handleZombies();
        handleSkulls();
        handleAllShortLifeTimeSprites();
        drawPlayerFromCenterPosition();
        energyIndicator.render(spriteBatch,15,true);
        livesIndicator.render(spriteBatch,5,false);
        amunitionIndicator.render(spriteBatch,1000,false);
        killsIndicator.render(spriteBatch,500,false);
        timerIndicator.render(spriteBatch,100,false);
        scoreBoardManager.render(spriteBatch);//must be called after batch.end() !
        updateWindowTitle();
    }


    @Override
    public void dispose() {
        spriteBatch.dispose();
        soundManager.dispose();
        energyIndicator.dispose();
        livesIndicator.dispose();
        amunitionIndicator.dispose();
        killsIndicator.dispose();
        timerIndicator.dispose();
    }

    private void updateWindowTitle() {
        String title = "";
        title += "Graphics Size " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() + "y" + " ";
        title += "X pos = " + playerState.getXPosTilePlayer() + " Y pos = " + playerState.getYPosTilePlayer() + " ";

        int tileValueUnderMainCharacter = tileManager.getBackgroundTileMap()[playerState.getXPosTilePlayer()][playerState.getYPosTilePlayer()];
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
        int rightStopDrawing = (Gdx.graphics.getWidth() + viewParameters.getLeftOffset()) / TileManager.TILE_WIDTH + 200;

        for (int row = 0; row < TileManager.TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TileManager.TILE_MAP_COLS && col < rightStopDrawing; col++) {
                int tileNr = tileManager.getBackgroundTileMap()[col][row];
                tileNr = tileNr > 0 ? tileNr - 1 : tileNr;
                spriteBatch.draw(tileManager.getSourceBackgroundTiles()[tileNr],
                    col * TileManager.TILE_WIDTH * TileManager.TILE_MAP_SCALE_FACTOR - viewParameters.getLeftOffset(),
                    row * TileManager.TILE_HEIGHT * TileManager.TILE_MAP_SCALE_FACTOR,
                    TileManager.TILE_WIDTH * TileManager.TILE_MAP_SCALE_FACTOR,
                    TileManager.TILE_HEIGHT * TileManager.TILE_MAP_SCALE_FACTOR);
            }
        }
    }

    private void drawPlayerFromCenterPosition() {
        spriteBatch.draw(PlayerState.getPlayerFrames(calculatePlayerFrameIndex()),
            playerState.getPlayerCenterPos().getX() - viewParameters.getLeftOffset() - ((TileManager.TILE_WIDTH * TileManager.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            playerState.getPlayerCenterPos().getY() - ((TileManager.TILE_HEIGHT * TileManager.TILE_MAP_SCALE_FACTOR) / 2) + (15),
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
            soundManager.playSoundEffect(SoundEffects.auwScream, 100f);
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

    private void handleAllShortLifeTimeSprites() {
        handleShortLifeTimeSpritesCountDown();
        handleShortLifeTimeSpritesDrawing();
    }

    private void handleShortLifeTimeSpritesCountDown() {
        shortLifeTimeSpriteTypeManager.handleShortLifeTimeSpritesCountDown();
    }

    private void handleShortLifeTimeSpritesDrawing() {
        for (ShortLifeTimeSprite shortLifeTimeSprite : shortLifeTimeSpriteTypeManager.getAllShortLifeTimeSprites()) {
            drawShortLifeTimeSpritesFromCenterPosition(shortLifeTimeSprite);
        }
    }

    private void drawShortLifeTimeSpritesFromCenterPosition(ShortLifeTimeSprite shortLifeTimeSprite) {
        if (shortLifeTimeSprite == null) return;
        spriteBatch.draw(
            shortLifeTimeSpriteTypeManager.getShortLifeTimeFrame(shortLifeTimeSprite),
            shortLifeTimeSprite.getPosition().getX() - viewParameters.getLeftOffset() - ((TileManager.TILE_WIDTH * TileManager.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            shortLifeTimeSprite.getPosition().getY() - ((TileManager.TILE_HEIGHT * TileManager.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            shortLifeTimeSprite.getShortLifeTimeSpriteType().DRAW_WIDTH,
            shortLifeTimeSprite.getShortLifeTimeSpriteType().DRAW_HEIGHT);
    }


    private void handleZombies() {
        handleZombieStartMovement();
        handleZombiesMovement();
        handleZombiesFrames();
        handleZombiesDrawing();
        handleZombiePlayerCollision();
        handleZombieLifeTimeExpired();
        handleZombieRebirth();
    }

    private void handleZombieRebirth(){
        Random random = new Random();
        if (zombieManager.getZombies().size() < 10 && random.nextInt(80) == 3 && random.nextBoolean() ) {
            addZombieAtRandomWalkablePositionAround(playerState.getPlayerTilePosition(), 4, 3, 15);
        }
    }

    private void handleZombieLifeTimeExpired() {
        Iterator<Zombie> iterator = zombieManager.getZombiesLifeTimeExpired().iterator();
        while (iterator.hasNext()) {
            Zombie zombie = iterator.next();
            //todo maybe add a soundEffect
            if (shortLifeTimeSpriteTypeManager.getNumberOfGifts() < 3) {
                shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(new ShortLifeTimeSprite(ShortLifeTimeSpriteType.BLACK_GIFT, zombie.getPosition(), 5000));
            }
            zombieManager.remove(zombie);
            iterator.remove();

        }
    }

    private void handleZombiePlayerCollision() {
        ArrayList<Zombie> collidingZombies =
            zombieManager.getCollidingZombiesWithPixelPos(playerState.getPlayerCenterPos());
        if (!collidingZombies.isEmpty()) {
            // System.out.println("Zombie says: You're so dead");
            // To Do implement player energy drain
            soundManager.playSoundEffect(SoundEffects.auwScream, 100f);
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
                if (tileManager.isTileWalkable(newTargetTilePos)) {
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

        if (tileManager.isTileWalkable(resultPosition)) {
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
            zombie.getPosition().getX() - viewParameters.getLeftOffset() - ((TileManager.TILE_WIDTH * TileManager.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            zombie.getPosition().getY() - ((TileManager.TILE_HEIGHT * TileManager.TILE_MAP_SCALE_FACTOR) / 2) + (15),
            zombieManager.ZOMBIE_WIDTH,
            zombieManager.ZOMBIE_HEIGHT);
    }

    private void handleGifts() {
        Iterator<ShortLifeTimeSprite> iterator = shortLifeTimeSpriteTypeManager.getAllShortLifeTimeSprites().iterator();
        ArrayList<ShortLifeTimeSprite> shortLifeTimeSpritesToAdd = new ArrayList<>();
        while (iterator.hasNext()) {
            ShortLifeTimeSprite gift = iterator.next();
            if (gift.getShortLifeTimeSpriteType().equals(ShortLifeTimeSpriteType.BLACK_GIFT)) {
                if (collisionDetector.isColliding(playerState.getPlayerCenterPos(), gift.getPosition())) {
                    shortLifeTimeSpritesToAdd.add(new ShortLifeTimeSprite(ShortLifeTimeSpriteType.WOW_YELL, gift.getPosition().clone(), 2000));
                    System.out.println("Gift collision"); // TODO do something with the gift
                    soundManager.playSoundEffect(SoundEffects.howo, 100f);
                    iterator.remove();
                }
            }
        }
        for (ShortLifeTimeSprite shortLifetimeSprite : shortLifeTimeSpritesToAdd) {
            shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(shortLifetimeSprite);
        }
    }


    private void handleBullets() {
        handleBulletCreation();
        handleFireCreation();
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

                    shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(new ShortLifeTimeSprite(ShortLifeTimeSpriteType.DEAD_ZOMBIE, zombie.getPosition(), 1000));
                    //soundEffectZombieIsShot.setVolume(100);
                    soundManager.stopSoundEffect(SoundEffects.zombieIsHit);
                    soundManager.playSoundEffect(SoundEffects.zombieIsHit, 100f);
                    zombieIterator.remove();
                    bulletIterator.remove();
                    // is now handled in handleZombieRebirth(); :   addZombieAtRandomWalkablePositionAround(playerState.getPlayerTilePosition(), 5, 3, 20);
                    scoreBoardManager.addKills(+1);
                    scoreBoardManager.addAmmo(+100);
                    break;
                }
                // to do implement this more
            }
        }
    }

    void handleFireWoodCollision(){
        handleFireWoodSkullCollision();
        handleFireWoodZombieCollision();
    }

    void handleFireWoodSkullCollision() {
        for (ShortLifeTimeSprite shortLifeTimeSprite : shortLifeTimeSpriteTypeManager.getAllShortLifeTimeSprites()) {
            Iterator<Skull> skullIterator = skullManager.getSkulls().iterator();
            while (skullIterator.hasNext()) {
                Skull skull = skullIterator.next();
                if (collisionDetector.isColliding(shortLifeTimeSprite.getPosition(), skull.getPosition())) {
                    skullIterator.remove();
                    scoreBoardManager.addKills(+1);
                    if (skullManager.getNumberOfSkulls() < 15)
                        skullManager.addSkull(generateRandomSkull());
                    if (skullManager.getNumberOfSkulls() < 15)
                        skullManager.addSkull(generateRandomSkull());
                    break;
                }
            }
        }
    }
    void handleFireWoodZombieCollision() {
        for (ShortLifeTimeSprite shortLifeTimeSprite : shortLifeTimeSpriteTypeManager.getAllShortLifeTimeSprites()) {
            Iterator<Zombie> zombieIterator = zombieManager.getZombies().iterator();
            while (zombieIterator.hasNext()) {
                Zombie zombie = zombieIterator.next();
                if (collisionDetector.isColliding(shortLifeTimeSprite.getPosition(), zombie.getPosition())) {
                    zombieIterator.remove();
                    scoreBoardManager.addKills(+1);
                    break;
                }
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
                    shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(new ShortLifeTimeSprite(ShortLifeTimeSpriteType.DEAD_SKULL, skull.getPosition(), 1000));
                    skullIterator.remove();
                    bulletIterator.remove();
                    scoreBoardManager.addKills(+1);
                    scoreBoardManager.addAmmo(+100);
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
        IntPosition randomTilePosition = new IntPosition(tileManager.getRandomColNumber(), tileManager.getRandomRowNumber());
        int randomSpeed = random.nextInt(3) + 1;
        IntPosition positionForNewSkull = getPixelPosFromTileCenterPos(randomTilePosition.clone());
        return new Skull(positionForNewSkull.clone(), Directions.lt, randomSpeed, skullManager.SKULL_COLS_IN_FILE);
    }

    private void handleBulletsTileCollisions() {
        Iterator<Bullet> iterator = bulletManager.getBullets().iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            IntPosition positionToTest = getTilePositionFromPixelPosition(bullet.getPosition());
            if (!tileManager.isTileWalkable(positionToTest)) {
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


    private void handleFireCreation() {
        if (pressedKeys.fireALT) {
            IntPosition position = getPixelPosFromTileCenterPos(getTilePositionFromPixelPosition(playerState.getPlayerCenterPos().clone()));
            if (!shortLifeTimeSpriteTypeManager.isOnPositionATypeOf(position,ShortLifeTimeSpriteType.WOOD_FIRE)) {
                shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(new ShortLifeTimeSprite(ShortLifeTimeSpriteType.WOOD_FIRE, position, 10000));
            }
        }
    }
    private void handleBulletCreation() {
        if (pressedKeys.fireSpace) {
            IntPosition startPositionBullet = playerState.getPlayerCenterPos().clone();
            switch (playerState.getPlayerPreviousDirection()) {
                case rt:
                    startPositionBullet.addX(TileManager.TILE_WIDTH);
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
                soundManager.playSoundEffect(SoundEffects.singleShot, 100f);
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
        if (pressedKeys.goRight && isValidStartCondition && tileManager.isTileWalkable(targetTileX, targetTileY)) {
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
        if (pressedKeys.goLeft && isValidStartCondition && tileManager.isTileWalkable(targetTileX, targetTileY)) {
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
        if (pressedKeys.goUp && isValidStartCondition && tileManager.isTileWalkable(targetTileX, targetTileY)) {
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
        if (pressedKeys.goDown && isValidStartCondition && tileManager.isTileWalkable(targetTileX, targetTileY)) {
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
                tileManager.setTile(portalMap.getEntryPosition(), tileManager.TILE_PORTAL_OPEN);
            } else {
                tileManager.setTile(portalMap.getEntryPosition(), tileManager.TILE_PORTAL_CLOSE);
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
                    soundManager.playSoundEffect(SoundEffects.teleport, 100f);
                    shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(new ShortLifeTimeSprite(ShortLifeTimeSpriteType.PORTAL_SHINE, getPixelPosFromTileCenterPos(portalMapSteppedOn.getOutComePosition()), 2500));
                    shortLifeTimeSpriteTypeManager.addShortLifeTimeSprite(new ShortLifeTimeSprite(ShortLifeTimeSpriteType.PORTAL_SHINE, getPixelPosFromTileCenterPos(portalMapSteppedOn.getEntryPosition()), 500));
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
        return playerState.getPlayerCenterPos().getX() + (TileManager.TILE_WIDTH * TileManager.TILE_MAP_SCALE_FACTOR / 2);
    }

    private int getPlayerYPosCenter() {
        return playerState.getPlayerCenterPos().getY() + (TileManager.TILE_HEIGHT * TileManager.TILE_MAP_SCALE_FACTOR / 2);
    }

    private IntPosition getPixelPosFromTileCenterPos(IntPosition tilePosition) {
        return new IntPosition(
            tilePosition.getX() * TileManager.TILE_WIDTH * 2 + TileManager.HALF_TILE_WIDTH,
            tilePosition.getY() * TileManager.TILE_HEIGHT * 2 + TileManager.HALF_TILE_HEIGHT);
    }

    private IntPosition getTilePositionFromPixelPosition(IntPosition pixelPosition) {
        int x = pixelPosition.getX() / (TileManager.TILE_WIDTH * TileManager.TILE_MAP_SCALE_FACTOR);
        int y = pixelPosition.getY() / (TileManager.TILE_HEIGHT * TileManager.TILE_MAP_SCALE_FACTOR);
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
                soundManager.playSoundEffect(SoundEffects.background, 0.5f);
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
