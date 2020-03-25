package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Peticiones extends AppCompatActivity {

    private String url = "https://vale-web.000webhostapp.com/wp-json/vale/v1/peticiones/1";

    private String[] lista;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticiones);
/*
        recyclerView = (RecyclerView) findViewById( R.id.listaSuge );

        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        RequestQueue requestQueue = Volley.newRequestQueue( this );

        JsonArrayRequest objectRequest = new JsonArrayRequest( Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse( JSONArray response ) {
                findViewById(R.id.gif).setVisibility(INVISIBLE);
                lista = new String[response.length()];
                for( int i = 0; i < response.length(); i++ ) {
                    try {
                        JSONObject sugerencia = response.getJSONObject(i);
                        lista[i] = sugerencia.getString( "titulo" );
                    } catch( JSONException e ) {
                        e.printStackTrace();
                    }
                }

                mAdapter = new MyAdapter( lista );
                recyclerView.setAdapter( mAdapter );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse( VolleyError error ) {
                Log.i( "asdf", "No funca" );
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = Global.user + ":" + Global.pass;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        requestQueue.add( objectRequest );*/
    }

    public void volver(android.view.View V){
        finish();
    }

    public void aniadirPeticion(android.view.View V){
        Intent intent = new Intent(this, AgregarPeticion.class);
        startActivity(intent);
    }

}
