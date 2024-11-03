package io.github.SoldierVsZombies;

import com.badlogic.gdx.utils.TimeUtils;

public class SpawnableSprite extends BaseSprite{
    final private int spriteLifetimeMilliSeconds;
    final private long startTime;

    final private SpawnableType spawnableType;

    public SpawnableSprite(SpawnableType spawnableType, IntPosition position, int spriteLifetimeMilliSeconds) {
        this.spriteLifetimeMilliSeconds = spriteLifetimeMilliSeconds;
        this.spawnableType = spawnableType;
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

    public SpawnableType getDeadType() {
        return spawnableType;
    }
}
