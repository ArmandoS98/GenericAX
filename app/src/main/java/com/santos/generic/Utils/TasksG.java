package com.santos.generic.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.IgnoreExtraProperties;

@Entity(tableName = "tareas")
@IgnoreExtraProperties
public class TasksG implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_curso")
    private String id_curso;

    @ColumnInfo(name = "nombre_curso")
    private String nombre_curso;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "detalle")
    private String detalle;

    @ColumnInfo(name = "tipo")
    private String tipo;  //Si es una TAREA|PROYECTO|INVESTIACION

    @Ignore
    public TasksG() {
    }

    public TasksG(String id_curso, String nombre_curso, String timestamp, String titulo, String detalle, String tipo) {
        this.id_curso = id_curso;
        this.nombre_curso = nombre_curso;
        this.timestamp = timestamp;
        this.titulo = titulo;
        this.detalle = detalle;
        this.tipo = tipo;
    }

    protected TasksG(Parcel in) {
        id = in.readInt();
        id_curso = in.readString();
        nombre_curso = in.readString();
        timestamp = in.readString();
        titulo = in.readString();
        detalle = in.readString();
        tipo = in.readString();
    }

    public static final Creator<TasksG> CREATOR = new Creator<TasksG>() {
        @Override
        public TasksG createFromParcel(Parcel in) {
            return new TasksG(in);
        }

        @Override
        public TasksG[] newArray(int size) {
            return new TasksG[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_curso() {
        return id_curso;
    }

    public void setId_curso(String id_curso) {
        this.id_curso = id_curso;
    }

    public String getNombre_curso() {
        return nombre_curso;
    }

    public void setNombre_curso(String nombre_curso) {
        this.nombre_curso = nombre_curso;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "TasksG{" +
                "id=" + id +
                ", id_curso='" + id_curso + '\'' +
                ", nombre_curso='" + nombre_curso + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", titulo='" + titulo + '\'' +
                ", detalle='" + detalle + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(id_curso);
        dest.writeString(nombre_curso);
        dest.writeString(timestamp);
        dest.writeString(titulo);
        dest.writeString(detalle);
        dest.writeString(tipo);
    }
}
