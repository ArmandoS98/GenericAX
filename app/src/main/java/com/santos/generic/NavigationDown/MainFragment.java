package com.santos.generic.NavigationDown;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.santos.generic.R;

public class MainFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;
    NavigationIconClickListener mNavigationIconClickListener;

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
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_agenda:
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_cursos:
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_horarios:
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_tareas:
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_recordatorios:
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_ayuda:
                mNavigationIconClickListener.onClick(v);
                break;
            case R.id.mo_perfil:
                mNavigationIconClickListener.onClick(v);
                break;
        }
    }
}
