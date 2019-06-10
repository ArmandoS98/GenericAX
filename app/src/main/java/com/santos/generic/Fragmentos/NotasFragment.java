package com.santos.generic.Fragmentos;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.santos.firestoremeth.FirebaseMethods;
import com.santos.firestoremeth.Logica.LNota;
import com.santos.firestoremeth.Models.Notas;
import com.santos.firestoremeth.Persistencia.UsuarioDAO;
import com.santos.generic.Adapters.AdaptadorNotasMaterial;
import com.santos.generic.R;
import com.santos.generic.RecyclerDecoration.NotasDecoracion;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import static com.santos.firestoremeth.Nodos.NODO_CURSOS;
import static com.santos.firestoremeth.Nodos.NODO_NOTAS;
import static com.santos.generic.Activities.TabActivity.id_docuento;

public class NotasFragment extends Fragment {
    private static final String TAG = "NotasFragment";
    //FirebaseMethods
    private RecyclerView recyclergenerico;

    //Variables
    private ArrayList<LNota> alumnos = new ArrayList<>();
    private DocumentSnapshot mLastQueriedDocument;
    private FirebaseMethods firebaseMethods;
    //private AdaptadorNotas mAdaptadorNotas;
    private AdaptadorNotasMaterial mAdaptadorNotas;
    private RotateLoading mRotateLoading;
    private FirebaseFirestore db;
    private CollectionReference notesCollectionRef;

    public NotasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notas, container, false);
        db = FirebaseFirestore.getInstance();
        recyclergenerico = view.findViewById(R.id.recyclergenerico);
        mRotateLoading = view.findViewById(R.id.rotateloading);

        mRotateLoading.start();

        getAlumnos();
        initRecyclerView();
        return view;
    }

    private void getAlumnos() {

        notesCollectionRef = db.collection(NODO_CURSOS)
                .document(id_docuento)
                .collection(NODO_NOTAS);

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef;
        }

        notesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                alumnos.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Notas notas = document.toObject(Notas.class);
                    LNota alumno = new LNota(document.getId(), notas);
                    alumnos.add(alumno);
                }

                if (alumnos.size() == 0) {
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
            mAdaptadorNotas = new AdaptadorNotasMaterial(getContext(), alumnos);
        }

        recyclergenerico.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        recyclergenerico.setAdapter(mAdaptadorNotas);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclergenerico.addItemDecoration(new NotasDecoracion(largePadding, smallPadding));
    }

    @Override
    public void onResume() {
        super.onResume();
        db.collection(NODO_CURSOS)
                .document(id_docuento)
                .collection(NODO_NOTAS)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.d(TAG, "onEvent: llego");
                        return;
                    }

                    if (alumnos.size() == 0) {
                        alumnos.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Notas notas = document.toObject(Notas.class);
                            LNota grado = new LNota(document.getId(), notas);
                            alumnos.add(grado);
                        }
                    } else {
                        alumnos.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Notas notas = document.toObject(Notas.class);
                            LNota grado = new LNota(document.getId(), notas);
                            alumnos.add(grado);
                        }
                    }
                    mAdaptadorNotas.notifyDataSetChanged();
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
