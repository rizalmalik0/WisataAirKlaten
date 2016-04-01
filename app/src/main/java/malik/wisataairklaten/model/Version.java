package malik.wisataairklaten.model;

/**
 * Created by Rizal Malik on 06/03/2016.
 */
public class Version {
    int id_version;
    String version;
    String tanggal;
    String pesan_update;
    String link;

    public int getId_version() {
        return id_version;
    }

    public void setId_version(int id_version) {
        this.id_version = id_version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPesan_update() {
        return pesan_update;
    }

    public void setPesan_update(String pesan_update) {
        this.pesan_update = pesan_update;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
