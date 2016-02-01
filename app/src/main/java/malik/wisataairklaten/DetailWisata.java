package malik.wisataairklaten;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost;
import android.widget.TextView;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class DetailWisata extends FragmentActivity {
    TextView txtNamaWisata, txtTipeWisata, txtFasilitas, txtFoto;
    FragmentTabHost th;
    DataAdapter data;
    Wisata wisata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_wisata);

        // init
        txtNamaWisata = (TextView) findViewById(R.id.txtNamaWisata);
        txtTipeWisata = (TextView) findViewById(R.id.txtTipeWisata);
        txtFasilitas = (TextView) findViewById(R.id.txtFasilitas);
        txtFoto = (TextView) findViewById(R.id.txtFoto);
        th = (FragmentTabHost) findViewById(R.id.tabHost);

        // intent data
        int id = getIntent().getIntExtra("id_wisata", 0);

        // get detail
        data = new DataAdapter(this);
        data.open();
        wisata = data.getDetailWisata(id);

        // set
        txtNamaWisata.setText(wisata.getNama_wisata());
        txtTipeWisata.setText(wisata.getTipe_wisata());
        txtFasilitas.setText(wisata.getFasilitas());
        txtFoto.setText(wisata.getFoto());

        // setup tabhost
        th.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);

        TabHost.TabSpec tab1 = th.newTabSpec("Rekomendasi").setIndicator("Rekomendasi");
        TabHost.TabSpec tab2 = th.newTabSpec("Gallery").setIndicator("Gallery");
        TabHost.TabSpec tab3 = th.newTabSpec("Review").setIndicator("Review");

        th.addTab(tab1, Rekomendasi.class, null);
        th.addTab(tab2, ListGallery.class, null);
        th.addTab(tab3, ReviewWisata.class, null);
    }
}
