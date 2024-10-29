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
        playerState.setPlayerCenterPos(getPixelPositionFromTileCenterPosition(new IntPosition(11, 5)));
    }

    private void addInitialEnemies() {
        IntPosition startTilePosition = new IntPosition(10, 9);
        IntPosition startPixelPosition = getPixelPositionFromTileCenterPosition(startTilePosition);
        zombieManager.addZombie(startPixelPosition, startTilePosition);
        skullManager.addSkull(new IntPosition(350, 350),
            Directions.lt, 1, skullManager.SKULL_COLS_IN_FILE);
    }


    private void initializePortals() {

        portals = new Portals();
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
        handleZombiesStartup();
        handleZombiesCenterToTile();
        handleZombieStartMovement();
        handleZombiesMovement();
        handleZombiesFrames();
        handleZombiesDrawing();
    }

    private void handleZombiesStartup() {
        for (Zombie zombie : zombieManager.getZombies()) {

        }
    }

    private void handleZombiesCenterToTile() {
        for (Zombie zombie : zombieManager.getZombies()) {
            IntPosition pixelPosition = getTilePositionFromPixelPosition(zombie.getTargetTilePosition());
            if (!zombie.isWalking()) {
            } else { // blabla

            }
        }
    }

    private Directions getDirectionToGoTo(IntPosition startPos, IntPosition endPos) {
        //System.out.print("Vertical   start " + startPos+" destination " + endPos+" player "+ playerState.getPlayerTilePosition());
        if (startPos.getX() < endPos.getX()) {
            System.out.println("must go rt");
            return Directions.rt;
        }
        if (startPos.getX() > endPos.getX()) {
            System.out.println("must go lt");
            return Directions.lt;
        }
        if (startPos.getY() < endPos.getY()) {
            System.out.println("must go up");
            return Directions.up;
        }
        if (startPos.getY() > endPos.getY()) {
            System.out.println("must go dn");
            return Directions.dn;
        }
        System.out.println("");
        return Directions.no;
    }

    private void showMoreThanOneTileMove(IntPosition startPos, IntPosition endPos) {
        int diffHor = Math.abs(startPos.getX() - endPos.getX());
        int diffVer = Math.abs(startPos.getY() - endPos.getY());
        if (diffHor > 1) {
            //System.out.println("Fuck horizontal");
        }
        if (diffVer > 1) {
            //System.out.println("Fuck vertical");
        }
    }

    private void handleZombieStartMovement() {
        for (Zombie zombie : zombieManager.getZombies()) {
            if (!zombie.isWalking()) {
                IntPosition tilePosZombie = getTilePositionFromPixelPosition(new IntPosition(zombie.getPosition()));
                IntPosition tilePosPlayer = playerState.getPlayerTilePosition();

                IntPosition resultPosition = findPath(tilePosZombie, tilePosPlayer);
                //if (resultPosition.equals(tilePosZombie)) {
                //    resultPosition = findPathStartingVertical(tilePosZombie,tilePosPlayer);
                //    System.out.println("Zombie pos " + tilePosZombie+ " result vertical " + resultPosition);
                //}

                if (!resultPosition.equals(tilePosZombie)) {
                    Directions newDirection = getDirectionToGoTo(tilePosZombie, resultPosition);
                    if (!newDirection.equals(Directions.no)) {
                        if (tiles.isTileWalkable(resultPosition)) {
                            showMoreThanOneTileMove(tilePosZombie, resultPosition);
                            zombie.setTargetTilePosition(resultPosition);
                            // System.out.println("Ik starte met " + tilePosZombie + " en gan nu naar " + resultPosition + " player at " + tilePosPlayer);
                            zombie.setDirection(newDirection);
                            zombie.setWalking(true);
                        }
                    }
                }
            }
        }
    }

    /*
    private IntPosition getNextTilePositionInPathToTilePosition(IntPosition tilePositionOrigin, IntPosition tilePositionDestination) {
        //private IntPosition findPath(IntPosition tilePositionStart,IntPosition tilePositionDestination) {
        // IndDuoNumbers testzone1 = horiziontal walkable zone ( with the ypos of tileposorigin)
        // while loop1 horizontal left and right
        //    IntPosition resultposition = intPosition(loop1,ypos tileposorigin)
        //    test found it ? yes : break
        //       IntDuoNumbers testzone2 = vertical walkable zone ( with the xpos of loop1)
        //       while loop2 vertical up down
        //            test found it ? yes : break
        //                 IntDuoNumber testzone3 = horizontal walkable zone (with the ypos of loop2)
        //                 while loop horizontal left and right
        //                    test found it ? yes break
        //
        //  when not found return tilePositionOrigin, else return resultposition
        // ?

        IntPosition resultPosition = new IntPosition(tilePositionDestination);
        IntDuoNumbers intDuoNumbers = getHorizontalWalkableRange(tilePositionOrigin);
        System.out.println("Range horizontal " + intDuoNumbers);
        intDuoNumbers = getVerticalWalkableRange(tilePositionOrigin);
        System.out.println("Range vertical " + intDuoNumbers);


        return resultPosition;
    }

     */




/*

    private IntPosition findPathStartingVertical(IntPosition tilePositionStart, IntPosition tilePositionDestination) {
        // Initialize the starting test zone using the horizontal vertical range from the starting X position
        IntDuoNumbers verticalZone1 = getVerticalWalkableRange(tilePositionStart);
        IntPosition helpTestPosition;
        IntPosition returnPositionPreferY;
        // level 1 : Loop horizontally to search left and right from the initial position
        int y1 = verticalZone1.getLowest();

        while (y1 <= verticalZone1.getHighest()) { // first while
            helpTestPosition = new IntPosition(tilePositionStart.getX(), y1);
            returnPositionPreferY = tilePositionStart.clone();
            if (y1 > tilePositionStart.getY()) {
                returnPositionPreferY.addY(1);
            }
            if (y1 < tilePositionStart.getY()) {
                returnPositionPreferY.addY(-1);
            }

            if (helpTestPosition.equals(tilePositionDestination)) {
                //System.out.println("Into vertical 1");

                return returnPositionPreferY;
            }

            // level 2 loop horizontal
            // Loop vertically to search up and down from the current horizontal position
            IntDuoNumbers horizontalZone1 = getHorizontalWalkableRange(helpTestPosition);

            for (int x1 = horizontalZone1.getLowest(); x1 <= horizontalZone1.getHighest(); x1++) {
                helpTestPosition = new IntPosition(x1, y1);

                if (helpTestPosition.equals(tilePositionDestination)) {
                    //System.out.println("Into vertical 2");

                    return returnPositionPreferY;
                }
                // level 3 loop vertical
                IntDuoNumbers verticalZone2 = getVerticalWalkableRange(helpTestPosition);
                for (int y2 = verticalZone2.getLowest(); y2 <= verticalZone2.getHighest(); y2++) {
                    helpTestPosition = new IntPosition(x1, y2);
                    // Check if we've reached the destination
                    if (helpTestPosition.equals(tilePositionDestination)) {
                        return returnPositionPreferY;
                    }

                    // level 4 loop horizon
                    IntDuoNumbers horizontalZone2 = getHorizontalWalkableRange(helpTestPosition);
                    for (int x2 = horizontalZone2.getLowest();x2<=horizontalZone2.getHighest();x2++) {
                        helpTestPosition = new IntPosition(x2,y2);
                        if (helpTestPosition.equals(tilePositionDestination)) {
                            return returnPositionPreferY;
                        }
                        // level 5 loop vertical
                        IntDuoNumbers verticalZone3 = getVerticalWalkableRange(helpTestPosition);
                        for (int y3=verticalZone3.getLowest();y3<=verticalZone3.getHighest();y3++) {
                            helpTestPosition = new IntPosition(x2,y2);
                            if (helpTestPosition.equals(tilePositionDestination)) {
                                return returnPositionPreferY;
                            }
                        }

                    } // level 4

                } // level 3


            } // level 2

            y1++;
        } // end first while
        // Return the start position if the destination was not found
        return tilePositionStart;
    }
*/


    private IntPosition findPath(IntPosition tileStartPos, IntPosition tileDestinationPos) {
        IntPosition earlyDetectionPath = findEarlyPaths(tileStartPos, tileDestinationPos);
        if (!earlyDetectionPath.equals(new IntPosition(-1, -1))) {
            //System.out.println("Early");
            return earlyDetectionPath;
        }
/*
        // Initialize the starting test zone using the horizontal walkable range from the starting Y position
        IntDuoNumbers horizontalZone1 = getHorizontalWalkableRange(tileStartPos);
        IntPosition helpTestPosition;
        IntPosition returnPositionPreferX;


        //System.out.println("Hor range = " + horizontalZone1);
        // level 1 : Loop horizontally to search left and right from the initial position
        int y0 = tileStartPos.getY();
        int x0 = tileStartPos.getX();
        int x1 = horizontalZone1.getLowest();

        do{ // first while
            IntDuoNumbers verticalZone1 = getVerticalWalkableRange(new IntPosition(x1,y0));
            //System.out.println("Ver range " + verticalZone1);
            int y1 = verticalZone1.getLowest();
            do { // second while
                helpTestPosition = new IntPosition(x1, y1);
                if (helpTestPosition.equals(tileDestinationPos)) {
                    helpTestPosition = new IntPosition(x0,y0);
                    if (y1 > tileStartPos.getY() ) {
                        helpTestPosition = new IntPosition(x1,tileStartPos.getY()+1);
                    }
                    if (y1 < tileStartPos.getY() ) {
                        helpTestPosition = new IntPosition(x1,tileStartPos.getY()-1);
                    }
                    System.out.println("in second loop 1");
                    return helpTestPosition;
                }

                IntDuoNumbers horizontalZone2 = getHorizontalWalkableRange(new IntPosition(x1,y1));
                for (int x2 = horizontalZone2.getLowest();x2<=horizontalZone2.getHighest();x2++) { // third loop
                    helpTestPosition = new IntPosition(x2,y1);
                    if (helpTestPosition.equals(tileDestinationPos)) {
                        // found
                        helpTestPosition = new IntPosition(x0,y0);
                        if (x0==x1) {
                            //helpTestPosition = new IntPosition(x0,y0);
                            if (y0>y1) {
                                helpTestPosition.addY(-1);
                            }
                            if (y0<y1) {
                                helpTestPosition.addY(1);
                            }
                            System.out.println("in third loop 1");
                            return helpTestPosition;
                        }
                        System.out.println("Level 2 y0=" + y0 + " y1 = "+y1 + "X0 = " + x0 + "  x1 = " + x1+ " x2 = " + x2);
                        if (x0<x1) {
                            helpTestPosition.addX(1);
                        }
                        if (x0>x1) {
                            helpTestPosition.addX(-1);
                        }
                        System.out.println("in third loop 2");
                        return  helpTestPosition;
                    }

                    IntDuoNumbers verticalZone2 = getVerticalWalkableRange(new IntPosition(x2,y1));
                    for (int y2=verticalZone2.getLowest();y2<verticalZone2.getHighest();y2++ ) { // fourth loop
                            //todo afwerken !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        helpTestPosition = new IntPosition(x2,y2);
                        if (helpTestPosition.equals(tileDestinationPos)) {
                            // found
                            helpTestPosition = new IntPosition(x0,y0);
                            if (x0==x1) {
                                // blablabla + return
                                if (y0>y1) {
                                    helpTestPosition.addY(-1);
                                }
                                if (y0<y1) {
                                    helpTestPosition.addY(1);
                                }
                                System.out.println("in fourth loop 1  Range vertical = "  + verticalZone2        + " y0=" + y0 + " y1 = "+y1 + "y2="+y2+   " X0 = " + x0 + "  x1 = " + x1+ " x2 = " + x2);  //                       System.out.println("Level 2 );

                                return helpTestPosition;
                            }
                            // blablabla + return
                            if (x0>x1) {
                                helpTestPosition.addX(-1);
                            }
                            if (x0<x1) {
                                helpTestPosition.addX(1);
                            }
                            System.out.println("in fourth loop 2");
                            return  helpTestPosition;

                        }
                    } // end fourth loop









                } // end third loop









                y1++; // end second while
            } while (y1 <= verticalZone1.getHighest());

             // end second while
            x1++; // end first while
        } while (x1 <= horizontalZone1.getHighest());

*/

        // Return the start position if the destination was not found
        //System.out.println("not found");
        return tileStartPos;

    }

    private IntPosition findEarlyPaths(IntPosition tilePositionStart, IntPosition tilePositionDestination) {
        if (tilePositionStart.equals(tilePositionDestination)) {
            return tilePositionDestination;
        }

        IntPosition directOrthogonal;

        directOrthogonal = pathDirectHorizontalLine(tilePositionStart, tilePositionDestination);
        if (!directOrthogonal.equals(tilePositionStart)) {
            //System.out.println("Found pathDirectHorizontalLine ");
            return directOrthogonal;
        }

        directOrthogonal = pathDirectVerticalLine(tilePositionStart, tilePositionDestination);
        if (!directOrthogonal.equals(tilePositionStart)) {
            //System.out.println("Found pathDirectVerticalLine");
            return directOrthogonal;
        }

        directOrthogonal = pathNextLevelStartHorizontal(tilePositionStart, tilePositionDestination, 1);
        if (!directOrthogonal.equals(tilePositionStart)) {
            //System.out.println("Found pathNextLevelStartHorizontal ");
            return directOrthogonal;
        }
        directOrthogonal = pathNextLevelStartHorizontal(tilePositionStart, tilePositionDestination, 3);
        if (!directOrthogonal.equals(tilePositionStart)) {
            //System.out.println("Found pathNextLevelStartHorizontal ");
            return directOrthogonal;
        }


        return new IntPosition(-1, -1);
    }


    private IntPosition pathNextLevelStartHorizontal(IntPosition tilePosStart, IntPosition tilePosDestination, int deepnessLevel) {
        IntDuoNumbers horizontalRange1 = getHorizontalWalkableRange(tilePosStart);
        IntDuoNumbers verticalRange1;
        IntDuoNumbers horizontalRange2;
        IntDuoNumbers verticalRange2;
        IntDuoNumbers horizontalRange3;
        IntPosition toTestTilePos;
        IntPosition returnResultTilePos = tilePosStart.clone();
        int x1 = horizontalRange1.getLowest();
        int y1;
        int x2;
        int y2;
        int x3;
        do {
            verticalRange1 = getVerticalWalkableRange(new IntPosition(x1, tilePosStart.getY()));
            y1 = verticalRange1.getLowest();
            do {
                toTestTilePos = new IntPosition(x1, y1);
                if (toTestTilePos.equals(tilePosDestination)) {
                    if (x1 != tilePosStart.getX()) {
                        if (x1 > tilePosStart.getX()) {
                            returnResultTilePos.addX(+1);
                        } else {
                            returnResultTilePos.addX(-1);
                        }
                    } else {
                        if (y1 > tilePosStart.getY()) {
                            returnResultTilePos.addY(+1);
                        } else {
                            returnResultTilePos.addY(-1);
                        }
                    }
                    return returnResultTilePos;
                }

                if (deepnessLevel > 2) {
                    horizontalRange2 = getHorizontalWalkableRange(new IntPosition(x1, y1));
                    x2 = horizontalRange2.getLowest();
                    do {
                        toTestTilePos = new IntPosition(x2, y1);
                        if (toTestTilePos.equals(tilePosDestination)) {
                            if (x1 != tilePosStart.getX()) {
                                if (x1 > tilePosStart.getX()) {
                                    returnResultTilePos.addX(+1);
                                } else {
                                    returnResultTilePos.addX(-1);
                                }
                            } else {
                                if (y1 > tilePosStart.getY()) {
                                    returnResultTilePos.addY(+1);
                                } else {
                                    returnResultTilePos.addY(-1);
                                }
                            }
                            return returnResultTilePos;
                        }
                        verticalRange2 = getVerticalWalkableRange(new IntPosition(x2, y1));
                        y2 = verticalRange2.getLowest();
                        do {
                            toTestTilePos = new IntPosition(x2, y2);
                            if (toTestTilePos.equals(tilePosDestination)) {
                                if (x1 != tilePosStart.getX()) {
                                    if (x1 > tilePosStart.getX()) {
                                        returnResultTilePos.addX(+1);
                                    } else {
                                        returnResultTilePos.addX(-1);
                                    }
                                } else {
                                    if (y1 > tilePosStart.getY()) {
                                        returnResultTilePos.addY(+1);
                                    } else {
                                        returnResultTilePos.addY(-1);
                                    }
                                }
                                System.out.println("Found with Y2");
                                return returnResultTilePos;
                            }


                            horizontalRange3 = getHorizontalWalkableRange(new IntPosition(x2, y2));
                            x3 = horizontalRange3.getLowest();
                            do {
                                toTestTilePos = new IntPosition(x3, y2);
                                if (toTestTilePos.equals(tilePosDestination)) {
                                    if (x1 != tilePosStart.getX()) {
                                        if (x1 > tilePosStart.getX()) {
                                            returnResultTilePos.addX(+1);
                                        } else {
                                            returnResultTilePos.addX(-1);
                                        }
                                    } else {
                                        if (y1 > tilePosStart.getY()) {
                                            returnResultTilePos.addY(+1);
                                        } else {
                                            returnResultTilePos.addY(-1);
                                        }
                                    }
                                    System.out.println("Found with x3 :    x1="+x1+" y1="+y1+" x2="+x2+" y2="+y2+" x3="+x3 );
                                    return returnResultTilePos;
                                }
                                x3++;
                            } while (x3 <= horizontalRange3.getHighest());

                            y2++;
                        } while (y2 <= verticalRange2.getHighest());


                        x2++;
                    } while (x2 <= horizontalRange2.getHighest());
                } // end if deepnesslevel

                y1++;
            } while (y1 <= verticalRange1.getHighest());
            x1++;
        } while (x1 <= horizontalRange1.getHighest());
        //System.out.println("Not found  first horizontal first vertical");
        return tilePosStart;
    }

    private IntPosition pathDirectHorizontalLine(IntPosition tilePosStart, IntPosition tilePosDestination) {
        IntPosition directOrthogonal = tilePosStart.clone();
        IntDuoNumbers horizontalRange = getHorizontalWalkableRange(tilePosStart);
        if ((tilePosDestination.getX() >= horizontalRange.getLowest() && tilePosDestination.getX() <= horizontalRange.getHighest()) && (tilePosStart.getY() == tilePosDestination.getY())) {

            if (tilePosStart.getX() < tilePosDestination.getX()) {
                directOrthogonal.addX(+1);
            } else {
                directOrthogonal.addX(-1);
            }
        }
        return directOrthogonal;
    }

    private IntPosition pathDirectVerticalLine(IntPosition tilePosStart, IntPosition tilePosDestination) {
        IntPosition directOthogonal = tilePosStart.clone();
        IntDuoNumbers verticalRange = getVerticalWalkableRange(tilePosStart);
        if ((tilePosDestination.getY() >= verticalRange.getLowest() && tilePosDestination.getY() <= verticalRange.getHighest()) && (tilePosStart.getX() == tilePosDestination.getX())) {
            if (tilePosStart.getY() < tilePosDestination.getY()) {
                directOthogonal.addY(1);
            } else {
                directOthogonal.addY(-1);
            }
        }
        return directOthogonal;
    }


    private IntDuoNumbers getWalkableRange(IntPosition startTilePos, Orthogonal orthogonal) {
        IntDuoNumbers resultDuoNumbers = new IntDuoNumbers();
        int testX = startTilePos.getX();
        int testY = startTilePos.getY();
        boolean moved = false;
        if (orthogonal.equals(Orthogonal.NOTHING)) {
            throw new IllegalArgumentException("Verboden nothing in functie getWalkableRange");
        }
        // Calculate negative bound (left or down)
        if (orthogonal == Orthogonal.HORIZONTAL) {
            while (testX > 0 && tiles.isTileWalkable(new IntPosition(testX, testY))) {
                moved = true;
                testX--;
            }
            resultDuoNumbers.setInt1(moved ? testX + 1 : testX);

            // Reset and calculate positive bound (right)
            testX = startTilePos.getX();
            moved = false;
            while (testX < Tiles.TILE_MAP_COLS - 1 && tiles.isTileWalkable(new IntPosition(testX, testY))) {
                moved = true;
                testX++;
            }
            resultDuoNumbers.setInt2(moved ? testX - 1 : testX);

        } else { // VERTICAL direction
            while (testY > 0 && tiles.isTileWalkable(new IntPosition(testX, testY))) {
                moved = true;
                testY--;
            }
            resultDuoNumbers.setInt1(moved ? testY + 1 : testY);

            // Reset and calculate positive bound (up)
            testY = startTilePos.getY();
            moved = false;
            while (testY < Tiles.TILE_MAP_ROWS - 1 && tiles.isTileWalkable(new IntPosition(testX, testY))) {
                moved = true;
                testY++;
            }
            resultDuoNumbers.setInt2(moved ? testY - 1 : testY);
        }
        return resultDuoNumbers;
    }

    // Wrapper methods for horizontal and vertical range detection
    private IntDuoNumbers getHorizontalWalkableRange(IntPosition startTilePos) {
        return getWalkableRange(startTilePos, Orthogonal.HORIZONTAL);
    }

    private IntDuoNumbers getVerticalWalkableRange(IntPosition startTilePos) {
        return getWalkableRange(startTilePos, Orthogonal.VERTICAL);
    }


    private void handleZombiesMovement() {
        for (Zombie zombie : zombieManager.getZombies()) {
            if (!zombie.isWalking()) {
                continue;
            }

            IntPosition testPosPixels = getPixelPositionFromTileCenterPosition(zombie.getTargetTilePosition());
            boolean zombieOnCenterTargetTile = false;
            int zombieX = zombie.getPosition().getX();
            int zombieY = zombie.getPosition().getY();
            Orthogonal orthogonal = getOrthoganalFrom(zombie.getDirection());
            if (!orthogonal.equals(Orthogonal.NOTHING)) {
                if (orthogonal.equals(Orthogonal.HORIZONTAL)) {
                    zombieOnCenterTargetTile = Math.abs(zombieX - testPosPixels.getX()) <= zombie.getStepSize() + 2;
                }
                if (orthogonal.equals(Orthogonal.VERTICAL)) {
                    zombieOnCenterTargetTile = Math.abs(zombieY - testPosPixels.getY()) <= zombie.getStepSize() + 2;
                }
            }

            if (zombieOnCenterTargetTile) {
                zombie.setPosition(testPosPixels);
                zombie.setWalking(false);
                System.out.println("zombie stop walking");
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
        //System.out.println("Zombie target tile = " + zombie.getTargetTilePosition());
        //System.out.println("Zombie pixel       = " + zombie.getPosition());
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
                    skullIterator.remove();
                    bulletIterator.remove();
                    randomCol = random.nextInt(Tiles.TILE_MAP_COLS + 1);
                    randomRow = random.nextInt(Tiles.TILE_MAP_ROWS + 1);
                    randomSpeed = random.nextInt(3) + 1;
                    IntPosition positionForNewSkull = getPixelPositionFromTileCenterPosition(new IntPosition(randomCol, randomRow));
                    newSkulls.add(new Skull(positionForNewSkull.clone(), Directions.lt, randomSpeed, skullManager.SKULL_COLS_IN_FILE));
                    randomCol = random.nextInt(Tiles.TILE_MAP_COLS + 1);
                    randomRow = random.nextInt(Tiles.TILE_MAP_ROWS + 1);
                    randomSpeed = random.nextInt(3) + 1;
                    positionForNewSkull = getPixelPositionFromTileCenterPosition(new IntPosition(randomCol, randomRow));
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
            IntPosition positionToTest = getTilePositionFromPixelPosition(bullet.getPosition());
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
            playerState.getPlayerTargetCenterPos().setPosition(getPixelPositionFromTileCenterPosition(new IntPosition(targetTileX, targetTileY)));

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
            playerState.getPlayerTargetCenterPos().setPosition(getPixelPositionFromTileCenterPosition(new IntPosition(targetTileX, targetTileY)));
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
            playerState.getPlayerTargetCenterPos().setPosition(getPixelPositionFromTileCenterPosition(new IntPosition(targetTileX, targetTileY)));
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
            playerState.getPlayerTargetCenterPos().setPosition(getPixelPositionFromTileCenterPosition(new IntPosition(targetTileX, targetTileY)));
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
                IntPosition newPlayerPos = getPixelPositionFromTileCenterPosition(portalExitPosition);
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

    private IntPosition getPixelPositionFromTileCenterPosition(IntPosition tilePosition) {
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
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.pause();
            } else {
                backgroundMusic.play();
            }
            sharedVariables.musicAllowed = false;
        }
    }

    private Orthogonal getOrthoganalFrom(Directions directions) {
        if (directions.equals(Directions.lt) || (directions.equals(Directions.rt))) {
            return Orthogonal.HORIZONTAL;
        }
        if (directions.equals(Directions.dn) || (directions.equals(Directions.up))) {
            return Orthogonal.VERTICAL;
        }
        return Orthogonal.NOTHING;

    }
}
