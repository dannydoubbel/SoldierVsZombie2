package io.github.SoldierVsZombies;

public class BaseSprite {

    private IntPosition position;
    private Directions direction;

    private int stepSize=0;

    public BaseSprite() {
        position = new IntPosition(0, 0);
        direction = Directions.dn;
    }

    public void move(){
        switch (direction) {
            case dn :
                getPosition().addY(-stepSize);
                break;
            case up :
                getPosition().addY(stepSize);
                break;
            case lt :
                getPosition().addX(-stepSize);
                break;
            case rt :
                getPosition().addX(+stepSize);
                break;
        }
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public IntPosition getPosition() {
        return position;
    }

    public void setPosition(IntPosition position) {
        this.position = position;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

}

