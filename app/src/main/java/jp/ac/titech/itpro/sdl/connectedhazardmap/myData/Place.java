package jp.ac.titech.itpro.sdl.connectedhazardmap.myData;

import java.util.List;

public class Place {
    public String placeName;
    public String displayName;
    public float governmentLat;
    public float governmentLng;

    public List<HazardMap> hazardMaps;

    public Place(String placeName, String displayName, float governmentLat, float governmentLng, List<HazardMap> hazardMaps) {
        this.placeName = placeName;
        this.displayName = displayName;
        this.governmentLat = governmentLat;
        this.governmentLng = governmentLng;
        this.hazardMaps = hazardMaps;
    }

    public boolean hasHazardMapOf(int type) {
        for (HazardMap hazardMap : hazardMaps) {
            if (hazardMap.type == type) return true;
        }
        return false;
    }
    public HazardMap getFirstHazardMap(int type) {
        for (HazardMap hazardMap : hazardMaps) {
            if (hazardMap.type == type) return hazardMap;
        }
        throw new Error("cannot find hazard map; place: " + placeName + " type: " + type);
    }
}
