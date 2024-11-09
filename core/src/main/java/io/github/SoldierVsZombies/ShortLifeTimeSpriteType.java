package io.github.SoldierVsZombies;

public enum ShortLifeTimeSpriteType {
    DEAD_ZOMBIE(1, "dead zombie"),
    DEAD_SKULL(2, "dead skull"),
    BLACK_GIFT(3,"black gift box"),
    PRINCESS(4,"princess"),
    PORTAL_SHINE(5,"portal shine"),
    WOW(6,"wow"),
    WOOD_FIRE(7,"wood fire");
    private final int value;
    private final String name;

    ShortLifeTimeSpriteType(int value, String name) {
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
        return "SpawnAbleType{" +
            "value=" + value +
            ", name='" + name + '\'' +
            '}';
    }
}
