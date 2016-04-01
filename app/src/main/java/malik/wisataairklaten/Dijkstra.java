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
        jarak.put(idWisataMulai, 0);

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
        idWisataChecked.add(idWisataDari);

        // get not in
        String notIn = implode(",", idWisataChecked);

        // get jarak dari database
        jarakWisata = data.getJarakWisata(idWisataDari, notIn);

        // get jarak minimum
        int jarakWisataDari = jarak.get(idWisataDari);

        for (Jarak j : jarakWisata) {
            Integer jarakLama = jarak.get(j.getWisataSampai());
            int jarakBaru = jarakWisataDari + j.getJarak();

            if (jarakLama != null) {
                // cek jarak terbaru lebih kecil dari jarak lama
                if (jarakBaru < jarakLama)
                    jarak.put(j.getWisataSampai(), jarakBaru);
            } else
                jarak.put(j.getWisataSampai(), jarakBaru);
        }

        cekWisataTerdekat();
    }

    private void cekWisataTerdekat() {
        Jarak palingDekat = null;

        // cek wisata yang belum dikunjungi
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
