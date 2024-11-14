package io.github.SoldierVsZombies;

public enum ShortLifeTimeSpriteType {
    DEAD_ZOMBIE(1, "dead zombie",64,96,64,96),
    DEAD_SKULL(2, "dead skull",100,100,100,100),
    BLACK_GIFT(3, "black gift box",100,100,64,64),
    PRINCESS(4, "princess",100,100,100,100),
    PORTAL_SHINE(5, "portal shine",100,100,100,100),
    WOW_YELL(6, "wow",100,100,100,100),
    WOOD_FIRE(7, "wood fire",100,100,64,64),
    FLAMING_TORCH(8, "flaming torch",32,64,32,64),
    WOOD_TO_COLLECT(9,"wood to collect",100,100,64,64),
    BULLET_MAGAZINE(10,"bullet magazine",100,100,64,64),
    MEDICAL_KIT(11,"medical kit",100,100,64,64),
    GAME_OVER(12,"game over",1500,1500,500,500),
    GAME_PLAY(13,"game play",1200,1200,500,500);



    private final int VALUE;
    public final int WIDTH;
    public final int HEIGHT;
    public final int DRAW_WIDTH;
    public final int DRAW_HEIGHT;
    private final String NAME;

    ShortLifeTimeSpriteType(int value, String name,int WIDTH,int HEIGHT,int DRAW_WIDTH,int DRAW_HEIGHT) {
        this.VALUE = value;
        this.NAME = name;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.DRAW_WIDTH =  DRAW_WIDTH <=0 ? WIDTH : DRAW_WIDTH;
        this.DRAW_HEIGHT = DRAW_HEIGHT <=0 ? HEIGHT : DRAW_HEIGHT;
    }


    @Override
    public String toString() {
        return "ShortLifeTimeSpriteType{" +
            "VALUE=" + VALUE +
            ", WIDTH=" + WIDTH +
            ", HEIGHT=" + HEIGHT +
            ", DRAW_WIDTH=" + DRAW_WIDTH +
            ", DRAW_HEIGHT=" + DRAW_HEIGHT +
            ", NAME='" + NAME + '\'' +
            '}';
    }
}
