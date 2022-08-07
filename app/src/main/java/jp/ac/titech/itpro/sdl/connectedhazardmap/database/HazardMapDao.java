package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HazardMapDao {
    @Insert
    void insertAll(HazardMap... hazardMaps);
    void insertAll(List<HazardMap> hazardMaps);

    @Update
    void update(HazardMap hazardMap);

    @Delete
    void delete(HazardMap hazardMap);

    @Query("SELECT * FROM hazardmap")
    List<HazardMap> getAll();
}
