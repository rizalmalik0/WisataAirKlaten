package malik.wisataairklaten;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.Foto;
import malik.wisataairklaten.view.ImageHandler;
import malik.wisataairklaten.view.SquareImageView;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Rizal Malik on 02/02/2016.
 */
public class DetailGalery extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView txtUsername, txtDeskripsi, txtTanggal;
    SquareImageView imgFoto;

    int id_foto, id_user;
    String nama_foto, dekripsi, tanggal, username, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_gallery);

        // getintent data
        id_foto = getIntent().getIntExtra("id_foto", 0);
        nama_foto = getIntent().getStringExtra("nama_foto");
        dekripsi = getIntent().getStringExtra("deskripsi");
        tanggal = getIntent().getStringExtra("tanggal");
        username = getIntent().getStringExtra("username");
        nama = getIntent().getStringExtra("nama");
        id_user = getIntent().getIntExtra("id_user", 0);

        //init
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgFoto = (SquareImageView) findViewById(R.id.imgFoto);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtDeskripsi = (TextView) findViewById(R.id.txtDeskripsi);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);

        // set view
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set data
        if (tanggal.equals("Baru Saja"))
            ImageHandler.with(getApplicationContext()).load(Uri.parse(nama_foto)).fit().centerCrop().into(imgFoto);
        else
            ImageHandler.with(getApplicationContext()).load(VariableGlobal.URL_GAMBAR + id_user + "/" + nama_foto).fit().centerCrop().into(imgFoto);

        txtUsername.setText(username);
        txtDeskripsi.setText(dekripsi);
        txtTanggal.setText(tanggal);

        // listener
        txtUsername.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, Profil.class);
        i.putExtra("id_user", id_user);
        i.putExtra("username", username);
        i.putExtra("nama", nama);
        startActivity(i);
    }
}
