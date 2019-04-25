package com.santos.generic.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.santos.generic.MainActivity;
import com.santos.generic.R;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageViewPerfil;
    private TextView mTextViewNombre;

    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> gotoMain());

        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.btn_salir).setOnClickListener(this);
        mImageViewPerfil = findViewById(R.id.img_perfil);
        mTextViewNombre = findViewById(R.id.textView2);
        retriveDataUser();

    }

    public void retriveDataUser() {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_time)
                .error(R.drawable.ic_error_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            if (mFirebaseUser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(mFirebaseUser.getPhotoUrl().toString())
                        .apply(options)
                        .into(mImageViewPerfil);
            }

            mTextViewNombre.setText(mFirebaseUser.getDisplayName());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button3:
                Toast.makeText(this, "Editar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_salir:
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(PerfilActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoMain();
    }

    private void gotoMain() {
        startActivity(new Intent(PerfilActivity.this, MainActivity.class));
        finish();
    }
}
