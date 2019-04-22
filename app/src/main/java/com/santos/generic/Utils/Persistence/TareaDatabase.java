package com.santos.generic.Utils.Persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
