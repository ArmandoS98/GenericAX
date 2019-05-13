package com.santos.firestoremeth.Logica;

import com.santos.firestoremeth.Models.Usuario;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LUsuario {
    private String key;
    private Usuario usuario;


    public LUsuario(String key, Usuario usuario) {
        this.key = key;
        this.usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String obtnerFechaDeCreacion() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return simpleDateFormat.format(usuario.getTimestamp());
    }
}
