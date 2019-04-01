package com.santos.firestoremeth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.santos.firestoremeth.Models.ArchivosAniadidos;
import com.santos.firestoremeth.Models.Cuestionario;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Notas;
import com.santos.firestoremeth.Models.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.santos.firestoremeth.Nodos.IDENTIFICADOR_USUARIO;
import static com.santos.firestoremeth.Nodos.NODO_CUESTIONARIO;
import static com.santos.firestoremeth.Nodos.NODO_CURSOS;
import static com.santos.firestoremeth.Nodos.NODO_IMAGENES_ANIADIDAS;
import static com.santos.firestoremeth.Nodos.NODO_NOTAS;
import static com.santos.firestoremeth.Nodos.NODO_USUARIOS;

@SuppressWarnings({"unused", "Convert2Lambda"})
public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";
    public static final String FOTO = "https://firebasestorage.googleapis.com/v0/b/school-it-fd8a7.appspot.com/o/fotos%20perfil%2Ffacebook-avatar.jpg?alt=media&token=af814dda-f2c9-438a-8a73-bc1c77e1142a";
    public static final String FRASE = "No importa cuántas veces falles, sólo debes de estar en lo correcto una vez. Entonces todos te llamarán un éxito de la noche a la mañana y te dirán lo afortunado que eres";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db; /*= FirebaseFirestore.getInstance();*/
    private DocumentReference newNoteRef; /*= db.collection("Alumnos").document();*/
    private DocumentSnapshot mLastQueriedDocument;
    private CollectionReference mCollectionReference;

    private String userID;
    private Context mContext;


    public FirebaseMethods(Context context, String... args) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //args(0) = nodo
        //args(1) = id documetno o para este caso nodo de cursos
        //args(2) = nodo de la subcollecion
        newNoteRef = db.collection(args[0]).document(args[1]).collection(args[2]).document();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }

    }

    public FirebaseMethods(Context context) {
        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //FirebaseFirestore
        db = FirebaseFirestore.getInstance();
        //El contexto de donde se esta llamando
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public FirebaseMethods(Context context, String nodo) {
        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //FirebaseFirestore
        db = FirebaseFirestore.getInstance();
        //DocumentoReference
        //newNoteRef = db.collection(nodo).document();
        mCollectionReference = db.collection(nodo);
        //El contexto de donde se esta llamando
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
            Toast.makeText(context, userID, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Con este metodo registramos a un nuevo maestro y/o alumno
     *
     * @param email
     * @param contrasenia
     */
    public void registrarNuevoEmail(String email, String contrasenia) {
        mAuth.createUserWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            //Toast.makeText(mContext, "Authstate change: " + userID, Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    //Todo: Incerciones en la DB

    public void nuevoCurso(String... datos) {
        Cursos cursos = new Cursos();
        cursos.setNombre_curso(datos[0]);
        cursos.setDescripcion_curso(datos[1]);
        cursos.setUrl_foto(datos[2]);
        cursos.setId_curso(newNoteRef.getId());
        cursos.setKey(datos[3]);
        cursos.setId_user_settings(datos[4]);

        newNoteRef.set(cursos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(mContext, "Curso Creado", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nuevaNota(String... datos) {

        //newNoteRef = db.collection(nodo).document("hi").collection("maiz").document();

        Notas nota = new Notas();
        nota.setTituloNota(datos[0]);
        nota.setDescripcionNota(datos[1]);
        nota.setNombreTemaNota(datos[2]);
        nota.setUrl_foto(datos[6]);
        nota.setIdNota(newNoteRef.getId());
        nota.setKey("1");
        nota.setUserName(datos[3]);
        nota.setUserPhoto(datos[4]);
        nota.setUserEmail(datos[5]);
        nota.setId_user_settings(datos[7]);
        nota.setId_curso(datos[8]);

        newNoteRef.set(nota).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(mContext, "Nota Creada", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(mContext, "Error al crear la njota", Toast.LENGTH_SHORT).show();
        });
    }

    public void nuevoCuestionario(String... datos) {
        //datos(0) = id del curso
        //datos(1) = id de la tarea
        //datos(2) = pregunta
        //datos(3) = respuesta
        newNoteRef = db.collection(NODO_CURSOS)
                .document(datos[0])
                .collection(NODO_NOTAS)
                .document(datos[1])
                .collection(NODO_CUESTIONARIO).document();

        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setId_cuestionario(newNoteRef.getId());
        cuestionario.setId_nota(datos[1]);
        cuestionario.setPregunta(datos[2]);
        cuestionario.setRespuesta_txt(datos[3]);
        cuestionario.setId_curso(datos[0]);

        newNoteRef.set(cuestionario).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(mContext, "Cuestionario Creado", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(mContext, "Error al crear la njota", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * @param datos
     */
    public void nuevoArchivo(String... datos) {
        //datos(0) = id del nota
        //datos(1) = url
        //datos(2) = descripcion
        //datos(3) = id curso
        newNoteRef = db
                .collection(NODO_CURSOS)
                .document(datos[3])
                .collection(NODO_NOTAS)
                .document(datos[0])
                .collection(NODO_IMAGENES_ANIADIDAS)
                .document();

        ArchivosAniadidos archivosAniadidos = new ArchivosAniadidos();
        archivosAniadidos.setId_image(newNoteRef.getId());
        archivosAniadidos.setId_nota(datos[0]);
        archivosAniadidos.setUrl(datos[1]);
        archivosAniadidos.setDescripcion(datos[2]);


        newNoteRef.set(archivosAniadidos).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(mContext, "Archivo creado", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(mContext, "Error al crear la njota", Toast.LENGTH_SHORT).show();
        });
    }

    public void creandoUsuario(String... datos) {
        //encontrado = false;

        // 0 = id_usuario
        // 1 = nombre;
        // 2 = telefono;
        // 3 = foto;
        // 4 = correo
        newNoteRef = db.collection(NODO_USUARIOS)
                .document(datos[0]);

        Usuario usuario = new Usuario();
        usuario.setId_usuario(datos[0]);
        usuario.setNombre(datos[1]);
        usuario.setTelefono(datos[2]);
        usuario.setUrl_perfil(datos[3]);
        usuario.setCorreo(datos[4]);


        newNoteRef.set(usuario).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(mContext, "Informacion Actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO: BUSQUEDAS
    public void cuenteExisteEnFirebase(FirebaseUser datos) {

        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(NODO_USUARIOS).document(datos.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                    //updateUI;
                } else {
                    Log.d(TAG, "No such document");
                    //encontrado = false;
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
                //encontrado = false;
            }
        });
        //return encontrado;
    }

    //TODO: ELIMINACIO
    public void deleteCurso() {

    }


    //TODO: OBTENER INFORMACION

    public ArrayList<Cursos> getCursos() {
        ArrayList<Cursos> mCursos = new ArrayList<>();

        //Query notesQuery = null;
        /*if (mLastQueriedDocument != null) {
            notesQuery = mCollectionReference
                    .whereEqualTo(IDENTIFICADOR_USUARIO, userID)
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = mCollectionReference
                    .whereEqualTo(IDENTIFICADOR_USUARIO, userID);

        }*/

        /*Query notesQuery = mCollectionReference
                .whereEqualTo(IDENTIFICADOR_USUARIO, userID);*/

        mCollectionReference.whereEqualTo(IDENTIFICADOR_USUARIO, userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mCursos.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Cursos cursos = document.toObject(Cursos.class);
                    mCursos.add(cursos);
                }

                Toast.makeText(mContext, "Terminado", Toast.LENGTH_SHORT).show();

                if (mCursos.size() == 0) {
                    //   mTextViewNoDatos.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "Vacio", Toast.LENGTH_SHORT).show();
                }

                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }

                //mRotateLoading.stop();
                //mAdaptadorNotas.notifyDataSetChanged();
            } else {
                Toast.makeText(mContext, "Error de sincronizacion", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        return mCursos;
    }


    /*
     * Este metodo fue creada para poder manupular la fecha que se recoge del dataPicker del Dialog de crear
     * modificar tareas para su correcta incercion en la DB
     * su unico parametro es pasarle un dato tipo string de la fecha.
     */
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Date getDateFromString(String datetoSaved) {

        try {
            Date date = format.parse(datetoSaved);
            return date;
        } catch (ParseException e) {
            return null;
        }

    }
}
