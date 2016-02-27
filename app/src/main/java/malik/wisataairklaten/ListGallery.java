package malik.wisataairklaten;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.*;
import java.util.Map;

import malik.wisataairklaten.adapter.RecyclerAdapter;
import malik.wisataairklaten.model.JSONData;
import malik.wisataairklaten.model.JSONPesan;
import malik.wisataairklaten.model.User;
import malik.wisataairklaten.view.RecyclerItemClickListener;
import malik.wisataairklaten.api.DataAPI;
import malik.wisataairklaten.api.DataDeserializer;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.Foto;
import malik.wisataairklaten.view.WrappedGridLayoutManager;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedFile;

/**
 * Created by Rizal Malik on 27/01/2016.
 */
public class ListGallery extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RecyclerItemClickListener.OnItemClickListener {
    SwipeRefreshLayout swGallery;
    MaterialRippleLayout ripple;
    FrameLayout layoutGallery;
    RecyclerView rvGallery;
    Button btnCobaLagi;
    AlertDialog dialog;
    List<Foto> foto;
    RecyclerAdapter adapter;
    WrappedGridLayoutManager wrappedGridLayoutManager;
    DataAPI api;
    SharedPreferences preferences;
    Uri imageUri;
    Snackbar snackbar;

    boolean load = true;
    boolean upload = false;
    boolean suksesUpload = true;
    boolean sukses = false;
    boolean last = false;
    boolean login;

    int posisi = 0;
    int id_wisata, id_user;
    int lastVisiblesItems, totalItemCount;
    int tipe;
    String username, nama, deskripsi;

    final int load_count = 12;
    public static final int GALLERY_SEMUA = 1;
    public static final int GALLERY_WISATA = 2;
    public static final int GALLERY_USER = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foto = new ArrayList<Foto>();
        adapter = new RecyclerAdapter(getActivity(), foto, RecyclerAdapter.TIPE_FOTO);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // get tipe
        tipe = getArguments().getInt("tipe", 1);
        switch (tipe) {
            case GALLERY_SEMUA:
                break;
            case GALLERY_WISATA:
                posisi = 1;
                login = preferences.getBoolean("login", false);
                id_user = preferences.getInt("id_user", 0);
                username = preferences.getString("username", "");
                nama = preferences.getString("nama", "");
                id_wisata = getArguments().getInt("id_wisata");
                break;
            case GALLERY_USER:
                id_user = getArguments().getInt("id_user");
                break;
            default:
                break;
        }

        // koneksi
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class,
                        new DataDeserializer<List<Foto>>()).create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(VariableGlobal.URL).build();
        api = restAdapter.create(DataAPI.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        View v = inflater.inflate(R.layout.list_gallery, container, false);
        swGallery = (SwipeRefreshLayout) v.findViewById(R.id.swGallery);
        rvGallery = (RecyclerView) v.findViewById(R.id.rvGallery);
        btnCobaLagi = (Button) v.findViewById(R.id.btnCobaLagi);
        ripple = (MaterialRippleLayout) v.findViewById(R.id.ripple);
        layoutGallery = (FrameLayout) v.findViewById(R.id.layout_list_galeri);

        // set view
        wrappedGridLayoutManager = new WrappedGridLayoutManager(getActivity(), 3);
        rvGallery.setLayoutManager(wrappedGridLayoutManager);
        rvGallery.getItemAnimator().setSupportsChangeAnimations(false);

        rvGallery.setAdapter(adapter);
        rvGallery.setNestedScrollingEnabled(false);
        btnCobaLagi.setSoundEffectsEnabled(false);
        swGallery.setColorSchemeResources(R.color.ColorPrimary);

        swGallery.post(new Runnable() {
            @Override
            public void run() {
                if (load)
                    swGallery.setRefreshing(true);
            }
        });

        getFoto();

        //listener
        btnCobaLagi.setOnClickListener(this);
        swGallery.setOnRefreshListener(this);
        rvGallery.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvGallery, this));

        // load more
        wrappedGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == RecyclerAdapter.TIPE_FOOTER)
                    return 3;
                else
                    return 1;
            }
        });
        rvGallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    totalItemCount = wrappedGridLayoutManager.getItemCount();
                    lastVisiblesItems = wrappedGridLayoutManager.findLastVisibleItemPosition();

                    if (!load && !last) {
                        if ((lastVisiblesItems + 3) >= totalItemCount) {
                            loadMoreFoto(foto.get(foto.size() - 1).getId_foto());
                        }
                    }
                }
            }
        });

        return v;
    }

    public void getFoto() {
        switch (tipe) {
            case GALLERY_SEMUA:
                getSemuaFoto();
                break;
            case GALLERY_WISATA:
                getGaleriWisata(id_wisata);
                break;
            case GALLERY_USER:
                getGaleriUser(id_user);
                break;
        }
    }

    // SEMUA FOTO
    public void getSemuaFoto() {
        load = true;

        api.getFoto(new Callback<List<Foto>>() {
            @Override
            public void success(List<Foto> fotos, Response response) {
                load = false;
                sukses = true;

                last = fotos.size() < load_count;
                if (last) adapter.setFooter(false);

                foto.addAll(fotos);
                swGallery.setRefreshing(false);
                ripple.setVisibility(View.GONE);
                btnCobaLagi.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
                swGallery.setRefreshing(false);
                btnCobaLagi.setVisibility(View.VISIBLE);
            }
        });
    }

    public void loadMoreFoto(final int last_id) {
        load = true;

        api.loadMoreFoto(last_id, new Callback<List<Foto>>() {
            @Override
            public void success(List<Foto> fotos, Response response) {
                load = false;
                foto.addAll(foto.size(), fotos);

                last = fotos.size() < load_count;
                if (last) adapter.setFooter(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
                lastVisiblesItems = 0;
            }
        });
    }

    public void getFotoTerbaru() {
        load = true;

        api.getFotoTerbaru(foto.get(0).getId_foto(), new Callback<List<Foto>>() {
            @Override
            public void success(List<Foto> fotos, Response response) {
                load = false;
                foto.addAll(posisi, fotos);
                swGallery.setRefreshing(false);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
                swGallery.setRefreshing(false);
            }
        });
    }

    //GALERI WISATA
    public void getGaleriWisata(int id_wisata) {
        load = true;

        api.getGalleryWisata(load_count - 1, id_wisata, new Callback<List<Foto>>() {
            @Override
            public void success(List<Foto> fotos, Response response) {
                load = false;
                sukses = true;
                last = fotos.size() < load_count - 1;
                if (last) adapter.setFooter(false);

                Foto f = new Foto();
                f.setId_foto(0);
                foto.add(f);

                foto.addAll(posisi, fotos);
                swGallery.setRefreshing(false);
                ripple.setVisibility(View.GONE);
                btnCobaLagi.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
                swGallery.setRefreshing(false);
                btnCobaLagi.setVisibility(View.VISIBLE);
            }
        });
    }

    public void loadMoreFotoWisata(final int id_wisata, final int last_id) {
        load = true;

        api.loadMoreFotoWisata(id_wisata, last_id, new Callback<List<Foto>>() {
            @Override
            public void success(List<Foto> fotos, Response response) {
                load = false;
                foto.addAll(foto.size(), fotos);

                last = fotos.size() < load_count;
                if (last) adapter.setFooter(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
            }
        });
    }

    //GALERI USER
    public void getGaleriUser(int id_user) {
        load = true;

        api.getGalleryUser(id_user, new Callback<List<Foto>>() {
            @Override
            public void success(List<Foto> fotos, Response response) {
                load = false;
                sukses = true;
                last = fotos.size() < load_count;
                if (last) adapter.setFooter(false);

                foto.addAll(posisi, fotos);
                swGallery.setRefreshing(false);
                ripple.setVisibility(View.GONE);
                btnCobaLagi.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
                swGallery.setRefreshing(false);
                btnCobaLagi.setVisibility(View.VISIBLE);
            }
        });
    }

    public void loadMoreFotoUser(final int id_user, final int last_id) {
        load = true;

        api.loadMoreFotoUser(id_user, last_id, new Callback<List<Foto>>() {
            @Override
            public void success(List<Foto> fotos, Response response) {
                load = false;
                foto.addAll(foto.size(), fotos);

                last = fotos.size() < load_count;
                if (last) adapter.setFooter(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
            }
        });
    }

    // UPLOAD
    public void uploadFoto() {
        upload = true;
        suksesUpload = false;

        File f = new File(getPath(imageUri));

        TypedFile typedImage = new TypedFile("image/jpg", f);

        Map<String, String> map = new HashMap<String, String>();
        map.put("id_user", id_user + "");
        map.put("id_wisata", id_wisata + "");
        map.put("deskripsi", deskripsi);

        api.uploadFoto(typedImage, map, new Callback<JSONPesan>() {
            @Override
            public void success(JSONPesan pesan, Response response) {
                upload = false;
                suksesUpload = true;

                // set view
                View v = rvGallery.getChildAt(0);
                RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout_upload);
                layout.setVisibility(View.GONE);

                // add new
                Foto f = new Foto();
                f.setId_foto(0);
                foto.add(0, f);
                adapter.notifyItemInserted(0);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                upload = false;
                suksesUpload = false;

                Toast.makeText(getActivity(), retrofitError.getMessage(), Toast.LENGTH_SHORT).show();

                // set view
                View v = rvGallery.getChildAt(0);
                Button btnUpload = (Button) v.findViewById(R.id.btnUpload);
                ProgressBar pbUpload = (ProgressBar) v.findViewById(R.id.pbUpload);
                pbUpload.setVisibility(View.GONE);
                btnUpload.setVisibility(View.VISIBLE);

                snackbar = Snackbar.make(layoutGallery, "Upload Gagal", Snackbar.LENGTH_LONG)
                        .setAction("Coba Lagi", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v = rvGallery.getChildAt(0);
                                Button btnUpload = (Button) v.findViewById(R.id.btnUpload);
                                ProgressBar pbUpload = (ProgressBar) v.findViewById(R.id.pbUpload);
                                pbUpload.setVisibility(View.VISIBLE);
                                btnUpload.setVisibility(View.GONE);
                                uploadFoto();
                            }
                        });
                snackbar.show();
            }
        });
    }

    // DELETE
    public void hapusFoto(final int position) {
        load = true;
        swGallery.setRefreshing(true);

        api.deleteFoto(foto.get(position).getId_foto(), id_user, new Callback<JSONPesan>() {
            @Override
            public void success(JSONPesan jsonPesan, Response response) {
                load = false;
                swGallery.setRefreshing(false);

                if (jsonPesan.getStatus() == 1) {
                    foto.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    Snackbar.make(layoutGallery, "Gagal menghapus foto", Snackbar.LENGTH_LONG)
                            .setAction("Coba lagi", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hapusFoto(position);
                                }
                            }).show();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                load = false;
                swGallery.setRefreshing(false);
                Snackbar.make(layoutGallery, "Gagal menghapus foto", Snackbar.LENGTH_LONG)
                        .setAction("Coba lagi", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hapusFoto(position);
                            }
                        }).show();
            }
        });
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void galeri() {
        Intent galeriPicker = new Intent(Intent.ACTION_PICK);
        galeriPicker.setType("image/jpg");
        startActivityForResult(galeriPicker, 1);
    }

    public void showDialogUpload() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_upload, null);

        // init
        ImageView imgUpload = (ImageView) layout.findViewById(R.id.imgUpload);
        final EditText etUpload = (EditText) layout.findViewById(R.id.etPostUpload);
        Button btnUpload = (Button) layout.findViewById(R.id.btnPostUpload);

        // listener
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                deskripsi = etUpload.getText().toString();

                Foto f = new Foto();
                f.setId_foto(-1);
                f.setNama_foto(imageUri.toString());
                f.setTanggal("Baru Saja");
                f.setDeskripsi(etUpload.getText().toString());
                f.setUser(new User(id_user, username, nama));

                foto.set(0, f);
                adapter.notifyItemChanged(0);

                uploadFoto();
            }
        });

        // set data
        Picasso.with(getActivity()).load(imageUri).centerCrop().fit().into(imgUpload);

        //setview
        builder.setTitle("Upload Foto");
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            imageUri = data.getData();

            showDialogUpload();
        }

    }

    @Override
    public void onClick(View v) {
        if (!load) {
            btnCobaLagi.setVisibility(View.GONE);
            swGallery.setRefreshing(true);
            getFoto();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position != foto.size()) {
            switch (foto.get(position).getId_foto()) {
                case -1:
                    if (!upload && !suksesUpload) {
                        Button btnUpload = (Button) view.findViewById(R.id.btnUpload);
                        ProgressBar pbUpload = (ProgressBar) view.findViewById(R.id.pbUpload);

                        btnUpload.setVisibility(View.GONE);
                        pbUpload.setVisibility(View.VISIBLE);

                        snackbar.dismiss();
                        uploadFoto();
                    } else {
                        if (position != foto.size()) {
                            Intent i = new Intent(getActivity(), DetailGalery.class);
                            i.putExtra("id_foto", foto.get(position).getId_foto());
                            i.putExtra("nama_foto", foto.get(position).getNama_foto());
                            i.putExtra("tanggal", foto.get(position).getTanggal());
                            i.putExtra("deskripsi", foto.get(position).getDeskripsi());
                            i.putExtra("username", foto.get(position).getUser().getUsername());
                            i.putExtra("nama", foto.get(position).getUser().getNama());
                            i.putExtra("id_user", foto.get(position).getUser().getId_user());
                            startActivity(i);
                        }
                    }

                    break;
                case 0:
                    if (login)
                        galeri();
                    else {
                        Snackbar snackbar = Snackbar
                                .make(layoutGallery, "Login untuk upload Foto", Snackbar.LENGTH_LONG)
                                .setAction("LOGIN", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getActivity(), Login.class);
                                        startActivity(i);
                                    }
                                });

                        snackbar.show();
                    }
                    break;
                default:
                    Intent i = new Intent(getActivity(), DetailGalery.class);
                    i.putExtra("nama_foto", foto.get(position).getNama_foto());
                    i.putExtra("tanggal", foto.get(position).getTanggal());
                    i.putExtra("deskripsi", foto.get(position).getDeskripsi());
                    i.putExtra("username", foto.get(position).getUser().getUsername());
                    i.putExtra("nama", foto.get(position).getUser().getNama());
                    i.putExtra("id_user", foto.get(position).getUser().getId_user());
                    startActivity(i);
                    break;
            }
        }
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        int myId = preferences.getInt("id_user", 0);

        if (tipe == GALLERY_USER && id_user == myId && !load) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Hapus Foto")
                    .setMessage("Apakah anda ingin menghapus foto ini?")
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hapusFoto(position);
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    public void onRefresh() {
        switch (tipe) {
            case GALLERY_SEMUA:
                btnCobaLagi.setVisibility(View.GONE);
                if (sukses) {
                    getFotoTerbaru();
                } else {
                    getSemuaFoto();
                }
                break;
            case GALLERY_WISATA:
            case GALLERY_USER:
                swGallery.setRefreshing(false);
                break;
        }
    }

    public static ListGallery newIntance(int id, int tipe_gallery) {
        ListGallery listGallery = new ListGallery();
        Bundle args = new Bundle();
        switch (tipe_gallery) {
            case GALLERY_USER:
                args.putInt("id_user", id);
                args.putInt("tipe", tipe_gallery);
                break;
            case GALLERY_WISATA:
                args.putInt("id_wisata", id);
                args.putInt("tipe", tipe_gallery);
                break;
            case GALLERY_SEMUA:
                args.putInt("tipe", tipe_gallery);
                break;
        }
        listGallery.setArguments(args);

        return listGallery;
    }
}
