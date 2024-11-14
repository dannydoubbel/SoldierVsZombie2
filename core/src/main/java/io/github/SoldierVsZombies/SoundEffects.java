package io.github.SoldierVsZombies;

public enum SoundEffects {
    background(0,"backgroundMusic","sound/music.mp3"),
    singleShot(1,"effectSingleShot","sound/gun-single.mp3"),
    zombieIsHit(2,"effectZombieIsShot","sound/strange_laugh.mp3"),
    auwScream(3,"effectAuwScream","sound/auw_scream.mp3"),
    howo(4,"soundEffectHowo","sound/howo.mp3"),
    teleport(5,"effectTeleport","sound/teleport.mp3"),
    feelingBetter(6,"feeling better","sound/forTheBetter.mp3"),
    ahahaugh(7,"ahahaugh","sound/ahahaugh.mp3"),
    zombieScream(8,"zombie scream","sound/zombieScream.mp3"),
    bulletMagazineCollect(9,"bulletMagazineCollect","sound/bulletMagazineCollect.mp3"),
    youLose(10,"youLose","sound/youLose.mp3"),
    collectWoodFirst(11,"collectWoodFirst","sound/collectWoodFirst.mp3"),
    collectAmmunitionFirst(12,"collectAmmoFirst","sound/collectAmmunitionFirst.mp3"),;

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

    public String getFileName() {
        return fileName;
    }
}


