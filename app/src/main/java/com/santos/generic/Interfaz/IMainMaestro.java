package com.santos.generic.Interfaz;



import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Notas;

public interface IMainMaestro {
    void onNotaSeleccionada(Notas notas);
    void onNotaUpdate(Notas notas);
    void onCursotoNotaa(Cursos cursos);

    void onNuevoCuestionario(String titulo, String content);
}
