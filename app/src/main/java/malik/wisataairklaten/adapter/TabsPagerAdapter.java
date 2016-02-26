package malik.wisataairklaten.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import malik.wisataairklaten.GeoPhoto;
import malik.wisataairklaten.ListGallery;
import malik.wisataairklaten.ListWisata;
import malik.wisataairklaten.ListReview;
import malik.wisataairklaten.view.CustomPager;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    CharSequence judul[];
    ListWisata listWisata;
    ListGallery listGallery;
    GeoPhoto geoPhoto;
    ListReview reviewWisata;

    int count = 3;
    int tipe;
    int id;

    private int mCurrentPosition = -1;

    public static final int ID_NULL = 0;
    public static final int PAGER_DETAIL_WISATA = 1;
    public static final int PAGER_MENU_UTAMA = 2;
    public static final int PAGER_PROFIL = 3;

    public TabsPagerAdapter(FragmentManager fm, CharSequence judul[], int tipe, int id) {
        super(fm);

        this.judul = judul;
        this.tipe = tipe;

        if (id != ID_NULL) this.id = id;

        if (tipe == PAGER_PROFIL) this.count = 1;
    }

    @Override
    public Fragment getItem(int index) {
        switch (tipe) {
            case PAGER_DETAIL_WISATA:
                switch (index) {
                    case 0:
                        if (listGallery == null)
                            listGallery = ListGallery.newIntance(id, ListGallery.GALLERY_WISATA);
                        return listGallery;
                    case 1:
                        if (reviewWisata == null)
                            reviewWisata = ListReview.newIntance(id);
                        return reviewWisata;
                    case 2:
                        if (listWisata == null)
                            listWisata = ListWisata.newIntance(id);
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
                            listGallery = ListGallery.newIntance(0, ListGallery.GALLERY_SEMUA);
                        return listGallery;
                    case 2:
                        if (geoPhoto == null)
                            geoPhoto = new GeoPhoto();
                        return geoPhoto;
                }
                break;
            case PAGER_PROFIL:
                switch (index) {
                    case 0:
                        if (listGallery == null)
                            listGallery = ListGallery.newIntance(id, ListGallery.GALLERY_USER);
                        return listGallery;
                }
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


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (container instanceof CustomPager) {
            if (position != mCurrentPosition) {
                Fragment fragment = (Fragment) object;
                CustomPager pager = (CustomPager) container;
                if (fragment != null && fragment.getView() != null) {
                    mCurrentPosition = position;
                    pager.measureCurrentView(fragment.getView());
                }
            }
        }
    }
}
