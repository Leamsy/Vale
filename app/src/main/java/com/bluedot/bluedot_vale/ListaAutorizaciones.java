package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
