package io.github.SoldierVsZombies;

public class Skull extends BaseSprite {

    public final int RESET_TIME_VALUE = 10;


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
            setFrameIndex(getFrameIndex()+1);
            if (getFrameIndex() > MAX_FRAMES - 1) {
                setFrameIndex(0);
            }
        } else {
            setFrameIndex(getFrameIndex()-1);
            if (getFrameIndex()< 0) {
                setFrameIndex(MAX_FRAMES-1);
            }
        }
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
