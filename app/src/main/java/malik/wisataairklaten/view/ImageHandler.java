package malik.wisataairklaten.view;

import android.content.Context;
import android.os.Environment;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Rizal Malik on 01/04/2016.
 */
public class ImageHandler {
    static Picasso picasso;

    public static Picasso with(Context mContext) {
        if (picasso==null){
            File customCacheDirectory = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/WisataAirKlaten");
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(new Cache(customCacheDirectory, Integer.MAX_VALUE)).build();
            picasso = new Picasso.Builder(mContext).downloader(new OkHttp3Downloader(okHttpClient))
                    .build();
        }
        return picasso;
    }
}
