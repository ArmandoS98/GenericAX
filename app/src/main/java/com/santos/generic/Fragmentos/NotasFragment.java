package com.santos.generic.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.santos.firestoremeth.FirebaseMethods;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Adapters.AdaptadorNotas;
import com.santos.generic.R;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.santos.firestoremeth.Nodos.NODO_CURSOS;
import static com.santos.firestoremeth.Nodos.NODO_NOTAS;
import static com.santos.firestoremeth.Nodos.PARAMETRO_KEY;
import static com.santos.firestoremeth.Nodos.PARAMETRO_VALOR;
import static com.santos.generic.Activities.TabActivity.id_docuento;

public class NotasFragment extends Fragment {
    private static final String TAG = "NotasFragment";
    //FirebaseMethods
    private RecyclerView recyclergenerico;

    //Variables
    private ArrayList<Notas> alumnos = new ArrayList<>();
    private DocumentSnapshot mLastQueriedDocument;
    private FirebaseMethods firebaseMethods;
    private AdaptadorNotas mAdaptadorNotas;
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
        recyclergenerico = view.findViewById(R.id.recyclergenerico);
        mRotateLoading = view.findViewById(R.id.rotateloading);

        mRotateLoading.start();

        getAlumnos();
        initRecyclerView();
        return view;
    }

    private void getAlumnos() {
        db = FirebaseFirestore.getInstance();

        notesCollectionRef = db.collection(NODO_CURSOS)
                .document(id_docuento)
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

                alumnos.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Notas alumno = document.toObject(Notas.class);
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
            mAdaptadorNotas = new AdaptadorNotas(getContext(), alumnos);
        }

        recyclergenerico.setLayoutManager(new LinearLayoutManager(getContext()));
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        recyclergenerico.setAdapter(mAdaptadorNotas);
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nueva_nota, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                //getDialog();
                break;
            case R.id.setting:
                //getDialog();
                break;
            case R.id.action_delete_curso:
                //getDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        db.collection(NODO_CURSOS)
                .document(id_docuento)
                .collection(NODO_NOTAS)
                .whereEqualTo(PARAMETRO_KEY, PARAMETRO_VALOR)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.d(TAG, "onEvent: llego");
                        return;
                    }

                    if (alumnos.size() == 0) {
                        alumnos.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Notas grado = doc.toObject(Notas.class);
                            alumnos.add(grado);
                        }
                    } else {
                        alumnos.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Notas grado = doc.toObject(Notas.class);
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
