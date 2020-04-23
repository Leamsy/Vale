package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PerfilUsuario extends AppCompatActivity implements View.OnClickListener{

    Context context = this;
    FirebaseFirestore database;
    String uid;
    private FirebaseAuth mAuth;
    public Button mBtnLogout;
    public ImageButton mBtnAtras;

    String foto_perfil;
    String rol;
    String nombre;
    String preferencias;
    String datos_adicionales;
    String contacto_emergencia;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        mAuth = FirebaseAuth.getInstance();

        mBtnLogout = findViewById(R.id.cerrar_sesion);
        mBtnAtras = findViewById(R.id.atrasperfil);

        mBtnLogout.setOnClickListener(this);
        mBtnAtras.setOnClickListener(this);

        loading();


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        foto_perfil = document.getData().get("foto_perfil").toString();
                        rol = document.getData().get("rol").toString();
                        nombre = document.getData().get("nombre").toString();
                        preferencias = document.getData().get("preferencias").toString();
                        datos_adicionales = document.getData().get("datos_adicionales").toString();
                        contacto_emergencia = document.getData().get("contacto_emergencia").toString();

                        ImageView foto_perfil_view = (ImageView) findViewById(R.id.foto_perfil);
                        try {
                            Picasso.get().load(foto_perfil).into(foto_perfil_view);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        TextView rol_view = findViewById(R.id.rol);
                        rol_view.setText(rol);
                        TextView nombre_view = findViewById(R.id.nombre);
                        nombre_view.setText(nombre);
                        TextView preferencias_view = findViewById(R.id.preferencias);
                        preferencias_view.setText(preferencias);
                        TextView datos_adicionales_view = findViewById(R.id.datos_adicionales);
                        datos_adicionales_view.setText(datos_adicionales);
                        TextView contacto_emergencia_view = findViewById(R.id.contacto_emergencia);
                        contacto_emergencia_view.setText(contacto_emergencia);

                        ready();

                    } else {
                    }
                } else {
                }
            }
        });
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

    public void volver(View v){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cerrar_sesion:
                signOut();
                break;
        }
    }

    private void loading(){
        findViewById(R.id.linearatrasperfil).setVisibility(INVISIBLE);
        findViewById(R.id.scrollView2).setVisibility(INVISIBLE);
        findViewById(R.id.cerrar_sesion).setVisibility(INVISIBLE);
        findViewById(R.id.gif).setVisibility(VISIBLE);
    }

    private void ready(){
        findViewById(R.id.linearatrasperfil).setVisibility(VISIBLE);
        findViewById(R.id.scrollView2).setVisibility(VISIBLE);
        findViewById(R.id.cerrar_sesion).setVisibility(VISIBLE);
        findViewById(R.id.gif).setVisibility(INVISIBLE);
    }

}
