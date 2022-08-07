package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import android.content.Context;

import androidx.room.Room;

public class DB {
    private static AppDatabase instance = null;
    public static AppDatabase getInstance(Context context) {
        if (instance != null) return instance;
        instance = Room.databaseBuilder(context, AppDatabase.class, "ConnectedHazardMapDatabase").build();
        return instance;
    }
}
