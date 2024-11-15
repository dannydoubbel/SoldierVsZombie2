package io.github.SoldierVsZombies;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class TileMapJSONReader {


    public int[][] getTileMapArray(String fileName) {
        try {
            // Create ObjectMapper instance
            ObjectMapper mapper = new ObjectMapper();

            // Parse the JSON file into a JsonNode
            JsonNode rootNode = mapper.readTree(new File(fileName));

            // Extract height and width
            int height = rootNode.get("height").asInt(); // Number of rows
            int width = rootNode.get("width").asInt();   // Number of columns

            // Initialize the 2D array
            int[][] array = new int[width][height];

            // Extract the data array
            JsonNode dataArray = rootNode.get("data");

            if (dataArray.isArray()) {
                int index = 0;

                // Populate the 2D array (row-major order)
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        array[col][row] = dataArray.get(index).asInt();
                        index++;
                    }
                }
            }
            return array;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}


