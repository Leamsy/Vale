package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "Guardar en firebase";
    Boolean jugador=false;
    Boolean juego=false;
    Boolean playa=false;
    Boolean flamenco=false;
    Boolean paseo=false;
    Boolean corriendo=false;

    String user="";
    String pass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
    }

    public void login(android.view.View V, final Context context) throws Exception {

        TextView myTextView = findViewById(R.id.logintxt);
        user = myTextView.getText().toString();

        Global.user = Global.encriptar(user, Global.key);
        Global.pass = Global.encriptar(pass, Global.key);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Global.web + "/wp-json/vale/v1/usuario/" + Global.desencriptar(Global.user, Global.key);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                registrarUsuario();
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
            }}){
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

    public void enviar(android.view.View V) throws Exception {
        findViewById(R.id.gif).setVisibility(VISIBLE);
        login(V, this);
    }

    public void clickJugador(android.view.View V){
        if(!jugador){
            jugador = true;
            findViewById(R.id.jugador).setAlpha((float)0.4);
            pass += "p";
        }
    }

    public void clickJuego(android.view.View V){
        if(!juego){
            juego = true;
            findViewById(R.id.juegomesa).setAlpha((float)0.4);
            pass += "g";
        }
    }

    public void clickPlaya(android.view.View V){
        if(!playa){
            playa = true;
            findViewById(R.id.playa).setAlpha((float)0.4);
            pass += "e";
        }
    }

    public void clickFlamenco(android.view.View V){
        if(!flamenco){
            flamenco = true;
            findViewById(R.id.flamenco).setAlpha((float)0.4);
            pass += "v";
        }
    }

    public void clickPaseo(android.view.View V){
        if(!paseo){
            paseo = true;
            findViewById(R.id.paseo).setAlpha((float)0.4);
            pass += "b";
        }
    }

    public void clickCorriendo(android.view.View V){
        if(!corriendo){
            corriendo = true;
            findViewById(R.id.corriendo).setAlpha((float)0.4);
            pass += "d";
        }
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
        pass="";
    }

    private void registrarUsuario(){
        String usuario = user;
        String tipo = "No asignado";
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(usuario);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        String meterusuario = user;
                        String tipo = "No asignado";
                        //Log.d(TAG, "No such document");
                        Map<String, Object> map = new HashMap<>();
                        map.put("nombre", meterusuario);
                        map.put("tipo", tipo);

                        db.collection("users")
                                .document(meterusuario)
                                .set(map);

                        Toast.makeText(getApplicationContext(),
                                "Bienvenido por primera vez", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });
    }//registrarUsuario

}
