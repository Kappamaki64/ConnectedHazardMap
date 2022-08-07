package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Place {
    @PrimaryKey(autoGenerate = true)
    public long placeId;

    public String placeName;
    public float governmentLat;
    public float governmentLng;
}
