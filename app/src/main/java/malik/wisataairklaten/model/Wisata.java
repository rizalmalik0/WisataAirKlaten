package malik.wisataairklaten.model;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class Wisata {
    int id_wisata;
    String nama_wisata;
    String tipe_wisata;
    String fasilitas;
    String foto;
    double latitude;
    double longitude;

    public Wisata() {
    }

    public int getId_wisata() {
        return id_wisata;
    }

    public void setId_wisata(int id_wisata) {
        this.id_wisata = id_wisata;
    }

    public String getNama_wisata() {
        return nama_wisata;
    }

    public void setNama_wisata(String nama_wisata) {
        this.nama_wisata = nama_wisata;
    }

    public String getTipe_wisata() {
        return tipe_wisata;
    }

    public void setTipe_wisata(String tipe_wisata) {
        this.tipe_wisata = tipe_wisata;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return getNama_wisata();
    }
}
