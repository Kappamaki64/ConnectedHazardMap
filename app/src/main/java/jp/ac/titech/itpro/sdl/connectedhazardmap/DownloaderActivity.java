package jp.ac.titech.itpro.sdl.connectedhazardmap;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        LinearLayout list = findViewById(R.id.downloader_list);
        // TODO: add proper items
        for (int i = 0; i < 1000; i++) {
            TextView item = new TextView(this);
            item.setText("example item " + i);
            list.addView(item);
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