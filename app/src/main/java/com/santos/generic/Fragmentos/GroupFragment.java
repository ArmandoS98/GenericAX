package com.santos.generic.Fragmentos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.santos.firestoremeth.FirebaseMethods;
import com.santos.firestoremeth.Models.TareasG;
import com.santos.generic.R;

import java.util.Arrays;
import java.util.List;

import static com.santos.firestoremeth.Nodos.NODO_CURSOS;
import static com.santos.firestoremeth.Nodos.NODO_TAREAS_GRUPALES;


public class GroupFragment extends Fragment implements View.OnClickListener {

    private EditText mEditText;
    private FirebaseMethods firebaseMethods;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        firebaseMethods = new FirebaseMethods(getContext(), NODO_TAREAS_GRUPALES);
        mEditText = view.findViewById(R.id.et_datos);
        view.findViewById(R.id.btn_guardar).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        String inputs = mEditText.getText().toString();
        String[] toArray = inputs.split("\\s*,\\s*");
        List<String> tags = Arrays.asList(toArray);


        firebaseMethods.crearTareaGrupal(tags, "Programacion", "hola", "hola2");
    }
}
