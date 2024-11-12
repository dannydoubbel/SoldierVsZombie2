package io.github.SoldierVsZombies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class IndicatorBar {

    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final int x, y, width, height;
    private final Texture barTexture;
    private final String title;


    public IndicatorBar(String title, int x, int y, int width, int height) {
        this.shapeRenderer = new ShapeRenderer();
        this.title = title;
        this.font = new BitmapFont(); // Use a pre-scaled font if available
        this.layout = new GlyphLayout();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE); // White color to allow tinting later
        pixmap.fill();
        barTexture = new Texture(pixmap);

    }

    public void render(SpriteBatch spriteBatch, float valueToIndicate, boolean valueIsPercentage,IntPosition offsetPixelPos) {
        if (spriteBatch.isDrawing()) spriteBatch.end();
        spriteBatch.begin();
        // Draw the background bar with semi-transparency
        spriteBatch.setColor(0, 0, 0, 0.5f); // Black with 50% transparency
        spriteBatch.draw(barTexture, x+offsetPixelPos.getX(), y+offsetPixelPos.getY(), width, height);/////////////////////////////////////////////////////////////////

        Color fontColor;
        if (valueIsPercentage) {
            if (valueToIndicate < 25) {
                spriteBatch.setColor(1, 0, 0, 0.8f); // Red with 80% opacity for low energy
                fontColor = Color.RED;
            } else {
                spriteBatch.setColor(0, 0, 1, 0.8f); // Blue with 80% opacity for sufficient energy
                fontColor = Color.WHITE;
            }
            // Draw the filled portion of the energy bar
            spriteBatch.draw(barTexture, x+offsetPixelPos.getX(), y+offsetPixelPos.getY(), (width * valueToIndicate) / 100, height);/////////////////////////////////////////////////////////////////
        } else {
            spriteBatch.setColor(0, 0, 1, 0.3f);
            spriteBatch.draw(barTexture, x+offsetPixelPos.getX(), y+offsetPixelPos.getY(), width, height);/////////////////////////////////////////////////////////////////
            fontColor = Color.WHITE;
        }

        // Reset color to white for drawing text
        spriteBatch.setColor(fontColor);

        // Draw the energy percentage text in the center of the energy bar
        String textValue = String.format("%.0f", valueToIndicate);
        if (valueIsPercentage) textValue += "%";
        layout.setText(font, textValue);
        float textWidth = layout.width;
        float textHeight = layout.height;

        //set font size
        font.getData().setScale(1.1f);

        // Calculate the centered position for the text
        float textX = x + (width - textWidth) / 2;
        float textY = y + (height + textHeight) / 2;
        font.draw(spriteBatch, layout, textX+offsetPixelPos.getX(), textY+offsetPixelPos.getY());/////////////////////////////////////////////////////////////////


        layout.setText(font, title);
        float textX2 = x + 5;
        float textY2 = y + (height + textHeight) / 2;
        font.draw(spriteBatch, layout, textX2+ offsetPixelPos.getX(), textY2+offsetPixelPos.getY());/////////////////////////////////////////////////////////////////


        spriteBatch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        barTexture.dispose();
    }
}

