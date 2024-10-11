package io.github.SoldierVsZombies;

public class CollisionDetector {

    private static CollisionDetector instance;


    // Sprite dimensions (30x30)
    private static final int SPRITE_WIDTH = 30;
    private static final int SPRITE_HEIGHT = 30;

    // Detect if two sprites are colliding based on their positions
    public boolean isColliding(IntPosition pos1, IntPosition pos2) {
        // Get boundaries for the first sprite
        int left1 = pos1.getX() - SPRITE_WIDTH / 2;
        int right1 = pos1.getX() + SPRITE_WIDTH / 2;
        int top1 = pos1.getY() - SPRITE_HEIGHT / 2;
        int bottom1 = pos1.getY() + SPRITE_HEIGHT / 2;

        // Get boundaries for the second sprite
        int left2 = pos2.getX() - SPRITE_WIDTH / 2;
        int right2 = pos2.getX() + SPRITE_WIDTH / 2;
        int top2 = pos2.getY() - SPRITE_HEIGHT / 2;
        int bottom2 = pos2.getY() + SPRITE_HEIGHT / 2;

        // Check for overlap in the horizontal and vertical directions
        boolean horizontalOverlap = left1 < right2 && right1 > left2;
        boolean verticalOverlap = top1 < bottom2 && bottom1 > top2;

        // If both horizontal and vertical overlap, the sprites are colliding
        return horizontalOverlap && verticalOverlap;
    }

    public static CollisionDetector getInstance() {
        if (instance == null) {
            instance = new CollisionDetector();
        }
        return instance;
    }
}

