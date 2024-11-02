package io.github.SoldierVsZombies;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.Objects;

public class PortalMap {
    final private IntPosition entryPosition;
    final private IntPosition outComePosition;

    private long startTimeOutOfOrder;

    private final long OUT_OF_ORDER_TIME = 20000; //20 sec

    public PortalMap(IntPosition entryPosition, IntPosition outComePosition) {
        this.entryPosition = entryPosition;
        this.outComePosition = outComePosition;
        startTimeOutOfOrder = TimeUtils.millis()-OUT_OF_ORDER_TIME;
    }

    public boolean isBeyondOutOfOrderTime() {
        long elapsedTime = TimeUtils.millis() - startTimeOutOfOrder;
        return elapsedTime > OUT_OF_ORDER_TIME;
    }

    public void startOutOfOrder() {
        startTimeOutOfOrder = TimeUtils.millis();
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


    public IntPosition getOutComePosition() {
        return outComePosition;
    }


}
