package malik.wisataairklaten;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import malik.wisataairklaten.adapter.DataAdapter;
import malik.wisataairklaten.model.Jarak;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 13/03/2016.
 */
public class Dijkstra {
    List<Wisata> wisata;
    List<Integer> idWisataChecked;
    List<Jarak> jarakWisata;
    HashMap<Integer, Integer> jarak; // id & jarak
    Context mContext;
    DataAdapter data;

    int idWisataMulai;
    int totalJarak = 0;

    private Dijkstra(Context mContext, List<Wisata> wisata) {
        this.mContext = mContext;
        this.wisata = wisata;

        data = new DataAdapter(mContext);
        idWisataChecked = new ArrayList<>();
        jarak = new HashMap<>();
    }

    public Dijkstra from(int idWisataMulai) {
        this.idWisataMulai = idWisataMulai;

        return this;
    }

    public void getSemuaJarak() {
        data.open();
        getJarakMinimumWisata(idWisataMulai);
        data.close();

        // set jarak minimum to list
        for (Wisata w : wisata) {
            int jarakMinimum = (jarak.get(w.getId_wisata()) != null) ? jarak.get(w.getId_wisata()) : 0;
            w.setJarak(jarakMinimum);
        }
    }

    private void getJarakMinimumWisata(int idWisataDari) {
        Jarak palingDekat = null;

        idWisataChecked.add(idWisataDari);

        // get not in
        String notIn = implode(",", idWisataChecked);

        // get jarak dari database
        jarakWisata = data.getJarakWisata(idWisataDari, notIn);

        // get jarak minimum
        for (Jarak j : jarakWisata) {
            Integer jarakLama = jarak.get(j.getWisataSampai());
            int jarakBaru = totalJarak + j.getJarak();

            if (jarakLama != null) {
                // cek jarak terbaru lebih kecil dari jarak lama
                if (jarakBaru < jarakLama)
                    jarak.put(j.getWisataSampai(), jarakBaru);
            } else
                jarak.put(j.getWisataSampai(), jarakBaru);

            if (palingDekat != null) {
                // cek jarak paling dekat
                if (j.getJarak() < palingDekat.getJarak())
                    palingDekat = j;
            } else
                palingDekat = j;
        }

        // cek last iteration
        boolean last = jarakWisata.isEmpty();
        if (!last) {
            totalJarak += palingDekat.getJarak();
            getJarakMinimumWisata(palingDekat.getWisataSampai());
        } else
            cekWisata();
    }

    private void cekWisata() {
        Jarak palingDekat = null;

        // cek wisata yang belum cek
        for (Integer id : jarak.keySet()) {
            if (!idWisataChecked.contains(id)) {
                if (palingDekat != null) {
                    if (jarak.get(id) < palingDekat.getJarak())
                        palingDekat = new Jarak(jarak.get(id), id);
                } else
                    palingDekat = new Jarak(jarak.get(id), id);
            }
        }

        if (palingDekat != null) {
            totalJarak = palingDekat.getJarak();
            getJarakMinimumWisata(palingDekat.getWisataSampai());
        }
    }

    private String implode(String glue, List<Integer> idWisata) {
        String result = "";

        for (int i = 0; i < idWisata.size(); i++) {
            result += (i == idWisata.size() - 1) ? idWisata.get(i).toString() : idWisata.get(i) + glue;
        }

        return result;
    }


    public static Dijkstra with(Context mContext, List<Wisata> wisata) {
        return new Dijkstra(mContext, wisata);
    }
}
