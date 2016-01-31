package malik.wisataairklaten.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class DataAdapter extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "WisataAirKlaten.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase mDb;
    Context mContext;

    public DataAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        setForcedUpgrade(DATABASE_VERSION);
    }

    public DataAdapter open() throws SQLException {
        try {
            mDb = this.getReadableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public DataAdapter write() throws SQLException {
        try {
            mDb = this.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void close() {
        mDb.close();
    }

    public List<Wisata> getSemuaWisata() {
        List<Wisata> wisata = new ArrayList<Wisata>();

        String[] columns = new String[] { "id_wisata", "nama_wisata", "tipe_wisata",
                "fasilitas", "foto" };

        Cursor cursor = mDb.query("wisata", columns, null, null, null, null, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Wisata w = new Wisata();
            w.setId_wisata(cursor.getInt(0));
            w.setNama_wisata(cursor.getString(1));
            w.setTipe_wisata(cursor.getString(2));
            w.setFasilitas(cursor.getString(3));
            w.setFoto(cursor.getString(4));
            wisata.add(w);
        }

        return wisata;
    }

    public Wisata getDetailWisata(int id){
        Wisata w = new Wisata();

        String[] columns = new String[] { "id_wisata", "nama_wisata", "tipe_wisata",
                "fasilitas", "foto" };

        Cursor cursor = mDb.query("wisata", columns, "id_wisata="+id, null, null, null, null);
        cursor.moveToFirst();

        w.setNama_wisata(cursor.getString(1));
        w.setTipe_wisata(cursor.getString(2));
        w.setFasilitas(cursor.getString(3));
        w.setFoto(cursor.getString(4));

        return w;
    }
}
