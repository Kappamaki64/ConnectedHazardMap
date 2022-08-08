package jp.ac.titech.itpro.sdl.connectedhazardmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;

import jp.ac.titech.itpro.sdl.connectedhazardmap.databinding.ActivityAdjusterBinding;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Data;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.HazardMap;
import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Place;

public class AdjusterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final static String TAG = AdjusterActivity.class.getSimpleName();

    private Place place;
    private HazardMap hazardMap;

    private Bitmap hazardMapBitmap;

    private GoogleMap map;
    private GroundOverlay hazardMapOverlay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAdjusterBinding binding = ActivityAdjusterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        setupHazardMapBitmap();

        EditText editTextLat = findViewById(R.id.editText_lat);
        EditText editTextLng = findViewById(R.id.editText_lng);
        EditText editTextWidth = findViewById(R.id.editText_width);
        EditText editTextHeight = findViewById(R.id.editText_height);
        editTextLat.setText(String.valueOf(hazardMap.centerLat));
        editTextLng.setText(String.valueOf(hazardMap.centerLng));
        editTextWidth.setText(String.valueOf(hazardMap.width));
        editTextHeight.setText(String.valueOf(hazardMap.height));

        // NOTE: listening is too heavy to continue application
//        editTextLat.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                hazardMap.centerLat = Float.parseFloat(editable.toString());
//                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(hazardMap.centerLat, hazardMap.centerLng)));
//            }
//        });
//        editTextLng.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                hazardMap.centerLng = Float.parseFloat(editable.toString());
//                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(hazardMap.centerLat, hazardMap.centerLng)));
//            }
//        });
//        editTextWidth.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                float newWidth = Float.parseFloat(editable.toString());
//                hazardMap.height = (newWidth / hazardMap.width) * hazardMap.height;
//                hazardMap.width = newWidth;
//                editTextHeight.setText(String.valueOf(hazardMap.height));
//            }
//        });
//        editTextHeight.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                float newHeight = Float.parseFloat(editable.toString());
//                hazardMap.width = (newHeight / hazardMap.height) * hazardMap.width;
//                hazardMap.height = newHeight;
//                editTextWidth.setText(String.valueOf(hazardMap.width));
//            }
//        });

        ImageButton updateOverlayButton = findViewById(R.id.updateOverlayButton);
        updateOverlayButton.setOnClickListener((view -> {
            hazardMap.centerLat = Float.parseFloat(editTextLat.getText().toString());
            hazardMap.centerLng = Float.parseFloat(editTextLng.getText().toString());

            float newWidth = Float.parseFloat(editTextWidth.getText().toString());
            float newHeight = Float.parseFloat(editTextHeight.getText().toString());
            if (newWidth != hazardMap.width && newHeight == hazardMap.height) {
                hazardMap.height = (newWidth / hazardMap.width) * hazardMap.height;
                hazardMap.width = newWidth;
                editTextHeight.setText(String.valueOf(hazardMap.height));
            } else if (newWidth == hazardMap.width && newHeight != hazardMap.height) {
                hazardMap.height = (newWidth / hazardMap.width) * hazardMap.height;
                hazardMap.width = newWidth;
                editTextHeight.setText(String.valueOf(hazardMap.height));
            } else if (newWidth != hazardMap.width && newHeight != hazardMap.height) {
                hazardMap.width = newWidth;
                hazardMap.height = newHeight;
            }

            overlayHazardMapBitmap();
        }));

        ImageButton updateCenterButton = findViewById(R.id.updateCenterButton);
        updateCenterButton.setOnClickListener((view -> {
            LatLng newCenter = map.getCameraPosition().target;
            hazardMap.centerLat = (float) newCenter.latitude;
            hazardMap.centerLng = (float) newCenter.longitude;
            editTextLat.setText(String.valueOf(hazardMap.centerLat));
            editTextLng.setText(String.valueOf(hazardMap.centerLng));
            overlayHazardMapBitmap();
        }));
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
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

        overlayHazardMapBitmap();
    }

    private void overlayHazardMapBitmap() {
        if (hazardMapOverlay != null) {
            hazardMapOverlay.remove();
        }

        LatLng center = new LatLng(hazardMap.centerLat, hazardMap.centerLng);
        float width = hazardMap.width;
        float height = hazardMap.height;

        GroundOverlayOptions overlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(hazardMapBitmap))
                .position(center, width, height);
        hazardMapOverlay = map.addGroundOverlay(overlayOptions);
    }

    private void setupHazardMapBitmap() {
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