package jp.ac.titech.itpro.sdl.connectedhazardmap.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DB {
    private static AppDatabase instance = null;
    public static AppDatabase getInstance(Context context) {
        Log.d("DATABASE", "instance exists?: " + (instance != null));
        if (instance != null) return instance;
        instance = Room.databaseBuilder(context, AppDatabase.class, "ConnectedHazardMapDatabase")
                .fallbackToDestructiveMigration()
                .addCallback(callback)
                .build();
        return instance;
    }

    static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Log.d("CreateRoomDatabase","version:" + db.getVersion());
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            Log.d("OpenRoomDatabase","version:" + db.getVersion());
        }
    };
}
