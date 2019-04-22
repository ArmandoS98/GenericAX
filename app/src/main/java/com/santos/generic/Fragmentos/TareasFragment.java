package com.santos.generic.Fragmentos;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santos.generic.Adapters.AdaptadorTareas;
import com.santos.generic.R;
import com.santos.generic.Utils.Persistence.TareaRepository;
import com.santos.generic.Utils.TasksG;

import java.util.ArrayList;

import static com.santos.generic.Activities.TabActivity.id_docuento;


public class TareasFragment extends Fragment {
    private static final String TAG = "TareasFragment";
    private AdaptadorTareas mAdaptadorTareas;
    private ArrayList<TasksG> mTasksGS = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private TareaRepository mTareaRepository;

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
        mTareaRepository = new TareaRepository(getContext());
        mRecyclerView = view.findViewById(R.id.recycler_tareas);
        initRecyclerView();
        retrieveTareas();
        return view;
    }

    private void retrieveTareas() {
        mTareaRepository.retriveTaskCustom(id_docuento).observe(this, tasksGS -> {
            if (mTasksGS.size() > 0) {
                mTasksGS.clear();
            }

            if (mTasksGS != null) {
                mTasksGS.addAll(tasksGS);
            }
            mAdaptadorTareas.notifyDataSetChanged();
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        //mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(mRecyclerView);
        mAdaptadorTareas = new AdaptadorTareas(getContext(), mTasksGS);
        mRecyclerView.setAdapter(mAdaptadorTareas);
    }

    private void getDataSQLite(View view) {
        Log.d(TAG, "getDataSQLite: ");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(mRecyclerView);
        //mAdaptadorTareas = new AdaptadorTareas(getContext(), databaseHelper.getTask());
        mRecyclerView.setAdapter(mAdaptadorTareas);

    }

    private void deleteTask(TasksG tasksG) {
        mTasksGS.remove(tasksG);
        mAdaptadorTareas.notifyDataSetChanged();

        mTareaRepository.deleteTask(tasksG);
    }

    private ItemTouchHelper.SimpleCallback itemSimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            deleteTask(mTasksGS.get(viewHolder.getAdapterPosition()));
        }
    };
}
