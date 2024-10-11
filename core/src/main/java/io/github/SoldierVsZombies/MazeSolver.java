package io.github.SoldierVsZombies;

public class MazeSolver {

    private static MazeSolver instance;
    private Tiles tiles;

    private MazeSolver() {
        tiles = Tiles.getInstance();
    }

    public static MazeSolver getInstance() {
        if (instance == null) {
            instance = new MazeSolver();
        }
        return instance;
    }

    // Predict the next move direction based on the start and end positions
    public Directions getBestDirection(IntPosition startTilePosition, IntPosition endTilePosition) {
        int startX = startTilePosition.getX();
        int startY = startTilePosition.getY();
        int endX = endTilePosition.getX();
        int endY = endTilePosition.getY();

        // Possible directions: down, up, right, left
        int[][] possibleDirectionPositions = {
            {0, 1},  // Down
            {0, -1}, // Up
            {1, 0},  // Right
            {-1, 0}  // Left
        };

        // Corresponding directions in the Directions enum
        Directions[] possibleDirections = {
            Directions.dn,
            Directions.up,
            Directions.rt,
            Directions.lt
        };

        Directions bestDirection = Directions.no;
        int minDistance = Integer.MAX_VALUE;

        // Iterate through possible directions
        for (int i = 0; i < possibleDirectionPositions.length; i++) {
            int newX = startX + possibleDirectionPositions[i][0];
            int newY = startY + possibleDirectionPositions[i][1];

            // If the immediate next move is valid
            if (isValidMove(newX, newY)) {

                // Perform a BFS-like scan for the next move to avoid dead ends
                if (!isDeadEnd(newX, newY)) {
                    int distance = manhattanDistance(newX, newY, endX, endY);
                    if (distance < minDistance) {
                        minDistance = distance;
                        bestDirection = possibleDirections[i];
                    }
                }
            }
        }

        return bestDirection;  // Return the best direction based on the lookahead
    }

    // Check if the position is within bounds and walkable
    private boolean isValidMove(int x, int y) {
        int cols = Tiles.TILE_MAP_COLS;
        int rows = Tiles.TILE_MAP_ROWS;
        return x >= 0 && x < cols && y >= 0 && y < rows && tiles.isTileWalkable(new IntPosition(x, y));
    }

    // Calculate Manhattan distance between two points
    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    // Check if the next move leads to a dead-end (scan ahead one more step)
    private boolean isDeadEnd(int x, int y) {
        int[][] possibleDirections = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int possibleMoves = 0;

        // Look at the next possible moves from the current position
        for (int[] direction : possibleDirections) {
            int nextX = x + direction[0];
            int nextY = y + direction[1];
            if (isValidMove(nextX, nextY)) {
                possibleMoves++;
            }
        }

        // A dead-end is when no further moves are possible from the new position
        return possibleMoves == 0;
    }
}



