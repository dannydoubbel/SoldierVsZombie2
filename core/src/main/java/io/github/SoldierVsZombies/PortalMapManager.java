package io.github.SoldierVsZombies;

import java.util.ArrayList;

public class PortalMapManager {
    public final IntPosition illegalPosition;

    public final PortalMap illegalPortalMap;
    private final ArrayList<PortalMap> portalMaps = new ArrayList<>();

    // todo implement if a gate is used it can not be used again for 60 sec
    // todo while gate can not be used, it should have a different tile picture

    PortalMapManager() {
        portalMaps.add(new PortalMap(new IntPosition(18, 19), new IntPosition(18, 1)));
        portalMaps.add(new PortalMap(new IntPosition(18, 0), new IntPosition(28, 18)));

        portalMaps.add(new PortalMap(new IntPosition(28, 0), new IntPosition(18, 18)));
        portalMaps.add(new PortalMap(new IntPosition(28, 19), new IntPosition(28, 1)));


        portalMaps.add(new PortalMap(new IntPosition(60, 0), new IntPosition(65, 18)));
        portalMaps.add(new PortalMap(new IntPosition(65, 19), new IntPosition(90, 1)));
        portalMaps.add(new PortalMap(new IntPosition(90, 0), new IntPosition(84, 18)));
        portalMaps.add(new PortalMap(new IntPosition(84, 19), new IntPosition(60, 1)));

        illegalPosition = new IntPosition(Legal.NOT_LEGAL);
        illegalPortalMap = new PortalMap(illegalPosition,illegalPosition);
    }

    public PortalMap getPortalMapSteppedOn(IntPosition entryPosition) {
        for (PortalMap portalMap : portalMaps) {
            if (portalMap.getEntryPosition().equals(entryPosition)) {
                return portalMap;
            }
        }
        return illegalPortalMap;
    }

    public ArrayList<PortalMap> getPortalMaps() {
        return portalMaps;
    }
}

