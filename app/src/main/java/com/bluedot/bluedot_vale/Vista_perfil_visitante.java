package com.bluedot.bluedot_vale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Vista_perfil_visitante extends AppCompatActivity implements View.OnClickListener{

    Context context = this;
    FirebaseFirestore database;
    String uid;
    String miuid;
    private FirebaseAuth mAuth;
    public ImageButton mBtnAtras;

    String foto_perfil;
    String rol;
    String rolmiusuario;
    boolean rolvoluntario;
    String nombre;
    String preferencias;
    String datos_adicionales;
    String contacto_emergencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_perfil_visitante);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        miuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();

        verRol();

        mBtnAtras = findViewById(R.id.atrasperfilvisitante);

        mBtnAtras.setOnClickListener(this);

        findViewById(R.id.contacto_emergencia).setVisibility(View.GONE);
        findViewById(R.id.datos_adicionales).setVisibility(View.GONE);
        findViewById(R.id.txtadicional).setVisibility(View.GONE);
        findViewById(R.id.txtemergencia).setVisibility(View.GONE);

        //////////////////////////////////////////////
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
                            //findViewById(R.id.datos_adicionales).setVisibility(View.VISIBLE);
                            TextView contacto_emergencia_view = findViewById(R.id.contacto_emergencia);
                            contacto_emergencia_view.setText(contacto_emergencia);
                            //findViewById(R.id.contacto_emergencia).setVisibility(View.VISIBLE);
                    } else {
                    }
                } else {
                }
            }
        });
    }
    public void volver(){
        finish();
    }

    public void verRol() {
        DocumentReference docRefusuario = FirebaseFirestore.getInstance().collection("users").document(miuid);
        docRefusuario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        rolmiusuario = document.getData().get("rol").toString();
                        if (rolmiusuario.equals("voluntario")){
                            findViewById(R.id.datos_adicionales).setVisibility(View.VISIBLE);
                            findViewById(R.id.contacto_emergencia).setVisibility(View.VISIBLE);
                            findViewById(R.id.txtadicional).setVisibility(View.VISIBLE);
                            findViewById(R.id.txtemergencia).setVisibility(View.VISIBLE);
                        }

                    } else {
                    }
                } else {
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.atrasperfilvisitante:
                volver();
                break;
        }
    }
}
