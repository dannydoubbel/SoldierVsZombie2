package io.github.SoldierVsZombies;

import com.badlogic.gdx.utils.JsonValue;

public class TiledMapLoader {
    public int[] loadTiledMapData() {


        // Get the GdxLib version
        String gdxVersion = JsonValue.class.getPackage().getImplementationVersion();

        // Get the JSON parsing library version (assuming it's part of GdxLib)
        String jsonParserVersion = JsonValue.class.getPackage().getSpecificationVersion();

        System.out.println("GdxLib version: " + gdxVersion);
        System.out.println("JSON parsing library version: " + jsonParserVersion);


        return null;
    }
}






