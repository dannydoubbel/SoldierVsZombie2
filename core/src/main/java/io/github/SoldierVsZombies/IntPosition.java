package io.github.SoldierVsZombies;


import java.util.Objects;

public class IntPosition {
    private int x;
    private int y;

    public IntPosition() {
        x = 0;
        y = 0;
    }

    public IntPosition(IntPosition position) {
        x = position.getX();
        y = position.getY();
    }

    public IntPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(IntPosition newPosition) {
        this.x = newPosition.getX();
        this.y = newPosition.getY();
    }

    public void addX(int deltaX) {
        this.x += deltaX;
    }

    public void addY(int deltaY) {
        this.y += deltaY;
    }

    public IntPosition clone() {
        return new IntPosition(this.x, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPosition that = (IntPosition) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "X = " + getX()+ " Y = " + getY();
    }
}

