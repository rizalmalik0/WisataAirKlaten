package malik.wisataairklaten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class ListWisata extends Fragment implements AdapterView.OnItemClickListener {
    RecyclerView rvWisata;
    List<Wisata> wisata;
    RecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataAdapter data = new DataAdapter(getActivity());
        data.open();
        wisata = data.getSemuaWisata();
        data.close();

        //adapter
        adapter = new RecyclerAdapter(getActivity(), wisata, RecyclerAdapter.TIPE_WISATA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_wisata, container, false);
        rvWisata = (RecyclerView) v.findViewById(R.id.rvWisata);

        rvWisata.setLayoutManager(new LinearLayoutManager(rvWisata.getContext()));

        rvWisata.setAdapter(adapter);

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
