package io.github.some_example_name;

public enum Directions {
    dn(0, "down"), up(1, "up"), lt(2, "left"), rt(3, "right"), no(4, "none");

    private final int value;
    private final String name;

    Directions(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;

    }
}
