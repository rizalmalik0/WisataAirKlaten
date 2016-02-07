package malik.wisataairklaten.api;

import java.util.List;

import malik.wisataairklaten.model.Foto;
import malik.wisataairklaten.model.JSONData;
import malik.wisataairklaten.model.JSONPesan;
import malik.wisataairklaten.model.Review;
import malik.wisataairklaten.model.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface DataAPI {
	@FormUrlEncoded
	@POST("/login.php")
	public void login(@Field("username") String username,
					  @Field("password") String password, Callback<JSONData<User>> json);

	@FormUrlEncoded
	@POST("/ubah_produk.php")
	public void ubahProduk(@Field("id_produk") int id_produk,
						   @Field("nama_produk") String nama_produk,
						   @Field("harga") String harga, Callback<JSONPesan> json);

	@FormUrlEncoded
	@POST("/hapus_produk.php")
	public void hapusProduk(@Field("id_produk") int id_produk,
							Callback<JSONPesan> json);

	@GET("/tampil_foto.php")
	public void getFoto(Callback<List<Foto>> foto);
}
