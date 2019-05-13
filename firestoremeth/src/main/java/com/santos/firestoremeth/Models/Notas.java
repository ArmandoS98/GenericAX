package com.santos.firestoremeth.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Notas implements Parcelable {

    private String tituloNota;
    private String descripcionNota;
    private @ServerTimestamp
    Date timestamp;
    private String nombreTemaNota;
    private String url_foto;
    private String idNota;
    private String id_user_settings;
    private String id_curso;

    public Notas() {
    }

    protected Notas(Parcel in) {
        tituloNota = in.readString();
        descripcionNota = in.readString();
        nombreTemaNota = in.readString();
        url_foto = in.readString();
        idNota = in.readString();
        id_user_settings = in.readString();
        id_curso = in.readString();
    }

    public static final Creator<Notas> CREATOR = new Creator<Notas>() {
        @Override
        public Notas createFromParcel(Parcel in) {
            return new Notas(in);
        }

        @Override
        public Notas[] newArray(int size) {
            return new Notas[size];
        }
    };

    public String getTituloNota() {
        return tituloNota;
    }

    public void setTituloNota(String tituloNota) {
        this.tituloNota = tituloNota;
    }

    public String getDescripcionNota() {
        return descripcionNota;
    }

    public void setDescripcionNota(String descripcionNota) {
        this.descripcionNota = descripcionNota;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNombreTemaNota() {
        return nombreTemaNota;
    }

    public void setNombreTemaNota(String nombreTemaNota) {
        this.nombreTemaNota = nombreTemaNota;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public String getIdNota() {
        return idNota;
    }

    public void setIdNota(String idNota) {
        this.idNota = idNota;
    }

    public String getId_user_settings() {
        return id_user_settings;
    }

    public void setId_user_settings(String id_user_settings) {
        this.id_user_settings = id_user_settings;
    }

    public String getId_curso() {
        return id_curso;
    }

    public void setId_curso(String id_curso) {
        this.id_curso = id_curso;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tituloNota);
        dest.writeString(descripcionNota);
        dest.writeString(nombreTemaNota);
        dest.writeString(url_foto);
        dest.writeString(idNota);
        dest.writeString(id_user_settings);
        dest.writeString(id_curso);
    }
}
