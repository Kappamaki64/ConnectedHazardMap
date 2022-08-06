package jp.ac.titech.itpro.sdl.connectedhazardmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaceRow {
    public final LinearLayout row;

    private PlaceRow(PlaceData data, LinearLayout row) {
        this.row = row;
        getPlaceText().setText(data.name);
    }

    private TextView getPlaceText() {
        return (TextView) row.getChildAt(0);
    }

    public static PlaceRow createHere(PlaceData data, Context context) {
        @SuppressLint("InflateParams") LinearLayout row = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.place_row, null);
        return new PlaceRow(data, row);
    }
}
