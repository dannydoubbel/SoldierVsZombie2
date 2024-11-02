package io.github.SoldierVsZombies;


class TilesTest {

    @org.junit.jupiter.api.Test
    void getRandomInt() {
        for (int lus = 1; lus < 1000; lus++) {
            System.out.println("G"+Tiles.getRandomInt(5,10));
        }
    }
}
