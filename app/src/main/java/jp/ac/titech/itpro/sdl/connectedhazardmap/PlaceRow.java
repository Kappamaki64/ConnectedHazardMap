package jp.ac.titech.itpro.sdl.connectedhazardmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;

import jp.ac.titech.itpro.sdl.connectedhazardmap.myData.Place;

public class PlaceRow {
    public final Place place;
    public final ConstraintLayout row;
    public final int tabType;

    private PlaceRow(Place place, int tabType, ConstraintLayout row) {
        this.place = place;
        this.row = row;
        this.tabType = tabType;
        getPlaceText().setText(place.displayName);

        // comment out when development
        //getAdjustButton().setVisibility(View.INVISIBLE);
    }

    public void updateDownloadButton(String hazardMapPath) {
        Button downloadButton = getDownloadButton();
        if (mapIsDownloaded(hazardMapPath)) {
            downloadButton.setText("データ削除");
            downloadButton.setBackgroundColor(Color.MAGENTA);
        } else {
            downloadButton.setText("ダウンロード");
            downloadButton.setBackgroundColor(Color.GREEN);
        }
    }

    public boolean mapIsDownloaded(String hazardMapPath) {
        File f = new File(hazardMapPath);
        return f.exists();
    }

    private TextView getPlaceText() {
        return (TextView) row.getChildAt(0);
    }
    private Button getAdjustButton() {
        return (Button) row.getChildAt(1);
    }
    public Button getDownloadButton() {
        return (Button) row.getChildAt(2);
    }

    public static PlaceRow createHere(Place place, int tabType, Context context) {
        @SuppressLint("InflateParams") ConstraintLayout row = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.place_row, null);
        return new PlaceRow(place, tabType, row);
    }
}
