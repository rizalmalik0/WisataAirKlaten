package malik.wisataairklaten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.adapter.RecyclerItemClickListener;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class ListWisata extends Fragment implements RecyclerItemClickListener.OnItemClickListener {
    RecyclerView rvWisata;
    List<Wisata> wisata;
    RecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataAdapter data = new DataAdapter(getActivity());

        if (getActivity() instanceof MenuUtama) {
            data.open();
            wisata = data.getSemuaWisata();
            data.close();
        } else {
            data.open();
            wisata = data.getRekomendasiWisata(getArguments().getInt("id_wisata"));
            data.close();
        }

        //adapter
        adapter = new RecyclerAdapter(getActivity(), wisata, RecyclerAdapter.TIPE_WISATA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        View v = inflater.inflate(R.layout.list_wisata, container, false);
        rvWisata = (RecyclerView) v.findViewById(R.id.rvWisata);

        // adapter
        rvWisata.setLayoutManager(new LinearLayoutManager(rvWisata.getContext()));
        rvWisata.setAdapter(adapter);
        rvWisata.setNestedScrollingEnabled(false);

        //listener
        rvWisata.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvWisata, this));

        return v;
    }

    @Override
    public void onItemClick(View view, int position) {
        Wisata w = wisata.get(position);
        Intent i = new Intent(getActivity(), DetailWisata.class);
        i.putExtra("id_wisata", w.getId_wisata());
        startActivity(i);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public static ListWisata newIntance(int id_wisata) {
        ListWisata listWisata = new ListWisata();
        Bundle args = new Bundle();
        args.putInt("id_wisata", id_wisata);
        listWisata.setArguments(args);

        return listWisata;
    }
}
