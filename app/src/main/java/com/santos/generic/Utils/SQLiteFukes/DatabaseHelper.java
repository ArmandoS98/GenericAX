package com.santos.generic.Utils.SQLiteFukes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Semana;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.santos.generic.Utils.SQLiteFukes.Campos.CATEDRATICO;
import static com.santos.generic.Utils.SQLiteFukes.Campos.CURSO;
import static com.santos.generic.Utils.SQLiteFukes.Campos.CURSO_NAME;
import static com.santos.generic.Utils.SQLiteFukes.Campos.DESCRIPCION_TASK;
import static com.santos.generic.Utils.SQLiteFukes.Campos.DIA;
import static com.santos.generic.Utils.SQLiteFukes.Campos.HORA_A;
import static com.santos.generic.Utils.SQLiteFukes.Campos.HORA_DE;
import static com.santos.generic.Utils.SQLiteFukes.Campos.ID_CURSOO;
import static com.santos.generic.Utils.SQLiteFukes.Campos.ID_SEMANA;
import static com.santos.generic.Utils.SQLiteFukes.Campos.NAME_DB;
import static com.santos.generic.Utils.SQLiteFukes.Campos.SALON;
import static com.santos.generic.Utils.SQLiteFukes.Campos.TABLE_HORARIOS;
import static com.santos.generic.Utils.SQLiteFukes.Campos.TABLE_NAME;
import static com.santos.generic.Utils.SQLiteFukes.Campos.TABLE_NAME_TASK;
import static com.santos.generic.Utils.SQLiteFukes.Campos.TABLE_TASK;
import static com.santos.generic.Utils.SQLiteFukes.Campos.TIMESTAMP;
import static com.santos.generic.Utils.SQLiteFukes.Campos.TIPO_TASK;
import static com.santos.generic.Utils.SQLiteFukes.Campos.TITULO_TASK;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, NAME_DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DATETIME DEFAULT CURRENT_TIMESTAMP
        db.execSQL(TABLE_HORARIOS);
        db.execSQL(TABLE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String delete = "DROP IF TABLE EXISTS ";
        db.execSQL(delete + TABLE_NAME);
        db.execSQL(delete + TABLE_NAME_TASK);
        onCreate(db);
    }

    public boolean addData(Semana semana) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIA, semana.getDia());
        contentValues.put(CURSO, semana.getCurso());
        contentValues.put(CATEDRATICO, semana.getCatedratico());
        contentValues.put(SALON, semana.getSalon());
        contentValues.put(HORA_DE, semana.getHora_de());
        contentValues.put(HORA_A, semana.getHora_a());

        Log.d(TAG, "addData: Adding " + semana + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);
        //db.update(TABLE_NAME, contentValues, DIA, null);
        db.close();

        // db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean addTaskNew(TasksG tasksG) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CURSOO, tasksG.getId_curso());
        contentValues.put(CURSO_NAME, tasksG.getNombre_curso());
        contentValues.put(TIMESTAMP, tasksG.getTimestamp());
        contentValues.put(TITULO_TASK, tasksG.getTitulo());
        contentValues.put(DESCRIPCION_TASK, tasksG.getDetalle());
        contentValues.put(TIPO_TASK, tasksG.getTipo());

        Log.d(TAG, "addData: Adding " + tasksG + " to " + TABLE_NAME_TASK);

        long result = db.insert(TABLE_NAME_TASK, null, contentValues);
        //db.update(TABLE_NAME, contentValues, DIA, null);
        db.close();

        // db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<Semana> getSemana(String fragment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Semana> weeklist = new ArrayList<>();
        Semana semana;
        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT * FROM " + TABLE_NAME + " ORDER BY " + HORA_DE + " ) WHERE " + DIA + " LIKE '" + fragment + "%'", null);
        while (cursor.moveToNext()) {
            semana = new Semana();
            semana.setId(cursor.getInt(cursor.getColumnIndex(ID_SEMANA)));
            semana.setCurso(cursor.getString(cursor.getColumnIndex(CURSO)));
            semana.setCatedratico(cursor.getString(cursor.getColumnIndex(CATEDRATICO)));
            semana.setSalon(cursor.getString(cursor.getColumnIndex(SALON)));
            semana.setHora_de(cursor.getString(cursor.getColumnIndex(HORA_DE)));
            semana.setHora_a(cursor.getString(cursor.getColumnIndex(HORA_A)));
            weeklist.add(semana);
        }
        return weeklist;
    }

    public ArrayList<TasksG> getTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TasksG> mTasksGS = new ArrayList<>();
        TasksG tasksG;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_TASK, null);

        while (cursor.moveToNext()) {
            tasksG = new TasksG();
            tasksG.setId_curso(cursor.getString(cursor.getColumnIndex(ID_CURSOO)));
            tasksG.setNombre_curso(cursor.getString(cursor.getColumnIndex(CURSO_NAME)));
            tasksG.setTimestamp(cursor.getString(cursor.getColumnIndex(TIMESTAMP)));
            tasksG.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_TASK)));
            tasksG.setDetalle(cursor.getString(cursor.getColumnIndex(DESCRIPCION_TASK)));
            tasksG.setTipo(cursor.getString(cursor.getColumnIndex(TIPO_TASK)));
            mTasksGS.add(tasksG);
        }
        return mTasksGS;
    }

    public void deleteWeekById(Semana semana) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_SEMANA + " = ? ", new String[]{String.valueOf(semana.getId())});
        db.close();
    }

    /*
     * Este metodo fue creada para poder manupular la fecha que se recoge del dataPicker del Dialog de crear
     * modificar tareas para su correcta incercion en la DB
     * su unico parametro es pasarle un dato tipo string de la fecha.
     */
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Date getDateFromString(String datetoSaved) {

        try {
            Date date = format.parse(datetoSaved);
            return date;
        } catch (ParseException e) {
            return null;
        }

    }
}
