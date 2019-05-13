package com.santos.firestoremeth.Logica;

import android.os.Parcel;
import android.os.Parcelable;

import com.santos.firestoremeth.Models.Notas;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LNota implements Parcelable {
    private String key;
    private Notas notas;
    private LUsuario lUsuario;

    public LNota(String key, Notas notas) {
        this.key = key;
        this.notas = notas;
    }

    protected LNota(Parcel in) {
        key = in.readString();
        notas = in.readParcelable(Notas.class.getClassLoader());
    }

    public static final Creator<LNota> CREATOR = new Creator<LNota>() {
        @Override
        public LNota createFromParcel(Parcel in) {
            return new LNota(in);
        }

        @Override
        public LNota[] newArray(int size) {
            return new LNota[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Notas getNotas() {
        return notas;
    }

    public void setNotas(Notas notas) {
        this.notas = notas;
    }

    public String obtnerFechaDeCreacion() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return simpleDateFormat.format(notas.getTimestamp());
    }

    public LUsuario getlUsuario() {
        return lUsuario;
    }

    public void setlUsuario(LUsuario lUsuario) {
        this.lUsuario = lUsuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeParcelable(notas, flags);
    }

}
