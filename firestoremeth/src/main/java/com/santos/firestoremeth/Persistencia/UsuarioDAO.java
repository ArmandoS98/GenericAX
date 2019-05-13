package com.santos.firestoremeth.Persistencia;

import com.google.firebase.auth.FirebaseAuth;

public class UsuarioDAO {

    private static UsuarioDAO usuarioDAO;

    public static UsuarioDAO getInstancia() {
        if (usuarioDAO == null) usuarioDAO = new UsuarioDAO();
        return usuarioDAO;
    }

    public String getKeyUsuario() {
        return FirebaseAuth.getInstance().getUid();
    }

    public long fechaDeCreacion() {
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }

    public long fechaUltimoLogueoUsuario() {
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }
}
