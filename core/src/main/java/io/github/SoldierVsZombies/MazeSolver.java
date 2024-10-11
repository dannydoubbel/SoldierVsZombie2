package io.github.SoldierVsZombies;

public class MazeSolver {

    public IntPosition predictNextMove(boolean[][] backGroundTileMap, IntPosition start, IntPosition end) { // refactor to int[][]
        int startX = start.getX();
        int startY = start.getY();
        int endX = end.getX();
        int endY = end.getY();

        // Possible directions: up, down, left, right
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        // Best next move initialization
        IntPosition bestMove = null;
        int minDistance = Integer.MAX_VALUE;

        for (int[] direction : directions) {
            int newX = startX + direction[0];
            int newY = startY + direction[1];

            // If the immediate next move is valid
            if (isValidMove(backGroundTileMap, newX, newY)) {

                // Perform a BFS-like scan for the next move to avoid dead ends
                if (!isDeadEnd(backGroundTileMap, newX, newY)) {
                    int distance = manhattanDistance(newX, newY, endX, endY);
                    if (distance < minDistance) {
                        minDistance = distance;
                        bestMove = new IntPosition(newX, newY);
                    }
                }
            }
        }

        return bestMove;  // Return the next best position based on lookahead
    }

    // Check if the position is within bounds and walkable
    private boolean isValidMove(boolean[][] map, int x, int y) { // refactor to int[][]
        int cols = map.length;
        int rows = map[0].length;
        return x >= 0 && x < cols && y >= 0 && y < rows && map[x][y]; // refactor to isWalkable
    }

    // Calculate Manhattan distance between two points
    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    // Check if the next move leads to a dead-end (scan ahead one more step)
    private boolean isDeadEnd(boolean[][] map, int x, int y) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int possibleMoves = 0;

        // Look at the next possible moves from the current position
        for (int[] direction : directions) {
            int nextX = x + direction[0];
            int nextY = y + direction[1];
            if (isValidMove(map, nextX, nextY)) {
                possibleMoves++;
            }
        }

        // A dead-end is when no further moves are possible from the new position
        return possibleMoves == 0;
    }
}
