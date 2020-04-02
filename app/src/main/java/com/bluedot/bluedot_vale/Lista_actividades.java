package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.View.INVISIBLE;

public class Lista_actividades extends AppCompatActivity {

    private List<ItemAdapter> data = new ArrayList<>();
    private ArrayAdapter<TextView> arrayAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    Context context = this;
    String uid;

    String imagen_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividades);

        recyclerView = (RecyclerView) findViewById(R.id.listaSuge);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference colRef = FirebaseFirestore.getInstance().collection("actividades");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    findViewById(R.id.gif).setVisibility(INVISIBLE);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("aa", document.getId() + " => " + document.getData());

                        final ItemAdapter itemAdapter = new ItemAdapter();
                        itemAdapter.setText(document.getData().get("titulo").toString());

                        imagen_url = document.getData().get("imagen").toString();
                        itemAdapter.setImage(imagen_url);

                        data.add(itemAdapter);
                    }
                    mAdapter = new MyAdapter(data, context);
                    recyclerView.setAdapter(mAdapter);

                    /*
                    titulo = document.getData().get("titulo").toString();
                    descripcion = document.getData().get("descripcion").toString();
                    imagen = document.getData().get("imagen").toString();
                    fecha = document.getData().get("fecha").toString();
                    hora = document.getData().get("hora").toString();
                    plazas_socios = document.getData().get("plazas_socios").toString();
                    plazas_voluntarios = document.getData().get("plazas_voluntarios").toString();
                    requiere_autorizacion = document.getData().get("requiere_autorizacion").toString();
                    */
                } else {
                    Log.d("aa", "No existe el usuario");
                }
            }
        });
/*
        RequestQueue requestQueue = Volley.newRequestQueue( this );

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                findViewById(R.id.gif).setVisibility(INVISIBLE);

                for( int i = 0; i < response.length(); i++ ) {
                    try {
                        JSONObject actividad = response.getJSONObject(i);

                        ItemAdapter itemAdapter = new ItemAdapter();
                        itemAdapter.setText(actividad.getString("titulo"));
                        itemAdapter.setImage(R.drawable.dinero);
                        data.add(itemAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter = new MyAdapter(data, context);
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
                String credentials = null;
                try {
                    credentials = Global.desencriptar(Global.user, Global.key) + ":" + Global.desencriptar(Global.pass, Global.key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void aniadir(android.view.View V){
        Intent intent = new Intent(this, AgregarActividad.class);
        startActivity(intent);
    }

}
