package com.santos.generic.NavigationDown;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.santos.generic.Fragmentos.DashboardFragment;
import com.santos.generic.R;

public class MainFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;
    NavigationIconClickListener mNavigationIconClickListener;
    private Fragment fragmentoGenerico = null;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mNavigationIconClickListener = new NavigationIconClickListener(getContext(), view.findViewById(R.id.product_grid));

        view.findViewById(R.id.mo_dashboard).setOnClickListener(this);
        view.findViewById(R.id.mo_agenda).setOnClickListener(this);
        view.findViewById(R.id.mo_cursos).setOnClickListener(this);
        view.findViewById(R.id.mo_horarios).setOnClickListener(this);
        view.findViewById(R.id.mo_tareas).setOnClickListener(this);
        view.findViewById(R.id.mo_recordatorios).setOnClickListener(this);
        view.findViewById(R.id.mo_ayuda).setOnClickListener(this);
        view.findViewById(R.id.mo_perfil).setOnClickListener(this);

        setUpToolbar(view);

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.product_grid).setBackground(getContext().getDrawable(R.drawable.shr_product_grid_background_shape));
        }
        return view;
    }

    private void setUpToolbar(View view) {
        toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(mNavigationIconClickListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mo_dashboard:
                navigationDownDrawer(v,"Dashboard",new DashboardFragment(), true);
                break;
            case R.id.mo_agenda:
                navigationDownDrawer(v,"Agenda",new DashboardFragment(), true);
                break;
            case R.id.mo_cursos:
                navigationDownDrawer(v,"Cursos",new DashboardFragment(), true);
                break;
            case R.id.mo_horarios:
                navigationDownDrawer(v,"Horarios",new DashboardFragment(), true);
                break;
            case R.id.mo_tareas:
                navigationDownDrawer(v,"Tareas",new DashboardFragment(), true);
                break;
            case R.id.mo_recordatorios:
                navigationDownDrawer(v,"Recordatorios",new DashboardFragment(), true);
                break;
            case R.id.mo_ayuda:
                navigationDownDrawer(v,"Ayuda",new DashboardFragment(), true);
                break;
            case R.id.mo_perfil:
                navigationDownDrawer(v,"Perfil",new DashboardFragment(), true);
                break;
        }
    }

    private void navigationDownDrawer(View v, String titulo, DashboardFragment fragment, boolean regreso) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        mNavigationIconClickListener.onClick(v);
        toolbar.setTitle(titulo);

        fragmentoGenerico = fragment;

        if (fragment != null){
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.product_grid, fragmentoGenerico)
                    .commit();
        }
    }



    private void showHome() {
        fragmentoGenerico = new DashboardFragment();
        toolbar.setTitle("Dashboard");
        if (fragmentoGenerico != null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.product_grid, fragmentoGenerico, fragmentoGenerico.getTag()).commit();
        }
    }

}
