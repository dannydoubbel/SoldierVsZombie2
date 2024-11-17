package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;

public class TileMapJSONReader {

   public int[][] getTileMapArray(String fileName) {
       try {
           // Create ObjectMapper instance
           ObjectMapper mapper = new ObjectMapper();

           FileHandle fileHandler = Gdx.files.internal(fileName);

           // Use the FileHandle's read() method to get an InputStream for the JSON parser
           JsonNode rootNode = mapper.readTree(fileHandler.read());

           // Parse the JSON file into a JsonNode

           // Extract the "layers" array
           JsonNode layersArray = rootNode.get("layers");

           if (layersArray.isArray() && !layersArray.isEmpty()) {
               // Use the first layer
               JsonNode firstLayer = layersArray.get(0);

               // Extract height, width, and data
               int height = firstLayer.get("height").asInt(); // Number of rows
               int width = firstLayer.get("width").asInt();   // Number of columns
               JsonNode dataArray = firstLayer.get("data");

               // Initialize the 2D array
               int[][] array = new int[width][height];

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
           } else {
               throw new IllegalArgumentException("No valid layers found in the JSON file.");
           }
       } catch (IOException e) {
           e.printStackTrace();
           return null;
       }
   }

}


