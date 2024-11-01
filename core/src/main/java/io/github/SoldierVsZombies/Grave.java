package io.github.SoldierVsZombies;

import com.badlogic.gdx.utils.TimeUtils;

public class Grave extends BaseSprite{
    private int countDown;
    private long startTime;

    public Grave(int countDown) {
        this.countDown = countDown;
        startTime = TimeUtils.millis();
    }

    public boolean isPastCountDown(){
        long elapsedTime = TimeUtils.millis() - startTime;  // Calculate elapsed time in milliseconds
        return elapsedTime > countDown;
    }
}
