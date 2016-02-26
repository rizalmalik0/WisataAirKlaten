package malik.wisataairklaten;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rizal Malik on 20/02/2016.
 */
public class About extends AppCompatActivity implements View.OnClickListener {
    TextView txtLaporan, txtKritik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        txtLaporan = (TextView) findViewById(R.id.txtLaporan);
        txtKritik = (TextView) findViewById(R.id.txtKritikSaran);

        //listener
        txtLaporan.setOnClickListener(this);
        txtKritik.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtLaporan:
                composeEmail(new String[]{"rizalmalik0@gmail.com"}, "[Laporan Masalah]");
                break;
            case R.id.txtKritikSaran:
                composeEmail(new String[]{"rizalmalik0@gmail.com"}, "[Kritik Saran]");
                break;
        }
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
