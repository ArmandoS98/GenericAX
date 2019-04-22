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
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Interfaz.IDatos;
import com.santos.generic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorNotas extends RecyclerView.Adapter<AdaptadorNotas.ViewHolder> {
    private static final String TAG = "AdaptadorMaestrosComple";
    private Context mContext;
    private ArrayList<Notas> alumnos = new ArrayList<>();
    private IDatos mIDatos;
    private int mSelectedNoteIndex;

    public AdaptadorNotas(Context mContext, ArrayList<Notas> alumnos) {
        this.mContext = mContext;
        this.alumnos = alumnos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_alumnos_layout, parent, false);
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
                .load(alumnos.get(position).getUrl_foto())
                .apply(options)
                .into(viewHolder.mImageView);

        Glide.with(mContext)
                .load(alumnos.get(position).getUserPhoto())
                .apply(options)
                .into(viewHolder.mCircleImageViewPerfil);

        viewHolder.mteTextViewDescripcion.setText(alumnos.get(position).getDescripcionNota());
        viewHolder.mTextViewTitulo.setText(alumnos.get(position).getTituloNota());

        if (alumnos.get(position).getTimestamp() != null) {
            SimpleDateFormat spf = new SimpleDateFormat("MMM dd");
            String date = spf.format(alumnos.get(position).getTimestamp());
            viewHolder.mTextViewFecha.setText(date);
        }
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
        private CircleImageView mCircleImageViewPerfil;
        private ImageView mImageView;
        private TextView mTextViewTitulo;
        private TextView mteTextViewDescripcion;
        private TextView mTextViewFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.img_ejercicio);
            mCircleImageViewPerfil = itemView.findViewById(R.id.img_user);
            mTextViewTitulo = itemView.findViewById(R.id.titleTextView);
            mteTextViewDescripcion = itemView.findViewById(R.id.tv_descripcion);
            mTextViewFecha = itemView.findViewById(R.id.dateTextView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            mSelectedNoteIndex = getAdapterPosition();
            mIDatos.onNotaSeleccionada(alumnos.get(mSelectedNoteIndex));
        }
    }

    public void updateList(ArrayList<Notas> newLista) {
        alumnos = new ArrayList<>();
        alumnos.addAll(newLista);
        notifyDataSetChanged();
    }

}
