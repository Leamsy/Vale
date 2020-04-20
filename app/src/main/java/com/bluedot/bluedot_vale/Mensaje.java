package com.bluedot.bluedot_vale;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Date;

public class Mensaje {
    private String mensaje;
    private String nombre;
    private Timestamp fecha;


    public Mensaje(){
        fecha = fecha.now();
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Timestamp getFecha(){ return fecha;}

}
