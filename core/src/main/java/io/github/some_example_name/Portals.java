package io.github.some_example_name;

import java.util.ArrayList;

public class Portals {
    private final ArrayList<PortalMap> portalMaps = new ArrayList<>();

    public final IntPosition illegalPosition;

    Portals() {
        portalMaps.add(new PortalMap(new IntPosition(18, 19), new IntPosition(18, 1)));
        portalMaps.add(new PortalMap(new IntPosition(18, 0), new IntPosition(28, 18)));

        portalMaps.add(new PortalMap(new IntPosition(28, 0), new IntPosition(18, 18)));
        portalMaps.add(new PortalMap(new IntPosition(28, 19), new IntPosition(28, 1)));


        illegalPosition =new IntPosition(-1,-1);
    }

    public IntPosition findPortalExitForPortalEntry(IntPosition entryPosition) {
        for (PortalMap portalMap : portalMaps) {
            if (portalMap.getEntryPosition().equals(entryPosition)) {
                return portalMap.getOutComePosition();
            }
        }
        return illegalPosition;
    }
}

