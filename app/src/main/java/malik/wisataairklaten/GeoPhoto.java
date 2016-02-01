package malik.wisataairklaten;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class GeoPhoto extends Fragment{
    static TextView txtHasil;
    Button btnHasil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.geo_photo, container, false);
        txtHasil = (TextView) v.findViewById(R.id.txtHasil);
        btnHasil = (Button) v.findViewById(R.id.btnHasil);

        btnHasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHasil.setText("COba");
            }
        });

        return v;
    }

    public static void setHasil(){
        txtHasil.setText("Berhasil");
    }

    public static GeoPhoto newInstance() {
        GeoPhoto myFragment = new GeoPhoto();
//
//        Bundle args = new Bundle();
//        args.putInt("someInt", someInt);
//        myFragment.setArguments(args);

        return myFragment;
    }
}
