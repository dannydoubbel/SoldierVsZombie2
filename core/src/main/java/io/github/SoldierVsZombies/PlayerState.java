package io.github.SoldierVsZombies;

public class PlayerState {
    private static PlayerState instance;
    public final int STEP_SIZE = 5;
    private Directions playerCurrentDirection = Directions.no;
    private Directions playerPreviousDirection = Directions.dn; // Make sure it's not Directions.no !!
    private int playerFrameIndex = 0;
    private final IntPosition playerCenterPos = new IntPosition();
    private final IntPosition playerTargetCenterPos = new IntPosition();

    private int xPosTilePlayer;
    private int yPosTilePlayer;

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
        return new IntPosition(xPosTilePlayer,yPosTilePlayer);
    }
    public void setPlayerCenterPos(IntPosition newPlayerCenterPos){
        playerCenterPos.setPosition(newPlayerCenterPos);
    }
    public void setPlayerTargetCenterPos(IntPosition newPlayerTargetCenterPos){
        playerTargetCenterPos.setPosition(newPlayerTargetCenterPos);
    }

    public static PlayerState getInstance() {
        if (instance == null) {
            instance = new PlayerState();
        }
        return instance;
    }
}
