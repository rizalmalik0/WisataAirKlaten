package malik.wisataairklaten;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.model.Foto;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class ListGallery extends Fragment {
    List<Foto> foto;
    RecyclerView rvGallery;
    RecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foto = new ArrayList<Foto>();
        adapter = new RecyclerAdapter(getActivity(), foto, RecyclerAdapter.TIPE_FOTO);

        foto.add(new Foto());
        foto.add(new Foto());
        foto.add(new Foto());
        foto.add(new Foto());
        foto.add(new Foto());
        foto.add(new Foto());
        foto.add(new Foto());
        foto.add(new Foto());
        foto.add(new Foto());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_gallery, container, false);
        rvGallery = (RecyclerView) v.findViewById(R.id.rvGallery);

        rvGallery.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rvGallery.setAdapter(adapter);

        return v;
    }
}
