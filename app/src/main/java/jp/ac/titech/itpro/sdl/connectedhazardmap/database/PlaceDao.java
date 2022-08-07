package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlaceDao {
    @Insert
    Completable insertAll(Place... places);
    @Insert
    Completable insertAll(List<Place> places);

    @Update
    Completable update(Place place);

    @Delete
    Completable delete(Place place);

    @Query("SELECT * FROM place")
    Single<List<Place>> getAll();

    @Transaction
    @Query("SELECT * FROM place")
    Single<List<PlaceWithHazardMaps>> getAllWithHazardMaps();
}
