package malik.wisataairklaten;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class Map extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    LatLng destination;
    DataAdapter data;
    Wisata wisata;
    int id_wisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        // intent data
        id_wisata = getIntent().getIntExtra("id_wisata", 0);

        // get detail wisata
        data = new DataAdapter(this);
        data.open();
        wisata = data.getDetailWisata(id_wisata);
        data.close();

        destination = new LatLng(wisata.getLatitude(), wisata.getLongitude());

        // map
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.layout_peta);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 12));
        googleMap.addMarker(new MarkerOptions()
                .position(destination)
                .title(wisata.getNama_wisata())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map)));
    }
}
