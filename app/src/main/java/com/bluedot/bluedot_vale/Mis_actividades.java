package com.bluedot.bluedot_vale;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
