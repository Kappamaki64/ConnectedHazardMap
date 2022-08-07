package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface HazardMapDao {
    @Insert
    Completable insertAll(HazardMap... hazardMaps);
    @Insert
    Completable insertAll(List<HazardMap> hazardMaps);

    @Update
    Completable update(HazardMap hazardMap);

    @Delete
    Completable delete(HazardMap hazardMap);

    @Query("SELECT * FROM hazardmap")
    Single<List<HazardMap>> getAll();
}
