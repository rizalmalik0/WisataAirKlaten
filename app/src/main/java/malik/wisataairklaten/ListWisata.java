package malik.wisataairklaten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class ListWisata extends Fragment implements AdapterView.OnItemClickListener {
    ListView lvWisata;
    List<Wisata> wisata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataAdapter data = new DataAdapter(getActivity());
        data.open();
        wisata = data.getSemuaWisata();
        data.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_wisata, container, false);
        lvWisata = (ListView) v.findViewById(R.id.lvWisata);

        lvWisata.setAdapter(new ArrayAdapter<Wisata>(getActivity(), android.R.layout.simple_list_item_1, wisata));

        lvWisata.setOnItemClickListener(this);

        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Wisata w = (Wisata) parent.getItemAtPosition(position);

        Intent i = new Intent(getActivity(), DetailWisata.class);
        i.putExtra("id_wisata", w.getId_wisata());
        startActivity(i);
    }
}
