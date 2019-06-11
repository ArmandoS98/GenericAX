package com.santos.generic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.santos.generic.Interfaz.IDatos;
import com.santos.generic.R;
import com.santos.generic.Utils.TasksG;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder> {

    private Context context;
    private ArrayList<TasksG> tasksGS;
    private IDatos mIDatos;
    private boolean flag = false;

    public AdaptadorTareas(Context context, ArrayList<TasksG> tasksGS, boolean flag) {
        this.context = context;
        this.tasksGS = tasksGS;
        this.flag = flag;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (flag) {
            view = LayoutInflater.from(context).inflate(R.layout.item_tareas, viewGroup, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_tareas_short, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (flag) {
            viewHolder.mTextViewTitulo.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fate_transition_animation));
            viewHolder.mCardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fate_scale_animation));
            viewHolder.mTextViewTitulo.setText(tasksGS.get(i).getTitulo());
            viewHolder.mTextViewContendio.setText(tasksGS.get(i).getDetalle());
            if (tasksGS.get(i).getTimestamp() != null) {
                SimpleDateFormat spf = new SimpleDateFormat("MMM dd");
                String date = spf.format(getDateFromString(tasksGS.get(i).getTimestamp()));
                viewHolder.mTextViewfecha.setText(date);
            }
        } else {
            viewHolder.mCardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fate_scale_animation));
            viewHolder.mTextViewTitulo.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fate_transition_animation));
            viewHolder.mTextViewTitulo.setText(tasksGS.get(i).getTitulo());
            if (tasksGS.get(i).getTimestamp() != null) {
                SimpleDateFormat spf = new SimpleDateFormat("MMM dd");
                String date = spf.format(getDateFromString(tasksGS.get(i).getTimestamp()));
                viewHolder.mTextViewfecha.setText(date);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tasksGS.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mIDatos = (IDatos) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewTitulo;
        private TextView mTextViewContendio;
        private TextView mTextViewfecha;
        private TextView mTextViewProgreso;
        private CardView mCardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (flag) {
                mTextViewTitulo = itemView.findViewById(R.id.tv_titulo);
                mTextViewContendio = itemView.findViewById(R.id.tv_contenido);
                mTextViewfecha = itemView.findViewById(R.id.tv_view_fecha);
                mTextViewProgreso = itemView.findViewById(R.id.tv_completado);
                mCardView = itemView.findViewById(R.id.card_view);
            } else {
                mTextViewTitulo = itemView.findViewById(R.id.tv_titulo);
                mTextViewfecha = itemView.findViewById(R.id.tv_view_fecha);
                mCardView = itemView.findViewById(R.id.card_view);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mIDatos.onSelectTarea(tasksGS.get(getAdapterPosition()));
        }
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private Date getDateFromString(String datetoSaved) {

        try {
            return format.parse(datetoSaved);
        } catch (ParseException e) {
            return null;
        }

    }
}
