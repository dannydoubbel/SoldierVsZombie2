package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScoreBoardManager  {
    private static ScoreBoardManager instance;
    private BitmapFont fontSmall;
    private BitmapFont fontBig;
    private Stage stage;

    private Table table;
    private Float timer = 0f; // Time in seconds
    private Integer lives = 3;
    private Integer ammo = 50;
    private Integer kills = 10;
    private Label livesLabel;
    private Label livesNumber;
    private Label ammoLabel;
    private Label ammoNumber;
    private Label killsLabel;
    private Label killsNumber;
    private Label timerLabel;
    private Label timerNumber;

    private boolean timerRunning;
    private ScheduledExecutorService scheduler;


    final SpriteBatch spriteBatch;

    private Pixmap pixmap;


    public ScoreBoardManager(SpriteBatch spriteBatch) {
        System.out.println("manager");
        this.spriteBatch = spriteBatch;
        stage = new Stage(new ScreenViewport(), spriteBatch);
        setDefaultValues();
        setUpFonts();
        createBackground();
        TextureRegionDrawable drawable = createDrawableTextureRegion(pixmap);
        createTable(drawable);
        pixmap.dispose(); // Dispose the pixmap after use (to avoid memory leaks)
        createLabels();
        addLabelsToTable();
        stage.addActor(table);
    }

    private void setDefaultValues() {
        kills = 0;
        ammo = 10000;
        lives = 3;
        startTimer();
    }

    private void createTable(TextureRegionDrawable drawable) {
        // Table to arrange labels
        table = new Table();
        table.top().left();
        table.setSize(40,100);
        table.setFillParent(false); // The table will take up the whole stage
        table.setPosition(0, Gdx.graphics.getHeight() - table.getHeight());
        table.setBackground(drawable);
    }

    private static TextureRegionDrawable createDrawableTextureRegion(Pixmap pixmap) {
        // Create a drawable from the pixmap
        Texture texture = new Texture(pixmap);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        return drawable;
    }

    private void createBackground() {
        // Create a semi-transparent green background
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 0.5f); // Light green (R=0.5, G=1, B=0.5), with 0.5 transparency (A=0.5)
        pixmap.fill();
    }

    private void addLabelsToTable() {
        // Add labels to the table for lives
        table.add(livesLabel).pad(0,0,0,0).expandX().left(); // Align livesLabel to left
        table.add(livesNumber).pad(0,0,0,2).expandX().right(); // Align livesNumber to right
        table.row(); // Move to the next row

        // Add labels to the table for ammo
        table.add(ammoLabel).pad(0,0,0,0).expandX().left(); // Align ammoLabel to left
        table.add(ammoNumber).pad(0,0,0,2).expandX().right(); // Align ammoNumber to right
        table.row(); // Move to the next row

        table.add(killsLabel).pad(0,0,0,0).expandX().left(); // Align ammoLabel to left
        table.add(killsNumber).pad(0,0,0,2).expandX().right(); // Align ammoNumber to right
        table.row(); // Move to the next row
        table.row(); // Move to the next row
        table.add(timerLabel).pad(0,0,0,0).expandX().left(); // Align ammoLabel to left
        table.add(timerNumber).pad(0,0,0,2).expandX().right(); // Align ammoNumber to right
        table.row(); // Move to the next row
    }

    private void createLabels() {
        // Create your labels
        livesLabel = new Label("Lives:" , new Label.LabelStyle(fontSmall, Color.WHITE));
        livesNumber = new Label(lives.toString(), new Label.LabelStyle(fontBig, Color.PURPLE));

        ammoLabel = new Label("Ammo:" , new Label.LabelStyle(fontSmall, Color.WHITE));
        ammoNumber = new Label(ammo.toString(), new Label.LabelStyle(fontBig, Color.PURPLE));

        killsLabel = new Label("Kills:" , new Label.LabelStyle(fontSmall, Color.WHITE));
        killsNumber = new Label(kills.toString(), new Label.LabelStyle(fontBig, Color.PURPLE));

        timerLabel = new Label("Time left:" , new Label.LabelStyle(fontSmall, Color.WHITE));
        timerNumber = new Label(timer.toString(), new Label.LabelStyle(fontBig, Color.PURPLE));
    }

    private void setUpFonts() {
        fontSmall = new BitmapFont();
        fontSmall.getData().setScale(0.4f);
        fontBig = new BitmapFont();
        fontBig.getData().setScale(0.4f);
    }

    public void draw(){ // must be called in render() from main after spritebatch.end() !!
        stage.act();
        stage.draw();
        timerNumber.setText(String.format("%.1f", timer));
    }



    public void startTimer() {
        timer = 300f; // Set timer to 300 seconds
        timerRunning = true; // Start the timer
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (timerRunning) {
                timer -= 1; // Decrease timer by 1 second
                if (timer <= 0) {
                    timer = 0f;
                    timerRunning = false;
                    System.out.println("Timer finished!");
                    scheduler.shutdown(); // Stop the timer
                }
                // Update the timer label on the main thread
                Gdx.app.postRunnable(() -> timerLabel.setText(String.format("%.1f", timer)));
            }
        }, 0, 1, TimeUnit.SECONDS); // Execute every second
    }


    public Integer getLives() {
        return lives;
    }

    public void setLives(Integer lives) {
        livesNumber.setText(lives.toString());
        this.lives = lives;
    }

    public Integer getAmmo() {
        return ammo;
    }

    public void setAmmo(Integer ammo) {
        ammoNumber.setText(ammo.toString());
        this.ammo = ammo;
    }

    public Float getTimer() {
        return timer;
    }

    public void setTimer(Float timer) {
        this.timer = timer;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        killsNumber.setText(kills.toString());
        this.kills = kills;
    }

    public static ScoreBoardManager getInstance(SpriteBatch spriteBatch) {
        if (instance == null) {
            instance = new ScoreBoardManager(spriteBatch);
        }
        return instance;
    }

}
