package com.santos.generic.Fragmentos;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.generic.Adapters.AdaptadorCursos;
import com.santos.generic.Adapters.AdaptadorTareas;
import com.santos.generic.R;
import com.santos.generic.Utils.Persistence.TareaRepository;
import com.santos.generic.Utils.SharedPrefences.PreferenceHelperDemo;
import com.santos.generic.Utils.TasksG;

import java.util.ArrayList;

import static com.santos.firestoremeth.Nodos.IDENTIFICADOR_USUARIO;
import static com.santos.firestoremeth.Nodos.NODO_CURSOS;


public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";
    private RecyclerView mRecyclerViewConverciones;
    private RecyclerView mRecyclerViewNotas;
    private ArrayList<Cursos> mCursos = new ArrayList<>();

    //Variables
    private DocumentSnapshot mLastQueriedDocument;
    //    private RotateLoading mRotateLoading;
    private AdaptadorCursos mAdaptadorNotas;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore db;
    private CollectionReference notesCollectionRef;
    private TextView mTextViewTaras;
    private TextView mTextViewCursos;

    private AdaptadorTareas mAdaptadorTareas;
    private ArrayList<TasksG> mTasksGS = new ArrayList<>();
    private TareaRepository mTareaRepository;

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
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        mTextViewTaras = view.findViewById(R.id.mtv_Tareas);
        mTextViewCursos = view.findViewById(R.id.textView12);

//        mRotateLoading = view.findViewById(R.id.rotateloading);
        if (PreferenceHelperDemo.getSharedPreferenceBoolean(getContext(), getString(R.string.cursos_dash), false)) {
            mRecyclerViewConverciones = view.findViewById(R.id.recyclergenerico);
            mTextViewCursos.setVisibility(View.VISIBLE);
            getDocumentsInf();
            initRecyclerView();
        }
        if (PreferenceHelperDemo.getSharedPreferenceBoolean(getContext(), getString(R.string.tareas_dash), false)) {
            mTareaRepository = new TareaRepository(getContext());
            mRecyclerViewNotas = view.findViewById(R.id.recyclergenerico_tareas);
            mTextViewTaras.setVisibility(View.VISIBLE);
            initRecyclerViewTareas();
            retrieveTareas();
        }


        return view;
    }

    private void initRecyclerViewTareas() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerViewNotas.setLayoutManager(layoutManager);
        mAdaptadorTareas = new AdaptadorTareas(getContext(), mTasksGS, false);
        mRecyclerViewNotas.setAdapter(mAdaptadorTareas);
    }

    private void retrieveTareas() {
        mTareaRepository.retriveTasks().observe(this, tasksGS -> {
            if (mTasksGS.size() > 0) {
                mTasksGS.clear();
            }

            if (mTasksGS != null) {
                mTasksGS.addAll(tasksGS);
            }
            mAdaptadorTareas.notifyDataSetChanged();
        });
    }


    private void getDocumentsInf() {
        notesCollectionRef = db.collection(NODO_CURSOS);

        Query notesQuery;
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
                    Log.d(TAG, "getDocumentsInf: Entro");
                    mCursos.add(cursos);

                }

                if (mCursos.size() == 0) {
                    //mTextViewNoDatos.setVisibility(View.VISIBLE);
                }

                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }

//                mRotateLoading.stop();
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

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerViewConverciones.setLayoutManager(layoutManager);
        mRecyclerViewConverciones.setAdapter(mAdaptadorNotas);
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
