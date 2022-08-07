package jp.ac.titech.itpro.sdl.connectedhazardmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.connectedhazardmap.database.Place;

public class PlaceRow {
    public final LinearLayout row;

    private PlaceRow(Place place, LinearLayout row) {
        this.row = row;
        getPlaceText().setText(place.placeName);
    }

    private TextView getPlaceText() {
        return (TextView) row.getChildAt(0);
    }

    public static PlaceRow createHere(Place place, Context context) {
        @SuppressLint("InflateParams") LinearLayout row = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.place_row, null);
        return new PlaceRow(place, row);
    }
}
