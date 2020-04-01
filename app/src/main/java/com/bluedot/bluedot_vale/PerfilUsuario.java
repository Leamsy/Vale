package com.bluedot.bluedot_vale;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.INVISIBLE;


public class PerfilUsuario extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = null;
        try {
            url = Global.web + "/wp-json/vale/v1/usuario/" + Global.desencriptar(Global.user, Global.key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String username = "manugg";
        final String password = "pde";
        final JSONObject[] recibido = new JSONObject[1];

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                findViewById(R.id.gif).setVisibility(INVISIBLE);

                //Imagen
                ImageView myImageView = (ImageView) findViewById(R.id.imageView2);
                try {
                    Picasso.get().load(response.getString("fotografia_url")).into(myImageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Rol
                TextView myTextView3 = (TextView) findViewById(R.id.textView13);
                try {
                    myTextView3.setText(response.getString("rol"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Nombre
                TextView myTextView2 = (TextView) findViewById(R.id.editText4);
                try {
                    myTextView2.setText(response.getString("nombre") + " " + response.getString("apellidos"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Preferencias
                TextView myTextView10 = (TextView) findViewById(R.id.editText);
                try {
                    myTextView10.setText(response.getString("preferencias"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Datos adicionales
                TextView myTextView11 = (TextView) findViewById(R.id.editText3);
                try {
                    myTextView11.setText(response.getString("datos"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Contacto de emergencia
                TextView myTextView12 = (TextView) findViewById(R.id.editText2);
                try {
                    myTextView12.setText(response.getString("contacto"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("asdf", "No funca");
                Toast.makeText(context, "Error de conexión.", Toast.LENGTH_LONG).show();

            }}) {
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


        requestQueue.add(objectRequest);
    }

    public void volver(android.view.View V){
        finish();
    }


    public void cerrar_sesion(android.view.View V){

        Toast.makeText(this, "Datos actualizados.", Toast.LENGTH_SHORT).show();

    }

}
