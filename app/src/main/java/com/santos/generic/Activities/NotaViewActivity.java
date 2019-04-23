package com.santos.generic.Activities;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import com.santos.generic.Adapters.AdaptadorCuestionario;
import com.santos.generic.Adapters.AdapterArchivosAdicionales;
import com.santos.generic.Dialogs.EditarFullScreen;
import com.santos.generic.Dialogs.NuevoCuestionarioFullScreen;
import com.santos.generic.Interfaz.IDatos;
import com.santos.generic.R;
import com.santos.generic.Utils.TasksG;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

import static com.santos.firestoremeth.Nodos.CONTENIDO_NOTA;
import static com.santos.firestoremeth.Nodos.KEY;
import static com.santos.firestoremeth.Nodos.NODO_CUESTIONARIO;
import static com.santos.firestoremeth.Nodos.NODO_CURSOS;
import static com.santos.firestoremeth.Nodos.NODO_IMAGENES_ANIADIDAS;
import static com.santos.firestoremeth.Nodos.NODO_NOTAS;
import static com.santos.firestoremeth.Nodos.PARAMETRO_ID_NOTA;
import static com.santos.firestoremeth.Nodos.TITULO_NOTA;
import static com.santos.generic.Utils.palReb.KEY_NOTAS;

public class NotaViewActivity extends AppCompatActivity implements IDatos {

    private static final String TAG = "ShowActivity";
    private static final int GalleriaPick = 1;

    private FirebaseMethods firebaseMethods;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentSnapshot mLastQueriedDocument;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private FirebaseMethods mFirebaseMethods;

    private Dialog epicDialog;
    private TextView mTextViewDescripcion;
    private ImageButton mImageButtonArchivo;
    private TextView mTextViewFecha;
    private TextView mButtonEditarNota;
    private TextView mButtonElimnarNota;
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewArchivos;
    private AdaptadorCuestionario mAdaptadorCuestionario;
    private AdapterArchivosAdicionales mAdapterArchivosAdicionales;
    private ArrayList<Cuestionario> cuestionarios;
    private ArrayList<ArchivosAniadidos> archivosAgregados;

    private Uri mImageUri;
    private String id_nota;
    public static String curso_id;
    private String nombre_nota;
    private Notas mNote = null;
    private String url_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_view);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mFirebaseMethods = new FirebaseMethods(this);

        epicDialog = new Dialog(this);

        Intent i = getIntent();
        mNote = i.getParcelableExtra(KEY_NOTAS);

        if (mNote != null) {
            Log.d(TAG, "onCreate: Valor: " + mNote);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            nombre_nota = mNote.getTituloNota();
            CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(nombre_nota);

            toolbar.setNavigationOnClickListener(v -> finish());
            loadBackdrop(mNote.getUrl_foto());

            mTextViewDescripcion = findViewById(R.id.tv_descripcion_show);
            mTextViewFecha = findViewById(R.id.tv_fecha);
            mButtonEditarNota = findViewById(R.id.tv_editar_nota);
            mButtonElimnarNota = findViewById(R.id.tv_eliminar_nota);
            mFloatingActionButton = findViewById(R.id.fab);
            mRecyclerView = findViewById(R.id.recyclergenerico);
            mRecyclerViewArchivos = findViewById(R.id.recyclergenerico_arcivos_agregados);
            mImageButtonArchivo = findViewById(R.id.img_archivo);

            String date = getIntent().getStringExtra("date");
            curso_id = getIntent().getStringExtra(KEY);
            mTextViewFecha.setText(date);
            id_nota = mNote.getIdNota();

            mRecyclerView.setHasFixedSize(true);
            mRecyclerViewArchivos.setHasFixedSize(true);

            cuestionarios = new ArrayList<>();
            archivosAgregados = new ArrayList<>();

            getAlumnos(id_nota);
            getImagenAniadidas(id_nota);
            initRecyclerView();

            if (mNote.getId_user_settings().equals(mAuth.getUid())) {
                mButtonEditarNota.setVisibility(View.VISIBLE);
                mButtonElimnarNota.setVisibility(View.VISIBLE);
            } else {
                mButtonEditarNota.setVisibility(View.GONE);
                mButtonElimnarNota.setVisibility(View.GONE);
            }

            final Notas finalMNote = mNote;
            mButtonEditarNota.setOnClickListener(v -> {
                EditarFullScreen editar_fullScreen = EditarFullScreen.newInstance(finalMNote);
                editar_fullScreen.setCancelable(false);
                editar_fullScreen.show(getSupportFragmentManager(), "Editar");
            });

            mButtonElimnarNota.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotaViewActivity.this);

                builder.setTitle("Confirmar");
                builder.setMessage("Esta seguro de eliminar esta nota?");

                builder.setPositiveButton("SI", (dialog, which) -> {
                    // Do nothing but close the dialog
                    if (deleteNotaData(finalMNote.getIdNota())) {
                        Toast.makeText(this, getString(R.string.mensaje_eleminacion_exitoso), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.mensaje_eleminacion_fallido), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    NotaViewActivity.this.finish();
                });

                builder.setNegativeButton("NO", (dialog, which) -> {

                    // Do nothing
                    dialog.dismiss();
                });

                AlertDialog alert = builder.create();
                alert.show();
            });

            mFloatingActionButton.setOnClickListener(v -> Toast.makeText(this, "Ups. Esta opcion esta en desarrollo", Toast.LENGTH_SHORT).show());

            mImageButtonArchivo.setOnClickListener(v -> {
                Intent galeriaIntent = new Intent();
                galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
                galeriaIntent.setType("image/*");
                startActivityForResult(galeriaIntent, GalleriaPick);
            });

            mTextViewDescripcion.setText(mNote.getDescripcionNota());

        }
    }

    private void showTheNewDialog(int type, final Uri mImageUri) {

        //Inicializacion de nuestros metodos
        firebaseMethods = new FirebaseMethods(this);
        epicDialog.setContentView(R.layout.nuevo_archivo_imagen);
        epicDialog.getWindow().getAttributes().windowAnimations = type;
        epicDialog.setCancelable(false);

        //Widgets
        final ImageView closePopupPositiveImg = epicDialog.findViewById(R.id.closePopupPositive);
        final ImageView imagenPreview = epicDialog.findViewById(R.id.img_preview);
        final Button aceptar = epicDialog.findViewById(R.id.btn_acept);
        final EditText mEditTextTitulo = epicDialog.findViewById(R.id.note_title);

        closePopupPositiveImg.setOnClickListener(v -> epicDialog.dismiss());

        mStorageReference = FirebaseStorage.getInstance().getReference("Imagenes");

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_close_black_24dp)
                .error(R.drawable.ic_close_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this)
                .load(mImageUri)
                .apply(options)
                .into(imagenPreview);

        aceptar.setOnClickListener(v -> {
            //Todo: obtenemos los valores de las vistas corresponidnetes
            String nombre = mEditTextTitulo.getText().toString();

            if (checkInputs(nombre)) {
                saveNuevoArchivo(nombre, mImageUri);
                //crearNuevoAlumno(nombre, apellidos, edad);
                epicDialog.dismiss();
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    private boolean checkInputs(String nombres) {
        if (nombres.equals("")) {
            Toast.makeText(this, "Todos los compos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveNuevoArchivo(final String nombre, Uri mImageUri) {
        final StorageReference fileReference = mStorageReference.child(/*System.currentTimeMillis()*/ "acpu" + firebaseUser.getUid() + getDate() + ".jpg" /*+ getFileExtencion(mImageUri)*/);

        Task<Uri> urlTask = fileReference.putFile(this.mImageUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return fileReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                url_imagen = downloadUri.toString();

                firebaseMethods.nuevoArchivo(
                        id_nota,
                        url_imagen,
                        nombre,
                        curso_id);
            } else {
                // Handle failures
                // ...
            }
        });

    }

    //Este metodo inicia el recycler view con sus componentes
    private void initRecyclerView() {
        if (mAdaptadorCuestionario == null) {
            mAdaptadorCuestionario = new AdaptadorCuestionario(this, cuestionarios);
        }

        if (mAdapterArchivosAdicionales == null) {
            mAdapterArchivosAdicionales = new AdapterArchivosAdicionales(this, archivosAgregados);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewArchivos.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(mAdaptadorCuestionario);
        mRecyclerViewArchivos.setAdapter(mAdapterArchivosAdicionales);
    }

    private void getAlumnos(String id_notas) {
        db = FirebaseFirestore.getInstance();

        CollectionReference notesCollectionRef = db
                .collection(NODO_CURSOS)
                .document(curso_id)
                .collection(NODO_NOTAS)
                .document(id_nota).collection(NODO_CUESTIONARIO);

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", id_nota)
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", id_nota);
        }


        notesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                cuestionarios.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Cuestionario _cuestionario = document.toObject(Cuestionario.class);
                    cuestionarios.add(_cuestionario);
                }

                if (cuestionarios.size() == 0) {
                    //mTextViewNoDatos.setVisibility(View.VISIBLE);
                }

                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }

                mAdaptadorCuestionario.notifyDataSetChanged();
            } else {
                Toast.makeText(NotaViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getImagenAniadidas(String id_notas) {
        db = FirebaseFirestore.getInstance();

        CollectionReference notesCollectionRef = db
                .collection(NODO_CURSOS)
                .document(curso_id)
                .collection(NODO_NOTAS)
                .document(id_nota)
                .collection(NODO_IMAGENES_ANIADIDAS);

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", id_nota)
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", id_nota);
        }


        notesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                archivosAgregados.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ArchivosAniadidos archivosAniadidos = document.toObject(ArchivosAniadidos.class);
                    archivosAgregados.add(archivosAniadidos);
                }

                if (archivosAgregados.size() == 0) {
                    //mTextViewNoDatos.setVisibility(View.VISIBLE);
                    Toast.makeText(NotaViewActivity.this, "No hay ningun archivo!", Toast.LENGTH_SHORT).show();
                }

                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }

                mAdapterArchivosAdicionales.notifyDataSetChanged();
            } else {
                Toast.makeText(NotaViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadBackdrop(String url_foto) {
        final ImageView imageView = findViewById(R.id.backdrop);
        Glide.with(this).load(url_foto).apply(RequestOptions.centerCropTransform()).into(imageView);
    }

    //TODO: OLLENTES DE LAS ACTUALIZACION EN BACKGROUND
    private void getUpdateCuestionarioRefresh() {
        db.collection(NODO_CURSOS).document(curso_id).collection(NODO_NOTAS).document(id_nota).collection(NODO_CUESTIONARIO)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (cuestionarios.size() == 0) {
                        cuestionarios.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Cuestionario cuestionario = doc.toObject(Cuestionario.class);
                            cuestionarios.add(cuestionario);
                        }
                    } else {
                        cuestionarios.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Cuestionario cuestionario = doc.toObject(Cuestionario.class);
                            cuestionarios.add(cuestionario);
                        }
                    }
                    mAdaptadorCuestionario.notifyDataSetChanged();
                });
    }

    private void getUpdateArchivoRefresh() {
        db.collection(NODO_CURSOS)
                .document(curso_id)
                .collection(NODO_NOTAS)
                .document(id_nota)
                .collection(NODO_IMAGENES_ANIADIDAS)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (archivosAgregados.size() == 0) {
                            archivosAgregados.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                ArchivosAniadidos archivosAniadidos = doc.toObject(ArchivosAniadidos.class);
                                archivosAgregados.add(archivosAniadidos);
                            }
                        } else {
                            archivosAgregados.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                ArchivosAniadidos archivosAniadidos = doc.toObject(ArchivosAniadidos.class);
                                archivosAgregados.add(archivosAniadidos);
                            }
                        }
                        mAdapterArchivosAdicionales.notifyDataSetChanged();
                    }
                });
    }

    private void getUpdateNotasRefresh() {
        db.collection(NODO_CURSOS).document(curso_id).collection(NODO_NOTAS)
                .whereEqualTo(PARAMETRO_ID_NOTA, id_nota)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            Notas notas = doc.toObject(Notas.class);


                            mTextViewDescripcion.setText(notas.getDescripcionNota());
                            nombre_nota = notas.getTituloNota();

                            /*Owner owner = doc.toObject(Owner.class);

                            user_ID = owner.getOwner_id();
                            tvcorre.setText(owner.getCorreo());
                            tvtelefono.setText(owner.getTelefono());
                            tvedad.setText(owner.getEdad());
                            tvnombres.setText(owner.getNombres());
                            mEditTextApellidos.setText(owner.getApellidos());
                            if (owner.getUrl_foto() != null) {
                                Glide.with(getApplicationContext()).load(owner.getUrl_foto()).into(imgpefil);
                            }*/
                        }
                    }
                });
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy  HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private boolean deleteNotaData(String id_nota) {
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
                                .document(curso_id)
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
                                    .document(curso_id)
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
                noteRef = db
                        .collection(NODO_CURSOS)
                        .document(curso_id)
                        .collection(NODO_NOTAS)
                        .document(id_nota);

                getDeleteMethod(noteRef, i, mNote.getUrl_foto());
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
                    //Toast.makeText(this, getString(R.string.mensaje_eleminacion_exitoso), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotaViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
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


    //INICIA IDATO CAMPOS
    @Override
    public void onNotaSeleccionada(Notas notas) {

    }

    @Override
    public void onNotaUpdate(Notas notas) {
        DocumentReference noteref = db.collection(NODO_CURSOS).document(curso_id).collection(NODO_NOTAS).document(notas.getIdNota());
        noteref.update(TITULO_NOTA, notas.getTituloNota(),
                CONTENIDO_NOTA, notas.getDescripcionNota()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(NotaViewActivity.this, "Informacion Actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NotaViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCursotoNotaa(Cursos cursos) {

    }

    @Override
    public void onNuevoCuestionario(String titulo, String content) {
        mFirebaseMethods.nuevoCuestionario(curso_id, id_nota, titulo, content);
    }

    @Override
    public void onNuevaTarea(String... arg) {

    }

    @Override
    public void onSelectTarea(TasksG tasksG) {

    }

    @Override
    public void onNuevaNota(String... arg) {

    }

    @Override
    public void onSekectNota(Notas notas) {

    }

    //FINALIZA IDATO CAMPOS


    @Override
    protected void onResume() {
        super.onResume();
        getUpdateNotasRefresh();
        getUpdateCuestionarioRefresh();
        getUpdateArchivoRefresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.shared) {
            Toast.makeText(this, "Opcion en desarrollo", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, mTextViewDescripcion.getText().toString());
            startActivity(Intent.createChooser(intent, "Share with"));
            return true;
        } else if (id == R.id.preguntas) {
            NuevoCuestionarioFullScreen dialog_fullScreen = NuevoCuestionarioFullScreen.newInstance(mNote);
            dialog_fullScreen.setCancelable(false);
            dialog_fullScreen.show(getSupportFragmentManager(), "Cuestionario");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleriaPick && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();

            showTheNewDialog(R.style.DialogScale, mImageUri);

        }
    }
}
