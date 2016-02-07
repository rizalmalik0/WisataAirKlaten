package malik.wisataairklaten;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.adapter.RecyclerItemClickListener;
import malik.wisataairklaten.adapter.TabsPagerAdapter;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class DetailWisata extends AppCompatActivity implements ViewPager.OnPageChangeListener, RecyclerItemClickListener.OnItemClickListener {
    PopupWindow popupWindow;
    TabLayout tabLayout;
    ViewPager viewPager;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView imgWisata, imgLokasi;
    TabsPagerAdapter mAdapter;
    RecyclerView rvFasilitas;
    DataAdapter data;
    Wisata wisata;
    List<String> fasilitas;

    CharSequence Titles[] = {"Gallery", "Review", "Rekomendasi"};

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
        viewPager = (ViewPager) findViewById(R.id.pager);

        // intent data
        int id = getIntent().getIntExtra("id_wisata", 0);

        // get detail
        data = new DataAdapter(this);
        data.open();
        wisata = data.getDetailWisata(id);
        data.close();

        // set data
        Picasso.with(this).load("file:///android_asset/gambar/" + wisata.getFoto()).fit().centerCrop().into(imgWisata);
        String url = "http://maps.googleapis.com/maps/api/staticmap?zoom=15&scale=1&size=400x200&maptype=roadmap&format=png&visual_refresh=true&markers=size:small|color:0x029789|label:1|";
        Picasso.with(this).load(url + wisata.getLatitude() + "," + wisata.getLongitude()).fit().into(imgLokasi);

        // set view
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(wisata.getNama_wisata());
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), Titles, TabsPagerAdapter.PAGER_DETAIL_WISATA, wisata.getId_wisata());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_gallery);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_rating);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_rekomendasi);

        // adapter
        String[] split = wisata.getFasilitas().split(",");
        fasilitas = new ArrayList<String>();
        for (String f : split) {
            fasilitas.add(f);
        }

        rvFasilitas.setLayoutManager(new LinearLayoutManager(rvFasilitas.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFasilitas.setAdapter(new RecyclerAdapter(this, fasilitas, RecyclerAdapter.TIPE_FASILITAS));
        rvFasilitas.setNestedScrollingEnabled(false);

        //listener
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(this);
        rvFasilitas.addOnItemTouchListener(new RecyclerItemClickListener(this, rvFasilitas, this));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        switch (position) {
            case 0:
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());

                viewPager.setLayoutParams(layoutParams);
                break;
            case 1:
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());

                viewPager.setLayoutParams(layoutParams);
                break;
            case 2:
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1750, getResources().getDisplayMetrics());

                viewPager.setLayoutParams(layoutParams);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(View view, int position) {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        } else {
            int x = 0;

            LayoutInflater layoutInflater
                    = (LayoutInflater) getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup, null);

            TextView txtPopup = (TextView) popupView.findViewById(R.id.txtPopup);
            txtPopup.setText(fasilitas.get(position));

            popupWindow = new PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),""));
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAsDropDown(view.findViewById(R.id.imgItem,0,0));
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_utama, menu);

        return true;
    }
}
