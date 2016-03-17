package malik.wisataairklaten.api;

import java.util.List;
import java.util.Map;

import malik.wisataairklaten.model.Foto;
import malik.wisataairklaten.model.JSONData;
import malik.wisataairklaten.model.JSONPesan;
import malik.wisataairklaten.model.Review;
import malik.wisataairklaten.model.Update;
import malik.wisataairklaten.model.User;
import malik.wisataairklaten.model.Wisata;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.mime.TypedFile;

public interface DataAPI {
    @FormUrlEncoded
    @POST("/json/login.php")
    public void login(@Field("username") String username,
                      @Field("password") String password, Callback<JSONData<User>> json);

    @FormUrlEncoded
    @POST("/json/registrasi.php")
    public void registrasi(@Field("username") String username,
                           @Field("nama") String nama,
                           @Field("email") String email,
                           @Field("password") String password,
                           Callback<JSONData<User>> json);

    // Tambah Data
    @FormUrlEncoded
    @POST("/json/tambah_review.php")
    public void tambahReview(@Field("id_user") int id_user,
                             @Field("id_wisata") int id_wisata,
                             @Field("rating") int rating,
                             @Field("isi") String isi,
                             Callback<JSONPesan> json);

    @FormUrlEncoded
    @POST("/json/tambah_foto.php")
    public void tambahFoto(@Field("id_produk") int id_produk,
                           Callback<JSONPesan> json);

    // Tampil Semua
    @GET("/json/tampil_foto.php")
    public void getFoto(Callback<List<Foto>> foto);

    @GET("/json/tampil_rating.php")
    public void getRating(Callback<List<Wisata>> wisata);

    @GET("/json/tampil_version.php")
    public void cekUpdate(Callback<Update> update);

    // Tampil Detail
    @FormUrlEncoded
    @POST("/json/tampil_galeri_wisata.php")
    public void getGalleryWisata(@Field("load_count") int load_count,
                                 @Field("id_wisata") int id_wisata,
                                 Callback<List<Foto>> foto);

    @FormUrlEncoded
    @POST("/json/tampil_galeri_user.php")
    public void getGalleryUser(@Field("id_user") int id_user,
                               Callback<List<Foto>> foto);

    @FormUrlEncoded
    @POST("/json/tampil_review_wisata.php")
    public void getReviewWisata(@Field("id_user") int id_user,
                                @Field("id_wisata") int id_wisata,
                                Callback<List<Review>> foto);

    // Tampil Status
    @FormUrlEncoded
    @POST("/json/status_review.php")
    public void getStatusReview(@Field("id_user") int id_user,
                                @Field("id_wisata") int id_wisata,
                                Callback<JSONData<Review>> json);

    @FormUrlEncoded
    @POST("/json/status_user.php")
    public void getStatusUsername(@Field("username") String username,
                                  Callback<JSONPesan> json);

    // Load More
    @FormUrlEncoded
    @POST("/json/tampil_foto_terbaru.php")
    public void getFotoTerbaru(@Field("first_id") int first_id,
                               Callback<List<Foto>> foto);

    @FormUrlEncoded
    @POST("/json/load_more_review.php")
    public void loadMoreReview(@Field("id_user") int id_user,
                               @Field("id_wisata") int id_wisata,
                               @Field("tanggal") String tanggal,
                               Callback<List<Review>> review);

    @FormUrlEncoded
    @POST("/json/load_more_foto.php")
    public void loadMoreFoto(@Field("last_id") int last_id,
                             Callback<List<Foto>> foto);

    @FormUrlEncoded
    @POST("/json/load_more_foto_user.php")
    public void loadMoreFotoUser(@Field("id_user") int id_user,
                                 @Field("last_id") int last_id,
                                 Callback<List<Foto>> foto);

    @FormUrlEncoded
    @POST("/json/load_more_foto_wisata.php")
    public void loadMoreFotoWisata(@Field("id_wisata") int id_wisata,
                                   @Field("last_id") int last_id,
                                   Callback<List<Foto>> foto);

    // edit
    @FormUrlEncoded
    @POST("/json/ubah_review.php")
    public void editReview(@Field("id_user") int id_user,
                           @Field("id_wisata") int id_wisata,
                           @Field("rating") int rating,
                           @Field("isi") String isi,
                           Callback<JSONPesan> json);

    @FormUrlEncoded
    @POST("/json/ubah_user.php")
    public void editUser(@Field("id_user") int id_user,
                         @Field("username") String username,
                         @Field("nama") String nama,
                         @Field("email") String email,
                         @Field("password") String password,
                         Callback<JSONPesan> json);

    // JSON Object
    @FormUrlEncoded
    @POST("/json/count_foto_user.php")
    public void getCountFoto(@Field("id_user") int id_user,
                             Callback<JSONData<Integer>> data);

    // upload
    @Multipart
    @POST("/json/upload.php")
    public void uploadFoto(@Part("nama_foto") TypedFile file,
                           @PartMap Map<String, String> map,
                           Callback<JSONData<Foto>> foto);

    // hapus
    @FormUrlEncoded
    @POST("/json/hapus_foto_user.php")
    public void deleteFoto(@Field("id_foto") int id_foto,
                           @Field("id_user") int id_user,
                           @Field("nama_foto") String nama_foto,
                           Callback<JSONPesan> pesan);
}
