package io.github.SoldierVsZombies;

public enum SoundEffects {
    background(0,"backgroundMusic","sound/music.mp3"),
    singleShot(1,"effectSingleShot","sound/gun-single.mp3"),
    zombieIsHit(2,"effectZombieIsShot","sound/strange_laugh.mp3"),
    auwScream(3,"effectAuwScream","sound/auw_scream.mp3"),
    howo(4,"soundEffectHowo","sound/howo.mp3"),
    teleport(5,"effectTeleport","sound/teleport.mp3");

    private final int value;
    private final String name;
    private final String fileName;

    SoundEffects(int value, String name,String fileName) {
        this.value = value;
        this.name = name;
        this.fileName = fileName;
    }

    public int getValue() {

        return value;
    }

    public String getName() {
        return name;

    }

    public String getFileName() {
        return fileName;
    }
}


