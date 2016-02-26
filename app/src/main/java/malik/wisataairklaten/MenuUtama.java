package malik.wisataairklaten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.adapter.TabsPagerAdapter;
import malik.wisataairklaten.api.DataAPI;
import malik.wisataairklaten.api.DataDeserializer;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.Wisata;
import malik.wisataairklaten.view.SlidingTabLayout;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MenuUtama extends AppCompatActivity {
    private ViewPager viewPager;
    private SlidingTabLayout tabs;
    private Toolbar toolbar;
    private TabsPagerAdapter mAdapter;
    Menu menu;
    DataAPI api;
    DataAdapter data;
    SharedPreferences preferences;

    CharSequence Titles[] = {"Wisata", "Gallery", "Geo Photo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utama);

        data = new DataAdapter(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initilization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);

        // set view
        setSupportActionBar(toolbar);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), Titles, TabsPagerAdapter.PAGER_MENU_UTAMA, TabsPagerAdapter.ID_NULL);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(mAdapter);
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.accent_material_dark));
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(viewPager);

        //koneksi
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class,
                        new DataDeserializer<List<Wisata>>())
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(VariableGlobal.URL).build();
        api = restAdapter.create(DataAPI.class);

        // get data
        getSemuaRating();
    }

    private void cekLogin() {
        boolean login = preferences.getBoolean("login", false);
        MenuItem menuLogin = menu.findItem(R.id.menu_login);
        MenuItem menuRegistrasi = menu.findItem(R.id.menu_registrasi);
        MenuItem menuProfil = menu.findItem(R.id.menu_profil);
        MenuItem menuLogout = menu.findItem(R.id.menu_logout);
        if (login) {
            menuLogin.setVisible(false);
            menuRegistrasi.setVisible(false);
            menuProfil.setVisible(true);
            menuLogout.setVisible(true);
        } else {
            menuLogin.setVisible(true);
            menuRegistrasi.setVisible(true);
            menuProfil.setVisible(false);
            menuLogout.setVisible(false);
        }
    }

    public void getSemuaRating() {
        api.getRating(new Callback<List<Wisata>>() {
            @Override
            public void success(List<Wisata> wisatas, Response response) {
                ListWisata listWisata = (ListWisata) mAdapter.getItem(0);

                data.write();
                for (int i = 0; i < wisatas.size(); i++) {
                    if (listWisata.wisata.get(i) != null) {
                        data.setRatingWisata(wisatas.get(i).getId_wisata(), wisatas.get(i).getRating());
                        listWisata.wisata.get(i).setRating(wisatas.get(i).getRating());
                        listWisata.adapter.notifyDataSetChanged();
                    }
                }
                data.close();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
//                Toast.makeText(MenuUtama.this, retrofitError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_utama, menu);

        cekLogin();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.menu_registrasi:
                i = new Intent(this, Registrasi.class);
                startActivity(i);
                break;
            case R.id.menu_login:
                i = new Intent(this, Login.class);
                startActivity(i);
                break;
            case R.id.menu_profil:
                i = new Intent(this, Profil.class);
                startActivity(i);
                break;
            case R.id.menu_logout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("login", false);
                editor.putInt("id_user", 0);
                editor.commit();
                invalidateOptionsMenu();
                break;
            case R.id.menu_about:
                i = new Intent(this, About.class);
                startActivity(i);
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }
}
