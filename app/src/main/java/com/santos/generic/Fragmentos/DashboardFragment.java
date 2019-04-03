package com.santos.generic.Fragmentos;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Adapters.AdaptadorCursos;
import com.santos.generic.Adapters.AdaptadorNotas;
import com.santos.generic.R;
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
    private CollectionReference mCollectionReferenceNotas;

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
        //mFirebaseMethods = new FirebaseMethods(getContext(), NODO_CURSOS);
        mRotateLoading = view.findViewById(R.id.rotateloading);
        mRotateLoading.start();

        handler.postDelayed(() -> {
            mRecyclerViewConverciones = view.findViewById(R.id.recyclergenerico);
            mRecyclerViewNotas = view.findViewById(R.id.recyclergenerico2);
            getDocumentsInf();
            initRecyclerView();
            initRecyclerViewNotas();
        }, 100);
        return view;
    }

    private void getNotasInf() {
        if (mCursos.size() > 0){
            db = FirebaseFirestore.getInstance();

            for (int i = 0; i < mCursos.size(); i++){
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
                        }else{
                            Toast.makeText(getContext(), "Si trae datos", Toast.LENGTH_SHORT).show();
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

                    //Seccion de busqueda de clases
                    /*mCollectionReferenceNotas = db.collection(NODO_CURSOS)
                            .document(cursos.getId_curso())
                            .collection(NODO_NOTAS);

                    Query notesQueryNotas = null;
                    if (mLastQueriedDocument != null) {
                        notesQueryNotas = mCollectionReferenceNotas
                                .whereEqualTo("key", "1")
                                .startAfter(mLastQueriedDocument);
                    } else {
                        notesQueryNotas = mCollectionReferenceNotas
                                .whereEqualTo("key", "1");
                    }


                    notesQueryNotas.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {

                            //alumnos.clear();
                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                Notas alumno = document1.toObject(Notas.class);
                                mNotas.add(alumno);
                            }

                            Log.d(TAG, "getDocumentsInf: " + mNotas);

                            if (mNotas.size() == 0) {
                                Toast.makeText(getContext(), "Vacio", Toast.LENGTH_SHORT).show();
                            }

                            if (task1.getResult().size() != 0) {
                                mLastQueriedDocument = task1.getResult().getDocuments().get(task1.getResult().size() - 1);
                            }

                            mRotateLoading.stop();
                            mAdaptadorNotas.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }



                if (mCursos.size() == 0) {
                    //   mTextViewNoDatos.setVisibility(View.VISIBLE);
                }else{
                    getNotasInf();
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
        if (mAdaptadorNotasReal== null) {
            mAdaptadorNotasReal = new AdaptadorNotas(getContext(), mNotas);
        }

        mRecyclerViewNotas.setLayoutManager(new LinearLayoutManager(getContext()));
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerViewNotas.setAdapter(mAdaptadorNotasReal);
    }
}
