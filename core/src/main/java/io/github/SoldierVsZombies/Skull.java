package io.github.SoldierVsZombies;

public class Skull extends BaseSprite {

    public final int RESET_TIME_VALUE = 10;
    private int frameIndex = 0;

    private int waitCycli = 0;

    private int stupidMoveCounter = 0;

    Skull(IntPosition position,int stepSize){
        setPosition(position);
        setStepSize(stepSize);
    }


    public int getStupidMoveCounter() {
        return stupidMoveCounter;
    }

    public void setStupidMoveCounter(int stupidMoveCounter) {
        this.stupidMoveCounter = stupidMoveCounter;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public int getWaitCycli() {
        return waitCycli;
    }

    public void setWaitCycli(int waitCycli) {
        this.waitCycli = waitCycli;
    }
}
