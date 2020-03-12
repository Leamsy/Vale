package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Login extends AppCompatActivity {


    Boolean jugador=false;
    Boolean juego=false;
    Boolean playa=false;
    Boolean flamenco=false;
    Boolean paseo=false;
    Boolean corriendo=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void login(android.view.View V, final Context context){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://vale-web.000webhostapp.com/wp-json/vale/v1/usuario/" + Global.user;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                findViewById(R.id.gif).setVisibility(INVISIBLE);
                Toast.makeText(context, "Entrando...", Toast.LENGTH_LONG).show();
                try {
                    cambiar(response.getString("rol"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.gif).setVisibility(INVISIBLE);
                Toast.makeText(context, "Usuario o contraseña incorrectos.", Toast.LENGTH_LONG).show();
                //FALSEADO
                cambiar("uwu");
            }}){
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

        requestQueue.add(objectRequest);

    }

    public void cambiar(String rol){
        if(rol.equals("tutor")){
            Intent intent = new Intent(this, Menu_tutor.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, Principal.class);
            startActivity(intent);
        }
    }

    public void enviar(android.view.View V){
        findViewById(R.id.gif).setVisibility(VISIBLE);
        TextView myTextView = findViewById(R.id.logintxt);
        Global.user = myTextView.getText().toString();
        login(V, this);
    }

    public void clickJugador(android.view.View V){
        if(!jugador){
            jugador = true;
            findViewById(R.id.jugador).setAlpha((float)0.4);
            Global.pass += "p";
        }

        Log.d("aa", Global.pass);
    }

    public void clickJuego(android.view.View V){
        if(!juego){
            juego = true;
            findViewById(R.id.juegomesa).setAlpha((float)0.4);
            Global.pass += "g";
        }
        Log.d("aa", Global.pass);
    }

    public void clickPlaya(android.view.View V){
        if(!playa){
            playa = true;
            findViewById(R.id.playa).setAlpha((float)0.4);
            Global.pass += "e";
        }
        Log.d("aa", Global.pass);
    }

    public void clickFlamenco(android.view.View V){
        if(!flamenco){
            flamenco = true;
            findViewById(R.id.flamenco).setAlpha((float)0.4);
            Global.pass += "v";
        }
        Log.d("aa", Global.pass);
    }

    public void clickPaseo(android.view.View V){
        if(!paseo){
            paseo = true;
            findViewById(R.id.paseo).setAlpha((float)0.4);
            Global.pass += "b";
        }
        Log.d("aa", Global.pass);
    }

    public void clickCorriendo(android.view.View V){
        if(!corriendo){
            corriendo = true;
            findViewById(R.id.corriendo).setAlpha((float)0.4);
            Global.pass += "d";
        }

        Log.d("aa", Global.pass);
    }

    public void clickGoma(android.view.View V){
        jugador=false;
        juego=false;
        playa=false;
        flamenco=false;
        paseo=false;
        corriendo=false;
        findViewById(R.id.jugador).setAlpha((float)1);
        findViewById(R.id.juegomesa).setAlpha((float)1);
        findViewById(R.id.playa).setAlpha((float)1);
        findViewById(R.id.flamenco).setAlpha((float)1);
        findViewById(R.id.paseo).setAlpha((float)1);
        findViewById(R.id.corriendo).setAlpha((float)1);
        Global.pass="";
        Log.d("aa", Global.pass);
    }
}
