package malik.wisataairklaten;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class GeoPhoto extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    SupportMapFragment mapFragment;
    List<Wisata> wisata;
    DataAdapter data;
    LatLngBounds bound;
    HashMap<Marker, Integer> mHashMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHashMap = new HashMap<Marker, Integer>();
        data = new DataAdapter(getActivity());

        data.open();
        wisata = data.getSemuaWisata();
        data.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map, container, false);

        if (mapFragment == null)
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.layout_peta);

        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-7.613596, 110.635787), 13));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < wisata.size(); i++) {
            Wisata w = wisata.get(i);
            Marker m = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(w.getLatitude(), w.getLongitude()))
                    .title(w.getNama_wisata())
                    .snippet(w.getNama_wisata()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map)));

            builder.include(new LatLng(w.getLatitude(), w.getLongitude()));
            mHashMap.put(m, i);
        }
        bound = builder.build();

        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (bound != null) {
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(
                            bound, 100);
                    googleMap.moveCamera(cu);
                    googleMap.animateCamera(cu);
                }
            }
        });
    }

    public static GeoPhoto newInstance() {
        GeoPhoto myFragment = new GeoPhoto();

        return myFragment;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int position = mHashMap.get(marker);
        Intent i = new Intent(getActivity(), DetailWisata.class);
        i.putExtra("id_wisata", wisata.get(position).getId_wisata());
        startActivity(i);
    }
}
