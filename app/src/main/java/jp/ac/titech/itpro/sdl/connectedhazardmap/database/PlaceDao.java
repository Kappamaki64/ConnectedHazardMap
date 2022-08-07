package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceDao {
    @Insert
    void insertAll(Place... places);
    void insertAll(List<Place> places);

    @Update
    void update(Place place);

    @Delete
    void delete(Place place);

    @Query("SELECT * FROM place")
    List<Place> getAll();

    @Transaction
    @Query("SELECT * FROM place")
    List<PlaceWithHazardMaps> getAllWithHazardMaps();
}
