package io.github.SoldierVsZombies;

public class Zombie extends BaseSprite {

    private boolean isWalking = false;
    public final int MAX_FRAMES;


    Zombie(IntPosition position,int MAX_FRAMES){
        this.MAX_FRAMES = MAX_FRAMES;
        setPreviousDirection(Directions.rt);
        setPosition(position);
        setStepSize(3);
    }


    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public void startWalking(IntPosition newTargetTilePosition,Directions newDirection){
        setTargetTilePosition(newTargetTilePosition);
        setDirection(newDirection);
        setWalking(true);
    }

    @Override
    public void setDirection(Directions direction) {
        super.setDirection(direction);
        if (direction != Directions.no) {
            setPreviousDirection(direction);
            return;
        }
        if (getPreviousDirection() == Directions.no) {
            setPreviousDirection(Directions.rt);
        }
    }
}
