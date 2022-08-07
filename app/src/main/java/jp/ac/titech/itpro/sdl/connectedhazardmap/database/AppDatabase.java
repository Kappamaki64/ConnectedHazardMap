package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Place.class, HazardMap.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlaceDao placeDao();
    public abstract HazardMapDao hazardMapDao();
}
