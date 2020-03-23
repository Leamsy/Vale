package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class VistaActividad extends AppCompatActivity {

    private String url = "https://vale-web.000webhostapp.com/wp-json/vale/v1/actividades/1";
    private String username = "manugg";
    private String password = "vale-web";
    private TextView titulo;
    private TextView descripcion;
    //private TextView coste;
    //private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actividad);

        titulo = findViewById( R.id.vistaTitulo );
        descripcion = findViewById( R.id.vistaDescripcion );
        //coste = findViewById( R.id.vistaPrecio );
        //imagen = findViewById( R.id.vistaImagen );
    }

    public void irListaActividades(android.view.View V){
        Intent intent = new Intent(this, Lista_actividades.class);
        startActivity(intent);
    }

    public void mostrarActividad() {

        RequestQueue requestQueue = Volley.newRequestQueue( this );
        JSONObject[] recibido = new JSONObject[1];

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i( "asdf", "No funca" );
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        requestQueue.add( objectRequest );

    }
}
