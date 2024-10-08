package io.github.some_example_name;


public class IntPosition {
    private int x;
    private int y;

    public IntPosition() {
        x = 0;
        y = 0;
    }

    public IntPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveX(int deltaX) {
        this.x += deltaX;
    }

    public void moveY(int deltaY) {
        this.y += deltaY;
    }
}

