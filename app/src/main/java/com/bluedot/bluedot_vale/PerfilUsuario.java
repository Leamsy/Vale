package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilUsuario extends AppCompatActivity implements View.OnClickListener{

    Context context = this;
    FirebaseFirestore database;
    String uid;
    String nombre;
    private FirebaseAuth mAuth;
    public Button mBtnLogout;
    public ImageButton mBtnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        mAuth = FirebaseAuth.getInstance();

        mBtnLogout = findViewById(R.id.cerrar_sesion);
        mBtnAtras = findViewById(R.id.atrasperfil);

        mBtnLogout.setOnClickListener(this);
        mBtnAtras.setOnClickListener(this);

        /*
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
         */

        /*
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseFirestore.getInstance();

        DocumentReference docRef = database.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("aa", "DocumentSnapshot data: " + document.getData());

                    } else {
                        Log.d("aa", "No such document");
                    }
                } else {
                    Log.d("aa", "get failed with ", task.getException());
                }
            }
        });
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent intent = new Intent(PerfilUsuario.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    public void volver(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cerrar_sesion:
                signOut();
                break;
            case R.id.atrasperfil:
                volver();
                break;
        }
    }

}
