package malik.wisataairklaten;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.api.DataAPI;
import malik.wisataairklaten.api.DataDeserializer;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.Foto;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class ListGallery extends Fragment {
    RecyclerView rvGallery;
    List<Foto> foto;
    RecyclerAdapter adapter;
    DataAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foto = new ArrayList<Foto>();
        adapter = new RecyclerAdapter(getActivity(), foto, RecyclerAdapter.TIPE_FOTO);

        // koneksi
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class,
                        new DataDeserializer<List<Foto>>()).create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(VariableGlobal.URL).build();
        api = restAdapter.create(DataAPI.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        View v = inflater.inflate(R.layout.list_gallery, container, false);
        rvGallery = (RecyclerView) v.findViewById(R.id.rvGallery);

        rvGallery.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvGallery.setNestedScrollingEnabled(false);

        getSemuaFoto();

        return v;
    }

    public void getSemuaFoto() {
        api.getFoto(new Callback<List<Foto>>() {
            @Override
            public void success(List<Foto> fotos, Response response) {
                foto.addAll(fotos);
                rvGallery.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
//                Toast.makeText(getActivity(), "Error " + retrofitError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
