package io.github.SoldierVsZombies;

public class SharedVariables {
    private static SharedVariables instance;
    boolean musicAllowed = false;

    private SharedVariables() {
    }



    public static SharedVariables getInstance() {
        if (instance == null) {
            instance = new SharedVariables();
        }
        return instance;
    }

}
