package io.github.some_example_name;

public class SharedVariables {
    private static SharedVariables instance;
    private Directions currentSolderDirection = Directions.dn;

    public int getTextureIndexSoldier() {
        return textureIndexSoldier;
    }

    public void setTextureIndexSoldier(int textureIndexSoldier) {
        this.textureIndexSoldier = textureIndexSoldier;
    }

    private int textureIndexSoldier = 0;

    private SharedVariables() {
    }

    public static SharedVariables getInstance() {
        if (instance == null) {
            instance = new SharedVariables();
        }
        return instance;
    }

    void setCurrentSolderDirection(Directions newDirection) {
        currentSolderDirection = newDirection;
    }

    Directions getCurrentSolderDirection() {
        return currentSolderDirection;
    }


}
