package malik.wisataairklaten;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import malik.wisataairklaten.adapter.TabsPagerAdapter;
import malik.wisataairklaten.view.SlidingTabLayout;

public class MenuUtama extends AppCompatActivity {
    private ViewPager viewPager;
    private SlidingTabLayout tabs;
    private Toolbar toolbar;
    private TabsPagerAdapter mAdapter;

    CharSequence Titles[] = {"Wisata", "Gallery", "Geo Photo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utama);

        // Initilization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setSupportActionBar(toolbar);

        // set view
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), Titles, TabsPagerAdapter.PAGER_MENU_UTAMA, TabsPagerAdapter.ID_WISATA_NULL);
        viewPager.setAdapter(mAdapter);
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.accent_material_dark));
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_utama, menu);

        return true;
    }
}
