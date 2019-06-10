package com.santos.generic.Utils.Persistence;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.santos.generic.Utils.TasksG;

import java.util.List;

@Dao
public interface TareaDao {

    @Insert
    long[] insertTask(TasksG... task);

    @Query("SELECT * FROM tareas ORDER BY timestamp")
    LiveData<List<TasksG>> getTareas();

    @Query("SELECT * FROM tareas WHERE id_curso LIKE :id_curso")
    LiveData<List<TasksG>> getTareasCursos(String id_curso);

    @Delete
    int eliminar(TasksG... task);

    @Update
    int actualizar(TasksG... task);
}
