package io.github.SoldierVsZombies;

import com.badlogic.gdx.utils.TimeUtils;

public class ShortLifeTimeSprite extends BaseSprite {
    final private int spriteLifetimeMilliSeconds;
    final private long startTime;

    final private ShortLifeTimeSpriteType shortLifeTimeSpriteType;

    public ShortLifeTimeSprite(ShortLifeTimeSpriteType shortLifeTimeSpriteType, IntPosition pixelPos, int spriteLifetimeMilliSeconds) {
        this.spriteLifetimeMilliSeconds = spriteLifetimeMilliSeconds;
        this.shortLifeTimeSpriteType = shortLifeTimeSpriteType;
        setPosition(pixelPos);
        startTime = TimeUtils.millis();
    }

    public boolean isBeyondLifeTime() {
        if (spriteLifetimeMilliSeconds <0) return false;
        long elapsedTime = TimeUtils.millis() - startTime;  // Calculate elapsed time in milliseconds
        return elapsedTime > spriteLifetimeMilliSeconds;
    }

    public boolean isBeyondHalfLifeTime() {
        if (spriteLifetimeMilliSeconds <0) return false;
        long elapsedTime = TimeUtils.millis() - startTime;  // Calculate elapsed time in milliseconds
        return elapsedTime > (spriteLifetimeMilliSeconds / 2);
    }

    public boolean isBeyondLifeTime(double percent) {
        if (spriteLifetimeMilliSeconds <0) return false;
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100.");
        }

        long elapsedTime = TimeUtils.millis() - startTime;  // Calculate elapsed time in milliseconds
        return elapsedTime > (spriteLifetimeMilliSeconds * (percent / 100.0));
    }

    public boolean isLifeTimeBetween(double lowPercent, double highPercent) {
        return (isBeyondLifeTime(lowPercent) && !(isBeyondLifeTime(highPercent)));
    }

    public boolean isLifeTimeInOddPartAlternatingPercentRange(int deltaPercent) {
        for (int loop = 0; loop < 100; loop += (2 * deltaPercent)) {
            if (isLifeTimeBetween(loop, loop + deltaPercent)) return true;
        }
        return false;
    }

    public ShortLifeTimeSpriteType getShortLifeTimeSpriteType() {
        return shortLifeTimeSpriteType;
    }
}
