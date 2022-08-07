package jp.ac.titech.itpro.sdl.connectedhazardmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import jp.ac.titech.itpro.sdl.connectedhazardmap.databinding.ActivityAdjusterBinding;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Data;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.HazardMap;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Place;

public class AdjusterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final static String TAG = AdjusterActivity.class.getSimpleName();

    private Place place;
    private HazardMap hazardMap;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAdjusterBinding binding = ActivityAdjusterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String placeName = intent.getStringExtra(DownloaderActivity.EXTRA_PLACE);
        place = Data.placeMap.get(placeName);
        if (place == null) {
            throw new Error("cannot find place of placeName:" + placeName);
        }
        int type = intent.getIntExtra(DownloaderActivity.EXTRA_TYPE, -1);
        if (type == -1) {
            throw new Error("cannot get type");
        }
        if (!place.hasHazardMapOf(type)) {
            throw new Error("this place does not have hazard map of type:" + type);
        }
        hazardMap = place.getFirstHazardMap(type);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            Log.d(TAG, "onCreate: getMapAsync");
            mapFragment.getMapAsync(this);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        map = googleMap;

        LatLng governmentLatLng = new LatLng(place.governmentLat, place.governmentLng);
        map.addMarker(new MarkerOptions().position(governmentLatLng).title("庁舎"));
        map.moveCamera(CameraUpdateFactory.zoomTo(12f));
        map.moveCamera(CameraUpdateFactory.newLatLng(governmentLatLng));
    }
}