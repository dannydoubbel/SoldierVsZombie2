package io.github.SoldierVsZombies;

import com.badlogic.gdx.utils.TimeUtils;

import static com.badlogic.gdx.utils.TimeUtils.millis;

public class Zombie extends BaseSprite {

    private boolean isWalking = false;
    public final int MAX_FRAMES;

    final private long startTime;
    private long lifeTimeSpan;


    Zombie(IntPosition position,int MAX_FRAMES,long lifeTimeSpan){
        this.MAX_FRAMES = MAX_FRAMES;
        this.lifeTimeSpan = lifeTimeSpan;
        setPreviousDirection(Directions.rt);
        setPosition(position);
        setStepSize(3);
        startTime = millis();
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

    public boolean isLifeTimeExpired() {
        long elapsedTime = TimeUtils.millis() - startTime;  // Calculate elapsed time in milliseconds
        return elapsedTime >= lifeTimeSpan;
    }
}
