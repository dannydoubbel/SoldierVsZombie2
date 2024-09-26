package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private TextureRegion textureRegion;
    private float rotationAngle = 0;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new MyInputProcessor());
        batch = new SpriteBatch();
//        image = new Texture("libgdx.png");
        image = new Texture(Gdx.files.internal("images/tank.png"));

        FileHandle fileHandle = Gdx.files.internal( "maze/doolhof.json");
        System.out.println(fileHandle.readString());


        textureRegion = new TextureRegion(image);
    }

    @Override
    public void render() {

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


}
