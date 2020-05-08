package com.bluedot.vale_oficial;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }


    public void irSubmenu(android.view.View V){
        Intent intent = new Intent(this, Submenu_Actividades.class);
        startActivity(intent);
    }

    public void cambiarPerfil(android.view.View V){
        Intent intent = new Intent(this, PerfilUsuario.class);
        startActivity(intent);
    }

    public void cerrarApp(android.view.View V){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
