package malik.wisataairklaten;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.adapter.TabsPagerAdapter;
import malik.wisataairklaten.api.DataAPI;
import malik.wisataairklaten.api.DataDeserializer;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.Version;
import malik.wisataairklaten.model.Wisata;
import malik.wisataairklaten.view.SlidingTabLayout;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MenuUtama extends AppCompatActivity {
    ViewPager viewPager;
    SlidingTabLayout tabs;
    Toolbar toolbar;
    TabsPagerAdapter mAdapter;
    LinearLayout layoutMenuUtama;
    ProgressDialog dialog;
    Menu menu;
    DataAPI api;
    DataAdapter data;
    SharedPreferences preferences;

    ListWisata listWisata;
    ListGallery listGallery;

    CharSequence Titles[] = {"Wisata", "Galeri", "Peta"};

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
        layoutMenuUtama = (LinearLayout) findViewById(R.id.layout_menu_utama);

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
                .registerTypeAdapter(Version.class,
                        new DataDeserializer<Version>())
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(VariableGlobal.URL).build();
        api = restAdapter.create(DataAPI.class);
        getSemuaRating();

        // cek update
        boolean update = preferences.getBoolean("update", false);
        if (!update) {
            cekUpdate(false);
        }
    }

    private void cekLogin() {
        boolean login = preferences.getBoolean("login", false);

        // init
        MenuItem menuLogin = menu.findItem(R.id.menu_login);
        MenuItem menuLogout = menu.findItem(R.id.menu_logout);
        MenuItem menuRegistrasi = menu.findItem(R.id.menu_registrasi);
        MenuItem menuProfil = menu.findItem(R.id.menu_profil);

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

    public void cekUpdate(final boolean manual) {
        final int last_update = preferences.getInt("last_update", 0);
        dialog = new ProgressDialog(this);
        if (manual) {
            dialog.setMessage("Checking Update..");
            dialog.setCancelable(false);
            dialog.show();
        }

        api.cekUpdate(new Callback<Version>() {
            @Override
            public void success(final Version version, Response response) {
                dialog.dismiss();

                if (version.getId_version() > last_update) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuUtama.this)
                            .setTitle("Update " + version.getVersion())
                            .setNegativeButton("Nanti", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("update", true);
                                    editor.commit();
                                }
                            }).setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("update", false);
                                    editor.commit();

                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(version.getLink()));
                                    startActivity(i);
                                }
                            });

                    if (version.getPesan_update() != null)
                        builder.setMessage(version.getPesan_update());


                    builder.create().show();
                } else {
                    if (manual) {
                        Snackbar.make(layoutMenuUtama, "Aplikasi anda sudah versi terbaru", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                dialog.dismiss();
                if (manual) {
                    Snackbar.make(layoutMenuUtama, "Gagal Cek Update", Snackbar.LENGTH_LONG)
                            .setAction("Coba Lagi", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cekUpdate(true);
                                }
                            }).show();
                }
            }
        });
    }

    public void getSemuaRating() {
        api.getRating(new Callback<List<Wisata>>() {
            @Override
            public void success(List<Wisata> wisatas, Response response) {
                if (listWisata == null)
                    listWisata = (ListWisata) mAdapter.getItem(0);

                if (listWisata != null) {
                    data.write();
                    for (int i = 0; i < wisatas.size(); i++) {
                        data.setRatingWisata(wisatas.get(i).getId_wisata(), wisatas.get(i).getRating());
                    }
                    data.close();

                    listWisata.getWisata();
                }
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
                startActivityForResult(i, 1);
                break;
            case R.id.menu_logout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("login", false);
                editor.putInt("id_user", 0);
                editor.commit();
                i = new Intent(MenuUtama.this, Login.class);
                startActivity(i);
                break;
            case R.id.menu_about:
                i = new Intent(this, About.class);
                startActivity(i);
                break;
            case R.id.menu_update:
                cekUpdate(true);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
            switch (requestCode) {
                default:
                    boolean delete = data.getBooleanExtra("delete", false);
                    boolean rate = data.getBooleanExtra("rate", false);

                    if (rate) getSemuaRating();
                    DetailWisata.rate = false;

                    if (delete) {
                        ArrayList<Integer> deletedId = data.getIntegerArrayListExtra("deletedId");
                        Collections.sort(deletedId);
                        Collections.reverse(deletedId);

                        if (listGallery == null) listGallery = (ListGallery) mAdapter.getItem(1);
                        listGallery.validateFoto(deletedId);
                    }
                    break;
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }
}
