package com.santos.generic.Utils.Persistence;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.santos.generic.Utils.Async.DeleteAsyncTask;
import com.santos.generic.Utils.Async.InsertAsyncTask;
import com.santos.generic.Utils.Async.UpdateAsyncTask;
import com.santos.generic.Utils.TasksG;

import java.util.List;

public class TareaRepository {
    private TareaDatabase mTareaDatabase;

    public TareaRepository(Context context) {
        mTareaDatabase = TareaDatabase.getInstance(context);
    }

    public void insertTask(TasksG tasksG) {
        new InsertAsyncTask(mTareaDatabase.getTaskDao()).execute(tasksG);
    }

    public void updateTask(TasksG tasksG) {
        new UpdateAsyncTask(mTareaDatabase.getTaskDao()).execute(tasksG);
    }

    public LiveData<List<TasksG>> retriveTasks() {
        return mTareaDatabase.getTaskDao().getTareas();
    }

    public  LiveData<List<TasksG>> retriveTaskCustom(String id_curso){
        return mTareaDatabase.getTaskDao().getTareasCursos(id_curso);
    }

    public void deleteTask(TasksG tasksG) {
        new DeleteAsyncTask(mTareaDatabase.getTaskDao()).execute(tasksG);
    }
}
