package com.bluedot.bluedot_vale;

public class mensaje {
    private String mensaje;
    private String nombre;

    public mensaje(String mensaje, String nombre) {
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
