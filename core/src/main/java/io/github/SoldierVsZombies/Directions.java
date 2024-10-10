package io.github.SoldierVsZombies;

public enum Directions {
    dn(1, "going down"),
    up(2, "going up"),
    lt(3, "going left"),
    rt(4, "going right"),
    no(0, "going nowhere");

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
