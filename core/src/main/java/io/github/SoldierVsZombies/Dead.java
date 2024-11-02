package io.github.SoldierVsZombies;

import com.badlogic.gdx.utils.TimeUtils;

public class Dead extends BaseSprite{
    final private int spriteLifetimeMilliSeconds;
    final private long startTime;

    final private DeadType deadType;

    public Dead(DeadType deadType, IntPosition position, int spriteLifetimeMilliSeconds) {
        this.spriteLifetimeMilliSeconds = spriteLifetimeMilliSeconds;
        this.deadType = deadType;
        setPosition(position);
        startTime = TimeUtils.millis();
    }

    public boolean isBeyondLifeTime(){
        long elapsedTime = TimeUtils.millis() - startTime;  // Calculate elapsed time in milliseconds
        return elapsedTime > spriteLifetimeMilliSeconds;
    }
    public boolean isBeyondHalfLifeTime(){
        long elapsedTime = TimeUtils.millis() - startTime;  // Calculate elapsed time in milliseconds
        return elapsedTime > (spriteLifetimeMilliSeconds/2);
    }

    public DeadType getDeadType() {
        return deadType;
    }
}
