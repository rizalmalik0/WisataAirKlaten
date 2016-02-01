package malik.wisataairklaten.model;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class Foto {
    int id_foto;
    String nama_foto;
    String deskripsi;
    User user;
    Wisata wisata;

    public Foto() {
    }

    public int getId_foto() {
        return id_foto;
    }

    public void setId_foto(int id_foto) {
        this.id_foto = id_foto;
    }

    public String getNama_foto() {
        return nama_foto;
    }

    public void setNama_foto(String nama_foto) {
        this.nama_foto = nama_foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wisata getWisata() {
        return wisata;
    }

    public void setWisata(Wisata wisata) {
        this.wisata = wisata;
    }
}
