package io.github.SoldierVsZombies;

public class Bullet extends BaseSprite {
    public Bullet(IntPosition position, Directions directions,int stepSize) {
        setDirection(directions);
        setPosition(position);
        setStepSize(stepSize);
    }
}
