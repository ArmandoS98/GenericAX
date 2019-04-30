package com.santos.firestoremeth.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class TareasG implements Parcelable {
    private String id_tarea;
    private String id_admin;
    private String nombre_curso;
    private String titulo;
    private String descripcion;
    private @ServerTimestamp
    Date timestamp;
    private List<String> users;

    public TareasG() {
    }

    public TareasG(String id_tarea, String id_admin, String nombre_curso, String titulo, String descripcion, Date timestamp, List<String> users) {
        this.id_tarea = id_tarea;
        this.id_admin = id_admin;
        this.nombre_curso = nombre_curso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.timestamp = timestamp;
        this.users = users;
    }

    protected TareasG(Parcel in) {
        id_tarea = in.readString();
        id_admin = in.readString();
        nombre_curso = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
        users = in.createStringArrayList();
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

    public String getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(String id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getId_admin() {
        return id_admin;
    }

    public void setId_admin(String id_admin) {
        this.id_admin = id_admin;
    }

    public String getNombre_curso() {
        return nombre_curso;
    }

    public void setNombre_curso(String nombre_curso) {
        this.nombre_curso = nombre_curso;
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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_tarea);
        dest.writeString(id_admin);
        dest.writeString(nombre_curso);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeStringList(users);
    }

    @Override
    public String toString() {
        return "TareasG{" +
                "id_tarea='" + id_tarea + '\'' +
                ", id_admin='" + id_admin + '\'' +
                ", nombre_curso='" + nombre_curso + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", timestamp=" + timestamp +
                ", users=" + users +
                '}';
    }
}
