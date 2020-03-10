package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
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

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;

public class PerfilUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://vale-web.000webhostapp.com/wp-json/vale/v1/usuario/manugg";

        final String username = "manugg";
        final String password = "pde";
        final JSONObject[] recibido = new JSONObject[1];

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                findViewById(R.id.gif).setVisibility(INVISIBLE);
                /*
                //campo de nick
                TextView myTextView4 = (TextView) findViewById(R.id.editText4);
                try {
                    myTextView4.setText(response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
                /*
                //campo de mail
                TextView myTextView = (TextView) findViewById(R.id.editText);
                try {
                    myTextView.setText(response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
                //Campo de imagen
                ImageView myImageView = (ImageView) findViewById(R.id.imageView2);
                try {
                    Picasso.get().load(response.getString("fotografia_url")).into(myImageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                //Campo de Datos:
                TextView myTextView3 = (TextView) findViewById(R.id.editText3);
                try {
                    myTextView3.setText(response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                 */
                /*
                //Campo de preferencias
                TextView myTextView2 = (TextView) findViewById(R.id.editText2);
                try {
                    myTextView2.setText(response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Campo de contacto emergencia
                TextView myTextView10 = (TextView) findViewById(R.id.textView10);
                try {
                    myTextView10.setText(response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */


            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("asdf", "No funca");

            }}) {
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


        requestQueue.add(objectRequest);
    }

    public void volver(android.view.View V){
        finish();
    }

    public void editar(android.view.View V){
        Toast.makeText(this, "Datos actualizados.", Toast.LENGTH_SHORT).show();
    }
}
