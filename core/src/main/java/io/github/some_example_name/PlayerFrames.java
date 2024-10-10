package io.github.some_example_name;

public class PlayerFrames {
    private static PlayerFrames instance;

    /*


            To Do everything for the Player Frames should come in this class

            * Holding and Loading the frames

            * Garde the frame index

            * return the frame to get drawn





     */







    public static PlayerFrames getInstance() {
        if (instance == null) {
            instance = new PlayerFrames();
        }
        return instance;
    }
}
