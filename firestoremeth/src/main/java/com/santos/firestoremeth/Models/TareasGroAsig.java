package com.santos.firestoremeth.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class TareasGroAsig implements Parcelable {
    private String id_asignacion;
    private String id_curso;
    private String titulo;
    private String descripcion;
    private int porcentaje;
    private @ServerTimestamp
    Date timestamp;

    public TareasGroAsig() {
    }

    public TareasGroAsig(String id_asignacion, String id_curso, String titulo, String descripcion, int porcentaje, Date timestamp) {
        this.id_asignacion = id_asignacion;
        this.id_curso = id_curso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.porcentaje = porcentaje;
        this.timestamp = timestamp;
    }

    protected TareasGroAsig(Parcel in) {
        id_asignacion = in.readString();
        id_curso = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
        porcentaje = in.readInt();
    }

    public static final Creator<TareasGroAsig> CREATOR = new Creator<TareasGroAsig>() {
        @Override
        public TareasGroAsig createFromParcel(Parcel in) {
            return new TareasGroAsig(in);
        }

        @Override
        public TareasGroAsig[] newArray(int size) {
            return new TareasGroAsig[size];
        }
    };

    public String getId_asignacion() {
        return id_asignacion;
    }

    public void setId_asignacion(String id_asignacion) {
        this.id_asignacion = id_asignacion;
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

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TareasGroAsig{" +
                "id_asignacion='" + id_asignacion + '\'' +
                ", id_curso='" + id_curso + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", porcentaje=" + porcentaje +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_asignacion);
        dest.writeString(id_curso);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeInt(porcentaje);
    }
}
