package com.bluedot.bluedot_vale;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Lista_actividades extends AppCompatActivity {

    private String url = "https://vale-web.000webhostapp.com/wp-json/vale/v1/actividades/1";

    private ListView lista;
    private String[] arrayList;
    private ArrayAdapter<TextView> arrayAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividades);

        recyclerView = (RecyclerView) findViewById(R.id.listaSuge);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        RequestQueue requestQueue = Volley.newRequestQueue( this );

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                findViewById(R.id.gif).setVisibility(INVISIBLE);
                arrayList = new String [response.length()];
                for( int i = 0; i < response.length(); i++ ) {
                    try {
                        JSONObject actividad = response.getJSONObject(i);
                        arrayList[i] = actividad.getString("titulo");
                        Log.i("asdf", actividad.getString("titulo"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter = new MyAdapter(arrayList);
                recyclerView.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asdf","No funca");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = Global.user + ":" + Global.pass;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        requestQueue.add( objectRequest );

    }

    public void volverPrincipal(android.view.View V){
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }

    public void aniadir(android.view.View V){
        Intent intent = new Intent(this, AgregarActividad.class);
        startActivity(intent);
    }

    public void verActividad(android.view.View V){
        Intent intent = new Intent(this, VistaActividad.class);
        startActivity(intent);
    }

}
