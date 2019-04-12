package com.santos.generic.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.santos.generic.Adapters.AdaptadorTareas;
import com.santos.generic.R;
import com.santos.generic.Utils.SQLiteFukes.DatabaseHelper;
import com.santos.generic.Utils.SQLiteFukes.TasksG;

import java.util.ArrayList;


public class TareasFragment extends Fragment {
    private static final String TAG = "TareasFragment";
    private AdaptadorTareas mAdaptadorTareas;
    private ArrayList<TasksG> mTasksGS;

    private RecyclerView mRecyclerView;

    private DatabaseHelper databaseHelper;

    public TareasFragment() {
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
        View view = inflater.inflate(R.layout.fragment_tareas, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_tareas);
        databaseHelper = new DatabaseHelper(getContext());
        getDataSQLite(view);
        return view;
    }

    private void getDataSQLite(View view) {
        Log.d(TAG, "getDataSQLite: ");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdaptadorTareas = new AdaptadorTareas(getContext(), databaseHelper.getTask());
        mRecyclerView.setAdapter(mAdaptadorTareas);

    }
}
