package com.santos.generic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santos.firestoremeth.Models.Cuestionario;
import com.santos.generic.R;

import java.util.ArrayList;

public class AdaptadorCuestionario extends RecyclerView.Adapter<AdaptadorCuestionario.ViewHolder> {
    private static final String TAG = "AdaptadorCuestionario";
    private Context context;
    private ArrayList<Cuestionario> cuestionarios = new ArrayList<>();

    public AdaptadorCuestionario(Context context, ArrayList<Cuestionario> cuestionarios) {
        this.context = context;
        this.cuestionarios = cuestionarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuestionario, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if (viewHolder != null) {
            viewHolder.mTextViewPregunta.setText(cuestionarios.get(i).getPregunta());
            viewHolder.mEditTextRespuesta.setHint(cuestionarios.get(i).getRespuesta_txt());
        }
    }

    @Override
    public int getItemCount() {
        return cuestionarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewPregunta;
        private EditText mEditTextRespuesta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewPregunta = itemView.findViewById(R.id.tv_titulo);
            mEditTextRespuesta = itemView.findViewById(R.id.et_content);
        }
    }
}
