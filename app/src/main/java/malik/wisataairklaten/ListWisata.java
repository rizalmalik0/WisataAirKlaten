package malik.wisataairklaten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.view.RecyclerItemClickListener;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class ListWisata extends Fragment implements RecyclerItemClickListener.OnItemClickListener {
    RecyclerView rvWisata;
    List<Wisata> wisata;
    RecyclerAdapter adapter;
    DataAdapter data;

    int tipe;
    int id_wisata;

    public static final int TIPE_UTAMA = 1;
    public static final int TIPE_REKOMENDASI = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get status
        id_wisata = (getArguments() == null) ? 0 : getArguments().getInt("id_wisata");
        tipe = (getArguments() == null) ? TIPE_UTAMA : TIPE_REKOMENDASI;

        //adapter
        wisata = new ArrayList<>();
        data = new DataAdapter(getActivity());
        adapter = new RecyclerAdapter(getActivity(), wisata, RecyclerAdapter.TIPE_WISATA);

        getWisata();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        View v = inflater.inflate(R.layout.list_wisata, container, false);
        rvWisata = (RecyclerView) v.findViewById(R.id.rvWisata);

        // adapter
        rvWisata.setLayoutManager(new LinearLayoutManager(rvWisata.getContext(), android.support.v7.widget.LinearLayoutManager.VERTICAL, false));
        rvWisata.setNestedScrollingEnabled(false);
        rvWisata.setAdapter(adapter);

        //listener
        rvWisata.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvWisata, this));

        return v;
    }


    public void getWisata() {
        switch (tipe) {
            case TIPE_UTAMA:
                data.open();
                wisata.clear();
                wisata.addAll(data.getSemuaWisata());
                data.close();
                break;
            case TIPE_REKOMENDASI:
                data.open();
                wisata.addAll(data.getRekomendasiWisata(id_wisata));
                data.close();

                // dijsktra
                Dijkstra.with(getActivity(), wisata).from(id_wisata).getSemuaJarak();
                sort(wisata);
                break;
        }

        adapter.notifyItemRangeChanged(0, wisata.size());
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position >= 0) {
            Wisata w = wisata.get(position);
            Intent i = new Intent(getActivity(), DetailWisata.class);
            i.putExtra("id_wisata", w.getId_wisata());
            startActivityForResult(i, 2);
        }
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

    private void sort(List<Wisata> wisata) {
        Collections.sort(wisata, new Comparator<Wisata>() {
            @Override
            public int compare(Wisata w1, Wisata w2) {
                return w1.getJarak() - w2.getJarak();
            }
        });
    }

}
