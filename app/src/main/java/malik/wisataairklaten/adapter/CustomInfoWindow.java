package malik.wisataairklaten.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import malik.wisataairklaten.R;
import malik.wisataairklaten.model.Wisata;
import malik.wisataairklaten.view.SquareImageView;

/**
 * Created by Rizal Malik on 06/03/2016.
 */
public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    Context mContext;
    List<Wisata> wisata;
    HashMap<Marker, Integer> listMarker;

    public CustomInfoWindow(Context mContext, List<Wisata> wisata, HashMap<Marker, Integer> listMarker) {
        this.mContext = mContext;
        this.wisata = wisata;
        this.listMarker = listMarker;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // get view
        View v = inflater.inflate(R.layout.info_window, null);
        RatingBar rbWisata = (RatingBar) v.findViewById(R.id.rbWisata);
        TextView txtNamaWisata = (TextView) v.findViewById(R.id.txtNamaWisata);

        // set view
        int position = listMarker.get(marker);
        Wisata w = wisata.get(position);
        rbWisata.setRating(w.getRating());
        txtNamaWisata.setText(w.getNama_wisata());

        return v;
    }
}
