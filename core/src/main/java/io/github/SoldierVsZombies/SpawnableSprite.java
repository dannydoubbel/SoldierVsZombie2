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

    public boolean isBeyondLifeTime(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100.");
        }

        long elapsedTime = TimeUtils.millis() - startTime;  // Calculate elapsed time in milliseconds
        return elapsedTime > (spriteLifetimeMilliSeconds * (percent / 100.0));
    }

    public boolean isLifeTimeBetween(double lowPercent,double highPercent ) {
        return  (isBeyondLifeTime(lowPercent) && !(isBeyondLifeTime(highPercent)) );
    }





    public SpawnableType getSpawnableType() {
        return spawnableType;
    }
}
