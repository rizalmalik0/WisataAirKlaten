package malik.wisataairklaten;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import malik.wisataairklaten.adapter.DataAdapter;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class Splash extends Activity {
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // cek db
        new DataAdapter(Splash.this);

        // cek lastupdate
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String last_update = preferences.getString("last_update", "");

        if (last_update.equals("")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("last_update", "2016-03-06");
            editor.commit();
        }

        // timer
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    Intent i = new Intent(Splash.this, MenuUtama.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }

    @Override
    public void onBackPressed() {

    }
}
