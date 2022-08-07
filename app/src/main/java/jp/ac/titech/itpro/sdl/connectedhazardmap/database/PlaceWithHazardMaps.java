package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PlaceWithHazardMaps {
    @Embedded public Place place;
    @Relation(
            parentColumn = "placeId",
            entityColumn = "placeId"
    )
    public List<HazardMap> hazardMaps;
}
