package io.github.SoldierVsZombies;

public class Zombie extends BaseSprite {

    private boolean isWalking = false;

    Zombie(IntPosition position){
        setPosition(position);
        setStepSize(3);
    }



}
