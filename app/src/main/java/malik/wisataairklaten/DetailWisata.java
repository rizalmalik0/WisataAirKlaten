package malik.wisataairklaten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.view.ImageHandler;
import malik.wisataairklaten.view.RecyclerItemClickListener;
import malik.wisataairklaten.adapter.TabsPagerAdapter;
import malik.wisataairklaten.model.Fasilitas;
import malik.wisataairklaten.model.Wisata;
import malik.wisataairklaten.view.CustomNestedScroll;
import malik.wisataairklaten.view.CustomPager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class DetailWisata extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener, CustomNestedScroll.OnBottomReachedListener {
    PopupWindow popupWindow;
    TabLayout tabLayout;
    CustomPager viewPager;
    CollapsingToolbarLayout collapsingToolbar;
    CustomNestedScroll nestedScrollView;
    ImageView imgWisata, imgLokasi;
    TabsPagerAdapter mAdapter;
    RecyclerView rvFasilitas;
    DataAdapter data;
    Wisata wisata;
    List<Fasilitas> fasilitas;
    ArrayList<Integer> deletedId;
    SharedPreferences preferences;

    ListReview listReview;
    ListGallery listGallery;

    boolean delete = false;
    static boolean rate;

    int id_wisata, id_user;

    CharSequence Titles[] = {"Gallery", "Review", "Rekomendasi"};
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_wisata);

        //init
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        imgWisata = (ImageView) findViewById(R.id.imgWisata);
        imgLokasi = (ImageView) findViewById(R.id.imgLokasi);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        rvFasilitas = (RecyclerView) findViewById(R.id.rvFasilitas);
        viewPager = (CustomPager) findViewById(R.id.pager);
        nestedScrollView = (CustomNestedScroll) findViewById(R.id.nest_scrollview);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get data
        id_user = preferences.getInt("id_user", 0);
        id_wisata = getIntent().getIntExtra("id_wisata", 0);
        deletedId = new ArrayList<>();

        // get detail
        data = new DataAdapter(this);
        data.open();
        wisata = data.getDetailWisata(id_wisata);
        data.close();

        // set data
        ImageHandler.with(getApplicationContext()).load("file:///android_asset/gambar/" + wisata.getFoto()).fit().centerCrop().into(imgWisata);
        ImageHandler.with(getApplicationContext()).load("file:///android_asset/map/" + wisata.getId_wisata() + ".png").placeholder(R.drawable.marker).fit().into(imgLokasi);
        getFasilitas();

        // set view
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(wisata.getNama_wisata());
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), Titles, TabsPagerAdapter.PAGER_DETAIL_WISATA, wisata.getId_wisata());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // setview tab
        View view1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.icon_gallery);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.icon_rating);
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.icon_rekomendasi);
        tabLayout.getTabAt(2).setCustomView(view3);

        // adapter
        rvFasilitas.setLayoutManager(new LinearLayoutManager(rvFasilitas.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFasilitas.setAdapter(new RecyclerAdapter(this, fasilitas, RecyclerAdapter.TIPE_FASILITAS));
        rvFasilitas.setNestedScrollingEnabled(false);

        //listener
        viewPager.setOffscreenPageLimit(2);
        rvFasilitas.addOnItemTouchListener(new RecyclerItemClickListener(this, rvFasilitas, this));
        nestedScrollView.setOnBottomReachedListener(this);
    }

    private void getFasilitas() {
        String[] split = wisata.getFasilitas().split(",");
        fasilitas = new ArrayList<Fasilitas>();

        Fasilitas kedalaman = new Fasilitas();
        kedalaman.setGambar(wisata.getKedalaman());
        kedalaman.setNama_fasilitas("Kedalaman");
        fasilitas.add(kedalaman);

        for (int i = 0; i < split.length; i++) {
            Fasilitas f = new Fasilitas();
            StringBuilder builder = new StringBuilder(split[i]);
            builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));
            String nama = builder.toString();
            nama = nama.replace("_", " ");

            f.setGambar(split[i] + ".png");
            f.setNama_fasilitas(nama);
            fasilitas.add(f);
        }
    }

    private void cekLogin() {
        boolean login = preferences.getBoolean("login", false);
        MenuItem menuLogin = menu.findItem(R.id.menu_login);
        MenuItem menuRegistrasi = menu.findItem(R.id.menu_registrasi);
        MenuItem menuProfil = menu.findItem(R.id.menu_profil);
        if (login) {
            menuLogin.setVisible(false);
            menuRegistrasi.setVisible(false);
            menuProfil.setVisible(true);
        } else {
            menuLogin.setVisible(true);
            menuRegistrasi.setVisible(true);
            menuProfil.setVisible(false);
        }
    }

    public void map(View v) {
        Intent i = new Intent(this, Map.class);
        i.putExtra("id_wisata", id_wisata);
        startActivity(i);
    }

    @Override
    public void onItemClick(View view, int position) {
        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);

        TextView txtPopup = (TextView) popupView.findViewById(R.id.txtPopup);
        txtPopup.setText(fasilitas.get(position).getNama_fasilitas());

        txtPopup.measure(0, 0);

        int x = ((60 - txtPopup.getMeasuredWidth()) / 2) + 30;

        popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), ""));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view.findViewById(R.id.imgItem), x, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow = null;
            }
        });
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wisata, menu);

        cekLogin();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            delete = data.getBooleanExtra("delete", false); // cek data delete
            rate = data.getBooleanExtra("rate", false);

            if (delete) {
                deletedId.addAll(data.getIntegerArrayListExtra("deletedId"));
                Collections.sort(deletedId);
                Collections.reverse(deletedId);

                if (listGallery == null) listGallery = (ListGallery) mAdapter.getItem(0);
                listGallery.validateFoto(deletedId); // validate foto in list gallery
            }
        }
    }

    @Override
    public void onBottomReached() {
        switch (viewPager.getCurrentItem()) {
            case 0:
                if (listGallery == null) listGallery = (ListGallery) mAdapter.getItem(0);
                if (!listGallery.load && !listGallery.last && listGallery.sukses && listGallery.foto.size() != 0)
                    listGallery.loadMoreFotoWisata(wisata.getId_wisata(), listGallery.foto.get(listGallery.foto.size() - 1).getId_foto());
                break;
            case 1:
                if (listReview == null) listReview = (ListReview) mAdapter.getItem(1);
                if (!listReview.load && !listReview.last && listReview.sukses && listReview.review.size() != 0)
                    listReview.loadMoreReview(listReview.id_user, wisata.getId_wisata(), listReview.review.get(listReview.review.size() - 1).getTanggal());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("delete", delete);
        i.putExtra("rate", rate);
        if (delete) i.putIntegerArrayListExtra("deletedId", deletedId);
        setResult(RESULT_OK, i);
        finish();
    }
}
