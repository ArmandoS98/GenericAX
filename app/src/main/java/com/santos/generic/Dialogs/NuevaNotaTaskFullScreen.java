package com.santos.generic.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.santos.firestoremeth.Models.Cuestionario;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Interfaz.IMainMaestro;
import com.santos.generic.R;

public class NuevaNotaTaskFullScreen extends DialogFragment implements View.OnClickListener {

    private Notas notas;
    private Cuestionario cuestionario;
    private EditText mEditTextTitulo;
    private EditText mEditTextContent;
    //private TextView mTextViewSave;
    private IMainMaestro iMainMaestro;
    private Toolbar toolbar;

    public static NuevaNotaTaskFullScreen newInstance(Notas notas) {
        NuevaNotaTaskFullScreen dialog = new NuevaNotaTaskFullScreen();

        Bundle args = new Bundle();
        args.putParcelable("notas", notas);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        setHasOptionsMenu(true);
        //notas = getArguments().getParcelable("notas");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nueva_notas_task, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(R.string.nueva_tarea_tittle);

        //mTextViewSave = view.findViewById(R.id.save);

        mEditTextTitulo = view.findViewById(R.id.et_titulo);
        mEditTextContent = view.findViewById(R.id.et_descripcion);

        //mTextViewSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.inflateMenu(R.menu.nueva_tarea);
        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iMainMaestro = (IMainMaestro) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /*case R.id.save: {

                String title = mEditTextTitulo.getText().toString();
                String content = mEditTextContent.getText().toString();

                if (!title.equals("")) {
                    iMainMaestro.onNuevoCuestionario(title, content);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }*/

        }
    }

}
