package io.github.SoldierVsZombies;

public enum DeadType {
    DEAD_ZOMBIE(1, "dead zombie"),
    DEAD_SKULL(2, "dead skull");
    private final int value;
    private final String name;

    DeadType(int value, String name) {
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
