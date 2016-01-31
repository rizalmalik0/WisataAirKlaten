package malik.wisataairklaten.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import malik.wisataairklaten.GeoPhoto;
import malik.wisataairklaten.ListGallery;
import malik.wisataairklaten.ListWisata;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new ListWisata();
            case 1:
                // Games fragment activity
                return new ListGallery();
            case 2:
                // Movies fragment activity
                return new GeoPhoto();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
