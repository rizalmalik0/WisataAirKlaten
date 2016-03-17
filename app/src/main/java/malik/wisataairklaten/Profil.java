package malik.wisataairklaten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import malik.wisataairklaten.adapter.TabsPagerAdapter;
import malik.wisataairklaten.api.DataAPI;
import malik.wisataairklaten.api.DataDeserializer;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.Foto;
import malik.wisataairklaten.model.JSONData;
import malik.wisataairklaten.view.CustomNestedScroll;
import malik.wisataairklaten.view.CustomPager;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class Profil extends AppCompatActivity implements CustomNestedScroll.OnBottomReachedListener {
    Toolbar toolbar;
    SharedPreferences preferences;
    TextView txtNama, txtFotoUpload;
    LinearLayout layoutProfil;
    TabsPagerAdapter pagerAdapter;
    CustomPager pager;
    CustomNestedScroll scroll;
    ListGallery listGallery;
    DataAPI api;
    ArrayList<Integer> deletedId;

    boolean login;
    boolean user = false;
    boolean delete = false;
    int id_user, count;
    public static final int IS_USER = 0;
    String nama, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        // init
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtNama = (TextView) findViewById(R.id.txtNamaUser);
        txtFotoUpload = (TextView) findViewById(R.id.txtFotoUpload);
        layoutProfil = (LinearLayout) findViewById(R.id.layout_profil);
        pager = (CustomPager) findViewById(R.id.pagerProfil);
        scroll = (CustomNestedScroll) findViewById(R.id.scrollView);

        // intent data
        id_user = getIntent().getIntExtra("id_user", 0);
        username = getIntent().getStringExtra("username");
        nama = getIntent().getStringExtra("nama");

        deletedId = new ArrayList<Integer>();

        // cek Login
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        login = preferences.getBoolean("login", false);
        if (id_user == preferences.getInt("id_user", 0)) user = true;

        if (id_user == IS_USER) {
            user = true;
            id_user = preferences.getInt("id_user", 0);
            username = preferences.getString("username", "");
            nama = preferences.getString("nama", "");
        }

        //adapter
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), new String[]{"Gallery"}, TabsPagerAdapter.PAGER_PROFIL, id_user);
        pager.setAdapter(pagerAdapter);

        // koneksi
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class,
                        new DataDeserializer<List<Foto>>()).create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(VariableGlobal.URL).build();
        api = restAdapter.create(DataAPI.class);

        //set view
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set data
        txtNama.setText(nama);
        getSupportActionBar().setTitle(username);
        getCountFoto();

        //listener
        scroll.setOnBottomReachedListener(this);
    }

    private void getCountFoto() {
        api.getCountFoto(id_user, new Callback<JSONData<Integer>>() {
            @Override
            public void success(JSONData<Integer> integerJSONData, Response response) {
                count = integerJSONData.getData();
                txtFotoUpload.setText(count + " foto");
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    public void sortId() {
        Collections.sort(deletedId);
        Collections.reverse(deletedId);
    }

    @Override
    public void onBottomReached() {
        if (listGallery == null) listGallery = (ListGallery) pagerAdapter.getItem(0);
        if (!listGallery.load && !listGallery.last && listGallery.sukses && listGallery.foto.size() != 0)
            listGallery.loadMoreFotoUser(id_user, listGallery.foto.get(listGallery.foto.size() - 1).getId_foto());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_profil, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_edit:
                Intent i = new Intent(this, Registrasi.class);
                i.putExtra("edit", true);
                startActivity(i);
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (user){
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            username = preferences.getString("username", "");
            nama = preferences.getString("nama", "");
            txtNama.setText(nama);
            getSupportActionBar().setTitle(username);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("delete", delete);
        if (delete) i.putIntegerArrayListExtra("deletedId", deletedId);
        setResult(RESULT_OK, i);
        finish();
    }
}
