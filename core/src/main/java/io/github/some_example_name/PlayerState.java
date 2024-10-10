package io.github.some_example_name;

public class PlayerState {
    private static PlayerState instance;
    public final int STEP_SIZE = 5;
    private Directions playerCurrentDirection = Directions.no;
    private Directions playerPreviousDirection = Directions.no;
    private int playerFrameIndex = 0;
    private final IntPosition playerCenterPos = new IntPosition();
    private final IntPosition playerTargetCenterPos = new IntPosition();

    private int xPosTilePlayer;
    private int yPosTilePlayer;

    public int getxPosTilePlayer() {
        return xPosTilePlayer;
    }

    public void setxPosTilePlayer(int xPosTilePlayer) {
        this.xPosTilePlayer = xPosTilePlayer;
    }

    public int getyPosTilePlayer() {
        return yPosTilePlayer;
    }

    public void setyPosTilePlayer(int yPosTilePlayer) {
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
