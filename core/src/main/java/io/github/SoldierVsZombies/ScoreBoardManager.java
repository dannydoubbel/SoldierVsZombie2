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

import java.util.Timer;
import java.util.TimerTask;

public class ScoreBoardManager  {
    private static ScoreBoardManager instance;
    private BitmapFont fontSmall;
    private BitmapFont fontBig;
    private final Stage stage;

    private Table scoreBoardTable;
    private Float timer = 0f; // Time in seconds
    private Integer lives = 3;
    private Integer kills = 10;
    private Integer ammo = 50;

    private Integer ammoFired = 0;
    private Label livesLabel;
    private Label livesNumber;
    private Label ammoLabel;
    private Label ammoNumber;
    private Label killsLabel;
    private Label killsNumber;
    private Label timerLabel;
    private Label timerNumber;

    private boolean timerRunning;
    //private ScheduledExecutorService scheduler;


    final SpriteBatch spriteBatch;

    private Pixmap pixmap;


    public ScoreBoardManager(SpriteBatch spriteBatch) {
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
        stage.addActor(scoreBoardTable);
    }

    private void setDefaultValues() {
        kills = 0;
        ammo = 200;
        ammoFired = 0;
        lives = 3;
        startTimer();
    }

    private void createTable(TextureRegionDrawable drawable) {
        // Table to arrange labels
        scoreBoardTable = new Table();
        scoreBoardTable.top().left();
        scoreBoardTable.setSize(60,100);
        scoreBoardTable.setFillParent(false); // The table will take up the whole stage
        scoreBoardTable.setPosition(0, Gdx.graphics.getHeight() - scoreBoardTable.getHeight());
        scoreBoardTable.setBackground(drawable);
    }

    private static TextureRegionDrawable createDrawableTextureRegion(Pixmap pixmap) {
        // Create a drawable from the pixmap
        Texture texture = new Texture(pixmap);
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private void createBackground() {
        // Create a semi-transparent green background
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 0.5f); // Light green (R=0.5, G=1, B=0.5), with 0.5 transparency (A=0.5)
        pixmap.fill();
    }

    private void addLabelsToTable() {
        addLabelPair(livesLabel, livesNumber);
        addLabelPair(ammoLabel, ammoNumber);
        addLabelPair(killsLabel, killsNumber);
        addLabelPair(timerLabel, timerNumber);

    }
    private void addLabelPair(Label label, Label number) {
        scoreBoardTable.add(label).pad(0, 0, 0, 0).expandX().left();  // Align label to the left
        scoreBoardTable.add(number).pad(0, 0, 0, 2).expandX().right(); // Align number to the right
        scoreBoardTable.row();  // Move to the next row
    }

    private void createLabels() {
        // Create your labels
        livesLabel = createLabel("Lives:", fontSmall, Color.WHITE);
        livesNumber = createLabel(lives.toString(), fontBig, Color.PURPLE);

        ammoLabel = createLabel("Ammo:", fontSmall, Color.WHITE);
        ammoNumber = createLabel(ammo.toString(), fontBig, Color.PURPLE);

        killsLabel = createLabel("Kills:", fontSmall, Color.WHITE);
        killsNumber = createLabel(kills.toString(), fontBig, Color.PURPLE);

        timerLabel = createLabel("Time left:", fontSmall, Color.WHITE);
        timerNumber = createLabel(timer.toString(), fontBig, Color.PURPLE);


    }
    private Label createLabel(String text, BitmapFont font, Color color) {
        return new Label(text, new Label.LabelStyle(font, color));
    }

    private void setUpFonts() {
        fontSmall = new BitmapFont();
        fontSmall.getData().setScale(0.4f);
        fontBig = new BitmapFont();
        fontBig.getData().setScale(0.4f);
    }

    public void render(SpriteBatch spriteBatch){ // must be called in render() from main after spritebatch.end() !!
        if (spriteBatch.isDrawing())        spriteBatch.end();
        stage.act();
        stage.draw();
        timerNumber.setText(String.format("%.1f", timer));
    }


public void startTimer() {
    timer = 300f; // Set timer to 300 seconds
    timerRunning = true; // Start the timer

    Timer timerScheduler = new Timer();
    timerScheduler.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            if (timerRunning) {
                timer -= 1; // Decrease timer by 1 second
                if (timer <= 0) {
                    timer = 0f;
                    timerRunning = false;
                    System.out.println("Timer finished!");
                    // TODO: implement further actions here
                    timerScheduler.cancel(); // Stop the timer
                }
                // Update the timer label on the main thread
                Gdx.app.postRunnable(() -> timerNumber.setText(String.format("%.1f", timer)));
            }
        }
    }, 0, 1000); // Start immediately and repeat every 1000 milliseconds (1 second)
}


    public Integer getAmmo() {
        return ammo;
    }

    public void setAmmo(Integer ammo) {
        ammoNumber.setText(ammo.toString());
        this.ammo = ammo;
    }
    public void addAmmo(Integer deltaAmmo) {
        setAmmo(ammo +deltaAmmo);
    }

    public Integer getAmmoFired() {
        return ammoFired;
    }

    public void setAmmoFired(Integer ammoFired) {
        this.ammoFired = ammoFired;
    }


    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        killsNumber.setText(kills.toString());
        this.kills = kills;
    }
    public void addKills(Integer deltaKills) {
        setKills(kills+deltaKills);
    }



    public static ScoreBoardManager getInstance(SpriteBatch spriteBatch) {
        if (instance == null) {
            instance = new ScoreBoardManager(spriteBatch);
        }
        return instance;
    }

}
