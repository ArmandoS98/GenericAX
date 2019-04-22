package com.santos.generic.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.santos.generic.R;
import com.santos.generic.Utils.TasksG;

import static com.santos.generic.Utils.palReb.KEY_NOTAS;

public class TareaViewActivity extends AppCompatActivity {
    private static final String TAG = "TareaViewActivity";
    private TasksG mTasksG = null;

    //Views
    private TextView mTextViewTitulo;
    private TextView mTextViewCurso;
    private TextView mTextViewContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_view);

        initComponents();
        getToolbarSetup();

        Intent i = getIntent();
        mTasksG = i.getParcelableExtra(KEY_NOTAS);
        if (mTasksG != null) {
            mTextViewTitulo.setText(mTasksG.getTitulo());
            mTextViewCurso.setText(mTasksG.getNombre_curso());
            mTextViewContenido.setText(mTasksG.getDetalle());
        }
    }

    private void initComponents() {
        mTextViewTitulo = findViewById(R.id.tv_titulo);
        mTextViewCurso = findViewById(R.id.tv_curso);
        mTextViewContenido = findViewById(R.id.tv_contenido);
    }

    private void getToolbarSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
