package com.santos.generic.Utils.SQLiteFukes;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Campos {

    //PALABRAS RECERBADAS
    private static final String COMA = ", ";
    private static final String TEXTO = " TEXT";
    private static final String INTERGER_PRIMARY = " INTEGER PRIMARY KEY AUTOINCREMENT ";


    /*
     * |-------------------------------------------------------|
     * |ID
     * |CURSO
     * |CATEDRATICO
     * |HORA
     * |-------------------------------------------------------|
     * */

    public static final String NAME_DB = "optsk";

    public static final String TABLE_NAME = "Horarios";
    public static final String ID_SEMANA = "ID";
    public static final String DIA = "dia";
    public static final String CURSO = "curso";
    public static final String CATEDRATICO = "catedratico";
    public static final String SALON = "salon";
    public static final String HORA_DE = "hora_de";
    public static final String HORA_A = "hora_a";

    public static final String TABLE_HORARIOS = "CREATE TABLE " + TABLE_NAME + " (" +
            ID_SEMANA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DIA + " TEXT, " +
            CURSO + " TEXT, " +
            CATEDRATICO + " TEXT, " +
            SALON + " TEXT, " +
            HORA_DE + " TEXT, " +
            HORA_A + " TEXT) ";

    //TABLA TASK
    public static final String TABLE_NAME_TASK = "task";
    public static final String ID = "id";
    public static final String ID_CURSOO = "id_curso";
    public static final String CURSO_NAME = "curso";
    public static final String TIMESTAMP = "timestamp";
    public static final String TITULO_TASK = "titulo_task";
    public static final String DESCRIPCION_TASK = "descripcion_task";
    public static final String TIPO_TASK = "tipo"; // 1 = tarea, 2 = examen, 3 = pendiente

    public static final String TABLE_TASK = "CREATE TABLE " + TABLE_NAME_TASK + " (" +
            ID + INTERGER_PRIMARY + COMA +
            ID_CURSOO + TEXTO + COMA +
            CURSO_NAME + TEXTO + COMA +
            TIMESTAMP + TEXTO + COMA +
            TITULO_TASK + TEXTO + COMA +
            DESCRIPCION_TASK + TEXTO + COMA +
            TIPO_TASK + TEXTO + ")";
}
