package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ListaAutorizaciones extends AppCompatActivity {

    private String url = "https://vale-web.000webhostapp.com/wp-json/vale/v1/actividades/1";
    private String username = "manugg";
    private String password = "vale-web";

    private String[] lista;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_autorizaciones);
    }

    public void volverPrincipal(android.view.View V){
        Intent intent = new Intent(this, Menu_tutor.class);
        startActivity(intent);
    }

    public void vistaAutorizacion(android.view.View V){
        Intent intent = new Intent(this, Autorizacion.class);
        startActivity(intent);
    }
}
