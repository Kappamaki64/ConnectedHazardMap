package jp.ac.titech.itpro.sdl.connectedhazardmap;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.ac.titech.itpro.sdl.connectedhazardmap.database.DB;
import jp.ac.titech.itpro.sdl.connectedhazardmap.database.Place;

public class DownloaderActivity extends AppCompatActivity {
    private final static String TAG = DownloaderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_downloader);

        DB.getInstance(getApplicationContext()).placeDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((places) -> {
                    LinearLayout placeList = findViewById(R.id.downloader_list);
                    for (Place place : places) {
                        PlaceRow placeRow = PlaceRow.createHere(place, this);
                        placeList.addView(placeRow.row);
                    }
                }, (e) -> {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }).dispose();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }
}