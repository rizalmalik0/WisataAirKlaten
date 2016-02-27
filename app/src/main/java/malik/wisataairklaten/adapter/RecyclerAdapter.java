package malik.wisataairklaten.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import malik.wisataairklaten.R;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.Fasilitas;
import malik.wisataairklaten.model.Foto;
import malik.wisataairklaten.model.Review;
import malik.wisataairklaten.model.Wisata;

/**
 * Created by Rizal Malik on 01/02/2016.
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<T> listData;
    Context mContext;
    int type;

    boolean isFooterVisible = true;

    public static final int TIPE_WISATA = 1;
    public static final int TIPE_FOTO = 2;
    public static final int TIPE_FASILITAS = 3;
    public static final int TIPE_REVIEW = 4;
    public static final int TIPE_FOOTER = 5;

    public RecyclerAdapter(Context mContext, List<T> listData, int type) {
        this.listData = listData;
        this.mContext = mContext;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType != TIPE_FOOTER) {
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
                case TIPE_REVIEW:
                    v = LayoutInflater.from(mContext).inflate(R.layout.recycler_review_item, parent, false);
                    ReviewHolder reviewHolder = new ReviewHolder(v);

                    return reviewHolder;
                default:
                    return null;
            }
        }

        v = LayoutInflater.from(mContext).inflate(R.layout.item_footer, parent, false);
        FooterHolder footerHolder = new FooterHolder(v);

        return footerHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != listData.size()) {
            switch (type) {
                case TIPE_WISATA:
                    WisataHolder wisataHolder = (WisataHolder) holder;
                    Wisata w = (Wisata) listData.get(position);

                    wisataHolder.txtNama.setText(w.getNama_wisata());
                    wisataHolder.rbWisata.setRating(w.getRating());
                    Picasso.with(mContext).load("file:///android_asset/gambar/" + w.getFoto()).fit().centerCrop().into(wisataHolder.imgItem);
                    break;
                case TIPE_FOTO:
                    FotoHolder fotoHolder = (FotoHolder) holder;
                    Foto p = (Foto) listData.get(position);

                    switch (p.getId_foto()) {
                        case -1:
                            fotoHolder.layoutUpload.setVisibility(View.VISIBLE);
                            fotoHolder.pbUpload.setVisibility(View.VISIBLE);
                            Picasso.with(mContext).load(Uri.parse(p.getNama_foto())).placeholder(R.color.backWhite).fit().centerCrop().into(fotoHolder.imgItem);
                            break;
                        case 0:
                            fotoHolder.layoutUpload.setVisibility(View.GONE);
                            fotoHolder.pbUpload.setVisibility(View.GONE);
                            Picasso.with(mContext).load(R.drawable.add_foto).placeholder(R.color.backWhite).fit().into(fotoHolder.imgItem);
                            break;
                        default:
                            Picasso.with(mContext).load(VariableGlobal.URL_GAMBAR + p.getUser().getId_user() + "/" + p.getNama_foto()).placeholder(R.color.backWhite).fit().centerCrop().into(fotoHolder.imgItem);
                            break;
                    }
                    break;
                case TIPE_FASILITAS:
                    FasilitasHolder fasilitasHolder = (FasilitasHolder) holder;
                    Fasilitas f = (Fasilitas) listData.get(position);

                    if (position == 0) {
                        fasilitasHolder.txtItem.setText(f.getGambar());
                        Picasso.with(mContext).load("file:///android_asset/icon/background.png").fit().into(fasilitasHolder.imgItem);
                    } else {
                        Picasso.with(mContext).load("file:///android_asset/icon/" + f.getGambar()).fit().into(fasilitasHolder.imgItem);
                    }

                    break;
                case TIPE_REVIEW:
                    ReviewHolder reviewHolder = (ReviewHolder) holder;
                    Review r = (Review) listData.get(position);

                    if (r.getIsi().trim().equals("")) {
                        reviewHolder.layoutIsi.setVisibility(View.GONE);
                    } else {
                        reviewHolder.layoutIsi.setVisibility(View.VISIBLE);
                        reviewHolder.txtIsi.setText(r.getIsi());
                    }
                    reviewHolder.rbReview.setRating(r.getRating());
                    reviewHolder.txtNama.setText(r.getUser().getUsername());
                    break;
            }
        } else {
            FooterHolder footerHolder = (FooterHolder) holder;
            if (isFooterVisible)
                footerHolder.pbFooter.setVisibility(View.VISIBLE);
            else
                footerHolder.pbFooter.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (type == TIPE_WISATA || type == TIPE_FASILITAS || listData.size() == 0)
            return listData.size();

        return listData.size() + 1;
    }

    public class WisataHolder extends RecyclerView.ViewHolder {
        TextView txtNama;
        ImageView imgItem;
        RatingBar rbWisata;

        public WisataHolder(View itemView) {
            super(itemView);

            txtNama = (TextView) itemView.findViewById(R.id.txtNamaWisata);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
            rbWisata = (RatingBar) itemView.findViewById(R.id.rbWisata);
        }
    }

    public class FotoHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        Button btnUpload;
        RelativeLayout layoutUpload;
        ProgressBar pbUpload;

        public FotoHolder(View itemView) {
            super(itemView);

            layoutUpload = (RelativeLayout) itemView.findViewById(R.id.layout_upload);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            pbUpload = (ProgressBar) itemView.findViewById(R.id.pbUpload);
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

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView txtNama, txtIsi;
        RatingBar rbReview;
        LinearLayout layoutIsi;

        public ReviewHolder(View itemView) {
            super(itemView);

            txtNama = (TextView) itemView.findViewById(R.id.txtNamaUser);
            txtIsi = (TextView) itemView.findViewById(R.id.txtIsiReview);
            rbReview = (RatingBar) itemView.findViewById(R.id.rbReview);
            layoutIsi = (LinearLayout) itemView.findViewById(R.id.layout_isi_review);

        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        ProgressBar pbFooter;

        public FooterHolder(View itemView) {
            super(itemView);

            pbFooter = (ProgressBar) itemView.findViewById(R.id.pbFooter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == listData.size())
            return TIPE_FOOTER;

        return super.getItemViewType(position);
    }

    public void setFooter(boolean isVisible) {
        isFooterVisible = isVisible;
    }

    public void getView(int position) {

    }
}
