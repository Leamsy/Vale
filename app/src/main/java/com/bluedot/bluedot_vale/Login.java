package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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

public class Login extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private static final String TAG = "Guardar en firebase";
    Boolean jugador=false;
    Boolean juego=false;
    Boolean playa=false;
    Boolean flamenco=false;
    Boolean paseo=false;
    Boolean corriendo=false;

    private EditText username;
    private String useremail = "";
    private String pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instancia de Firebase
        mAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.logintxt);
    }

    public void clicklogin(){
        //
        String dni = username.getText().toString().trim();
        //Obtenemos los datos del dni

        if (dni.isEmpty()){
            Toast.makeText(Login.this, "Campo email está vacío",
                    Toast.LENGTH_SHORT).show();
        }
        else if (pass.isEmpty() || pass.length() < 3){
            Toast.makeText(Login.this, "El campo contraseña debe tener al menos una imagen seleccionada",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            //Sacar el email del dni
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(dni);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        //Si el documento existe
                        if (document.exists()) {
                            Log.d(TAG, "Mail usuario: " /*+ document.getData()*/);
                            //Que meta en usermail el email
                            useremail = document.getData().get("email").toString();
                            //Poner Login aquí
                            enterFirebase();
                            //
                        } else {
                            Log.d(TAG, "No existe el usuario");
                            Toast.makeText(Login.this, "No existe el usuario",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "Fallo en la conexión ", task.getException());
                        Toast.makeText(Login.this, "Fallo en la conexión",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //Login user
        }

    }//Fin clicklogin

    public void enterFirebase(){
        mAuth.signInWithEmailAndPassword(useremail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(Login.this, "Bienvenido",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(Login.this, Principal.class));
                            finish();
                        } else{
                                /*if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(RegisterActivity.this, "El usuario ya existe",
                                            Toast.LENGTH_SHORT).show();
                                } else {*/
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Login incorrecto",
                                    Toast.LENGTH_SHORT).show();
                            //}
                        }

                        // ...
                    }


                });
    }

    public void clickjugador(){
        if(!jugador){
            jugador = true;
            findViewById(R.id.jugador).setAlpha((float)0.4);
            pass += "P1a";
        }
    }
    public void clickjuegomesa(){
        if(!juego){
            juego = true;
            findViewById(R.id.juegomesa).setAlpha((float)0.4);
            pass += "g4m";
        }
    }

    public void clickplaya(){
        if(!playa){
            playa = true;
            findViewById(R.id.playa).setAlpha((float)0.4);
            pass += "c0s";
        }
    }

    public void clickflamenco(){
        if(!flamenco){
            flamenco = true;
            findViewById(R.id.flamenco).setAlpha((float)0.4);
            pass += "f1A";
        }
    }

    public void clickpaseo(){
        if(!paseo){
            paseo = true;
            findViewById(R.id.paseo).setAlpha((float)0.4);
            pass += "W41";
        }
    }

    public void clickcorriendo(){
        if(!corriendo){
            corriendo = true;
            findViewById(R.id.corriendo).setAlpha((float)0.4);
            pass += "RuNn";
        }
    }

    public void clickgoma(){
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


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }



    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Login.this, Principal.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jugador:
                clickjugador();
                break;
            case R.id.juegomesa:
                clickjuegomesa();
                break;
            case R.id.playa:
                clickplaya();
                break;
            case R.id.flamenco:
                clickflamenco();
                break;
            case R.id.paseo:
                clickpaseo();
                break;
            case R.id.corriendo:
                clickcorriendo();
                break;
            case R.id.goma:
                clickgoma();
                break;
            case R.id.loginbutton:
                clicklogin();
                break;
        }
    }

    //public void login(android.view.View V, final Context context) throws Exception {

        //TextView myTextView = findViewById(R.id.logintxt);
        //user = myTextView.getText().toString();

        /*
        Global.user = Global.encriptar(user, Global.key);
        Global.pass = Global.encriptar(pass, Global.key);
        */

        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //String url = Global.web + "/wp-json/vale/v1/usuario/" + Global.desencriptar(Global.user, Global.key);

        /*JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
    */
    /*
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


     */
}
