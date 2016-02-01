package malik.wisataairklaten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import malik.wisataairklaten.R;
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
                v = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
                WisataHolder wisataHolder = new WisataHolder(v);

                return wisataHolder;
            case TIPE_FOTO:
                v = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
                FotoHolder fotoHolder = new FotoHolder(v);

                return fotoHolder;
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
                wisataHolder.txtFasilitas.setText(w.getFasilitas());
                break;
            case TIPE_FOTO:
                FotoHolder fotoHolder = (FotoHolder) holder;
                Foto f = (Foto) listData.get(position);

                break;
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class WisataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNama, txtFasilitas;

        public WisataHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            txtNama = (TextView) itemView.findViewById(R.id.txtNamaWisata);
            txtFasilitas = (TextView) itemView.findViewById(R.id.txtFasilitas);
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(v.getContext(), "position = " + ((Wisata) listData.get(getPosition())).getNama_wisata(), Toast.LENGTH_SHORT).show();
        }
    }

    public class FotoHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;

        public FotoHolder(View itemView) {
            super(itemView);

            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
        }
    }
}
