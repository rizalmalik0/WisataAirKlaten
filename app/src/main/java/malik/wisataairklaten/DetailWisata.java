package malik.wisataairklaten;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class DetailWisata extends Activity {
    TextView txtNamaWisata, txtTipeWisata, txtFasilitas, txtFoto;
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
    }
}
