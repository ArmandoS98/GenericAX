package com.santos.generic.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.santos.generic.R;
import com.santos.generic.Utils.SQLiteFukes.TasksG;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder> {

    private Context context;
    private ArrayList<TasksG> tasksGS = new ArrayList<>();

    public AdaptadorTareas(Context context, ArrayList<TasksG> tasksGS) {
        this.context = context;
        this.tasksGS = tasksGS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tareas, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewTitulo;
        private TextView mTextViewContendio;
        private TextView mTextViewfecha;
        private TextView mTextViewProgreso;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitulo = itemView.findViewById(R.id.tv_titulo);
            mTextViewContendio = itemView.findViewById(R.id.tv_contenido);
            mTextViewfecha = itemView.findViewById(R.id.tv_view_fecha);
            mTextViewProgreso = itemView.findViewById(R.id.tv_completado);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Hola", Toast.LENGTH_SHORT).show();
        }
    }

    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Date getDateFromString(String datetoSaved) {

        try {
            Date date = format.parse(datetoSaved);
            return date;
        } catch (ParseException e) {
            return null;
        }

    }
}
