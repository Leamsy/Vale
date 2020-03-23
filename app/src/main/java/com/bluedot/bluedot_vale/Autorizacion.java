package com.bluedot.bluedot_vale;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Autorizacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorizacion);
    }

    public void volver(android.view.View V){
        finish();
    }
}
