package io.github.SoldierVsZombies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Timer;
import java.util.TimerTask;

public class ScoreBoardManager {

    private Float timer; // Time in seconds
    private int lives;
    private int kills;
    private int ammo;
    private int ammoFired;
    private int woodToCollect;
    private float health;
    private int score;


    private boolean timerRunning;

    private final IndicatorBar healthIndicator;
    private final IndicatorBar livesIndicator;
    private final IndicatorBar ammunitionIndicator;
    private final IndicatorBar killsIndicator;
    private final IndicatorBar timerIndicator;
    private final IndicatorBar woodToCollectIndicator;

    private final SpriteBatch spriteBatch;

    private GameState gameState;



    public ScoreBoardManager(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        healthIndicator = new IndicatorBar("Health",10,10,250,15);
        livesIndicator = new IndicatorBar("Lives",10,10+15+5,250,15);
        ammunitionIndicator = new IndicatorBar("Ammunition",10,10+20+20,250,15);
        killsIndicator = new IndicatorBar("Kills",10,10+20+20+20,250,15);
        timerIndicator = new IndicatorBar("Time left",10,10+20+20+20+20,250,15);
        woodToCollectIndicator = new IndicatorBar("Wood",10,10+20+20+20+20+20,250,15);
        gameState = GameState.PAUSED;
        setDefaultValues();
    }

    private void setDefaultValues() {
        kills = 0;
        ammo = 200;
        ammoFired = 0;
        lives = 3;
        health = 100;
        woodToCollect = 3;
        startTimer();
    }


    public void render(IntPosition offsetPixelPos) { // must be called in render() from main after spritebatch.end() !!
        if (spriteBatch.isDrawing()) spriteBatch.end();
        healthIndicator.render(spriteBatch,getHealth(),true,offsetPixelPos);
        livesIndicator.render(spriteBatch,getLives(),false,offsetPixelPos);
        ammunitionIndicator.render(spriteBatch,getAmmo(),false,offsetPixelPos);
        killsIndicator.render(spriteBatch,getKills(),false,offsetPixelPos);
        timerIndicator.render(spriteBatch,getTimer(),false,offsetPixelPos);
        woodToCollectIndicator.render(spriteBatch,getWoodToCollect(),false,offsetPixelPos   );

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
                }
            }
        }, 0, 1000); // Start immediately and repeat every 1000 milliseconds (1 second)
    }


    public  void dispose(){
        healthIndicator.dispose();
        livesIndicator.dispose();
        ammunitionIndicator.dispose();
        killsIndicator.dispose();
        timerIndicator.dispose();
        woodToCollectIndicator.dispose();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Float getTimer() {
        return timer;
    }

    public void setTimer(Float timer) {
        this.timer = timer;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(Integer lives) {
        this.lives = lives;
    }

    public void addLives(int deltaLives) {
        lives+=deltaLives;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getAmmo() {
        return ammo;
    }

    public void setAmmo(Integer ammo) {

        this.ammo = ammo > 0 ? ammo : 0;
    }

    public Integer getAmmoFired() {
        return ammoFired;
    }

    public void setAmmoFired(Integer ammoFired) {
        this.ammoFired = ammoFired;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health <= 100 ? health : 100;
        this.health = this.health >= 0 ? this.health : 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWoodToCollect() {
        return woodToCollect;
    }

    public void setWoodToCollect(int woodToCollect) {
        this.woodToCollect = woodToCollect;
    }

    public void addKills(int deltaKills ) {
        kills+=deltaKills;
    }

    public void addAmmo(int deltaAmmo) {
        ammo+=deltaAmmo;
        ammo = ammo >= 0 ? ammo : 0;
    }
    public void addHealth(float deltaHealth) {
        health+=deltaHealth;
        health = health <= 100 ? health : 100;
        health = this.health >= 0 ? this.health : 0;
    }

    public void addWoodToCollect(int deltaWoodToCollect) {
        woodToCollect+=deltaWoodToCollect;
    }
}
