package io.github.SoldierVsZombies;

public enum GameState {
    RUNNING(0,"running"),
    PAUSED(1,"pauzed"),
    STOPPED(2,"stopped");

    int value;
    String name;

    GameState(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
