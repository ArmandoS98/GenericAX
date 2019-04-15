package com.santos.generic.Utils.Async;

import android.os.AsyncTask;

import com.santos.generic.Utils.Persistence.TareaDao;
import com.santos.generic.Utils.TasksG;

public class InsertAsyncTask extends AsyncTask<TasksG, Void, Void> {
    private static final String TAG = "InsertAsyncTask";
    private TareaDao mTareaDao;

    public InsertAsyncTask(TareaDao dao) {
        mTareaDao = dao;
    }

    @Override
    protected Void doInBackground(TasksG... tasksGS) {
        mTareaDao.insertTask(tasksGS);
        return null;
    }
}
