package malik.wisataairklaten.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import malik.wisataairklaten.GeoPhoto;
import malik.wisataairklaten.ListGallery;
import malik.wisataairklaten.ListWisata;
import malik.wisataairklaten.ReviewWisata;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    CharSequence judul[];
    ListWisata listWisata;
    ListGallery listGallery;
    GeoPhoto geoPhoto;
    ReviewWisata reviewWisata;

    int count = 3;
    int tipe;
    int id_wisata;

    public static final int ID_WISATA_NULL = 0;
    public static final int PAGER_DETAIL_WISATA = 1;
    public static final int PAGER_MENU_UTAMA = 2;

    public TabsPagerAdapter(FragmentManager fm, CharSequence judul[], int tipe, int id_wisata) {
        super(fm);

        this.judul = judul;
        this.tipe = tipe;

        if (id_wisata != ID_WISATA_NULL) {
            this.id_wisata = id_wisata;
        }
    }

    @Override
    public Fragment getItem(int index) {
        switch (tipe) {
            case PAGER_DETAIL_WISATA:
                switch (index) {
                    case 0:
                        if (listGallery == null)
                            listGallery = new ListGallery();
                        return listGallery;
                    case 1:
                        if (reviewWisata == null)
                            reviewWisata = new ReviewWisata();
                        return reviewWisata;
                    case 2:
                        if (listWisata == null)
                            listWisata = ListWisata.newIntance(id_wisata);
                        return listWisata;
                }
                break;
            case PAGER_MENU_UTAMA:
                switch (index) {
                    case 0:
                        if (listWisata == null)
                            listWisata = new ListWisata();
                        return listWisata;
                    case 1:
                        if (listGallery == null)
                            listGallery = new ListGallery();
                        return listGallery;
                    case 2:
                        if (geoPhoto == null)
                            geoPhoto = new GeoPhoto();
                        return geoPhoto;
                }
                break;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tipe == PAGER_MENU_UTAMA)
            return judul[position];

        return null;
    }

    @Override
    public int getCount() {
        return count;
    }


}
