package io.github.SoldierVsZombies;

import java.util.Objects;

public class PortalMap {
    private IntPosition entryPosition;
    private IntPosition outComePosition;

    private long lastTimeUsedInMiliSeconds; // todo implement this

    public PortalMap(IntPosition entryPosition, IntPosition outComePosition) {
        this.entryPosition = entryPosition;
        this.outComePosition = outComePosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortalMap portalMap = (PortalMap) o;
        return Objects.equals(entryPosition, portalMap.entryPosition) && Objects.equals(outComePosition, portalMap.outComePosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryPosition, outComePosition);
    }

    public IntPosition getEntryPosition() {
        return entryPosition;
    }

    public void setEntryPosition(IntPosition entryPosition) {
        this.entryPosition = entryPosition;
    }

    public IntPosition getOutComePosition() {
        return outComePosition;
    }

    public void setOutComePosition(IntPosition outComePosition) {
        this.outComePosition = outComePosition;
    }
}
