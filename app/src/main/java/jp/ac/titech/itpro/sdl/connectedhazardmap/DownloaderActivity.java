package jp.ac.titech.itpro.sdl.connectedhazardmap;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.LinearLayout;

import jp.ac.titech.itpro.sdl.connectedhazardmap.databinding.ActivityDownloaderBinding;

public class DownloaderActivity extends AppCompatActivity {
    private final static String TAG = DownloaderActivity.class.getSimpleName();

    private ActivityDownloaderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        binding = ActivityDownloaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        LinearLayout placeList = findViewById(R.id.downloader_list);
        for (int i = 0; i < 100; i++) {
            PlaceRow placeRow = PlaceRow.createHere(new PlaceData("place" + i), this);
            placeList.addView(placeRow.row);
        }
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