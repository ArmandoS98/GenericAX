package com.santos.generic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    public AdaptadorTareas(Context context, ArrayList<TasksG> tasksGS) {
        this.context = context;
        this.tasksGS = tasksGS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tareas, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTextViewTitulo.setText(tasksGS.get(i).getTitulo());
        viewHolder.mTextViewContendio.setText(tasksGS.get(i).getDetalle());
        if (tasksGS.get(i).getTimestamp() != null) {
            SimpleDateFormat spf = new SimpleDateFormat("MMM dd");
            String date = spf.format(getDateFromString(tasksGS.get(i).getTimestamp()));
            viewHolder.mTextViewfecha.setText(date);
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitulo = itemView.findViewById(R.id.tv_titulo);
            mTextViewContendio = itemView.findViewById(R.id.tv_contenido);
            mTextViewfecha = itemView.findViewById(R.id.tv_view_fecha);
            mTextViewProgreso = itemView.findViewById(R.id.tv_completado);

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
