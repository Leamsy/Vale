package com.bluedot.bluedot_vale;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Mis_actividades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_actividades);
    }

    public void volver(android.view.View V){
        finish();
    }
}
