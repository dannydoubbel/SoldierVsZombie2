package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

public class SplitUpAndRearangePNGs {
    //
    // SIDE PROJECT
    //
    // THIS CLASS PURPOSE IS ONLY FOR DEVELOPMENT
    //
    // IT LOADS A SPRITE MAP, DIVIDES IT IN PIECES AND REARANGES THE ORDER
    //
    // IN ORDER TO GET THE DIRECTIONS OF THE SPRITE IN LINE WITH THE DIRECTIONS ENUM
    //
    //
    //
    public void splitUpAndReArrangeHelper() {
        // Load the image file as a texture
        FileHandle fileHandle = Gdx.files.internal("images/woodFire100x100.png"); // Your PNG file path here
        Texture texture = new Texture(fileHandle);

        // Get the texture data
        TextureData textureData = texture.getTextureData();

        // Make sure the texture data is prepared
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        // Get the texture data and create a Pixmap
        Pixmap sourcePixmap = texture.getTextureData().consumePixmap();


        // Make sure the texture data is prepared
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        // Dimensions of each part
        int width = 100;
        int height = 100;
        Pixmap[] part = new Pixmap[12];
        Pixmap resultPixmap = new Pixmap(width, 100 * 12, sourcePixmap.getFormat());
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                int position = ((x * width)/100)+((y * height * 4)/100);
                part[position]  = new Pixmap(width, height, sourcePixmap.getFormat());
                part[position].drawPixmap(sourcePixmap, 0,0,x*width, y*width,  width, height);
                FileHandle outputFile = Gdx.files.local("images/woodFire100x100long"+position+".png"); // Output file path
                PixmapIO.writePNG(outputFile, part[position]);
                System.out.println("Teller " + position);
            }
        }
        for (int lus = 0; lus < 12; lus++) {
            resultPixmap.drawPixmap(part[lus],lus*width,0,0,0,width,height);
        }


        // Save the new image to a file
        FileHandle outputFile = Gdx.files.local("images/woodFire100x100long.png"); // Output file path
        PixmapIO.writePNG(outputFile, resultPixmap);

        sourcePixmap.dispose();

        resultPixmap.dispose();
        texture.dispose();
    }

    public void splitUpAndReArrangeHelperOriginal() {
        // Load the image file as a texture
        FileHandle fileHandle = Gdx.files.internal("images/zombie.png"); // Your PNG file path here
        Texture texture = new Texture(fileHandle);

        // Get the texture data
        TextureData textureData = texture.getTextureData();

        // Make sure the texture data is prepared
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }


        // Get the texture data and create a Pixmap
        Pixmap pixmap = texture.getTextureData().consumePixmap();


        // Make sure the texture data is prepared
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        // Dimensions of each part
        int width = 128;
        int height = 48; // 192 / 4

        // Create four pixmaps from the original image
        Pixmap part1 = new Pixmap(width, height, pixmap.getFormat());
        Pixmap part2 = new Pixmap(width, height, pixmap.getFormat());
        Pixmap part3 = new Pixmap(width, height, pixmap.getFormat());
        Pixmap part4 = new Pixmap(width, height, pixmap.getFormat());

        // Copy each part from the original pixmap
        part1.drawPixmap(pixmap, 0, 0, 0, 0, width, height);        // Top (part 1)
        part2.drawPixmap(pixmap, 0, 0, 0, height, width, height);   // Second (part 2)
        part3.drawPixmap(pixmap, 0, 0, 0, 2 * height, width, height); // Third (part 3)
        part4.drawPixmap(pixmap, 0, 0, 0, 3 * height, width, height); // Bottom (part 4)

        // Create a new pixmap for the rearranged image
        Pixmap resultPixmap = new Pixmap(width, 192, pixmap.getFormat());

        // Draw the parts in the desired order (1, 4, 2, 3)
        resultPixmap.drawPixmap(part1, 0, 0);                     // Part 1 on top
        resultPixmap.drawPixmap(part4, 0, height);                // Part 4 under Part 1
        resultPixmap.drawPixmap(part2, 0, 2 * height);            // Part 2 under Part 4
        resultPixmap.drawPixmap(part3, 0, 3 * height);            // Part 3 on the bottom

        // Save the new image to a file
        FileHandle outputFile = Gdx.files.local("images/zombie128x192DnUpLtRt.png"); // Output file path
        PixmapIO.writePNG(outputFile, resultPixmap);

        pixmap.dispose();
        part1.dispose();
        part2.dispose();
        part3.dispose();
        part4.dispose();
        resultPixmap.dispose();
        texture.dispose();
    }
}
