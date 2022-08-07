package jp.ac.titech.itpro.sdl.connectedhazardmap;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileNotFoundException;

import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Data;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.HazardMap;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Place;

public class DownloaderActivity extends AppCompatActivity {
    public static final String EXTRA_PLACE = "jp.ac.titech.itpro.sdl.connectedhazardmap.PLACE";
    public static final String EXTRA_TYPE = "jp.ac.titech.itpro.sdl.connectedhazardmap.TYPE";
    private static final String TAG = DownloaderActivity.class.getSimpleName();

    private int tabType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_downloader);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "tab selected: " + tab.getId() + " " + tab.getText());
                CharSequence text = tab.getText();
                if (getText(R.string.type1).equals(text)) {
                    tabType = 1;
                } else if (getText(R.string.type2).equals(text)) {
                    tabType = 2;
                } else {
                    Log.e(TAG, "unknown tab is selected");
                    return;
                }
                updateContent();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        updateContent();
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

    private void updateContent() {
        LinearLayout placeList = findViewById(R.id.downloader_list);
        placeList.removeAllViews();
        final int type = tabType;
        for (Place place : Data.placeMap.values()) {
            if (!place.hasHazardMapOf(type)) continue;
            PlaceRow placeRow = PlaceRow.createHere(place, type, this);
            placeRow.getDownloadButton().setOnClickListener((view -> {
                if (placeRow.mapIsDownloaded(externalStorageFilePath(placeRow.tabType, placeRow.place.placeName))) {
                    deleteHazardMap(placeRow);
                } else{
                    downloadHazardMap(placeRow);
                }
            }));
            placeRow.getAdjustButton().setOnClickListener((view -> {
                Intent intent = new Intent(this, AdjusterActivity.class);
                intent.putExtra(EXTRA_PLACE, placeRow.place.placeName);
                intent.putExtra(EXTRA_TYPE, placeRow.tabType);
                startActivity(intent);
            }));
            placeRow.updateDownloadButton(externalStorageFilePath(placeRow.tabType, place.placeName));
            placeList.addView(placeRow.row);
        }
    }

    private void downloadHazardMap(PlaceRow placeRow) {
        final int type = placeRow.tabType;
        Place place = placeRow.place;
        HazardMap hazardMap = place.getFirstHazardMap(type);

        String fileName = place.placeName;
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(hazardMap.uri);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(fileName);
        request.setDescription("Downloading HazardMap of " + place.placeName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Uri destinationUri = Uri.parse("file://" + externalStorageFilePath(type, place.placeName));
        request.setDestinationUri(destinationUri);
        long downloadId = manager.enqueue(request);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != downloadId) return;
                try {
                    Log.d(TAG, "hazard map downloaded in " + manager.getUriForDownloadedFile(id));
                    placeRow.updateDownloadButton(externalStorageFilePath(type, place.placeName));
                    ParcelFileDescriptor parcelFileDescriptor = manager.openDownloadedFile(id);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void deleteHazardMap(PlaceRow placeRow) {
        final int type = placeRow.tabType;
        Place place = placeRow.place;
        File f = new File(externalStorageFilePath(type, place.placeName));
        f.delete();
        Log.d(TAG, "hazard map deleted in " + f.getPath());
        placeRow.updateDownloadButton(externalStorageFilePath(type, place.placeName));
    }

    private String externalStorageFilePath(int tabType, String placeName) {
        return externalStorageDir(tabType) + "/" + placeName + ".pdf";
    }
    private String externalStorageDir(int tabType) {
        String dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/type" + tabType;
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return dir;
    }
}