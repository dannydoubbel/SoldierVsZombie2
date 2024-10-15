package io.github.SoldierVsZombies;

public class Skull extends BaseSprite {

    public final int RESET_TIME_VALUE = 10;
    private int frameIndex = 0;

    private int waitCycli = 0;

    private int stupidMoveCounter = 0;

    private final int MAX_FRAMES;

    Skull(IntPosition position,Directions directions, int stepSize, int maxFrames){
        MAX_FRAMES = maxFrames;
        setPosition(position);
        setStepSize(stepSize);
        setDirection(directions == Directions.lt ?  Directions.lt : Directions.rt);

    }

    public void changeFrameIndexByDirection() {
        if (getDirection()==Directions.lt) {
            frameIndex++;
            if (frameIndex > MAX_FRAMES - 1) {
                frameIndex = 0;
            }
        } else {
            frameIndex--;
            if (frameIndex < 0) {
                frameIndex= MAX_FRAMES-1;
            }
        }
    }


    public int getFrameIndex() {
        return frameIndex;
    }

    public int getStupidMoveCounter() {
        return stupidMoveCounter;
    }

    public void setStupidMoveCounter(int stupidMoveCounter) {
        this.stupidMoveCounter = stupidMoveCounter;
    }

    public int getWaitCycli() {
        return waitCycli;
    }

    public void setWaitCycli(int waitCycli) {
        this.waitCycli = waitCycli;
    }
}
