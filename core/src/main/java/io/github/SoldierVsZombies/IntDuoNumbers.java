package io.github.SoldierVsZombies;

import java.util.Objects;

public class IntDuoNumbers {
    private int int1;
    private int int2;

    public IntDuoNumbers() {
        int1 = 0;
        int2 = 0;
    }

    public IntDuoNumbers(int int1, int int2) {
        this.int1 = int1;
        this.int2 = int2;
    }

    public IntDuoNumbers clone() {
        return new IntDuoNumbers(int1,int2);
    }

    public int getLowest() {
        return Math.min(int1,int2);
    }
    public int getHighest() {
        return Math.max(int1,int2);
    }

    @Override
    public String toString() {
        return "IntDuoNumbers{" +
            "int1=" + int1 +
            ", int2=" + int2 +
            '}';
    }

    public boolean equals(IntDuoNumbers intDuoNumbersToTest) {
        return int1 == intDuoNumbersToTest.int1 && int2 == intDuoNumbersToTest.int2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(int1, int2);
    }

    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public int getInt2() {
        return int2;
    }

    public void setInt2(int int2) {
        this.int2 = int2;
    }
}
