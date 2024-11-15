package io.github.SoldierVsZombies;

public enum Legal {
    LEGAL(1,true),
    NOT_LEGAL(0,false);
    final int value;
    final boolean asBoolean;

    Legal(int value, boolean asBoolean) {
        this.value = value;
        this.asBoolean = asBoolean;
    }

    public int getValue() {
        return value;
    }

    public boolean asBoolean() {
        return asBoolean;
    }
}
