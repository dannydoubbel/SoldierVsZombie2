package io.github.SoldierVsZombies;

public enum ShortLifeTimeSpriteType {
    DEAD_ZOMBIE(1, "dead zombie",64,96),
    DEAD_SKULL(2, "dead skull",100,100),
    BLACK_GIFT(3, "black gift box",100,100),
    PRINCESS(4, "princess",100,100),
    PORTAL_SHINE(5, "portal shine",100,100),
    WOW(6, "wow",100,100),
    WOOD_FIRE(7, "wood fire",100,100),
    FLAMING_TORCH(8, "flaming torch",352/11,64);

    private final int VALUE;
    private final int internalWIDTH;
    private final int internalHEIGHT;
    private final String NAME;

    ShortLifeTimeSpriteType(int value, String name,int WIDTH,int HEIGHT) {
        this.VALUE = value;
        this.NAME = name;
        this.internalWIDTH = WIDTH;
        this.internalHEIGHT = HEIGHT;
    }

    public int getVALUE() {
        return VALUE;
    }

    public String getNAME() {
        return NAME;
    }

    public int WIDTH() {
        return internalWIDTH;
    }

    public int HEIGHT() {
        return internalHEIGHT;
    }

    @Override
    public String toString() {
        return "SpawnAbleType{" +
            "value=" + VALUE +
            ", name='" + NAME + '\'' +
            '}';
    }
}
