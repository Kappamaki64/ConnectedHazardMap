package jp.ac.titech.itpro.sdl.connectedhazardmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Data;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.HazardMap;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Place;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    public static final int MAX_DOTS_OF_HAZARD_MAP = 2000;
    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQ_PERMISSIONS = 1234;

    private GoogleMap map;
    private final Map<String, GroundOverlay> hazardMapOverlayMap = new LinkedHashMap<>();
    private GroundOverlay frontHazardMapOverlay;
    private Address cameraAddress;

    private FusedLocationProviderClient locationClient;
    private LocationRequest request;
    private LocationCallback callback;

    private Location latestMyLocation = null;

    private TextView locationTextView;
    private int tabType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_maps);

        locationTextView = findViewById(R.id.locationTextView);

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
                updateOverlays();
                updateFrontOverlay();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            Log.d(TAG, "onCreate: getMapAsync");
            mapFragment.getMapAsync(this);
        }

        ImageButton myLocationButton = findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener((view) -> {
            Log.d(TAG, "myLocationButton.onClick");
            displayLatestMyLocation();
        });

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        request = LocationRequest.create();
        request.setInterval(10000L);
        request.setFastestInterval(5000L);
        request.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Log.d(TAG, "onLocationResult");
                if (latestMyLocation != null) {
                    latestMyLocation = locationResult.getLastLocation();
                    return;
                }

                // display user's location automatically only the first time
                latestMyLocation = locationResult.getLastLocation();
                displayLatestMyLocation();
            }
        };
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
        startLocationUpdate(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        stopLocationUpdate();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_downloader:
                startActivity(new Intent(this, DownloaderActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        this.map = googleMap;
        map.setOnCameraIdleListener(this);
        map.moveCamera(CameraUpdateFactory.zoomTo(15f));
        updateCameraAddress();
        updateOverlays();
        updateFrontOverlay();
        updateLocationText();
    }

    @Override
    public void onCameraIdle() {
        Log.d(TAG, "onCameraIdle");
        updateCameraAddress();
        updateFrontOverlay();
        updateLocationText();
    }

    private void updateFrontOverlay() {
        if (cameraAddress == null) return;
        String locality = cameraAddress.getLocality();
        if (locality == null) return;
        GroundOverlay hazardMapOverlay = hazardMapOverlayMap.get(locality);
        if (hazardMapOverlay == null) return;
        if (hazardMapOverlay == frontHazardMapOverlay) return;
        if (frontHazardMapOverlay != null) frontHazardMapOverlay.setZIndex(0);
        hazardMapOverlay.setZIndex(1);
        frontHazardMapOverlay = hazardMapOverlay;
    }

    private void updateLocationText() {
        if (cameraAddress == null) return;
        String adminArea = cameraAddress.getAdminArea();
        String locality = cameraAddress.getLocality();
        if (adminArea == null || locality == null) return;
        String text = getString(R.string.location_text, adminArea, locality);
        locationTextView.setText(text);
    }

    private void updateCameraAddress() {
        if (!Geocoder.isPresent()) return;
        Geocoder geocoder = new Geocoder(getApplicationContext());
        LatLng cameraLatLng = map.getCameraPosition().target;
        try {
            List<Address> addresses = geocoder.getFromLocation(cameraLatLng.latitude, cameraLatLng.longitude, 1);
            if (addresses == null || addresses.isEmpty()) return;
            cameraAddress = addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateOverlays() {
        for (GroundOverlay hazardMapOverlay : hazardMapOverlayMap.values()) {
            hazardMapOverlay.remove();
        }
        hazardMapOverlayMap.clear();

        final int type = tabType;
        for (Place place : Data.placeMap.values()) {
            if (!place.hasHazardMapOf(type)) continue;
            HazardMap hazardMap = place.getFirstHazardMap(type);
            if (hazardMap.centerLat == -1 || hazardMap.centerLng == -1 || hazardMap.width == -1 || hazardMap.height == -1) continue;

            Bitmap hazardMapBitmap = getHazardMapBitmap(place, hazardMap);
            LatLng center = new LatLng(hazardMap.centerLat, hazardMap.centerLng);
            GroundOverlayOptions overlayOptions = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromBitmap(hazardMapBitmap))
                    .position(center, hazardMap.width, hazardMap.height);
            GroundOverlay hazardMapOverlay = map.addGroundOverlay(overlayOptions);
            hazardMapOverlayMap.put(place.displayName, hazardMapOverlay);
        }
    }


    private Bitmap getHazardMapBitmap(Place place, HazardMap hazardMap) {
        Bitmap hazardMapBitmap = null;

        try {
            File f = new File(externalStorageFilePath(hazardMap.type, place.placeName));
            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer renderer = new PdfRenderer(parcelFileDescriptor);
            PdfRenderer.Page page = renderer.openPage(hazardMap.usedPageIndex);

            // densityDpi: dots / inch, get{Width,Height}(): (1 / 72) inch
            int defaultWidth = getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
            int defaultHeight = getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();

            int width, height;
            if (defaultWidth > MapsActivity.MAX_DOTS_OF_HAZARD_MAP || defaultHeight > MapsActivity.MAX_DOTS_OF_HAZARD_MAP) {
                if (defaultWidth > defaultHeight) {
                    width = MapsActivity.MAX_DOTS_OF_HAZARD_MAP;
                    height = (int) ((MapsActivity.MAX_DOTS_OF_HAZARD_MAP / (double) defaultWidth) * defaultHeight);
                } else {
                    width = (int) ((MapsActivity.MAX_DOTS_OF_HAZARD_MAP / (double) defaultHeight) * defaultWidth);
                    height = MapsActivity.MAX_DOTS_OF_HAZARD_MAP;
                }
            } else {
                width = defaultWidth;
                height = defaultHeight;
            }

            if (hazardMap.centerLat == -1) hazardMap.centerLat = place.governmentLat;
            if (hazardMap.centerLng == -1) hazardMap.centerLng = place.governmentLng;
            if (hazardMap.width == -1) hazardMap.width = defaultWidth;
            if (hazardMap.height == -1) hazardMap.height = defaultHeight;

            hazardMapBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            page.render(hazardMapBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();
            renderer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hazardMapBitmap;
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

    @SuppressLint("MissingPermission")
    private void startLocationUpdate(boolean reqPermission) {
        Log.d(TAG, "startLocationUpdate");
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (reqPermission) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, REQ_PERMISSIONS);
                } else {
                    String text = getString(R.string.toast_requires_permission_format, permission);
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        locationClient.requestLocationUpdates(request, callback, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grants) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (reqCode == REQ_PERMISSIONS) {
            startLocationUpdate(false);
        } else {
            super.onRequestPermissionsResult(reqCode, permissions, grants);
        }
    }

    private void stopLocationUpdate() {
        Log.d(TAG, "stopLocationUpdate");
        locationClient.removeLocationUpdates(callback);
    }

    private void displayLatestMyLocation() {
        if (latestMyLocation == null) {
            Log.d(TAG, "displayLatestMyLocation: latestMyLocation == null");
            return;
        }
        LatLng ll = new LatLng(latestMyLocation.getLatitude(), latestMyLocation.getLongitude());
        if (map == null) {
            Log.d(TAG, "displayLatestMyLocation: map == null");
            return;
        }
        map.animateCamera(CameraUpdateFactory.newLatLng(ll));
    }
}