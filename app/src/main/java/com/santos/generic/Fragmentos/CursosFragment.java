package com.santos.generic.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.santos.firestoremeth.FirebaseMethods;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Persistencia.UsuarioDAO;
import com.santos.generic.Activities.NuevoCursooActivity;
import com.santos.generic.Adapters.AdaptadorCursos;
import com.santos.generic.R;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static com.santos.firestoremeth.Nodos.IDENTIFICADOR_USUARIO;
import static com.santos.firestoremeth.Nodos.NODO_CURSOS;


public class CursosFragment extends Fragment {

    private static final String TAG = "CursosFragment";
    private RecyclerView mRecyclerViewConverciones;
    private ArrayList<Cursos> mCursos = new ArrayList<>();

    //Variables
    private DocumentSnapshot mLastQueriedDocument;
    private RotateLoading mRotateLoading;
    private AdaptadorCursos mAdaptadorNotas;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore db;
    private CollectionReference notesCollectionRef;

    private Handler handler = new Handler();


    //TODO, test de cerebro
    private FirebaseMethods mFirebaseMethods;

    public CursosFragment() {
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
        View view = inflater.inflate(R.layout.fragment_cursos, container, false);
        //mFirebaseMethods = new FirebaseMethods(getContext(), NODO_CURSOS);
        db = FirebaseFirestore.getInstance();

        mRotateLoading = view.findViewById(R.id.rotateloading);
        mRotateLoading.start();

        handler.postDelayed(() -> {
            mRecyclerViewConverciones = view.findViewById(R.id.recyclergenerico);
            getDocumentsInf();
            initRecyclerView();
        }, 100);
        return view;
    }

    private void getDocumentsInf() {


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
                    //   mTextViewNoDatos.setVisibility(View.VISIBLE);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nuevo_curso, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nuevo_curso:
                startActivity(new Intent(getContext(), NuevoCursooActivity.class));

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        db.collection(NODO_CURSOS)
                .whereEqualTo(IDENTIFICADOR_USUARIO, UsuarioDAO.getInstancia().getKeyUsuario())
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.d(TAG, "onEvent: llego");
                        return;
                    }

                    if (mCursos.size() == 0) {
                        mCursos.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Cursos cursos = doc.toObject(Cursos.class);
                            mCursos.add(cursos);
                        }
                    } else {
                        mCursos.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Cursos cursos = doc.toObject(Cursos.class);
                            mCursos.add(cursos);
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
       /* if (listenerRegistration != null) {
            listenerRegistration.remove();
        }*/
        super.onStop();
    }
}
