package com.santos.generic.Utils.SQLiteFukes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Task implements Parcelable {
    private String id_curso;
    private String nombre_curso;
    private String timestamp;
    private String titulo;
    private String detalle;
    private String tipo;  //Si es una TAREA|PROYECTO|INVESTIACION

    public Task() {
    }

    public Task(String id_curso, String nombre_curso, String timestamp, String titulo, String detalle, String tipo) {
        this.id_curso = id_curso;
        this.nombre_curso = nombre_curso;
        this.timestamp = timestamp;
        this.titulo = titulo;
        this.detalle = detalle;
        this.tipo = tipo;
    }

    protected Task(Parcel in) {
        id_curso = in.readString();
        nombre_curso = in.readString();
        timestamp = in.readString();
        titulo = in.readString();
        detalle = in.readString();
        tipo = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_curso);
        dest.writeString(nombre_curso);
        dest.writeString(timestamp);
        dest.writeString(titulo);
        dest.writeString(detalle);
        dest.writeString(tipo);
    }
}
