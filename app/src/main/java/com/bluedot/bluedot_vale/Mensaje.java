package com.bluedot.bluedot_vale;

public class Mensaje {
    private String mensaje;
    private String nombre;

    public Mensaje(String mensaje, String nombre) {
        this.mensaje = mensaje;
        this.nombre = nombre;
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
}
