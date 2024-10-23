package io.github.SoldierVsZombies;

public class Zombie extends BaseSprite {

    private boolean isWalking = false;
    public final int MAX_FRAMES;


    Zombie(IntPosition position,int MAX_FRAMES){
        this.MAX_FRAMES = MAX_FRAMES;
        setPosition(position);
        setStepSize(3);
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

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }
}
