package com.santos.generic.Dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Interfaz.IDatos;
import com.santos.generic.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class NuevaNotaFullScreen extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "NuevaNotaFullScreen";
    public static final String FOTO1 = "https://firebasestorage.googleapis.com/v0/b/trigonometria-1c5cb.appspot.com/o/Imagenes%2Fnoimage.png?alt=media&token=1e1df63e-25ef-42b0-8520-3cd0992799c3";
    private static final int GalleriaPick = 1;
    public static final int CAMERA_COD = 502;

    //Views
    private EditText mEditTextTitulo;
    private EditText mEditTextContenido;
    private ImageButton mImageButtonImagen;
    private Dialog mChoosingDialog;

    private IDatos iDatos;
    private Toolbar toolbar;

    //Variables
    private String titulo;
    private String contenido;
    private String other;
    private String url_imagen;
    public static String id_curso;
    private Uri mImageUri;
    private Bitmap thumb_bitmap = null;

    public static NuevaNotaFullScreen newInstance(Notas notas) {
        NuevaNotaFullScreen dialog = new NuevaNotaFullScreen();

        Bundle args = new Bundle();
        args.putParcelable("notas", notas);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        setHasOptionsMenu(true);
        //notas = getArguments().getParcelable("notas");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fullscreen_nueva_nota, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(R.string.nueva_nota);

        mEditTextTitulo = view.findViewById(R.id.click_post_tittle);
        mEditTextContenido = view.findViewById(R.id.click_post_description);
        mImageButtonImagen = view.findViewById(R.id.select_post_image);
        initDialog(view);

        view.findViewById(R.id.select_post_image).setOnClickListener(this);
        view.findViewById(R.id.update_post_button).setOnClickListener(this);

        return view;
    }

    private void initDialog(View view) {

        mChoosingDialog = new Dialog(getContext());
        mChoosingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mChoosingDialog.getWindow().setGravity(Gravity.BOTTOM);
        mChoosingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mChoosingDialog.setCancelable(true);
        mChoosingDialog.setContentView(R.layout.dialog_choose_cameragallery);

        mChoosingDialog.findViewById(R.id.ivbChooseClose).setOnClickListener(this);
        mChoosingDialog.findViewById(R.id.llCamera).setOnClickListener(this);
        mChoosingDialog.findViewById(R.id.llGallery).setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.inflateMenu(R.menu.nueva_tarea);
        toolbar.setOnMenuItemClickListener(item -> {

            titulo = mEditTextTitulo.getText().toString();
            contenido = mEditTextContenido.getText().toString();
            other = "options";

            if (!titulo.equals("") || !contenido.equals("") || !other.equals("")) {
                iDatos.onNuevaNota(titulo, contenido, other);
                getDialog().dismiss();
            } else {
                Toast.makeText(getActivity(), "Todos los campos son necesarios!", Toast.LENGTH_SHORT).show();
            }
            dismiss();
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iDatos = (IDatos) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_post_image:
                mChoosingDialog.show();
                break;
            case R.id.ivbChooseClose:
                mChoosingDialog.dismiss();
                break;
            case R.id.llCamera:
                mChoosingDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {
                        openCamera();
                        mChoosingDialog.dismiss();
                    }
                } else {
                    openCamera();
                    mChoosingDialog.dismiss();
                }
                break;
            case R.id.llGallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                        mChoosingDialog.dismiss();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 401);
                        mChoosingDialog.dismiss();
                    }
                } else {
                    openGallery();
                    mChoosingDialog.dismiss();
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);

        int permissionReadStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 402);
            return false;
        }
        return true;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GalleriaPick);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_COD);
    }

    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Date getDateFromString(String datetoSaved) {

        try {
            Date date = format.parse(datetoSaved);
            return date;
        } catch (ParseException e) {
            return null;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleriaPick && resultCode == RESULT_OK && data != null) {
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);


        } else if (requestCode == CAMERA_COD && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");

            Uri selectedURI = getImageUri(mphoto);

            CropImage.activity(selectedURI)
                    .start(getContext(), this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_close_black_24dp)
                        .error(R.drawable.ic_close_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH);

                Glide.with(getContext())
                        .load(mImageUri)
                        .apply(options)
                        .into(mImageButtonImagen);


                File thumb_file_path = new File(mImageUri.getPath());
                thumb_file_path.getAbsolutePath();

                try {
                    thumb_bitmap = new Compressor(getContext())
                            .setQuality(75)
                            .compressToBitmap(thumb_file_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    private Uri getImageUri(Bitmap bitmap) {
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (bitmap != null) {
            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Title", null);
            return Uri.parse(path);
        } else {
            return Uri.parse("");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 401) {
            if (grantResults.length == 0 || grantResults == null) {
//                Logger.e(TAG, "Null Every thing");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }
        } else if (requestCode == 402) {
            if (grantResults.length == 0 || grantResults == null) {
//                Logger.e(TAG, "Null Every thing");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }
        }
    }
}
