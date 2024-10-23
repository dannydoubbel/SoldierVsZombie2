package io.github.SoldierVsZombies;

public class BaseSprite {

    private IntPosition position;
    private IntPosition targetTilePosition;
    private Directions direction;

    private int stepSize=0;

    private int frameIndex = 0;

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

    public IntPosition getTargetTilePosition() {
        return targetTilePosition;
    }

    public void setTargetTilePosition(IntPosition targetTilePosition) {
        this.targetTilePosition = targetTilePosition;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }
}

