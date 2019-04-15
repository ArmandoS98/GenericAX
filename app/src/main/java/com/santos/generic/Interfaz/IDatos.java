package com.santos.generic.Interfaz;



import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Utils.TasksG;

public interface IDatos {
    void onNotaSeleccionada(Notas notas);
    void onNotaUpdate(Notas notas);
    void onCursotoNotaa(Cursos cursos);

    void onNuevoCuestionario(String titulo, String content);

    //Tarea
    void onNuevaTarea(String... arg);
    void onSelectTarea(TasksG tasksG);
}
