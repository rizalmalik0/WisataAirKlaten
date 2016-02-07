package malik.wisataairklaten;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Rizal Malik on 02/02/2016.
 */
public class DetailGalery extends FragmentActivity{
    RecyclerView rvImage;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //init
        rvImage = (RecyclerView) findViewById(R.id.rvImage);

    }
}
