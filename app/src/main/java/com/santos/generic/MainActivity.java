package com.santos.generic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Activities.LoginActivity;
import com.santos.generic.Activities.PerfilActivity;
import com.santos.generic.Activities.PortalWebActivity;
import com.santos.generic.Activities.SettingsActivity;
import com.santos.generic.Activities.TabActivity;
import com.santos.generic.Activities.TareaViewActivity;
import com.santos.generic.Fragmentos.AgendaFragment;
import com.santos.generic.Fragmentos.CursosFragment;
import com.santos.generic.Fragmentos.DashboardFragment;
import com.santos.generic.Fragmentos.TareaGFragment;
import com.santos.generic.Interfaz.IDatos;
import com.santos.generic.NavigationDown.NavigationIconClickListener;
import com.santos.generic.Utils.Connectivity;
import com.santos.generic.Utils.SharedPrefences.PreferenceHelperDemo;
import com.santos.generic.Utils.TasksG;

import static com.santos.generic.Utils.palReb.KEY_NOTAS;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        IDatos,
        View.OnClickListener {

    // Vistas
    private Fragment fragmentoGenerico = null;
    private Toolbar toolbar;
    private NavigationIconClickListener mNavigationIconClickListener;

    private FirebaseUser firebaseUser;
    //private StorageReference mStorageReference;

    public static boolean isFirstInstall(Context context) {
        try {
            long firstInstallTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
            return firstInstallTime == lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Verificaion de internet
        if (Connectivity.isConnectedWifi(this) || Connectivity.isConnectedMobile(this)) {

        } else {

        }


        //Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //TODO: Verificacion si el usuario esta logeado.
        if (firebaseUser == null) {
            backtoLogin();
        } else {
            if (isFirstInstall(this)) {
                PreferenceHelperDemo.setSharedPreferenceBoolean(this, getString(R.string.cursos_dash), false);
                PreferenceHelperDemo.setSharedPreferenceBoolean(this, getString(R.string.notas_dash), false);
                PreferenceHelperDemo.setSharedPreferenceBoolean(this, getString(R.string.tareas_dash), false);
            }
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
        getSupportFragmentManager().beginTransaction().add(R.id.contenedor, fragmentoGenerico).commit();
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
                navigationDownDrawer(v, "Dashboard", new DashboardFragment());
                break;
            case R.id.mo_agenda:
                navigationDownDrawer(v, "Agenda", new AgendaFragment());
                break;
            case R.id.mo_cursos:
                navigationDownDrawer(v, "Cursos", new CursosFragment());
                break;
            case R.id.mo_horarios:
                navigationDownDrawer(v, "Horarios", new DashboardFragment());
                break;
            case R.id.mo_tareas:
                navigationDownDrawer(v, "Tareas", new TareaGFragment());
                break;
            case R.id.mo_recordatorios:
                startActivity(new Intent(this, PortalWebActivity.class));
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_ayuda:
                //navigationDownDrawer(v, "Ajustes", new DashboardFragment(), true);
                startActivity(new Intent(this, SettingsActivity.class));
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_perfil:
                startActivity(new Intent(this, PerfilActivity.class));
                finish();
                mNavigationIconClickListener.onClick(v);
                break;
        }
    }

    private void navigationDownDrawer(View v, String titulo, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        toolbar.setTitle(titulo);

        fragmentoGenerico = fragment;

        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor, fragmentoGenerico)
                    .commit();
        }

        mNavigationIconClickListener.onClick(v);
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
            manager.beginTransaction().replace(R.id.contenedor, fragmentoGenerico, fragmentoGenerico.getTag()).commit();
        }
    }

    @Override
    public void onNotaSeleccionada(Notas notas) {

    }

    @Override
    public void onNotaUpdate(Notas notas) {

    }

    @Override
    public void onCursotoNotaa(Cursos cursos) {
        Intent intent = new Intent(this, TabActivity.class);
        intent.putExtra(KEY_NOTAS, cursos);
        startActivity(intent);
    }

    @Override
    public void onNuevoCuestionario(String titulo, String content) {

    }

    @Override
    public void onNuevaTarea(String... arg) {

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

    private int getFirstTimeRun() {
        SharedPreferences sp = getSharedPreferences("MYAPP", 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
        if (lastVersionCode == -1) result = 0;
        else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
        return result;
    }
}
