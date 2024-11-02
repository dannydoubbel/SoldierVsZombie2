package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

public class AddTransparantCrossInPNG {
    public AddTransparantCrossInPNG() {


        // Load the original image
        FileHandle originalFile =  Gdx.files.internal("images/skullSheet800x100.png");
        Texture texture = new Texture(originalFile);

        // Get the texture data
        TextureData textureData = texture.getTextureData();

        // Make sure the texture data is prepared
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        Pixmap pixmap = texture.getTextureData().consumePixmap();


        // Calculate dimensions
        int originalWidth = 100;
        int originalHeight = 100;
        int halfWidth = originalWidth / 2;
        int halfHeight = originalHeight / 2;
        int transparentZoneSize = 4;  // Width of the transparent area

        // New dimensions including the transparent zones
        int newWidth = originalWidth + transparentZoneSize;
        int newHeight = originalHeight + transparentZoneSize;

        // Get the texture data and create a Pixmap



        // Create a new pixmap for the rearranged image
        Pixmap resultPixmap = new Pixmap(newWidth,newHeight, pixmap.getFormat());

        // Create four pixmaps from the original image
        Pixmap part1 = new Pixmap(halfWidth, halfHeight, pixmap.getFormat());
        Pixmap part2 = new Pixmap(halfWidth, halfHeight, pixmap.getFormat());
        Pixmap part3 = new Pixmap(halfWidth, halfHeight, pixmap.getFormat());
        Pixmap part4 = new Pixmap(halfWidth, halfHeight, pixmap.getFormat());

        part1.drawPixmap(pixmap, 0, 0,0,0,halfWidth,halfHeight);  // Top-left
        part2.drawPixmap(pixmap, halfWidth,0,0,0,halfWidth,halfHeight);  // Top-right
        part3.drawPixmap(pixmap, 0,halfHeight,0,0,halfWidth,halfHeight);  // Bottom-left
        part4.drawPixmap(pixmap,halfWidth,  halfHeight,0,0,halfWidth,halfHeight);  // Bottom-right


        // Copy each quadrant of the original pixmap to the new pixmap
        resultPixmap.drawPixmap(part1, 0, 0);  // Top-left
        resultPixmap.drawPixmap(part2, halfWidth+ transparentZoneSize,0);  // Top-right
        resultPixmap.drawPixmap(part3, 0,halfHeight+ transparentZoneSize);  // Bottom-left
        resultPixmap.drawPixmap(part4,halfWidth+transparentZoneSize,  halfHeight+transparentZoneSize);  // Bottom-right

        // Save the new pixmap as a PNG
        FileHandle fileHandle = Gdx.files.local("images/explodedskull4.png"); // Output file path
        System.out.println("Tot hier");
        PixmapIO.writePNG(fileHandle, resultPixmap);


        // Dispose of pixmaps to free memory
        pixmap.dispose();
        part1.dispose();
        part2.dispose();
        part3.dispose();
        part4.dispose();
        resultPixmap.dispose();
        texture.dispose();
    }
}
