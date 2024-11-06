package io.github.SoldierVsZombies;

public enum SpawnableType {
    DEAD_ZOMBIE(1, "dead zombie"),
    DEAD_SKULL(2, "dead skull"),
    BLACK_GIFT(3,"black gift box"),
    PRINCESS(4,"princess"),
    PORTAL_SHINE(5,"portal shine");
    private final int value;
    private final String name;

    SpawnableType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SpawnableType{" +
            "value=" + value +
            ", name='" + name + '\'' +
            '}';
    }
}
