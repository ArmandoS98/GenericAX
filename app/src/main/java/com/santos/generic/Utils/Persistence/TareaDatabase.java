package com.santos.generic.Utils.Persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.santos.generic.Utils.TasksG;

@Database(entities = {TasksG.class}, version = 1)
public abstract class TareaDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "tareas_db";

    private static TareaDatabase instance;

    static TareaDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TareaDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract TareaDao getTaskDao();
}
