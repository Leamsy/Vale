package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.INVISIBLE;

public class VistaActividad extends AppCompatActivity {

    String uid;
    String uid_act;
    String titulo;
    String descripcion;
    String imagen;
    String fecha;
    String hora;
    String precio;
    String plazas_socios;
    String plazas_voluntarios;
    String requiere_autorizacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actividad);

        Intent intent = getIntent();
        uid_act = intent.getStringExtra("uid");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        
                        if(uid.equals(document.getData().get("autor").toString())){
                            Button reservar = findViewById(R.id.reservar);
                            reservar.setVisibility(View.INVISIBLE);
                        }

                        if(uid.equals(document.getData().get("autor").toString())){
                            Button reservar = findViewById(R.id.reservar);
                            reservar.setVisibility(View.INVISIBLE);
                        }

                        titulo = document.getData().get("titulo").toString();
                        descripcion = document.getData().get("descripcion").toString();
                        imagen = document.getData().get("imagen").toString();
                        fecha = document.getData().get("fecha").toString();
                        hora = document.getData().get("hora").toString();
                        precio = document.getData().get("precio").toString();
                        plazas_socios = document.getData().get("plazas_socios").toString();
                        plazas_voluntarios = document.getData().get("plazas_voluntarios").toString();
                        requiere_autorizacion = document.getData().get("requiere_autorizacion").toString();

                        ImageView imagen_view = findViewById(R.id.imagen);
                        Picasso.get().load(imagen).into(imagen_view);
                        TextView titulo_view = findViewById(R.id.titulo);
                        titulo_view.setText(titulo);
                        TextView descripcion_view = findViewById(R.id.descripcion);
                        descripcion_view.setText(descripcion);
                        TextView fecha_view = findViewById(R.id.fecha);
                        fecha_view.setText(fecha);
                        TextView hora_view = findViewById(R.id.hora);
                        hora_view.setText(hora);
                        TextView precio_view = findViewById(R.id.precio);
                        precio_view.setText(precio);
                        //TextView plazas_socios_view = findViewById(R.id.plazas_socios);
                        //plazas_socios_view.setText(plazas_socios);
                        //TextView plazas_voluntarios_view = findViewById(R.id.plazas_voluntarios);
                        //plazas_voluntarios_view.setText(plazas_voluntarios);
                        //TextView requiere_autorizacion_view = findViewById(R.id.requiere_autorizacion);
                        //requiere_autorizacion_view.setText(requiere_autorizacion);

                    } else {
                    }
                } else {
                }
            }
        });


    }

    public void volver(android.view.View V){
        finish();
    }
}
