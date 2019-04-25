package com.santos.generic.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.santos.firestoremeth.FirebaseMethods;
import com.santos.firestoremeth.Models.ArchivosAniadidos;
import com.santos.firestoremeth.Models.Cuestionario;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Dialogs.NuevaTaskFullScreen;
import com.santos.generic.Fragmentos.NotasFragment;
import com.santos.generic.Fragmentos.TareasFragment;
import com.santos.generic.Interfaz.IDatos;
import com.santos.generic.R;
import com.santos.generic.Utils.Persistence.TareaRepository;
import com.santos.generic.Utils.TasksG;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import yuku.ambilwarna.AmbilWarnaDialog;

import static com.santos.firestoremeth.Nodos.ID_CURSO;
import static com.santos.firestoremeth.Nodos.KEY;
import static com.santos.firestoremeth.Nodos.NODO_CUESTIONARIO;
import static com.santos.firestoremeth.Nodos.NODO_CURSOS;
import static com.santos.firestoremeth.Nodos.NODO_IMAGENES_ANIADIDAS;
import static com.santos.firestoremeth.Nodos.NODO_NOTAS;
import static com.santos.generic.Utils.palReb.KEY_NOTAS;

public class TabActivity extends AppCompatActivity implements IDatos {

    private static final String TAG = "TabActivity";
    public static final String FOTO1 = "https://firebasestorage.googleapis.com/v0/b/trigonometria-1c5cb.appspot.com/o/Imagenes%2Fnoimage.png?alt=media&token=1e1df63e-25ef-42b0-8520-3cd0992799c3";
    private static final int GalleriaPick = 1;

    //Widgets
    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;
    private Dialog epicDialog;

    //FirebaseMethods
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private StorageReference mStorageReference;

    private String url_imagen;
    public static String id_docuento;
    private int currentColor;
    private Cursos cursos = null;
    private Uri mImageUri;
    private Bitmap thumb_bitmap = null;
    private boolean pos = false;
    private ArrayList<Cuestionario> cuestionarios = new ArrayList<>();
    private ArrayList<ArchivosAniadidos> archivosAgregados = new ArrayList<>();
    private CollectionReference notesCollectionRef;
    private ArrayList<Notas> alumnos = new ArrayList<>();
    private DocumentSnapshot mLastQueriedDocument;

    private ViewPagerAdapter adapter;
    private TareaRepository mTareaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        mTareaRepository = new TareaRepository(this);

        epicDialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        currentColor = ContextCompat.getColor(this, R.color.colorAccent);

        Intent i = getIntent();
        cursos = i.getParcelableExtra(KEY_NOTAS);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(cursos.getNombre_curso());

        toolbar.setNavigationOnClickListener(v -> finish());
        if (cursos != null) {

            id_docuento = cursos.getId_curso();
            //getNotasGrado();

            //Donde se carggaran las tabs
            viewPager = findViewById(R.id.htab_viewpager);
            setupViewPager(viewPager);

            TabLayout tabLayout = findViewById(R.id.htab_tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    viewPager.setCurrentItem(tab.getPosition());
                    if (tab.getPosition() != 0) {
                        pos = true;
                    } else {
                        pos = false;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //bundle
        /*Bundle bundle = new Bundle();
        bundle.putStringArrayList("DATE", materias);*/

        /*FormulasFragment tabTareasFragment = new FormulasFragment();

        tabTareasFragment.setArguments(bundle);*/
        //adapter.addFrag(tabTareasFragment, "Notas");
        adapter.addFrag(new NotasFragment(), "Notas");
        adapter.addFrag(new TareasFragment(), "Tareas");
        viewPager.setAdapter(adapter);


    }

    @Override
    public void onNotaSeleccionada(Notas notas) {
        Intent intent = new Intent(this, NotaViewActivity.class);
        intent.putExtra(KEY_NOTAS, notas);
        String date = "";


        if (notas.getTimestamp().toString() != null) {
            SimpleDateFormat spf = new SimpleDateFormat("dd MMM, yyyy, HH:mm aa");
            date = spf.format(notas.getTimestamp());
        } else {
            date = "Hace unos momentos";
        }
        intent.putExtra("date", date);
        intent.putExtra(KEY, id_docuento);
        startActivity(intent);
    }

    @Override
    public void onNotaUpdate(Notas notas) {

    }

    @Override
    public void onCursotoNotaa(Cursos cursos) {

    }

    @Override
    public void onNuevoCuestionario(String titulo, String content) {

    }

    @Override
    public void onNuevaTarea(String... arg) {

        TasksG task = new TasksG();
        task.setTitulo(arg[0]);
        task.setDetalle(arg[1]);
        task.setTimestamp(arg[2]);
        task.setId_curso(cursos.getId_curso());
        task.setNombre_curso(cursos.getNombre_curso());
        task.setTipo("1");

        mTareaRepository.insertTask(task);
    }

    @Override
    public void onSelectTarea(TasksG tasksG) {
        Intent intent = new Intent(this, TareaViewActivity.class);
        intent.putExtra(KEY_NOTAS, tasksG);
        startActivity(intent);
    }

    @Override
    public void onNuevaNota(String... arg) {

    }

    @Override
    public void onSekectNota(Notas notas) {

    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nueva_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                //getDialog();
                break;
            case R.id.setting:
                if (pos) {
                    NuevaTaskFullScreen mNuevaTaskFullScreen = new NuevaTaskFullScreen();
                    mNuevaTaskFullScreen.setCancelable(false);
                    mNuevaTaskFullScreen.show(getSupportFragmentManager(), "Nueva Tarea");
                } else {
                    Intent intent = new Intent(TabActivity.this, NotasActivity.class);
                    intent.putExtra(KEY_NOTAS, id_docuento);
                    startActivity(intent);
                }
                break;
            case R.id.action_delete_curso:
                if (getNotasGrado()) {
                    for (Notas notas : alumnos) {
                        if (deleteNotaData(notas.getIdNota(), notas.getUrl_foto())) {
                            Toast.makeText(this, "EXITO", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else
                    Toast.makeText(this, getString(R.string.mensaje_eleminacion_fallido), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    boolean status = false;

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean getNotasGrado() {

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


                for (int i = 0; i < alumnos.size(); i++) {
                    getCuestionario(alumnos.get(i).getIdNota());
                    getImagenAniadidas(alumnos.get(i).getIdNota());
                }

                status = true;

                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return status;
    }

    private void getCuestionario(String id_nota) {
        db = FirebaseFirestore.getInstance();

        CollectionReference notesCollectionRef = db
                .collection(NODO_CURSOS)
                .document(id_docuento)
                .collection(NODO_NOTAS)
                .document(id_nota)
                .collection(NODO_CUESTIONARIO);

        getData(notesCollectionRef);
    }

    private void getImagenAniadidas(String id_nota) {
        db = FirebaseFirestore.getInstance();

        CollectionReference notesCollectionRef = db
                .collection(NODO_CURSOS)
                .document(id_docuento)
                .collection(NODO_NOTAS)
                .document(id_nota)
                .collection(NODO_IMAGENES_ANIADIDAS);

        getData(notesCollectionRef);
    }

    private void getData(CollectionReference notesCollectionRef) {
        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo(ID_CURSO, id_docuento)
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo(ID_CURSO, id_docuento);
        }

        notesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    notesCollectionRef.document(document.getId()).delete();
                    Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
                        /*Cuestionario _cuestionario = document.toObject(Cuestionario.class);
                        cuestionarios.add(_cuestionario);*/
                }
                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean deleteNotaData(String id_nota, String url) {
        boolean status = false;
        //TODO: ELMINAR (FOTOS | CUESTIONARIOS)
        db = FirebaseFirestore.getInstance();
        int i = 0;
        DocumentReference noteRef = null;

        for (i = 0; i <= 2; i++) {
            if (i == 0) {
//                Cuestionario
                if (cuestionarios.size() > 0) {
                    for (int j = 0; j < cuestionarios.size(); j++) {
                        noteRef = db
                                .collection(NODO_CURSOS)
                                .document(id_docuento)
                                .collection(NODO_NOTAS)
                                .document(id_nota)
                                .collection(NODO_CUESTIONARIO)
                                .document(cuestionarios.get(j).getId_cuestionario());

                        getDeleteMethod(noteRef, i);
                    }

                    status = false;
                }
            } else if (i == 1) {
//                Relacionados
                noteRef = null;
                if (archivosAgregados.size() > 0) {
                    for (int j = 0; j < archivosAgregados.size(); j++) {
                        try {
                            noteRef = db
                                    .collection(NODO_CURSOS)
                                    .document(id_docuento)
                                    .collection(NODO_NOTAS)
                                    .document(id_nota)
                                    .collection(NODO_IMAGENES_ANIADIDAS)
                                    .document(archivosAgregados.get(j).getId_image());

                            getDeleteMethod(noteRef, i, archivosAgregados.get(j).getUrl());
                        } catch (Exception ex) {
                            Log.d(TAG, "deleteNotaData: error: " + ex.getMessage());
                        }
                    }
                    status = false;
                }
            } else if (i == 2) {
                noteRef = null;
                try {
                    for (int j = 0; j < alumnos.size(); j++) {
                        noteRef = db
                                .collection(NODO_CURSOS)
                                .document(id_docuento)
                                .collection(NODO_NOTAS)
                                .document(id_nota);

                        getDeleteMethod(noteRef, i, url);
                    }

                } catch (Exception es) {
                    Log.d(TAG, "deleteNotaData: ERROR " + es.getMessage());
                }
                status = true;
            }
        }

        return status;
    }

    private void getDeleteMethod(DocumentReference noteRef, int posicion, String... arg) {
        if (posicion == 1 || posicion == 2) {
            mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(arg[0]);
            mStorageReference.delete().addOnSuccessListener(aVoid -> {
                // File deleted successfully
                Log.e("firebasestorage", "onSuccess: deleted file");
                //Toast.makeText(this, getString(R.string.mensaje_eleminacion_exitoso), Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(exception -> {
                // Uh-oh, an error occurred!
                Log.e("firebasestorage", "onFailure: did not delete file");
                Toast.makeText(this, getString(R.string.mensaje_eleminacion_fallido), Toast.LENGTH_SHORT).show();
            });
            noteRef.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            noteRef.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //Toast.makeText(this, getString(R.string.mensaje_eleminacion_exitoso), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final StorageReference fileReference = mStorageReference.child(firebaseUser.getUid() + getDate() + ".jpg");

        if (requestCode == GalleriaPick && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                File thumb_file_path = new File(mImageUri.getPath());
                thumb_file_path.getAbsolutePath();

                try {
                    thumb_bitmap = new Compressor(this)
                            .setQuality(75)
                            .compressToBitmap(thumb_file_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                Task<Uri> uriTask = fileReference.putBytes(thumb_byte).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        url_imagen = downloadUri.toString();
                    } else {
                        // Handle failures
                        // ...
                    }
                });

                if (uriTask != null) {
                    Toast.makeText(this, "Subido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy  HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    private boolean otherform(String... args) {
        db = FirebaseFirestore.getInstance();
        notesCollectionRef = db.collection(NODO_CUESTIONARIO);
        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", args[0])
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", args[0]);
        }

        notesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Toast.makeText(this, document.getId(), Toast.LENGTH_SHORT).show();
                    notesCollectionRef.document(document.getId()).delete();

                }
                status = true;
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
                status = false;
            }
        });

        return status;
    }

}
