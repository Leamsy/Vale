package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VistaActividad extends AppCompatActivity  implements View.OnClickListener{

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
    String sala_chat;

    private ImageView atras;
    private Button reservar;
    private Button chatear;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actividad);

        atras = findViewById(R.id.btnatras);
        reservar = findViewById(R.id.reservar);
        chatear = findViewById(R.id.chatear);

        atras.setOnClickListener(this);
        reservar.setOnClickListener(this);
        chatear.setOnClickListener(this);

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
                        //Falta hacer:
                        // Si para el tipo de usuario que eres no quedan plazas que el boton de reservar no se muestre

                        titulo = document.getData().get("titulo").toString();
                        descripcion = document.getData().get("descripcion").toString();
                        imagen = document.getData().get("imagen").toString();
                        fecha = document.getData().get("fecha").toString();
                        hora = document.getData().get("hora").toString();
                        precio = document.getData().get("precio").toString();
                        plazas_socios = document.getData().get("plazas_socios").toString();
                        plazas_voluntarios = document.getData().get("plazas_voluntarios").toString();
                        requiere_autorizacion = document.getData().get("requiere_autorizacion").toString();
                        sala_chat = document.getData().get("salachat").toString();

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
                    } else {
                    }
                } else {
                }
            }
        });


    }

    public void apuntarse(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("mis_actividades").document(uid_act).set("");
        db.collection("actividades").document(uid_act).collection("apuntados").document(uid).set("");

        //Recoger el dato del tipo usuario que esta reservando
        //Recoger datos de el numero de plazas
        //Restar 1 al tipo de usuario correspondiente y actualizar el dato en la actividad
    }

    public void chatear(){
        Intent intent = new Intent(VistaActividad.this, ChatActividades.class);
        intent.putExtra("uid", uid_act);
        intent.putExtra("salachat", sala_chat);
        startActivity(intent);
    }

    public void volver(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnatras:
                volver();
                break;
            case R.id.reservar:
                apuntarse();
                break;
            case R.id.chatear:
                chatear();
                break;
        }
    }
}
