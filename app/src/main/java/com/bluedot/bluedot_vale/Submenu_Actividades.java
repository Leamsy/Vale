package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Submenu_Actividades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenu__actividades);
    }
    public void volver(android.view.View V) {
        finish();
    }

    public void irListaActividades(android.view.View V){
        Intent intent = new Intent(this, Lista_actividades.class);
        startActivity(intent);
    }

    public void irPeticiones(android.view.View V){
        Intent intent = new Intent(this, Peticiones.class);
        startActivity(intent);
    }

    public void cambiarMisActividades(android.view.View V){
        Intent intent = new Intent(this, Mis_actividades.class);
        startActivity(intent);
    }
}
