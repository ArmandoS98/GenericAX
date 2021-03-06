package com.santos.generic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.santos.firestoremeth.Logica.LNota;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Interfaz.IDatos;
import com.santos.generic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorNotasMaterial extends RecyclerView.Adapter<AdaptadorNotasMaterial.ViewHolder> {
    private static final String TAG = "AdaptadorMaestrosComple";
    private Context mContext;
    private ArrayList<LNota> alumnos = new ArrayList<>();
    private IDatos mIDatos;
    private int mSelectedNoteIndex;

    public AdaptadorNotasMaterial(Context mContext, ArrayList<LNota> alumnos) {
        this.mContext = mContext;
        this.alumnos = alumnos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_dash_notas, parent, false);
        //itemView = LayoutInflater.from(mContext).inflate(R.layout.item_dash_notas, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //obtenemos la foto de perfil
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_error_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(mContext)
                .load(alumnos.get(position).getNotas().getUrl_foto())
                .apply(options)
                .into(viewHolder.mImageView);

        viewHolder.mteTextViewDescripcion.setText(alumnos.get(position).getNotas().getDescripcionNota());
        viewHolder.mTextViewTitulo.setText(alumnos.get(position).getNotas().getTituloNota());
    }


    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mIDatos = (IDatos) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTextViewTitulo;
        private TextView mteTextViewDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.product_image);
            mTextViewTitulo = itemView.findViewById(R.id.product_title);
            mteTextViewDescripcion = itemView.findViewById(R.id.product_price);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            mIDatos.onNotaSeleccionada(alumnos.get(getAdapterPosition()));
        }
    }
}
