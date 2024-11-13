package io.github.SoldierVsZombies;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

public class PlayerState {
    public final int STEP_SIZE = 5;
    private static PlayerState instance;
    private static PlayerFrames playerFrames;
    private Directions playerCurrentDirection;
    private Directions playerPreviousDirection;
    private IntPosition playerCenterPos;
    private IntPosition playerTargetCenterPos;
    private int playerFrameIndex;
    private int xPosTilePlayer;
    private int yPosTilePlayer;
    private boolean playerHurts;

    public int getXPosTilePlayer() {
        return xPosTilePlayer;
    }

    public void setTilePosPlayer(IntPosition tilePosPlayer) {
        setXPosTilePlayer(tilePosPlayer.getX());
        setYPosTilePlayer(tilePosPlayer.getY());
    }

    public void setXPosTilePlayer(int xPosTilePlayer) {
        this.xPosTilePlayer = xPosTilePlayer;
    }

    public int getYPosTilePlayer() {
        return yPosTilePlayer;
    }

    public void setYPosTilePlayer(int yPosTilePlayer) {
        this.yPosTilePlayer = yPosTilePlayer;
    }

    public IntPosition getPlayerCenterPos() {
        return playerCenterPos;
    }

    public IntPosition getPlayerTargetCenterPos() {
        return playerTargetCenterPos;
    }

    public int getPlayerFrameIndex() {
        return playerFrameIndex;
    }

    public void setPlayerFrameIndex(int playerFrameIndex) {
        this.playerFrameIndex = playerFrameIndex;
    }

    public void addPlayerFrameIndex(int delta) {
        playerFrameIndex += delta;
    }


    public Directions getPlayerCurrentDirection() {
        return playerCurrentDirection;
    }

    public void setPlayerCurrentDirection(Directions playerCurrentDirection) {
        this.playerCurrentDirection = playerCurrentDirection;
    }

    public Directions getPlayerPreviousDirection() {
        return playerPreviousDirection;
    }

    public void setPlayerPreviousDirection(Directions playerPreviousDirection) {
        this.playerPreviousDirection = playerPreviousDirection;
    }

    public IntPosition getPlayerTilePosition() {
        return new IntPosition(xPosTilePlayer, yPosTilePlayer);
    }

    public void setPlayerCenterPos(IntPosition newPlayerCenterPos) {
        playerCenterPos.setPosition(newPlayerCenterPos);
    }

    public void setPlayerTargetCenterPos(IntPosition newPlayerTargetCenterPos) {
        playerTargetCenterPos.setPosition(newPlayerTargetCenterPos);
    }

    public static PlayerState getInstance() {
        if (instance == null) {
            instance = new PlayerState();
            initializeVariables();
        }
        return instance;
    }

    public boolean isPlayerHurts() {
        return playerHurts;
    }

    public void setPlayerHurts(boolean playerHurts) {
        this.playerHurts = playerHurts;
    }

    private static void initializeVariables() {
        playerFrames = PlayerFrames.getInstance();
        instance.playerPreviousDirection = Directions.dn;// Make sure it's not Directions.no !!
        instance.playerCurrentDirection = Directions.no;
        instance.playerFrameIndex = 0;
        instance.playerCenterPos = new IntPosition();
        instance.playerTargetCenterPos = new IntPosition();
        instance.playerHurts = false;
    }

    public static Sprite getPlayerFrames(int direction) {
        return
            playerFrames.getPlayerFramesSprites()[direction]
                [instance.isPlayerHurts()
                ? new Random().nextInt(7, 11)
                : instance.playerFrameIndex];
    }
}
