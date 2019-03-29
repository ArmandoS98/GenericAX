package com.santos.generic;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.santos.generic.Activities.LoginActivity;
import com.santos.generic.Fragmentos.AgendaFragment;
import com.santos.generic.Fragmentos.DashboardFragment;
import com.santos.generic.NavigationDown.MainFragment;
import com.santos.generic.NavigationDown.NavigationIconClickListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    // Constantes
    private static final String TAG = "MainActivity";
    public static final String KEY_NOTAS = "Valor";

    // Vistas
    private Fragment fragmentoGenerico = null;
    private Toolbar toolbar;
    private NavigationIconClickListener mNavigationIconClickListener;

    //Firebase
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser firebaseUser;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new MainFragment());

        /*if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();*/

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //TODO: Verificacion si el usuario esta logeado.
        if (firebaseUser == null) {
            backtoLogin();
        }


        mNavigationIconClickListener = new NavigationIconClickListener(this, findViewById(R.id.product_grid));

        findViewById(R.id.mo_dashboard).setOnClickListener(this);
        findViewById(R.id.mo_agenda).setOnClickListener(this);
        findViewById(R.id.mo_cursos).setOnClickListener(this);
        findViewById(R.id.mo_horarios).setOnClickListener(this);
        findViewById(R.id.mo_tareas).setOnClickListener(this);
        findViewById(R.id.mo_recordatorios).setOnClickListener(this);
        findViewById(R.id.mo_ayuda).setOnClickListener(this);
        findViewById(R.id.mo_perfil).setOnClickListener(this);

        setUpToolbar();

        fragmentoGenerico = new DashboardFragment();
        //fragmentoGenerico = new FormulasFragment();
        toolbar.setTitle("Dashboard");
        getSupportFragmentManager().beginTransaction().add(R.id.product_grid, fragmentoGenerico).commit();

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.product_grid).setBackground(this.getDrawable(R.drawable.shr_product_grid_background_shape));
        }


    }

    private void backtoLogin() {
        firebaseUser = null;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.app_bar);
        AppCompatActivity activity = this;
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(mNavigationIconClickListener);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mo_dashboard:
                navigationDownDrawer(v, "Dashboard", new DashboardFragment(), true);
                break;
            case R.id.mo_agenda:
                navigationDownDrawer(v, "Agenda", new AgendaFragment(), true);
                break;
            case R.id.mo_cursos:
                navigationDownDrawer(v, "Cursos", new DashboardFragment(), true);
                break;
            case R.id.mo_horarios:
                navigationDownDrawer(v, "Horarios", new DashboardFragment(), true);
                break;
            case R.id.mo_tareas:
                navigationDownDrawer(v, "Tareas", new DashboardFragment(), true);
                break;
            case R.id.mo_recordatorios:
                navigationDownDrawer(v, "Recordatorios", new DashboardFragment(), true);
                break;
            case R.id.mo_ayuda:
                navigationDownDrawer(v, "Ayuda", new DashboardFragment(), true);
                break;
            case R.id.mo_perfil:
                navigationDownDrawer(v, "Perfil", new DashboardFragment(), true);
                break;
        }
    }

    private void navigationDownDrawer(View v, String titulo, Fragment fragment, boolean regreso) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mNavigationIconClickListener.onClick(v);
        toolbar.setTitle(titulo);

        fragmentoGenerico = fragment;

        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.product_grid, fragmentoGenerico)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentoGenerico instanceof DashboardFragment) {
            super.onBackPressed();
        } else {
            showHome();
        }
    }

    private void showHome() {
        fragmentoGenerico = new DashboardFragment();
        toolbar.setTitle("Dashboard");
        if (fragmentoGenerico != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.product_grid, fragmentoGenerico, fragmentoGenerico.getTag()).commit();
        }
    }
}
