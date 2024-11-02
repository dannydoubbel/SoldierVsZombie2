package io.github.SoldierVsZombies;

public class MazeSolver {

    private static MazeSolver instance;
    private static Tiles tiles =  Tiles.getInstance();

    private MazeSolver() {
    }


    // Wrapper methods for horizontal and vertical range detection
    public static IntDuoNumbers getHorizontalWalkableRange(IntPosition startTilePos) {
        return getWalkableRange(startTilePos, Orthogonal.HORIZONTAL);
    }

    public static IntDuoNumbers getVerticalWalkableRange(IntPosition startTilePos) {
        return getWalkableRange(startTilePos, Orthogonal.VERTICAL);
    }

    private static IntDuoNumbers getWalkableRange(IntPosition startTilePos, Orthogonal orthogonal) {
        IntDuoNumbers resultDuoNumbers = new IntDuoNumbers();
        int testX = startTilePos.getX();
        int testY = startTilePos.getY();
        boolean moved = false;
        if (orthogonal.equals(Orthogonal.NOTHING)) {
            throw new IllegalArgumentException("Verboden nothing in functie getWalkableRange");
        }
        // Calculate negative bound (left or down)
        if (orthogonal == Orthogonal.HORIZONTAL) {
            while (testX > 0 && tiles.isTileWalkable(new IntPosition(testX, testY))) {
                moved = true;
                testX--;
            }
            resultDuoNumbers.setInt1(moved ? testX + 1 : testX);

            // Reset and calculate positive bound (right)
            testX = startTilePos.getX();
            moved = false;
            while (testX < Tiles.TILE_MAP_COLS - 1 && tiles.isTileWalkable(new IntPosition(testX, testY))) {
                moved = true;
                testX++;
            }
            resultDuoNumbers.setInt2(moved ? testX - 1 : testX);

        } else { // VERTICAL direction
            while (testY > 0 && tiles.isTileWalkable(new IntPosition(testX, testY))) {
                moved = true;
                testY--;
            }
            resultDuoNumbers.setInt1(moved ? testY + 1 : testY);

            // Reset and calculate positive bound (up)
            testY = startTilePos.getY();
            moved = false;
            while (testY < Tiles.TILE_MAP_ROWS - 1 && tiles.isTileWalkable(new IntPosition(testX, testY))) {
                moved = true;
                testY++;
            }
            resultDuoNumbers.setInt2(moved ? testY - 1 : testY);
        }
        return resultDuoNumbers;
    }

    public static IntPosition findPath(IntPosition tileStartPos, IntPosition tileDestinationPos) {
        IntPosition earlyDetectionPath = findEarlyPaths(tileStartPos, tileDestinationPos);
        if (!earlyDetectionPath.equals(new IntPosition(-1, -1))) {
            //System.out.println("Early");
            return earlyDetectionPath;
        }
        return tileStartPos;
    }

    private static IntPosition findEarlyPaths(IntPosition tilePositionStart, IntPosition tilePositionDestination) {
        if (tilePositionStart.equals(tilePositionDestination)) {
            return tilePositionDestination;
        }

        IntPosition directOrthogonal;

        directOrthogonal =  pathDirectHorizontalLine(tilePositionStart, tilePositionDestination);
        if (!directOrthogonal.equals(tilePositionStart)) {
            //System.out.println("Found pathDirectHorizontalLine ");
            return directOrthogonal;
        }

        directOrthogonal = pathDirectVerticalLine(tilePositionStart, tilePositionDestination);
        if (!directOrthogonal.equals(tilePositionStart)) {
            //System.out.println("Found pathDirectVerticalLine");
            return directOrthogonal;
        }

        int deepnessLevel = 2;
        do {
            directOrthogonal = pathNextLevel(tilePositionStart, tilePositionDestination, deepnessLevel);
            if (!directOrthogonal.equals(tilePositionStart)) {
                return directOrthogonal;
            }
            deepnessLevel++;
        } while (deepnessLevel <= 9);
        return new IntPosition(-1, -1);
    }

    private static IntPosition pathDirectHorizontalLine(IntPosition tilePosStart, IntPosition tilePosDestination) {
        IntPosition directOrthogonal = tilePosStart.clone();
        IntDuoNumbers horizontalRange = getHorizontalWalkableRange(tilePosStart);
        if ((tilePosDestination.getX() >= horizontalRange.getLowest() && tilePosDestination.getX() <= horizontalRange.getHighest()) && (tilePosStart.getY() == tilePosDestination.getY())) {
            if (tilePosStart.getX() < tilePosDestination.getX()) {
                directOrthogonal.addX(+1);
            } else {
                directOrthogonal.addX(-1);
            }
        }
        return directOrthogonal;
    }

    private static IntPosition pathDirectVerticalLine(IntPosition tilePosStart, IntPosition tilePosDestination) {
        IntPosition directOrthogonal = tilePosStart.clone();
        IntDuoNumbers verticalRange = getVerticalWalkableRange(tilePosStart);
        if ((tilePosDestination.getY() >= verticalRange.getLowest() && tilePosDestination.getY() <= verticalRange.getHighest()) && (tilePosStart.getX() == tilePosDestination.getX())) {
            if (tilePosStart.getY() < tilePosDestination.getY()) {
                directOrthogonal.addY(1);
            } else {
                directOrthogonal.addY(-1);
            }
        }
        return directOrthogonal;
    }

    private static IntPosition pathNextLevel(IntPosition tilePosStart, IntPosition tilePosDestination, int searchDeepnessLevel) {
        final int MAX_STEPS = 100000;
        int currentSteps = 0;
        IntPosition toTestTilePos;
        IntDuoNumbers horizontalRange1 =  getHorizontalWalkableRange(tilePosStart);
        int y0 = tilePosStart.getY();
        int x1 = horizontalRange1.getLowest();
        do {
            IntDuoNumbers verticalRange1 = getVerticalWalkableRange(new IntPosition(x1, y0));
            int y1 = verticalRange1.getLowest();
            do {
                toTestTilePos = new IntPosition(x1, y1);
                IntPosition nextTilePos = getNextTilePos(tilePosStart, toTestTilePos);
                if (toTestTilePos.equals(tilePosDestination)) {
                    return getNextTilePos(tilePosStart, toTestTilePos);
                }
                if (searchDeepnessLevel >= 3) {
                    IntDuoNumbers horizontalRange2 = getHorizontalWalkableRange(new IntPosition(x1, y1));
                    int x2 = horizontalRange2.getLowest();
                    do {
                        if (isAtTargetDepthAndPosition(searchDeepnessLevel, 3, new IntPosition(x2, y1), tilePosDestination))
                            return nextTilePos;
                        if (searchDeepnessLevel >= 4) {
                            IntDuoNumbers verticalRange2 = getVerticalWalkableRange(new IntPosition(x2, y1));
                            int y2 = verticalRange2.getLowest();
                            do {
                                if (isAtTargetDepthAndPosition(searchDeepnessLevel, 4, new IntPosition(x2, y2), tilePosDestination))
                                    return nextTilePos;
                                if (searchDeepnessLevel >= 5) {
                                    IntDuoNumbers horizontalRange3 = getHorizontalWalkableRange(new IntPosition(x2, y2));
                                    int x3 = horizontalRange3.getLowest();
                                    do {
                                        if (isAtTargetDepthAndPosition(searchDeepnessLevel, 5, new IntPosition(x3, y2), tilePosDestination))
                                            return nextTilePos;
                                        if (searchDeepnessLevel >= 6) {
                                            IntDuoNumbers verticalRange3 = getVerticalWalkableRange(new IntPosition(x3, y2));
                                            int y3 = verticalRange3.getLowest();
                                            do {
                                                if (isAtTargetDepthAndPosition(searchDeepnessLevel, 6, new IntPosition(x3, y3), tilePosDestination))
                                                    return nextTilePos;
                                                if (searchDeepnessLevel >= 7) {
                                                    IntDuoNumbers horizontalRange4 = getHorizontalWalkableRange(new IntPosition(x3, y3));
                                                    int x4 = horizontalRange4.getLowest();
                                                    do {
                                                        if (isAtTargetDepthAndPosition(searchDeepnessLevel, 7, new IntPosition(x4, y3), tilePosDestination))
                                                            return nextTilePos;
                                                        if (searchDeepnessLevel >= 8) {
                                                            IntDuoNumbers verticalRange4 = getVerticalWalkableRange(new IntPosition(x4, y3));
                                                            int y4 = verticalRange4.getLowest();
                                                            do {
                                                                if (isAtTargetDepthAndPosition(searchDeepnessLevel, 8, new IntPosition(x4, y4), tilePosDestination))
                                                                    return nextTilePos;
                                                                if (searchDeepnessLevel >= 9) {
                                                                    IntDuoNumbers horizontalRange5 = getHorizontalWalkableRange(new IntPosition(x4, y4));
                                                                    int x5 = horizontalRange5.getLowest();
                                                                    do {
                                                                        if (isAtTargetDepthAndPosition(searchDeepnessLevel, 9, new IntPosition(x5, y4), tilePosDestination))
                                                                            return nextTilePos;
                                                                        x5++;
                                                                    } while (x5 < horizontalRange5.getHighest());
                                                                }
                                                                y4++;
                                                                currentSteps++;
                                                            } while (currentSteps < MAX_STEPS && y4 <= verticalRange4.getHighest());
                                                        } // end if searchDeepnessLevel >=8

                                                        x4++;
                                                        currentSteps++;
                                                    } while (currentSteps < MAX_STEPS && x4 <= horizontalRange4.getHighest());
                                                } // end if (searchDeepnessLevel >= 7) {
                                                y3++;
                                                currentSteps++;
                                            } while (currentSteps < MAX_STEPS && y3 <= verticalRange3.getHighest());
                                        } //end if (searchDeepnessLevel >=6)
                                        x3++;
                                        currentSteps++;
                                    } while (currentSteps < MAX_STEPS && x3 <= horizontalRange3.getHighest());
                                } //end if (searchDeepnessLevel >= 5)
                                y2++;
                                currentSteps++;
                            } while (currentSteps < MAX_STEPS && y2 <= verticalRange2.getHighest());
                        } // end if searchDeepnessLevel >= 4
                        x2++;
                        currentSteps++;
                    } while (currentSteps < MAX_STEPS && x2 <= horizontalRange2.getHighest());
                } // end if searchDeepnessLevel >= 3
                y1++;
                currentSteps++;
            } while (currentSteps < MAX_STEPS && y1 <= verticalRange1.getHighest());
            x1++;
            currentSteps++;
        } while (currentSteps < MAX_STEPS && x1 <= horizontalRange1.getHighest());
        return tilePosStart;
    }

    private static IntPosition getNextTilePos(IntPosition tilePosStart, IntPosition tilePosTarget) {
        IntPosition returnResultTilePos = tilePosStart.clone();

        if (tilePosTarget.getX() != tilePosStart.getX()) {
            if (tilePosTarget.getX() > tilePosStart.getX()) {
                returnResultTilePos.addX(+1);
            } else {
                returnResultTilePos.addX(-1);
            }
        } else {
            if (tilePosTarget.getY() > tilePosStart.getY()) {
                returnResultTilePos.addY(+1);
            } else {
                returnResultTilePos.addY(-1);
            }
        }
        return returnResultTilePos;
    }

    private static boolean  isAtTargetDepthAndPosition(int demandDeepNessLevel, int currentDeepNessLevel, IntPosition primTilePos, IntPosition secTilePos) {
        return (currentDeepNessLevel == demandDeepNessLevel && primTilePos.equals(secTilePos));
    }
















    public static MazeSolver getInstance() {
        if (instance == null) {
            instance = new MazeSolver();
        }
        return instance;
    }
}



