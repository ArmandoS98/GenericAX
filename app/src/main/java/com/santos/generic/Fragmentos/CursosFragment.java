package com.santos.generic.Fragmentos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.santos.firestoremeth.FirebaseMethods;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.generic.Adapters.AdaptadorCursos;
import com.santos.generic.R;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

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
        inflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
