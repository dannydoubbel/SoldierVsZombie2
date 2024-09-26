package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {


    private static final int MAX_SOLDIER_PER_DIRECTION  = 11;
    private static final int MAX_SOLDIER_DIRECTIONS = 4;
    private static final int WIDTH_SOLDIER = 1650 / MAX_SOLDIER_PER_DIRECTION ;
    private static final int HEIGHT_SOLDIER = 468 / MAX_SOLDIER_DIRECTIONS;



    private SpriteBatch batch;
    private Texture image;
    private TextureRegion textureRegion;
    private float rotationAngle = 0;

    private TextureRegion[][] SolderTextureRegion;

    @Override
    public void create() {
        SharedVariables.getInstance().setCurrentSolderDirection(Directions.dn);
        SolderTextureRegion = loadMainCharacter();
        Gdx.input.setInputProcessor(new MyInputProcessor());
        batch = new SpriteBatch();
//        image = new Texture("libgdx.png");
        image = new Texture(Gdx.files.internal("images/tank.png"));

        // FileHandle fileHandle = Gdx.files.internal( "maze/doolhof.json");
        // System.out.println(fileHandle.readString());


        textureRegion = new TextureRegion(image);
    }

    @Override
    public void render() {
        SharedVariables sharedVariables = SharedVariables.getInstance();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(textureRegion,
            Gdx.graphics.getWidth() / 2 - image.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - image.getHeight() / 2,
            textureRegion.getRegionWidth() / 2,
            textureRegion.getRegionHeight() / 2,
            textureRegion.getRegionWidth(),
            textureRegion.getRegionHeight(),
            1,1,
            rotationAngle
        );

        batch.draw(SolderTextureRegion[sharedVariables.getCurrentSolderDirection().getValue()][sharedVariables.getTextureIndexSoldier()],100,100);



        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            rotationAngle += 1;
            //System.out.println("Spacebar pressed, rotationAngle: " + rotationAngle);

        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }

    TextureRegion[][] loadMainCharacter() {
        Texture spriteSheet = new Texture(Gdx.files.internal("images/top down soldier.png"));
        TextureRegion[][] spriteRegions = new TextureRegion[MAX_SOLDIER_DIRECTIONS][MAX_SOLDIER_PER_DIRECTION];
        for (int row = 0; row < MAX_SOLDIER_DIRECTIONS; row++) {
            for (int col = 0; col < MAX_SOLDIER_PER_DIRECTION; col++) {
                spriteRegions[row][col] =
                    new TextureRegion(
                        spriteSheet,
                        col * WIDTH_SOLDIER,
                        row * HEIGHT_SOLDIER,
                        WIDTH_SOLDIER,
                        HEIGHT_SOLDIER);
            }
        }
        return spriteRegions;
    }


}
