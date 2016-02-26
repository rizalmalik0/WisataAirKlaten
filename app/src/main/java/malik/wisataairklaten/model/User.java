package malik.wisataairklaten.model;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class User {
    int id_user;
    String username;
    String nama;

    public User() {
    }

    public User(int id_user, String username, String nama) {
        this.id_user = id_user;
        this.username = username;
        this.nama = nama;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}

