package com.santos.generic.Utils.Persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.santos.generic.Utils.TasksG;

import java.util.List;

@Dao
public interface TareaDao {

    @Insert
    long[] insertTask(TasksG... task);

    @Query("SELECT * FROM tareas")
    LiveData<List<TasksG>> getTareas();

    @Query("SELECT * FROM tareas WHERE id_curso LIKE :id_curso")
    LiveData<List<TasksG>> getTareasCursos(String id_curso);

    @Delete
    int eliminar(TasksG... task);

    @Update
    int actualizar(TasksG... task);
}
