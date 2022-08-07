package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HazardMap {
    @PrimaryKey(autoGenerate = true)
    public long hazardMapId;
    public long placeId;

    public int type;
    public String uri;
    public int usedPage;
    public float centerLat;
    public float centerLag;
    public float width;
    public float height;
}
