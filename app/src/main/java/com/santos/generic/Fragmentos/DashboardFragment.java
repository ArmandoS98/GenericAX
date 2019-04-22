package com.santos.generic.Fragmentos;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Adapters.AdaptadorCursos;
import com.santos.generic.Adapters.AdaptadorNotas;
import com.santos.generic.R;
import com.santos.generic.Utils.SharedPrefences.PreferenceHelperDemo;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import static com.santos.firestoremeth.Nodos.IDENTIFICADOR_USUARIO;
import static com.santos.firestoremeth.Nodos.NODO_CURSOS;
import static com.santos.firestoremeth.Nodos.NODO_NOTAS;


public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";
    private RecyclerView mRecyclerViewConverciones;
    private RecyclerView mRecyclerViewNotas;
    private ArrayList<Cursos> mCursos = new ArrayList<>();
    private ArrayList<Notas> mNotas = new ArrayList<>();

    //Variables
    private DocumentSnapshot mLastQueriedDocument;
    private RotateLoading mRotateLoading;
    private AdaptadorCursos mAdaptadorNotas;
    private AdaptadorNotas mAdaptadorNotasReal;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore db;
    private CollectionReference notesCollectionRef;

    private Handler handler = new Handler();

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mRotateLoading = view.findViewById(R.id.rotateloading);
        if (PreferenceHelperDemo.getSharedPreferenceBoolean(getContext(), getString(R.string.cursos_dash), false)) {
          //  mRotateLoading.start();
            handler.postDelayed(() -> {
                mRecyclerViewConverciones = view.findViewById(R.id.recyclergenerico);
                mRecyclerViewNotas = view.findViewById(R.id.recyclergenerico2);
                //getDocumentsInf();
                //initRecyclerView();
                //initRecyclerViewNotas();
            }, 100);

        }




        return view;
    }

    private void getNotasInf() {
        if (mCursos.size() > 0) {
            db = FirebaseFirestore.getInstance();

            for (int i = 0; i < mCursos.size(); i++) {
                notesCollectionRef = db.collection(NODO_CURSOS)
                        .document(mCursos.get(i).getId_curso())
                        .collection(NODO_NOTAS);

                Query notesQuery = null;
                if (mLastQueriedDocument != null) {
                    notesQuery = notesCollectionRef
                            .whereEqualTo("key", "1")
                            .startAfter(mLastQueriedDocument);
                } else {
                    notesQuery = notesCollectionRef
                            .whereEqualTo("key", "1");
                }


                notesQuery.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        //alumnos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Notas notas = document.toObject(Notas.class);
                            mNotas.add(notas);
                        }

                        if (mNotas.size() == 0) {
                            //mTextViewNoDatos.setVisibility(View.VISIBLE);
                        }

                        if (task.getResult().size() != 0) {
                            mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        }

                        mRotateLoading.stop();
                        mAdaptadorNotasReal.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void getDocumentsInf() {
        db = FirebaseFirestore.getInstance();

        notesCollectionRef = db.collection(NODO_CURSOS);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo(IDENTIFICADOR_USUARIO, mFirebaseUser.getUid())
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo(IDENTIFICADOR_USUARIO, mFirebaseUser.getUid());
        }


        notesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mCursos.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Cursos cursos = document.toObject(Cursos.class);
                    mCursos.add(cursos);

                }

                if (mCursos.size() == 0) {
                    //mTextViewNoDatos.setVisibility(View.VISIBLE);
                }

                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }

                mRotateLoading.stop();
                mAdaptadorNotas.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Este metodo inicia el recycler view con sus componentes
    private void initRecyclerView() {
        if (mAdaptadorNotas == null) {
            mAdaptadorNotas = new AdaptadorCursos(getContext(), mCursos);
        }

        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerViewConverciones.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewConverciones.setAdapter(mAdaptadorNotas);
    }

    private void initRecyclerViewNotas() {
        if (mAdaptadorNotasReal == null) {
            mAdaptadorNotasReal = new AdaptadorNotas(getContext(), mNotas);
        }

        mRecyclerViewNotas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerViewNotas.setAdapter(mAdaptadorNotasReal);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shr_toolbar_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                getDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.item_pin_input, (ViewGroup) getView(), false);

        final CheckBox mCheckBoxCursos = viewInflated.findViewById(R.id.chk_cursos);
        final CheckBox mCheckBoxPendientes = viewInflated.findViewById(R.id.chk_notas);
        final CheckBox mCheckBoxTareas = viewInflated.findViewById(R.id.chk_tareas);
        builder.setView(viewInflated);

        if (PreferenceHelperDemo.getSharedPreferenceBoolean(getContext(), getString(R.string.cursos_dash), false)) {
            mCheckBoxCursos.setChecked(true);
        }
        if (PreferenceHelperDemo.getSharedPreferenceBoolean(getContext(), getString(R.string.notas_dash), false)) {
            mCheckBoxPendientes.setChecked(true);
        }
        if (PreferenceHelperDemo.getSharedPreferenceBoolean(getContext(), getString(R.string.tareas_dash), false)) {
            mCheckBoxTareas.setChecked(true);
        }


        mCheckBoxCursos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mCheckBoxCursos.isChecked()) {
                PreferenceHelperDemo.setSharedPreferenceBoolean(getContext(), getString(R.string.cursos_dash), true);
            } else {
                PreferenceHelperDemo.setSharedPreferenceBoolean(getContext(), getString(R.string.cursos_dash), false);
            }
        });

        mCheckBoxPendientes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mCheckBoxPendientes.isChecked()) {
                PreferenceHelperDemo.setSharedPreferenceBoolean(getContext(), getString(R.string.notas_dash), true);
            } else {
                PreferenceHelperDemo.setSharedPreferenceBoolean(getContext(), getString(R.string.notas_dash), false);
            }
        });

        mCheckBoxTareas.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mCheckBoxTareas.isChecked()) {
                PreferenceHelperDemo.setSharedPreferenceBoolean(getContext(), getString(R.string.tareas_dash), true);
            } else {
                PreferenceHelperDemo.setSharedPreferenceBoolean(getContext(), getString(R.string.tareas_dash), false);
            }
        });


        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
