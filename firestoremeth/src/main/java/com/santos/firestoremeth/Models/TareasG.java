package com.santos.firestoremeth.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class TareasG implements Parcelable {
    private String id_tarea;
    private String id_curso;
    private String titulo;
    private String descripcion;
    private @ServerTimestamp
    Date timestamp;

    public TareasG() {
    }

    public TareasG(String id_tarea, String id_curso, String titulo, String descripcion, Date timestamp) {
        this.id_tarea = id_tarea;
        this.id_curso = id_curso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.timestamp = timestamp;
    }

    protected TareasG(Parcel in) {
        id_tarea = in.readString();
        id_curso = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
    }

    public static final Creator<TareasG> CREATOR = new Creator<TareasG>() {
        @Override
        public TareasG createFromParcel(Parcel in) {
            return new TareasG(in);
        }

        @Override
        public TareasG[] newArray(int size) {
            return new TareasG[size];
        }
    };

    @Override
    public String toString() {
        return "TareasG{" +
                "id_tarea='" + id_tarea + '\'' +
                ", id_curso='" + id_curso + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(String id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getId_curso() {
        return id_curso;
    }

    public void setId_curso(String id_curso) {
        this.id_curso = id_curso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_tarea);
        dest.writeString(id_curso);
        dest.writeString(titulo);
        dest.writeString(descripcion);
    }
}
