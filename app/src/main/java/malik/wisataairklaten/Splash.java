package malik.wisataairklaten;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class Splash extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

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
}
