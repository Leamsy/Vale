package com.bluedot.vale_oficial;

import com.google.firebase.Timestamp;

public class Mensaje {
    private String mensaje;
    private String nombre;
    private Timestamp fecha;
    private String tipo = "";
    private String uid;


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

    public String getTipo() {
        return tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Timestamp getFecha(){ return fecha;}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
