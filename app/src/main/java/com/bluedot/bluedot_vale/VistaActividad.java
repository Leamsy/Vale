package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class VistaActividad extends AppCompatActivity {

    String titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actividad);

        Intent intent = getIntent();
        titulo = intent.getStringExtra("titulo");
        Log.i("aa", titulo);
    }

    public void volver(android.view.View V){
        finish();
    }
}
