package malik.wisataairklaten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import malik.wisataairklaten.R;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.Foto;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 01/02/2016.
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<T> listData;
    Context mContext;
    int type;

    public static final int TIPE_WISATA = 1;
    public static final int TIPE_FOTO = 2;
    public static final int TIPE_FASILITAS = 3;

    public RecyclerAdapter(Context mContext, List<T> listData, int type) {
        this.listData = listData;
        this.mContext = mContext;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (type) {
            case TIPE_WISATA:
                v = LayoutInflater.from(mContext).inflate(R.layout.recycler_wisata_item, parent, false);
                WisataHolder wisataHolder = new WisataHolder(v);

                return wisataHolder;
            case TIPE_FOTO:
                v = LayoutInflater.from(mContext).inflate(R.layout.recycler_foto_item, parent, false);
                FotoHolder fotoHolder = new FotoHolder(v);

                return fotoHolder;
            case TIPE_FASILITAS:
                v = LayoutInflater.from(mContext).inflate(R.layout.recycler_fasilitas_item, parent, false);
                FasilitasHolder fasilitasHolder = new FasilitasHolder(v);

                return fasilitasHolder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (type) {
            case TIPE_WISATA:
                WisataHolder wisataHolder = (WisataHolder) holder;
                Wisata w = (Wisata) listData.get(position);

                wisataHolder.txtNama.setText(w.getNama_wisata());

                Picasso.with(mContext).load("file:///android_asset/gambar/" + w.getFoto()).fit().centerCrop().into(wisataHolder.imgItem);
                break;
            case TIPE_FOTO:
                FotoHolder fotoHolder = (FotoHolder) holder;
                Foto f = (Foto) listData.get(position);

                Picasso.with(mContext).load(VariableGlobal.URL_GAMBAR + f.getNama_foto()).fit().centerCrop().into(fotoHolder.imgItem);

                break;
            case TIPE_FASILITAS:
                FasilitasHolder fasilitasHolder = (FasilitasHolder) holder;
                String s = (String) listData.get(position);

                if (position == 0) {
                    fasilitasHolder.txtItem.setText(s);
                    Picasso.with(mContext).load("file:///android_asset/icon/background.png").fit().into(fasilitasHolder.imgItem);
                } else {
                    Picasso.with(mContext).load("file:///android_asset/icon/" + s + ".png").error(R.drawable.fasilitas).fit().into(fasilitasHolder.imgItem);
                }


                break;
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class WisataHolder extends RecyclerView.ViewHolder {
        TextView txtNama;
        ImageView imgItem;

        public WisataHolder(View itemView) {
            super(itemView);

            txtNama = (TextView) itemView.findViewById(R.id.txtNamaWisata);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
        }
    }

    public class FotoHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;

        public FotoHolder(View itemView) {
            super(itemView);

            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
        }
    }

    public class FasilitasHolder extends RecyclerView.ViewHolder {
        TextView txtItem;
        ImageView imgItem;

        public FasilitasHolder(View itemView) {
            super(itemView);

            txtItem = (TextView) itemView.findViewById(R.id.txtItem);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
        }
    }
}
