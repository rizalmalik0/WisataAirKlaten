package malik.wisataairklaten.model;

/**
 * Created by Rizal Malik on 13/03/2016.
 */
public class Jarak {
    int jarak;
    int wisataDari;
    int wisataSampai;

    public Jarak() {

    }

    public Jarak(int jarak, int wisataSampai) {
        this.jarak = jarak;
        this.wisataSampai = wisataSampai;
    }

    public int getJarak() {
        return jarak;
    }

    public void setJarak(int jarak) {
        this.jarak = jarak;
    }

    public int getWisataDari() {
        return wisataDari;
    }

    public void setWisataDari(int wisataDari) {
        this.wisataDari = wisataDari;
    }

    public int getWisataSampai() {
        return wisataSampai;
    }

    public void setWisataSampai(int wisataSampai) {
        this.wisataSampai = wisataSampai;
    }
}
