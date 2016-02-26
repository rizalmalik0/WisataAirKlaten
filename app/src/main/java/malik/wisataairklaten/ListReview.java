package malik.wisataairklaten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.model.JSONData;
import malik.wisataairklaten.view.ProgressGenerator;
import malik.wisataairklaten.view.RecyclerItemClickListener;
import malik.wisataairklaten.api.DataAPI;
import malik.wisataairklaten.api.DataDeserializer;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.JSONPesan;
import malik.wisataairklaten.model.Review;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class ListReview extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RecyclerItemClickListener.OnItemClickListener, ProgressGenerator.OnCompleteListener {
    SwipeRefreshLayout swReview;
    MaterialRippleLayout ripple;
    LinearLayout layoutPost, layoutListReview;
    CardView layoutReview;
    RecyclerView rvReview;
    EditText etReview;
    RatingBar rbReview;
    ActionProcessButton btnPost;
    ProgressGenerator progressGenerator;
    Button btnCobaLagi;
    TextView txtReview;
    List<Review> review;
    RecyclerAdapter adapter;
    DataAPI api;
    SharedPreferences preferences;

    boolean load = true;
    boolean statusReview = false;
    boolean sukses = false;
    boolean last = false;
    boolean login;
    boolean edit = false;
    int id_user, id_wisata;

    final int load_count = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        review = new ArrayList<Review>();
        adapter = new RecyclerAdapter(getActivity(), review, RecyclerAdapter.TIPE_REVIEW);

        // getlogin
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        login = preferences.getBoolean("login", false);
        id_user = preferences.getInt("id_user", 0);
        id_wisata = getArguments().getInt("id_wisata");

        // koneksi
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class,
                        new DataDeserializer<List<Review>>())
                .registerTypeAdapter(JSONPesan.class, new DataDeserializer<JSONPesan>()).create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(VariableGlobal.URL).build();
        api = restAdapter.create(DataAPI.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        View v = inflater.inflate(R.layout.list_review, container, false);
        swReview = (SwipeRefreshLayout) v.findViewById(R.id.swGallery);
        rvReview = (RecyclerView) v.findViewById(R.id.rvReview);
        btnCobaLagi = (Button) v.findViewById(R.id.btnCobaLagi);
        ripple = (MaterialRippleLayout) v.findViewById(R.id.ripple);
        layoutListReview = (LinearLayout) v.findViewById(R.id.layout_list_review);
        layoutPost = (LinearLayout) v.findViewById(R.id.layout_post_review);
        rbReview = (RatingBar) v.findViewById(R.id.ratingBar);
        etReview = (EditText) v.findViewById(R.id.etPostReview);
        btnPost = (ActionProcessButton) v.findViewById(R.id.btnPost);
        txtReview = (TextView) v.findViewById(R.id.txtReview);
        layoutReview = (CardView) v.findViewById(R.id.layout_review);

        //setview
        rvReview.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(rvReview.getContext(), LinearLayoutManager.VERTICAL, false));
        rvReview.setAdapter(adapter);
        rvReview.setNestedScrollingEnabled(false);
        btnCobaLagi.setSoundEffectsEnabled(false);
        swReview.setColorSchemeResources(R.color.ColorPrimary);
        btnPost.setMode(ActionProcessButton.Mode.PROGRESS);

        swReview.post(new Runnable() {
            @Override
            public void run() {
                if (load)
                    swReview.setRefreshing(true);
            }
        });

        getStatusReview();

        //listener
        btnCobaLagi.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        layoutReview.setOnClickListener(this);
        swReview.setOnRefreshListener(this);
        rvReview.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvReview, this));
        progressGenerator = new ProgressGenerator(this);

        return v;
    }

    public void getSemuaReview(int id_user, int id_wisata) {
        load = true;

        api.getReviewWisata(id_user, id_wisata, new Callback<List<Review>>() {
            @Override
            public void success(List<Review> reviews, Response response) {
                load = false;
                sukses = true;

                last = reviews.size() < load_count;
                if (last) adapter.setFooter(false);

                review.clear();
                review.addAll(reviews);
                swReview.setRefreshing(false);
                ripple.setVisibility(View.GONE);
                btnCobaLagi.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
                swReview.setRefreshing(false);
                btnCobaLagi.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getStatusReview() {
        setFormEnabled(false);

        api.getStatusReview(id_user, id_wisata, new Callback<JSONData<Review>>() {
            @Override
            public void success(JSONData<Review> jsonReview, Response response) {
                statusReview = true;
                layoutReview.setVisibility(View.VISIBLE);

                if (jsonReview.getStatus() == 0) {
                    setFormEnabled(true);
                    layoutPost.setVisibility(View.VISIBLE);
                } else {
                    rbReview.setRating(jsonReview.getData().getRating());
                    if (jsonReview.getData().getIsi().trim().length() != 0) {
                        txtReview.setText(jsonReview.getData().getIsi());
                        txtReview.setVisibility(View.VISIBLE);
                    } else {
                        txtReview.setVisibility(View.GONE);
                    }
                }

                getSemuaReview(id_user, id_wisata);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
                swReview.setRefreshing(false);
                btnCobaLagi.setVisibility(View.VISIBLE);
            }
        });
    }

    public void loadMoreReview(int id_user, int id_wisata, String tanggal) {
        load = true;

        api.loadMoreReview(id_user, id_wisata, tanggal, new Callback<List<Review>>() {
            @Override
            public void success(List<Review> reviews, Response response) {
                load = false;

                last = reviews.size() < load_count;
                if (last) adapter.setFooter(false);

                adapter.setFooter(false);
                adapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "Load More", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    public void tambahReview() {
        setFormEnabled(false);

        api.tambahReview(id_user, id_wisata, (int) rbReview.getRating(), etReview.getText().toString(), new Callback<JSONPesan>() {
            @Override
            public void success(JSONPesan jsonPesan, Response response) {
                if (jsonPesan.getStatus() == 1) {
                    String isi = etReview.getText().toString().trim();

                    btnPost.setProgress(100);
                    layoutPost.setVisibility(View.GONE);
                    if (isi.length() != 0) {
                        txtReview.setText(isi);
                        txtReview.setVisibility(View.VISIBLE);
                    } else {
                        txtReview.setVisibility(View.GONE);
                    }
                } else {
                    btnPost.setProgress(-1);
                    setFormEnabled(true);
                    Toast.makeText(getActivity(), "Gagal menambah data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                setFormEnabled(true);
                btnPost.setProgress(-1);
                Toast.makeText(getActivity(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editReview() {
        setFormEnabled(false);

        api.editReview(id_user, id_wisata, (int) rbReview.getRating(), etReview.getText().toString(), new Callback<JSONPesan>() {
            @Override
            public void success(JSONPesan jsonPesan, Response response) {
                if (jsonPesan.getStatus() == 1 || jsonPesan.getPesan().equals("Tidak ada data diubah")) {
                    String isi = etReview.getText().toString().trim();

                    btnPost.setProgress(100);
                    layoutPost.setVisibility(View.GONE);
                    if (isi.length() != 0) {
                        txtReview.setText(isi);
                        txtReview.setVisibility(View.VISIBLE);
                    } else {
                        txtReview.setVisibility(View.GONE);
                    }
                } else {
                    btnPost.setProgress(-1);
                    setFormEnabled(true);
                    Toast.makeText(getActivity(), "Tidak dapat mengubah data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                btnPost.setProgress(-1);
                setFormEnabled(true);
                Toast.makeText(getActivity(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFormEnabled(boolean enable) {
        rbReview.setIsIndicator(!enable);
        layoutReview.setClickable(!enable);
        etReview.setEnabled(enable);
        btnPost.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCobaLagi:
                if (!load) {
                    btnCobaLagi.setVisibility(View.GONE);
                    swReview.setRefreshing(true);

                    if (statusReview)
                        getStatusReview();
                    else
                        getSemuaReview(id_user, id_wisata);
                }
                break;
            case R.id.btnPost:
                etReview.setError(null);

                if (rbReview.getRating() == 0) {
                    rbReview.requestFocus();
                    Snackbar
                            .make(layoutListReview, "Rating tidak boleh kosong", Snackbar.LENGTH_SHORT).show();
                } else if (id_user == 0) {
                    Snackbar
                            .make(layoutListReview, "Login untuk menambahkan Review", Snackbar.LENGTH_LONG)
                            .setAction("LOGIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getActivity(), Login.class);
                                    startActivity(i);
                                }
                            }).show();
                } else {
                    progressGenerator.start(btnPost);
                }
                break;
            case R.id.layout_review:
                edit = true;
                setFormEnabled(true);
                txtReview.setVisibility(View.GONE);
                etReview.setText(txtReview.getText().toString());
                layoutPost.setVisibility(View.VISIBLE);
                etReview.requestFocus();
                break;
        }
    }

    @Override
    public void onRefresh() {
        swReview.setRefreshing(false);
    }

    public static ListReview newIntance(int id_wisata) {
        ListReview reviewWisata = new ListReview();
        Bundle args = new Bundle();
        args.putInt("id_wisata", id_wisata);
        reviewWisata.setArguments(args);

        return reviewWisata;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent i = new Intent(getActivity(), Profil.class);
        i.putExtra("id_user", review.get(position).getUser().getId_user());
        i.putExtra("username", review.get(position).getUser().getUsername());
        i.putExtra("nama", review.get(position).getUser().getNama());
        startActivity(i);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onComplete() {
        if (!edit) tambahReview();
        else editReview();
    }
}
