package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Menu_tutor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tutor);
    }

    public void cambiarAutorizaciones(android.view.View V){
        Intent intent = new Intent(this, ListaAutorizaciones.class);
        startActivity(intent);
    }

    public void cerrarApp(android.view.View V){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
